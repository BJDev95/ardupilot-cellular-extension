//#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <time.h>

#define BUFSIZE		2048
#define SERVERPORT      40001
#define MAX_REMOTES     10
#define NAME_LENGTH     8

#define min(a,b) ((a)<(b)?(a):(b))

struct sockaddr_in serverAddr;

int numberOfRemotes = 0;
struct sockaddr_in knownRemotes[MAX_REMOTES];
time_t remoteTimestamps[4];
char remoteNames[MAX_REMOTES][NAME_LENGTH] ;

uint8_t buffer[BUFSIZE];
uint8_t testpattern[256];

int serverSocket;
int addr_size = sizeof(struct sockaddr_in);

int socketsEqual(struct sockaddr_in* a, struct sockaddr_in* b) {
  return a->sin_family==b->sin_family
    &&
    a->sin_port==b->sin_port
    &&
    a->sin_addr.s_addr==b->sin_addr.s_addr;
}

int knowsRemote(struct sockaddr_in* candidate) {
  int i;
  for (i=0; i<MAX_REMOTES; i++) {
    if (socketsEqual(&knownRemotes[i], candidate)) return i;
  }
  return -1;
}

int leastRecentlyHeardFromRemote(void) {
  time_t oldest = time(NULL) + 1;
  int i;
  int result = -1;
  for (i=0; i<MAX_REMOTES; i++) {
    if (remoteTimestamps[i] < oldest) {
      result = i;
      oldest = remoteTimestamps[i];
    }
  }
  return result;
}

void broadcastToAll(uint8_t buffer[], int length) {
  int i;
  for (i=0; i<numberOfRemotes; i++) {
    if (sendto(serverSocket, buffer, length, 0, (struct sockaddr*)&knownRemotes[i], addr_size) < 0) {
      perror("relay");
    }
  }
}

int fillRemoteList(int sidx) {
  int i;
  for (i=0; i<numberOfRemotes; i++) {
    sidx += sprintf(&buffer[sidx], "%s:%d (%s)\n", inet_ntoa(knownRemotes[i].sin_addr), ntohs(knownRemotes[i].sin_port), remoteNames[i]);  
  }
  return sidx;
}

int greetNewRemote(int index) {
  int sidx=0;
  
  sidx = sprintf(buffer, "Hello %s:%d (%s)\n", inet_ntoa(knownRemotes[index].sin_addr), ntohs(knownRemotes[index].sin_port), remoteNames[index]);
  sidx =  fillRemoteList(sidx);
  
  if (sendto(serverSocket, buffer, sidx, 0, (struct sockaddr*)&knownRemotes[index], addr_size) < 0) {
    perror("At greeting new remote");
    return -1;
  } else {
    return 0;
  }
}

int main(int argc, char* argv[]) {

  int bytes_read;

  memset(knownRemotes, 0, sizeof(knownRemotes));
  memset(remoteTimestamps, 0, sizeof(knownRemotes));
  
  serverSocket = socket(PF_INET, SOCK_DGRAM, 0);

  if (serverSocket < 0) {
    perror("obtaining socket");
    abort();
  }
  
  serverAddr.sin_family = AF_INET;
  if (argc<2) {
    serverAddr.sin_port = htons(SERVERPORT);
  } else {
    serverAddr.sin_port = htons(atoi(argv[1]));
  }
  serverAddr.sin_addr.s_addr = INADDR_ANY;

  if ( bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
    perror("bind");
    abort();
  }

  // Set socket "file" to nonblocking I/O.
  int flags = fcntl(serverSocket, F_GETFL);
  flags |= O_NONBLOCK;
  fcntl(serverSocket, F_SETFL, flags);

  int go = 1;

  while(go) {
    struct sockaddr_in remote;
    int remoteIndex;
    //    int i;

    bzero(buffer, BUFSIZE);  

    // Get a datagram from anyone at all.
    bytes_read = recvfrom(serverSocket, buffer, BUFSIZE, 0, (struct sockaddr*)&remote, &addr_size);
    
    // Is there no error? (I assume a length of zero is OK, 
    // and means that a datagram of zero content length was received).
    if (bytes_read >= 0) {
      // for the log:
      // printf("Message from: %d, %s:%d : \"%s\"\n", i, inet_ntoa(remote.sin_addr), ntohs(remote.sin_port), buffer);
      if ((remoteIndex = knowsRemote(&remote)) < 0) {
	if (numberOfRemotes < MAX_REMOTES) {
	  remoteIndex = numberOfRemotes;
	  knownRemotes[remoteIndex] = remote;
	  memset(remoteNames[remoteIndex], ' ', NAME_LENGTH);
	  memcpy(remoteNames[remoteIndex], buffer, min(NAME_LENGTH, bytes_read));
	  numberOfRemotes++;
	  greetNewRemote(remoteIndex);
	  bytes_read = fillRemoteList(0);
	  broadcastToAll(buffer, bytes_read);
	  printf("Accepted new remote# %d, %s:%d\n", remoteIndex, inet_ntoa(remote.sin_addr), ntohs(remote.sin_port));
	} else {
	  // We have a new remote, but the array of known remotes
	  // is full. We must find the least recently heard from
	  // one and kick him out.
	  remoteIndex = leastRecentlyHeardFromRemote();
	  memset(remoteNames[remoteIndex], ' ', NAME_LENGTH);
	  memcpy(remoteNames[remoteIndex], buffer, min(NAME_LENGTH, bytes_read));
	  knownRemotes[remoteIndex] = remote;
	  greetNewRemote(remoteIndex);
	  bytes_read = fillRemoteList(0);
	  broadcastToAll(buffer, bytes_read);
	  printf("Replacing remote# %d, %s:%d with %s:%d\n", remoteIndex, inet_ntoa(knownRemotes[remoteIndex].sin_addr), ntohs(knownRemotes[remoteIndex].sin_port), inet_ntoa(remote.sin_addr), ntohs(remote.sin_port));
	  }
      } else {
	remoteTimestamps[remoteIndex] = time(NULL);
	int i, j;
	for (i=0; i<16; i++) {
	  for (j=0; j<16; j++) {
	    testpattern[j] = i * 16 + j;
	  }
	  broadcastToAll(testpattern, 16);
	}
      }
      remoteTimestamps[remoteIndex] = time(NULL);
    }
    
    else if (errno == EAGAIN) {
      usleep(1000); // If no data, sleep for 1 ms.
    } else {
      // Other error.
      perror("recvfrom");
      // go = 0;
    }
  }

  return 0;
}

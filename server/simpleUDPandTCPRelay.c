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
#include <pthread.h>

#define BUFSIZE		1024
#define SERVERPORT      10000
#define MAX_CONNECTIONS 10
#define NAME_LENGTH     8

#define min(a,b) ((a)<(b)?(a):(b))

struct sockaddr_in UDPServerAddr;
//static volatile int numberOfConnections = 0;

enum {
  TCP,
  UDP
} INET_PROTOCOLS;

/*
 * This serves to help organize our connections.
 * It contains - a remote IP address, a port number, a protocol (TCP or UDP),
 * a timestamp of last time data was received and a name for presentation.
 */
struct protocolAndAddress {
  int protocol;
  union {
    struct sockaddr_in UDPAddress;
    int TCPSocket;
  } endpoint;
  time_t lastHeardFrom;
  char name[NAME_LENGTH];
  int isReadonly;
  uint8_t use;
};

static struct protocolAndAddress connections[MAX_CONNECTIONS];

static volatile uint8_t go = 1;

static uint8_t UDPInBuffer[BUFSIZE];
static uint8_t TCPInBuffer[BUFSIZE];

static volatile int UDPInBufferCnt;
static volatile int TCPInBufferCnt;

static volatile int lastUDPReceiver;
static volatile int lastTCPReceiver;

pthread_mutex_t connectionListMutex;

pthread_mutex_t UDPBufferMutex;
pthread_mutex_t TCPBufferMutex;

pthread_cond_t UDPBufferCond;
pthread_cond_t TCPBufferCond;

static int UDPServerSocket;
static int TCPServerSocket;
static int TCPReadonlyServerSocket;

//int addr_size = sizeof(struct sockaddr_in);

int indexForNewConnection(void) {
  time_t oldest = time(NULL) + 1;
  int i;
  int result = -1;
  for (i=0; i<MAX_CONNECTIONS; i++) {
    if (!connections[i].use)
      return i;
    if (connections[i].lastHeardFrom < oldest) {
      result = i;
      oldest = connections[i].lastHeardFrom;
    }
  }
  return result;
}

int getNumberOfConnections(void) {
  int i;
  int result=0;
  for (i=0; i<MAX_CONNECTIONS; i++) {
    if (connections[i].use) result++;
  }
  return result;
}

int endpointName(struct protocolAndAddress* pa, uint8_t* buffer) {
  if (pa->protocol==TCP) {
    struct sockaddr_in sin;
    int len = sizeof(sin);
    if (getpeername(pa->endpoint.TCPSocket, (struct sockaddr *) &sin, &len))
      perror("getpeername");
    //inet_ntoa(sin.sin_addr);
    return sprintf(buffer, "%s:%d (%s)", inet_ntoa(sin.sin_addr), ntohs(sin.sin_port), pa->name);
  }
}

int fillConnectionList(uint8_t* buffer) {
  int i, offset=0;
  for (i=0; i<MAX_CONNECTIONS; i++) {
    if(connections[i].use) {
      offset += endpointName(connections+i, buffer+offset);
      offset += sprintf(buffer+offset, "\n");
    }
  }
  return offset;
}

int newConnectionGreeting(int index, uint8_t* buffer) {
  int offset;
  offset = sprintf(buffer, "Hello ");
  offset += endpointName(connections+index, buffer);
  offset += sprintf(buffer+offset, "\n");
  offset += fillConnectionList(buffer+offset);
  return offset;
}

void prepareUDPServerSocket(int portNum) {
  UDPServerSocket = socket(PF_INET, SOCK_DGRAM, 0);

  if (UDPServerSocket < 0) {
    perror("obtaining socket");
    abort();
  }
  
  UDPServerAddr.sin_family = AF_INET;
  UDPServerAddr.sin_port = htons(portNum);
  UDPServerAddr.sin_addr.s_addr = INADDR_ANY;

  // Set socket "file" to nonblocking I/O.
  int flags = fcntl(UDPServerSocket, F_GETFL);
  flags |= O_NONBLOCK;
  if (fcntl(UDPServerSocket, F_SETFL, flags)) {
    perror("setting nonblocking");
    abort();
  }

  // Bind to server port (hmm will this work when also binding to same port in TCP? It should).
  if ( bind(UDPServerSocket, (struct sockaddr*)&UDPServerAddr, sizeof(UDPServerAddr)) < 0) {
    perror("bind");
    abort();
  }
}

int prepareTCPServerSocket(int portNum) {
  int TCPServerSocket = socket(PF_INET, SOCK_STREAM, 0);

  if (TCPServerSocket < 0) {
    perror("obtaining TCP socket");
    abort();
  }
  
  struct sockaddr_in TCPServerAddr;
  TCPServerAddr.sin_family = AF_INET;
  TCPServerAddr.sin_port = htons(portNum);
  TCPServerAddr.sin_addr.s_addr = INADDR_ANY;

  // Set socket "file" to nonblocking I/O. WELL! Its the connections that should be nonblocking, not the server socket.
  // int flags = fcntl(TCPserverSocket, F_GETFL);
  // flags |= O_NONBLOCK;
  // fcntl(TCOserverSocket, F_SETFL, flags);

  if (bind(TCPServerSocket, (struct sockaddr*)&TCPServerAddr, sizeof(TCPServerAddr)) < 0) {
    perror("bind");
    abort();
  }

  if (listen(TCPServerSocket, MAX_CONNECTIONS)) {
    perror("listen");
    abort();
  }
  
  return TCPServerSocket;
}

int sendAnyProtocol(struct protocolAndAddress* destination, uint8_t* buffer, int length) {
  //  if (!destination->use) return 0;
  if (destination->protocol == UDP) {
    return (sendto(UDPServerSocket, buffer, length, 0, (struct sockaddr*)&destination->endpoint.UDPAddress, sizeof(struct sockaddr)) < 0);
  } else {
    return (send(destination->endpoint.TCPSocket, buffer, length, 0)<0);
  }
}

int sendAnyProtocolAutoclose(struct protocolAndAddress* destination, uint8_t* buffer, int length) {
  if(sendAnyProtocol(destination, buffer, length)) {
    destination->use=0;
    if (destination->protocol == UDP) {
    } else {
      close(destination->endpoint.TCPSocket);
    }
  }
}

void* transmitTask(void* nothing) {
  // Both buffers need to be sent to both sockets..
  static int transmittedBytes = 0;
  static int printedKilobytes = 0;
  while(go) {
    int i;
    pthread_mutex_lock(&connectionListMutex);
    pthread_mutex_lock(&UDPBufferMutex);
    if(UDPInBufferCnt) {
      transmittedBytes += UDPInBufferCnt;
      for (i=0; i<MAX_CONNECTIONS; i++) {
	if (!connections[i].use) continue;
	if (i!=lastUDPReceiver) { // do not loop back.
	  if (sendAnyProtocolAutoclose(connections+i, UDPInBuffer, UDPInBufferCnt)) {
	    perror("UDP relay");
	  }
	}
      }
      UDPInBufferCnt = 0;
      pthread_cond_signal(&UDPBufferCond);
    }
    pthread_mutex_unlock(&UDPBufferMutex);

    pthread_mutex_lock(&TCPBufferMutex);
    if (TCPInBufferCnt) {
      transmittedBytes += TCPInBufferCnt;
      for (i=0; i<MAX_CONNECTIONS; i++) {
	if (!connections[i].use) continue;
	if (i!=lastTCPReceiver) { // do not loop back.
	  if (sendAnyProtocolAutoclose(connections+i, TCPInBuffer, TCPInBufferCnt)) {
	    perror("TCP relay");
	  }
	}
      }
      TCPInBufferCnt = 0;
      pthread_cond_signal(&TCPBufferCond);
    }
    pthread_mutex_unlock(&TCPBufferMutex);
    pthread_mutex_unlock(&connectionListMutex);
    
    int kilobytes = transmittedBytes / 1024;
    if(kilobytes > printedKilobytes) {
      printf("Transfered %d kB\n", kilobytes);
      printedKilobytes = kilobytes;
    }
    usleep(1000);
  }
}

int searchExistingConnection(struct protocolAndAddress* candidate) {
  int i;
  for (i=0; i<MAX_CONNECTIONS; i++) {
    if (!connections[i].use) continue;
    if (candidate->protocol != connections[i].protocol) continue;
    if (candidate->protocol == UDP) {
      if (candidate->endpoint.UDPAddress.sin_family==connections[i].endpoint.UDPAddress.sin_family
	  &&
	  candidate->endpoint.UDPAddress.sin_port==connections[i].endpoint.UDPAddress.sin_port
	  &&
	  candidate->endpoint.UDPAddress.sin_addr.s_addr==connections[i].endpoint.UDPAddress.sin_addr.s_addr)
	return i;
    } else if (candidate->protocol == TCP) {
      if (candidate->endpoint.TCPSocket == connections[i].endpoint.TCPSocket) 
	return i;
    }
  }
  return -1;
}

// TODO: Still need to close connections that we kick out!
int indexOfNewConnection(struct protocolAndAddress* connection) {
  int connectionIndex = indexForNewConnection();
  connections[connectionIndex] = *connection;
  connections[connectionIndex].use = 1;
  printf("Now have %d connections.\n", getNumberOfConnections());
  return connectionIndex;
}

void* UDPReceiveAndConnectTask(void* nothing) {
  struct protocolAndAddress connection;
  connection.protocol = UDP;
  connection.isReadonly = 0;
    while(go) {
    struct sockaddr_in address;
    int addr_size = sizeof(struct sockaddr_in);
    int bytesRead;

    pthread_mutex_lock(&UDPBufferMutex);

    //int pthread_cond_init (pthread_cond_t *cond, pthread_condattr_t *condattr);
    //int pthread_cond_destroy (pthread_cond_t *cond);
    //int pthread_cond_wait (pthread_cond_t *condition, pthread_mutex_t *mutex); 

    while (UDPInBufferCnt) {
      // Buffer is not yet empty! Wait for that to occur first.
      pthread_cond_wait(&UDPBufferCond, &UDPBufferMutex);
    }

    // Get a datagram from anyone at all. This had better be nonblocking IO, or we will block holding the lock, no good!!
    bytesRead = recvfrom(UDPServerSocket, UDPInBuffer, BUFSIZE, 0, (struct sockaddr*)&address, &addr_size);
    if(bytesRead>=0) {
      // This is for the transmit task, to avoid returning data to its sender.
      UDPInBufferCnt = bytesRead;
      //      lastUDPReceiver = index;
    }
    pthread_mutex_unlock(&UDPBufferMutex);

    // Is there no error? (I assume a length of zero is OK, 
    // and means that a datagram of zero content length was received).
    if (bytesRead >= 0) {
      connection.endpoint.UDPAddress = address;

      pthread_mutex_lock(&connectionListMutex);
      int index = searchExistingConnection(&connection);
      if (index<0) {
	index = indexOfNewConnection(&connection);
	//getpeername(UDPServerSocket, (struct sockaddr*)&address, &addr_size);
	printf("Accepted a UDP connection: %s:%d\n", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
      }

      // TODO: This is outside of its exclusive access!
      lastUDPReceiver = index;
      connections[index].lastHeardFrom = time(NULL);
      pthread_mutex_unlock(&connectionListMutex);
    } 

    if (bytesRead < 0) {
      if (errno == EAGAIN || errno == EWOULDBLOCK) usleep(1000);
      else {
	perror("UDP recvfrom");
      }
    }
  }  
}

// This needs be nonblocking, and iterate over all TCP sockets...
void* TCPReceiveTask(void* nothing) {
  //struct protocolAndAddress connection;
  //connection.protocol = TCP;
  int numberOfBytes;
  while(go) {
    int i;
    
    for (i=0; i<MAX_CONNECTIONS; i++) {
	// If connections are not TCP, are read-only (which means write-only from this end) or are closed, skip them.
      if (connections[i].protocol != TCP || connections[i].isReadonly || !connections[i].use) continue;

      pthread_mutex_lock(&TCPBufferMutex);
      while (TCPInBufferCnt) {
	// Buffer is not yet empty! Wait for that to occur first.
	pthread_cond_wait(&TCPBufferCond, &TCPBufferMutex);
      }
      numberOfBytes = recv(connections[i].endpoint.TCPSocket, TCPInBuffer, BUFSIZE, 0); 
      if(numberOfBytes>0) {
	TCPInBufferCnt = numberOfBytes; // else there was no new data!
	lastTCPReceiver = i;
      }
      pthread_mutex_unlock(&TCPBufferMutex);
 
      pthread_mutex_lock(&connectionListMutex);
      if (numberOfBytes==0) {
	connections[i].use = 0;
	// TODO: Close socket locally, remove from list.
      } else if (numberOfBytes>0) {
	// This is for the transmit task, to avoid returning data to its sender.
	connections[i].lastHeardFrom = time(NULL);
      }
      pthread_mutex_unlock(&connectionListMutex);
      if (numberOfBytes<0) {
	if (errno==EAGAIN || numberOfBytes==EWOULDBLOCK) {
	  // do nothing.
	} else {
	  perror("TCP recv");
	}
      }
    }
    usleep(1000);
  }
}

void* TCPAcceptConnectionsTask(void* nothing) {
  struct protocolAndAddress newConnection;
  newConnection.protocol = TCP;
  newConnection.isReadonly = 0;
  while(go) {
    struct sockaddr_in newRemoteAddress;
    socklen_t addr_size = sizeof(struct sockaddr);
    int newTCPSocket = accept(TCPServerSocket, (struct sockaddr*)&newRemoteAddress, &addr_size);

    int flags = fcntl(newTCPSocket, F_GETFL);
    flags |= O_NONBLOCK;
    if (fcntl(newTCPSocket, F_SETFL, flags)) {
      perror("setting nonblocking");
    }

    struct sockaddr_in sin;
    int len = sizeof(sin);
    getpeername(newTCPSocket, (struct sockaddr*)&sin, &len);
    printf("Accepted a TCP connection: %s:%d\n", inet_ntoa(sin.sin_addr), ntohs(sin.sin_port));
    newConnection.endpoint.TCPSocket = newTCPSocket;

    pthread_mutex_lock(&connectionListMutex);
    int index =  indexOfNewConnection(&newConnection);
    pthread_mutex_unlock(&connectionListMutex);
  }
}

void* TCPAcceptReadonlyConnectionsTask(void* nothing) {
  struct protocolAndAddress newConnection;
  newConnection.protocol = TCP;
  newConnection.isReadonly = 1;
  while(go) {
    struct sockaddr_in newRemoteAddress;
    socklen_t addr_size = sizeof(struct sockaddr);
    int newTCPSocket = accept(TCPReadonlyServerSocket, (struct sockaddr*)&newRemoteAddress, &addr_size);

    int flags = fcntl(newTCPSocket, F_GETFL);
    flags |= O_NONBLOCK;
    if (fcntl(newTCPSocket, F_SETFL, flags)) {
      perror("setting nonblocking");
    }

    struct sockaddr_in sin;
    int len = sizeof(sin);
    getpeername(newTCPSocket, (struct sockaddr*)&sin, &len);
    printf("Accepted a readonly TCP connection: %s:%d\n", inet_ntoa(sin.sin_addr), ntohs(sin.sin_port));
    newConnection.endpoint.TCPSocket = newTCPSocket;

    pthread_mutex_lock(&connectionListMutex);
    int index =  indexOfNewConnection(&newConnection);
    pthread_mutex_unlock(&connectionListMutex);
  }
}

int main(int argc, char* argv[]) {
  int bytes_read;

  memset(connections, 0, sizeof(connections));

  int portNum, readonlyPortNum;

  if (argc<2) {
    portNum = SERVERPORT;
	readonlyPortNum = SERVERPORT + 1;
 } else {
    portNum = atoi(argv[1]);
	if (argc==2) {
		readonlyPortNum = atoi(argv[2]);
	} else {
		readonlyPortNum = portNum + 1;
	}
   }
   
   printf("Opening TCP and UDP read/write on port %d and TCP readonly on port %d\n", portNum, readonlyPortNum);
  
  pthread_mutex_init(&connectionListMutex, 0);

  pthread_mutex_init(&UDPBufferMutex, 0);
  pthread_mutex_init(&TCPBufferMutex, 0);

  pthread_cond_init(&UDPBufferCond, 0);
  pthread_cond_init(&TCPBufferCond, 0);

  prepareUDPServerSocket(portNum);
  TCPServerSocket = prepareTCPServerSocket(portNum);
  TCPReadonlyServerSocket = prepareTCPServerSocket(readonlyPortNum);

  pthread_t UDPReceiverThread;
  pthread_t TCPReceiverThread;
  pthread_t TCPAcceptorThread;
  pthread_t TCPReadonlyAcceptorThread;
  pthread_t transmitterThread;
  
  pthread_create(&UDPReceiverThread, NULL, UDPReceiveAndConnectTask, NULL);
  pthread_create(&TCPReceiverThread, NULL, TCPReceiveTask, NULL);
  pthread_create(&TCPAcceptorThread, NULL, TCPAcceptConnectionsTask, NULL);
  pthread_create(&TCPReadonlyAcceptorThread, NULL, TCPAcceptReadonlyConnectionsTask, NULL);
  pthread_create(&transmitterThread, NULL, transmitTask, NULL);

  pthread_join(transmitterThread, NULL);

  return 0;
}

/*
 * We have 2 producers (of data) and one consumer. When one producer has produced some data, 
 * both should block until the consumer has consumed the data.
 * When there is no data in the buffer, the producers should take turns to obtain locks on the buffer. The consumer should be blocked.
 * Guess we are going to have a var indicating if there is data in the buffer, and use broadcase aka notifyAll when a producer
 * has gotten data. Producers enter on a check that the buffer is empty, and the consumer on that it is not empty.
 * Special problem: TCP data retransmitted as UDP should still be one MavLink message per UDP datagram. Either that should be transmitted
 * in separate frames (does Mission Planner do that? Drones wont be using TCP anyway) or a server side parser should check boundaries to
 * break at. That implies a separate buffer for that.

 * Missing: Mutexes on the connection list.
 * Start up with init of TCP socket and forking
 * ???
 */

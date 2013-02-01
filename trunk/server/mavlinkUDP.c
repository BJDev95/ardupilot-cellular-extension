/* mavlnkUDP.c:
 * Mavlink serial to UDP relay for use onboard airplane.
 * Will communicate with simpeUDPRelay.
 */
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <inttypes.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sgtty.h>
#include "include/v1.0/common/mavlink.h"

/*
 * This function reports the error and
 * exits back to the shell:
 */
static void bail(const char *on_what) {
   fputs(strerror(errno),stderr);
   fputs(": ",stderr);
   fputs(on_what,stderr);
   fputc('\n',stderr);
   //   system ("/bin/stty cooked");
   exit(1);
}

#define SERVERPORT      40001

struct sockaddr_in serverAddr;
int sock;

int socketInputFD, socketOutputFD;
int serialInputFD = 0;  // just use stdin as default
int serialOutputFD = 1; // just use stdout as default

#define MAX_MESSAGE_LENGTH 1024

uint8_t serialToUDP = 1;
uint8_t UDPToSerial = 1;

#define BUFFER_LENGTH (MAX_MESSAGE_LENGTH * 1)
uint8_t serialReadBuffer[BUFFER_LENGTH];
uint8_t serialWriteBuffer[BUFFER_LENGTH];
int serialReadBufferIn=0, serialReadBufferOut=0;
int serialWriteBufferIn=0, serialWriteBufferOut=0;

uint8_t UDPReceivedBuffer[MAX_MESSAGE_LENGTH];
uint8_t UDPTransmitBuffer[MAX_MESSAGE_LENGTH];

pid_t serialReceiverThreadPID;

mavlink_message_t mavlinkMsg;
mavlink_status_t mavlinkStatus;

int mavlink_parse(uint8_t c) {
  uint8_t messageComplete = mavlink_parse_char(0, c, &mavlinkMsg, &mavlinkStatus);
  //  if (messageComplete) printf("Got a message: %d\n", mavlinkMsg.msgid);
  return messageComplete;
}

void sendUDP(int length) {
  int numberOfBytes;
  numberOfBytes = 
    sendto(sock,
	   UDPTransmitBuffer,
	   length,
	   0,               /* Flags: no options */
	   (struct sockaddr *)&serverAddr,/* addr */
	   sizeof(struct sockaddr_in));  /* Server address length */
  if (numberOfBytes < 0)
    ; // ignore error.
}

void serialReadThread(void) {
  int UDPTransmitBufferLength;
  while(serialToUDP) {
    int c;
    c=getchar();
    if (c>=0) {
      //      printf("got char: %d\n", c);
      serialReadBuffer[serialReadBufferIn] = c;
      serialReadBufferIn = (serialReadBufferIn+1) % BUFFER_LENGTH;
      if(mavlink_parse(c)) {
	if(serialReadBufferIn >= serialReadBufferOut) {
	  // Non-wrap-round case.
	  memcpy(UDPTransmitBuffer, serialReadBuffer+serialReadBufferOut, serialReadBufferIn-serialReadBufferOut);
	  UDPTransmitBufferLength = serialReadBufferIn-serialReadBufferOut;
	} else {
	  // wrap-round case, eg. 
	  // 01234567
	  // xx---xxx
	  // in = 2, out=5
	  // copy 5-7 (3 bytes) to 0-2 and 0-2 (2 bytes) to 3-4
	  memcpy(UDPTransmitBuffer, serialReadBuffer+serialReadBufferOut, BUFFER_LENGTH-serialReadBufferOut);
	  memcpy(UDPTransmitBuffer+BUFFER_LENGTH-serialReadBufferOut, serialReadBuffer, serialReadBufferIn);
	  UDPTransmitBufferLength = BUFFER_LENGTH-serialReadBufferOut + serialReadBufferIn;
	}
	sendUDP(UDPTransmitBufferLength); // Will block until send, so afterwards the UDPBuffer is available again.
	// empty the buffer.
	serialReadBufferOut = serialReadBufferIn;
      }
    }
  }
}

int socketsEqual(struct sockaddr_in* a, struct sockaddr_in* b) {
  return a->sin_family==b->sin_family
    &&
    a->sin_port==b->sin_port
    &&
    a->sin_addr.s_addr==b->sin_addr.s_addr;
}

int receiveUDP(void) {
  int numberOfBytes;
  struct sockaddr_in fromAddr;
  int fromAddrSize = sizeof fromAddr;

  while(1) {
    numberOfBytes = recvfrom(sock,
		 UDPReceivedBuffer,
		 BUFFER_LENGTH,
		 0,
		 (struct sockaddr *)&fromAddr,
		 &fromAddrSize);
    if (numberOfBytes>=0) {
      if (!socketsEqual(&fromAddr, &serverAddr)) {
	fprintf(stderr, "Got a datagram from a different host than connected to; ignoring!\n");
      } else {
	return numberOfBytes;
      }
    }
  }
}

void serialWriteThread(void) {
  while(UDPToSerial) {
    int UDPReceivedBufferLength;
    // Hopefully!! the stack will buffer a few datagrams.
    if ((UDPReceivedBufferLength = receiveUDP())) {
      if(serialWriteBufferIn + UDPReceivedBufferLength < BUFFER_LENGTH) {
	// All fits in one go.
	memcpy(serialWriteBuffer+serialWriteBufferIn, UDPReceivedBuffer, UDPReceivedBufferLength);
      } else {
      	memcpy(serialWriteBuffer+serialWriteBufferIn, UDPReceivedBuffer, BUFFER_LENGTH-serialWriteBufferIn);
	memcpy(serialWriteBuffer, UDPReceivedBuffer+BUFFER_LENGTH-serialWriteBufferIn, UDPReceivedBufferLength-BUFFER_LENGTH+serialWriteBufferIn);
      }
      serialWriteBufferIn = (serialWriteBufferIn+UDPReceivedBufferLength) % BUFFER_LENGTH;
    }
    
    if (serialWriteBufferIn > serialWriteBufferOut) {
      do {
	// Non-wrap-round case.
	serialWriteBufferOut += write(STDOUT_FILENO, serialWriteBuffer+serialWriteBufferOut, serialWriteBufferIn-serialWriteBufferOut);
	serialWriteBufferOut %= BUFFER_LENGTH;
      } while(serialWriteBufferIn > serialWriteBufferOut);
    } else if (serialWriteBufferIn < serialWriteBufferOut) {
      // wrap-round case
      while(serialWriteBufferOut < BUFFER_LENGTH) {
	serialWriteBufferOut += write(STDOUT_FILENO, serialWriteBuffer+serialWriteBufferOut, BUFFER_LENGTH-serialWriteBufferOut);
      }
      serialWriteBufferOut=0;
      while(serialWriteBufferOut < serialWriteBufferIn) {
	serialWriteBufferOut += write(STDOUT_FILENO, serialWriteBuffer+serialWriteBufferOut, serialWriteBufferIn-serialWriteBufferOut);
      }
    } else usleep(1000);
  }
}

void startProcesses(void) {
  int i;

  i = fork();

  if (i) {
    //serialReceiverThreadPID = getpid();
    serialReadThread();
  } else {
    //serialTransmitterThreadPID = getpid();
    serialWriteThread();
  }
}

int main(int argc,char **argv) {
  char* serverIPAddress;
  int serverPort;
  int myPort;
  //  char myName[8];
  struct sockaddr_in myAddr;
  int addr_size = sizeof serverAddr;

  freopen(NULL, "rb", stdin);
  
  memset(&serverAddr, 0, addr_size);

  if (argc >= 4) {
    // the server address
    serverIPAddress = argv[1];

    // the server port
    serverPort = atoi(argv[2]);

    // the local port
    myPort = atoi(argv[3]);
    
    // the client name
    /*
    if (argc >= 5) {
      int l = strlen(argv[4]);
      memcpy(myName, argv[4], l);
      memset(myName+l, ' ', 8-l);
    } else {
      sprintf(myName, "DEFAULT ");
    }
    */
  } else {
    bail("Usage: mavlinkUDP <serveraddress> <serverport> <myport>");
  }

  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(serverPort);
  serverAddr.sin_addr.s_addr = inet_addr(serverIPAddress);
  if (serverAddr.sin_addr.s_addr == INADDR_NONE )
    bail("bad server address.");
  
  /*
   * Create a UDP socket for server comms:
   */
  sock = socket(AF_INET,SOCK_DGRAM,0);
  if (sock == -1)
    bail("socket()");
  
  // Set socket "file" to nonblocking I/O.
  // int flags = fcntl(s, F_GETFL);
  // flags |= O_NONBLOCK;
  // fcntl(s, F_SETFL, flags);
  
  /*
   * Bind client to the given source port (important for knocking holes through NATs
   * Actually client binding is not necessary onboard the UAV. We still want to
do it though, so we can make new "connections" just the see if the server gets them.
  */
  myAddr.sin_family = AF_INET;
  myAddr.sin_port = htons(myPort);
  myAddr.sin_addr.s_addr = INADDR_ANY;

  if (bind(sock, (struct sockaddr*)&myAddr, addr_size) < 0) {
    bail("bind");
    abort();
  }
  
  startProcesses();
  while(1);
  //  system ("/bin/stty cooked");
  return 0;
}

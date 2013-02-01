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
#define MAX_REMOTES     10
#define NAME_LENGTH     8

struct sockaddr_in serverAddr;
int sock;

int socketInputFD, socketOutputFD;
int serialInputFD = 0;  // just use stdin as default
int serialOutputFD = 1; // just use stdout as default

#define MAX_MESSAGE_LENGTH 1024

/*
#define MESSAGE_BUFFER_SIZE 2

volatile uint8_t serialToUDPBuffer[MESSAGE_BUFFER_SIZE][MAX_MESSAGE_LENGTH];
volatile size_t serialToUDPBufferLengths[MESSAGE_BUFFER_SIZE];
volatile int serialToUDPIn = 0;
volatile int serialToUDPOut = 0;

volatile uint8_t UDPToSerialBuffer[MESSAGE_BUFFER_SIZE][MAX_MESSAGE_LENGTH];
volatile size_t UDPToSerialBufferLengths[MESSAGE_BUFFER_SIZE];
volatile int UDPToSerialIn = 0;
volatile int UDPToSerialOut = 0;
*/

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

/*
static struct sgttyb savemodes;
static int havemodes = 0;

int tty_break() {
  struct sgttyb modmodes;
  if(ioctl(fileno(stdin), TIOCGETP, &savemodes) < 0)
    return -1;
  havemodes = 1;
  modmodes = savemodes;
  modmodes.sg_flags |= CBREAK;
  return ioctl(fileno(stdin), TIOCSETN, &modmodes);
}

int tty_getchar() {
  return getchar();
}

int tty_fix() {
  if(!havemodes)
    return 0;
  return ioctl(fileno(stdin), TIOCSETN, &savemodes);
}
*/

int mavlink_parse(uint8_t c) {
  static int totalBytesParsed = 0;
  if ((++totalBytesParsed % 1024) == 0)
    printf("Received %d kB\n", totalBytesParsed/1024);
  uint8_t messageComplete = mavlink_parse_char(0, c, &mavlinkMsg, &mavlinkStatus);
  if (messageComplete) printf("Got a message: %d\n", mavlinkMsg.msgid);
  //  else if (mavlinkStatus
  return messageComplete;
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

void sniffUDPPackets(void) {
  int i;
  while(UDPToSerial) {
    int UDPReceivedBufferLength;
    if ((UDPReceivedBufferLength = receiveUDP())) {
      for (i=0; i<UDPReceivedBufferLength; i++) {
	mavlink_parse(UDPReceivedBuffer[i]);
      }
    } else usleep(1000);
  }
}

int main(int argc,char **argv) {
  char* serverIPAddress;
  int serverPort;
  int myPort;
  //  char myName[8];
  struct sockaddr_in myAddr;
  int addr_size = sizeof serverAddr;

  memset(&serverAddr, 0, addr_size);

  if (argc >= 4) {
    // the server address
    serverIPAddress = argv[1];

    // the server port
    serverPort = atoi(argv[2]);

    // the local port
    myPort = atoi(argv[3]);
    
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
  
  sniffUDPPackets();
  return 0;
}

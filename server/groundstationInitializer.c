/* 
 * groundstationInitilizer.c:
 * Serves to send a first datagram to the UDP distributor server, punching a hole in NATs and getting the local host
 * on the distributor server's distribution list.
 * It should be possible to bind this to a local port simulatenously with a groundstation software. If not, it does
 * not really matter so much, as long as we get the host on the server's distribution list.
 */
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <time.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>

#define min(a,b) ((a)<(b)?(a):(b))

/*
 * This function reports the error and
 * exits back to the shell:
 */
static void
bail(const char *on_what) {
    fputs(strerror(errno),stderr);
    fputs(": ",stderr);
    fputs(on_what,stderr);
    fputc('\n',stderr);
    exit(1);
}

int main(int argc,char **argv) {
  char* serverIPAddress;
  int serverPort;
  int myPort;
  char myName[8];
  uint8_t buffer[1024];
  struct sockaddr_in serverAddr, myAddr;
  struct sockaddr_in receivedServerAddr;
  int addr_size = sizeof serverAddr;
  int sock;  

  if (argc >= 4) {
    // the server address
    serverIPAddress = argv[1];
    
    // the server port
    serverPort = atoi(argv[2]);
    
    // the local port
    myPort = atoi(argv[3]);
    
    // the client name
    if (argc >= 5) {
      int l = strlen(argv[4]);
      memcpy(myName, argv[4], l);
      memset(myName+l, ' ', 8-l);
    } else {
      sprintf(myName, "DEFAULT ");
    }
  } else {
    bail("Usage: mavlinkUDP <serveraddress> <serverport> <myport> [clientname]");
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
  
  memset(buffer, 32, 8);
  memcpy(buffer, myName, min(8, strlen(myName)));
  
  int z = sendto(sock,
		 buffer,
		 8,
		 0,
		 (struct sockaddr *)&serverAddr,
		 addr_size);
  if ( z < 0 )
    bail("sendto(2)");
  
  /*
   * Wait for a response:
   */
  int x = sizeof receivedServerAddr;
  z = recvfrom(sock,
	       buffer,           /* Receiving buffer */
	       sizeof buffer,   /* Max recv buf size */
	       0,               /* Flags: no options */
	       (struct sockaddr *)&receivedServerAddr,     /* Addr */
	       &x);             /* Addr len, in & out */
  
  if (z < 0) {
    bail("recvfrom(2)");
  }
  
  buffer[z] = 0;          /* null terminate */

  /*
   * Report Result:
   */
  printf("Result from %s port %u :\n'%s'\n",
	 inet_ntoa(receivedServerAddr.sin_addr),
	 (unsigned)ntohs(receivedServerAddr.sin_port),
	 buffer);

  /*
   * Close the socket and exit:
   */
  close(sock);
  putchar('\n');
}

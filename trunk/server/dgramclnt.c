/* dgramclnt.c:
 *
 * Example datagram client:
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

#define PORT 10000

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

int
main(int argc,char **argv) {
    int z;
    int x; 
    char *srvr_addr = NULL;
    char my_name[8];
    struct sockaddr_in adr_srvr;/* AF_INET */
    struct sockaddr_in adr_my;  /* AF_INET */
    struct sockaddr_in adr;     /* AF_INET */
    int len_inet;                /* length  */
    int serverPort;
    int s;                         /* Socket */
    char dgram[512];        /* Recv buffer */

    /*
     * Use a server address from the command
     * line, if one has been provided.
     * Otherwise, this program will default
     * to using the arbitrary address
     * 127.0.0.1:
     */
    if ( argc >= 2 ) {
        /* Addr on cmdline: */
        srvr_addr = argv[1];
    } else {
        /* Use default address: */
        srvr_addr = "127.0.0.1";
    }

    if (argc >= 3) {
      serverPort = atoi(argv[2]);
    } else serverPort = 10000;

    if ( argc >= 4 ) {
      z = strlen(argv[3]);
      memcpy(my_name, argv[3], z);
      memset(my_name+z, ' ', 8-z);
    } else {
      sprintf(my_name, "DEFAULT ");
    }

    /*
     * Create a socket address, to use
     * to contact the server with:  
     */
    memset(&adr_srvr, 0, sizeof adr_srvr);
    adr_srvr.sin_family = AF_INET;
    adr_srvr.sin_port = htons(serverPort);
    adr_srvr.sin_addr.s_addr = inet_addr(srvr_addr);

    adr_my.sin_family = AF_INET;
    adr_my.sin_port = htons(PORT);
    adr_my.sin_addr.s_addr = INADDR_ANY;

    if ( adr_srvr.sin_addr.s_addr == INADDR_NONE )
        bail("bad address.");

    len_inet = sizeof adr_srvr;

    /*
     * Create a UDP socket to use:
     */
    s = socket(AF_INET,SOCK_DGRAM,0);
    if ( s == -1 )
        bail("socket()");

    // Set socket "file" to nonblocking I/O.
    int flags = fcntl(s, F_GETFL);
    flags |= O_NONBLOCK;
    fcntl(s, F_SETFL, flags);

    if (bind(s, (struct sockaddr*)&adr_my, len_inet) < 0) {
      bail("bind");
      abort();
    }

    for (;;) {
      sleep(1);
        /*
         * Prompt user for a date format string:
         */

        /*
         * Send format string to server:
         */
      memset(dgram, 32, 8);
      z = sprintf(dgram, "%s",  my_name); //"Hello from: %s:%d", inet_ntoa(adr_my.sin_addr), ntohs(adr_my.sin_port));
      
        z = sendto(s,   /* Socket to send result */
            dgram, /* The datagram result to snd */
            8, /* The datagram length */
            0,               /* Flags: no options */
            (struct sockaddr *)&adr_srvr,/* addr */
            len_inet);  /* Server address length */
        if ( z < 0 )
            bail("sendto(2)");

        /*
         * Wait for a response:
         */
        x = sizeof adr;
        z = recvfrom(s,                /* Socket */
            dgram,           /* Receiving buffer */
            sizeof dgram,   /* Max recv buf size */
            0,               /* Flags: no options */
            (struct sockaddr *)&adr,     /* Addr */
            &x);             /* Addr len, in & out */
	
        if (z < 0) {
	  if (errno == EAGAIN) continue;
	  else bail("recvfrom(2)");
	}

        dgram[z] = 0;          /* null terminate */

        /*
         * Report Result:
         */
        printf("Result from %s port %u :\n'%s'\n",
            inet_ntoa(adr.sin_addr),
            (unsigned)ntohs(adr.sin_port),
            dgram);
    }

    /*
     * Close the socket and exit:
     */
    close(s);
    putchar('\n');

    return 0;
}

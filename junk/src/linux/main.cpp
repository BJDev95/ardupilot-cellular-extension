#include "MobileStream.h"
#include <unistd.h>
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
#include "include/v1.0/common/mavlink.h"
#include <termios.h>
//#include <asm-generic/termbits.h>

int modemfd;
int ardupilotfd;

pthread_mutex_t everythingMutex;
BufferedStream* modem;
BufferedStream* userdata;
// MobileStream* mobileStream;

#define BUFSIZE 16

/*
 * Forward serial input from modem to serialX buffer. Simulate what Ardupilot interrupt does.
 */
void* modemInputDriver(void* nothing) {
	uint8_t buf[BUFSIZE];
	int i, j;
	while (true) {
		pthread_mutex_lock(&everythingMutex);
		i = read(modemfd, buf, sizeof(buf));
		for (j = 0; j < i; j++) {
			printf("%c", buf[i]);
			modem->backend_write(buf[j]);
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) (1000.0 * (1 / 10.0)));
	}
	return NULL;
}

/*
 * Forward serial input from modem to serialX buffer. Simulate what Ardupilot interrupt does.
 */
void* modemOutputDriver(void* nothing) {
	uint8_t buf[1];
	while (true) {
		pthread_mutex_lock(&everythingMutex);
		if (modem->backend_available()) {
			buf[0] = modem->backend_read();
			write(modemfd, buf, 1);
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) (1000.0 * (1 / 60.0)));
	}
	return NULL;
}

/*
 * Forward serial input from ardupilot to userdata buffer..
 * TODO: Chunking to complete MAVLink messages.
 */
void* ardupilotInputDriver(void* nothing) {
	uint8_t buf[BUFSIZE];
	uint8_t messageBuf[1024];
	uint8_t messageLen = 0;

	mavlink_status_t status;
	mavlink_message_t message;
	int channel = 0;

	int i, j, k;

	while (true) {
		pthread_mutex_lock(&everythingMutex);
		i = read(ardupilotfd, buf, sizeof(buf));
		printf("%d\n", i);
		for (j = 0; j < i; j++) {
			printf("%c", buf[j]);
			messageBuf[messageLen++] = buf[i];
			if (mavlink_parse_char(channel, buf[i], &message, &status)) {
				for (k = 0; k < messageLen; k++) {
					userdata->backend_write(messageBuf[k]);
				}
				messageLen = 0;
			}
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) (1000.0 * (1 / 10.0)));
	}
	return NULL;
}

/*
 * Forward serial input from userdata buffer to ardupilot.
 */
void* ardupilotOutputDriver(void* nothing) {
	uint8_t buf[1];
	while (true) {
		pthread_mutex_lock(&everythingMutex);
		if (userdata->backend_available()) {
			buf[0] = userdata->backend_read();
			write(ardupilotfd, buf, 1);
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) (1000.0 * (1 / 60.0)));
	}
	return NULL;
}

/*
 * Forward serial input from modem to serialX buffer. Simulate what Ardupilot interrupt does.
 */
void ardupilotLoopSimulation() {

}

void setupPort(int fd) {
	fcntl(fd, F_SETFL, FNDELAY);

	struct termios options;
	fcntl(fd, F_SETFL, FNDELAY);
	tcgetattr(fd, &options);

	cfsetspeed(&options, B57600);
	cfmakeraw(&options);
	tcsetattr(fd, TCSANOW, &options);
}

int /* O - 0 = MODEM ok, -1 = MODEM bad */
init_modem(int fd) /* I - Serial port file */
{
	char buffer[255]; /* Input buffer */
	char *bufptr; /* Current char in buffer */
	int nbytes; /* Number of bytes read */
	int tries; /* Number of tries so far */

    fcntl(fd, F_SETFL, 0);

    struct termios options;
    /* get the current options */
    tcgetattr(fd, &options);

    /* set raw input, 1 second timeout */
    options.c_cflag     |= (CLOCAL | CREAD);
    options.c_lflag     &= ~(ICANON | ECHO | ECHOE | ISIG);
    options.c_oflag     &= ~OPOST;
    options.c_cc[VMIN]  = 0;
    options.c_cc[VTIME] = 10;

    /* set the options */
    tcsetattr(fd, TCSANOW, &options);

    int retc;

	for (tries = 0; tries < 3; tries++) {
		/* send an AT command followed by a CR */
		if ((retc = write(fd, "AT\r", 3)) < 0) {
			printf("ERROR: %d\n", retc);
		}
		if (retc < 3)
			continue;

		/* read characters into our string buffer until we get a CR or NL */
		bufptr = buffer;
		while ((nbytes = read(fd, bufptr, buffer + sizeof(buffer) - bufptr - 1))
				> 0) {
			bufptr += nbytes;
			if (bufptr[-1] == '\n' || bufptr[-1] == '\r')
				break;
		}

		/* nul terminate the string and see if we got an OK response */
		*bufptr = '\0';

		if (strncmp(buffer, "OK", 2) == 0)
			return (0);
	}

	return (-1);
}

int main(void) {
	MobileStream *mobile;

	BufferedStream::Buffer __Modem__rxBuffer;
	BufferedStream::Buffer __Modem__txBuffer;

	modem = new BufferedStream(&__Modem__rxBuffer, &__Modem__txBuffer);
	modem->begin(128, 256);

	mobile = new MobileStream(modem);
	mobile->begin(128, 256);

	userdata = &mobile->userdata;

	pthread_mutex_init(&everythingMutex, 0);

	pthread_t modemInputThread;
	pthread_t modemOutputThread;
	pthread_t ardupilotInputThread;
	pthread_t ardupilotOutputThread;

	system(
			"stty -F /dev/ttyUSB1 raw ispeed 57600 ospeed 57600 -ignpar cs8 -cstopb");
	modemfd = open("/dev/ttyS1", O_RDWR | O_NOCTTY | O_NDELAY);
	//setupPort(modemfd);

	system(
			"stty -F /dev/ttyUSB0 raw ispeed 57600 ospeed 57600 -ignpar cs8 -cstopb");
	ardupilotfd = open("/dev/ttyS0", O_RDWR | O_NOCTTY | O_NDELAY);
	//setupPort(ardupilotfd);

	init_modem(ardupilotfd);

	exit(1);

	if (modemfd > 0 && ardupilotfd > 0) {
		pthread_create(&modemInputThread, NULL, modemInputDriver, NULL);
		pthread_create(&modemOutputThread, NULL, modemOutputDriver, NULL);
		pthread_create(&ardupilotInputThread, NULL, ardupilotInputDriver, NULL);
		pthread_create(&ardupilotOutputThread, NULL, ardupilotOutputDriver,
				NULL);
	} else {
		perror("open_port: Unable to open /dev/ttyS0 - ");
	}
	while (true) {
		pthread_mutex_lock(&everythingMutex);
		mobile->task();
		pthread_mutex_unlock(&everythingMutex);
		usleep(1000 * 20);
	}
}

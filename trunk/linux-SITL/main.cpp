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
#include <signal.h>
//#include <asm-generic/termbits.h>

bool run;

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
	while (run) {
		i = read(modemfd, buf, sizeof(buf));
		if (i < 0)
			printf("Error at modem input: %d\n", i);
		else if (i > 0) {
			pthread_mutex_lock(&everythingMutex);
			for (j = 0; j < i; j++) {
				modem->backend_write(buf[j]);
				printf("%c", buf[j]);
			}
			pthread_mutex_unlock(&everythingMutex);
		}
		usleep((int) (100.0));
	}
	return NULL;
}

/*
 * Forward serial input from modem to serialX buffer. Simulate what Ardupilot interrupt does.
 */
void* modemOutputDriver(void* nothing) {
	uint8_t buf[1];
	while (run) {
		pthread_mutex_lock(&everythingMutex);
		if (modem->backend_available()) {
			buf[0] = modem->backend_read();
			printf("%c", buf[0]);
			int i = write(modemfd, buf, 1);
			if (i < 0)
				printf("Error at modem output: %d\n", i);
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) (100.0));
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

	while (run) {
		i = read(ardupilotfd, buf, sizeof(buf));
		if (i < 0)
			printf("Error at ardupilot input: %d\n", i);
		else {
			pthread_mutex_lock(&everythingMutex);
			for (j = 0; j < i; j++) {
				//printf("%c", buf[j]);
				messageBuf[messageLen++] = buf[j];
				if (mavlink_parse_char(channel, buf[j], &message, &status)) {
					printf("m");
					for (k = 0; k < messageLen; k++) {
						userdata->write(messageBuf[k]);
					}
					messageLen = 0;
				}
			}
			pthread_mutex_unlock(&everythingMutex);
		}
		usleep((int) (100.0));
	}
	return NULL;
}

/*
 * Forward serial input from userdata buffer to ardupilot.
 */
void* ardupilotOutputDriver(void* nothing) {
	uint8_t buf[1];
	while (run) {
		pthread_mutex_lock(&everythingMutex);
		if (userdata->available()) {
			buf[0] = userdata->read();
			int i = write(ardupilotfd, buf, 1);
			if (i < 0)
				printf("Error at ardupilot output: %d\n", i);
		}
		pthread_mutex_unlock(&everythingMutex);
		usleep((int) 100.0);
	}
	return NULL;
}

/*
 * Forward serial input from modem to serialX buffer. Simulate what Ardupilot interrupt does.
 */
void ardupilotLoopSimulation() {

}

void /* O - 0 = MODEM ok, -1 = MODEM bad */
init_modem(int fd) /* I - Serial port file */
{
//	fcntl(fd, F_SETFL, 0);
	fcntl(fd, F_SETFL, FNDELAY);

	struct termios options;
	/* get the current options */
	tcgetattr(fd, &options);

	/* set raw input, 1 second timeout */
	options.c_cflag |= (CLOCAL | CREAD);
	options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
	options.c_oflag &= ~OPOST;
	options.c_cc[VMIN] = 0;
	options.c_cc[VTIME] = 10;

	cfsetspeed(&options, B57600);

	/* set the options */
	tcsetattr(fd, TCSANOW, &options);
}

typedef void (*sig_t) (int);

void terminate(int func) {
	run = false;
	printf("Got signal to terminate\n");
	usleep(1500000L);
}

int main(void) {
	MobileStream *mobile;

	modemfd = open("/dev/tty.usbserial-FTE1XHTP", O_RDWR | O_NOCTTY | O_NDELAY);
	init_modem(modemfd);

	ardupilotfd = open("/dev/tty.usbserial-FTDZICJM",
			O_RDWR | O_NOCTTY | O_NDELAY);
	init_modem(ardupilotfd);

	BufferedStream::Buffer __Modem__rxBuffer;
	BufferedStream::Buffer __Modem__txBuffer;

	run = true;
	signal(SIGABRT, terminate);
	signal(SIGINT, terminate);
	signal(SIGTERM, terminate);

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

	if (modemfd > 0 && ardupilotfd > 0) {
		pthread_create(&modemInputThread, NULL, modemInputDriver, NULL);
		pthread_create(&modemOutputThread, NULL, modemOutputDriver, NULL);
		pthread_create(&ardupilotInputThread, NULL, ardupilotInputDriver, NULL);
		pthread_create(&ardupilotOutputThread, NULL, ardupilotOutputDriver,
				NULL);
	} else {
		perror("open_port: Unable to open /dev/ttyS0 - ");
	}

	while (run) {
		pthread_mutex_lock(&everythingMutex);
		mobile->task();
		pthread_mutex_unlock(&everythingMutex);
		usleep(1000 * 20);
	}
}

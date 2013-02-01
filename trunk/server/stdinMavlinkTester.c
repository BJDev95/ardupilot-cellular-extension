/* mavlnkUDP.c:
 * Mavlink serial to UDP relay for use onboard airplane.
 * Will communicate with simpeUDPRelay.
 */
#include <stdio.h>
#include <stdlib.h>
#include "include/v1.0/common/mavlink.h"


mavlink_message_t mavlinkMsg;
mavlink_status_t mavlinkStatus;

int mavlink_parse(uint8_t c) {
  static int totalBytesParsed = 0;
  if ((++totalBytesParsed % 1024) == 0)
    printf("Received %d kB\n", totalBytesParsed/1024);
  uint8_t messageComplete = mavlink_parse_char(0, c, &mavlinkMsg, &mavlinkStatus);
  if (messageComplete) printf("Got a message: %d\n", mavlinkMsg.msgid);
  //  else if (mavlinkStatus
  return messageComplete;
}

void sniffStdin(void) {
  int i;
  while(1) {
    mavlink_parse(getchar());
  }
}

int main(int argc,char **argv) {
  freopen(NULL, "rb", stdin);
  sniffStdin();
  return 0;
}

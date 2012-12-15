// -*- tab-width: 4; Mode: C++; c-basic-offset: 4; indent-tabs-mode: nil -*-

// This file is just a placeholder for your configuration file.  If
// you wish to change any of the setup parameters from their default
// values, place the appropriate #define statements here.

#define CONFIG_APM_HARDWARE APM_HARDWARE_APM2

// Ordinary users should please ignore the following define.
// APM2_BETA_HARDWARE is used to support early (September-October 2011) APM2
// hardware which had the BMP085 barometer onboard. Only a handful of
// developers have these boards.
//#define APM2_BETA_HARDWARE

// The following are the recommended settings for Xplane
// simulation. Remove the leading "/* and trailing "*/" to enable:

//#define HIL_MODE            HIL_MODE_DISABLED

/*
 *  // HIL_MODE SELECTION
 *  //
 *  // Mavlink supports
 *  // 1. HIL_MODE_ATTITUDE : simulated position, airspeed, and attitude
 *  // 2. HIL_MODE_SENSORS: full sensor simulation
 *  //#define HIL_MODE            HIL_MODE_ATTITUDE
 *
 */

// For using a DroneCell or similar on UART3(APM1) or UART2(APM2). 
// Baud rate will be that ser for Serial3 on both APM1 and APM2!
#define TELEMETRY_UART2 MOBILE

// For direct wireless telemetry on UART3(APM1) or UART2(APM2).
// Baud rate will be that ser for Serial3 on both APM1 and APM2!
// #define TELEMETRY_UART2 MOBILE

// For having telemetry only on UART0 (all APM versions). APM2 will
// still use its multiplexing feature on UART0:
// #define TELEMETRY_UART2 MOBILE

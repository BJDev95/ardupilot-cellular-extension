These are the ArduPlane 2.68 source files modified for addition of the dongfang ardupilot cellular extension. They can be copied to overwrite the files of the same names in the ArduPlane sketch.
The modified libraries/FastSerial source files of the extension also need to be copied to libraries/FastSerial of the Ardupilot sources.
Of course, a better ultimate way to get this extension integrated would be to have it included into the ArduPilot repository and distrubution.

NOTE that Ardupilot 2.68 requires Arduio 1.0.2. With older versions, one will get lots of "strange" compilation errors.

About the #defines CONFIG_APM_HARDWARE, TELEMETRY_UART2 and USB_MUX_PIN: The semantics in ArduPilot is a little confusing. I have taken the liberty to change it a little. It might have broken some original intension. There is still some TO-DO there.


Completely manual integration step by step:
-------------------------------------------

This describes all changes made to ArduPlane to add this cellular extension. If you don't care to do all the manual editing described, see "Integration of pre-cooked code" below.


1) Get Ardupilot from Google Code.

2) Copy all files from this extension project libraries/FastSerial directory into the libraries/FastSerial directory of Ardupilot, overwriting FastSerial.h and FastSerial.cpp. Also copy all files from this extension project libraries/AP_Menu directory into the libraries/AP_Menu directory of Ardupilot, overwriting AP_Menu.h and AP_Menu.cpp

3) In ArduPlane.pde
Add this line where the other #include lines are:
#include <MobileStream.h>

Replace
////////////////////////////////////////////////////////////////////////////////
// Serial ports
////////////////////////////////////////////////////////////////////////////////
//
// Note that FastSerial port buffers are allocated at ::begin time,
// so there is not much of a penalty to defining ports that we don't
// use.
//
FastSerialPort0(Serial);        // FTDI/console
FastSerialPort1(Serial1);       // GPS port
#if TELEMETRY_UART2 == ENABLED
// solder bridge set to enable UART2 instead of USB MUX
FastSerialPort2(Serial3);
#else
FastSerialPort3(Serial3);        // Telemetry port for APM1
#endif

by

////////////////////////////////////////////////////////////////////////////////
// Serial ports
////////////////////////////////////////////////////////////////////////////////
//
// Note that FastSerial port buffers are allocated at ::begin time,
// so there is not much of a penalty to defining ports that we don't
// use.
// 
FastSerialPort0(Serial);        // FTDI/console
FastSerialPort1(Serial1);       // GPS port
#if CONFIG_APM_HARDWARE == APM_HARDWARE_APM2
// APM2: The UART#2 is named as Serial3(!)
FastSerialPort2(Serial3);
#else
// APM1:
FastSerialPort3(Serial3);
#endif
#if TELEMETRY_UART2 == MOBILE
MobileStream mobile(&Serial3);
#endif
// Changed the code to use CONFIG_APM_HARDWARE instead of TELEMETRY_UART2 to decide 
// whether to define "Serial3" to use UART2(on APM2) or UART3(on APM1). It is clearer.
// Added a MobileStream definition.

and replace

// port to use for command line interface
static FastSerial *cliSerial = &Serial;

by

// port to use for command line interface
static BufferedStream *cliSerial = &Serial;

4) In system.pde
Replace
#if USB_MUX_PIN > 0
    if (!usb_connected) {
        // we are not connected via USB, re-init UART0 with right
        // baud rate
        Serial.begin(map_baudrate(g.serial3_baud, SERIAL3_BAUD));
    }
#else
    // we have a 2nd serial port for telemetry
    Serial3.begin(map_baudrate(g.serial3_baud, SERIAL3_BAUD), 128, SERIAL_BUFSIZE);
    gcs3.init(&Serial3);
#endif

by

#if USB_MUX_PIN > 0
    if (!usb_connected) {
        // we are not connected via USB, re-init UART0 with right
        // baud rate
        Serial.begin(map_baudrate(g.serial3_baud, SERIAL3_BAUD));
    }
#endif

// we have a 2nd serial port for telemetry
    Serial3.begin(map_baudrate(g.serial3_baud, SERIAL3_BAUD), 128, SERIAL_BUFSIZE);
    gcs3.init(&Serial3);

#if TELEMETRY_UART2 == MOBILE
mobile.begin(128, SERIAL_BUFSIZE);
#endif

and replace

// the user wants the CLI. It never exits
static void run_cli(FastSerial *port)
{
    // disable the failsafe code in the CLI
    timer_scheduler.set_failsafe(NULL);

    cliSerial = port;
    Menu::set_port(port);
    port->set_blocking_writes(true);

    while (1) {
        main_menu.run();
    }
}

by

// the user wants the CLI. It never exits
static void run_cli(BufferedStream *port)
{
    // disable the failsafe code in the CLI
    timer_scheduler.set_failsafe(NULL);

    cliSerial = port;
    Menu::set_port(port);
    port->set_blocking_writes(true);

    while (1) {
        main_menu.run();
    }
}

5) in GCS.h:
Replace
    void        init(FastSerial *port) {
        _port = port;
        initialised = true;
        last_gps_satellites = 255;
    }

by

    void init(BufferedStream *port) {
        _port = port;
        initialised = true;
    }

and replace:

protected:
    /// The stream we are communicating over
    FastSerial *      _port;
};	

by

protected:
    /// The stream we are communicating over
    BufferedStream *      _port;
};	

and replace

    void        init(FastSerial *port);

by
	
    void        init(BufferedStream *port);



6) In GCS_Mavlink.pde
Replace

void
GCS_MAVLINK::init(FastSerial * port)
{
    GCS_Class::init(port);
    if (port == &Serial) {
        mavlink_comm_0_port = port;
        chan = MAVLINK_COMM_0;
    }else{
        mavlink_comm_1_port = port;
        chan = MAVLINK_COMM_1;
    }
    _queued_parameter = NULL;
}

by

void
GCS_MAVLINK::init(BufferedStream* port)
{
    GCS_Class::init(port);
    if (port == &Serial) {
        mavlink_comm_0_port = port;
        chan = MAVLINK_COMM_0;
    } else {
#if TELEMETRY_UART2 == MOBILE
    	mavlink_comm_1_port = &mobile;
#else
        mavlink_comm_1_port = port;
#endif
        chan = MAVLINK_COMM_1;
    }
    _queued_parameter = NULL;
}

and after the lines
void
GCS_MAVLINK::update(void)
{
    // receive new packets
    mavlink_message_t msg;
    mavlink_status_t status;
    status.packet_rx_drop_count = 0;
	
add

#if TELEMETRY_UART2 == MOBILE
    if (chan==1)
    	((MobileStream*)mavlink_comm_1_port)->task();
#endif

7)
In AP_Menu.h,

replace

	static void set_port(FastSerial *port) {
		_port = port;
	}

	by
	
		static void set_port(BufferedStream *port) {
		_port = port;
	}

and replace

	static FastSerial       *_port;
	
by

	static BufferedStream       *_port;

8) In AP_Menu.cpp, replace

FastSerial *Menu::_port;

by

BufferedStream *Menu::_port;

(but do leave the #include<FastSerial.h> as-is).
	
In APM_Config.h, add:
// For using a DroneCell or similar on UART3(APM1) or UART2(APM2). 
// Baud rate will be that ser for Serial3 on both APM1 and APM2!
#define TELEMETRY_UART2 MOBILE

// For direct wireless telemetry on UART3(APM1) or UART2(APM2).
// Baud rate will be that ser for Serial3 on both APM1 and APM2!
// #define TELEMETRY_UART2 MOBILE

// For having telemetry only on UART0 (all APM versions). APM2 will
// still use its multiplexing feature on UART0:
// #define TELEMETRY_UART2 MOBILE


9) You're done. Upload the Ardulane sketch from Arduino.


Integration of pre-cooked code
------------------------------

1) Get Ardupilot from Google Code.

2) Copy all files from this extension project libraries/FastSerial directory into the libraries/FastSerial directory of Ardupilot, overwriting FastSerial.h and FastSerial.cpp. Also copy all files from this extension project libraries/AP_Menu directory into the libraries/AP_Menu directory of Ardupilot, overwriting AP_Menu.h and AP_Menu.cpp

3) Copy all the ArduPlane source files in this directory into the checked-out ArduPlane files of the same names, overwriting them.

4) You're done. Upload the Ardulane sketch from Arduino.

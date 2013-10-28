dongfang Ardupilot Cellular Extension v2

This modification to the Ardupilot UAV firmware allows using a cellular (mobile) phone transceiver to be connected to the APM or PX4, using IP over the mobile network for in-flight MAVLink telemetry.

Earlier solutions would open an TCP/IP connection on the cellular module, and then put it into a data-transparent mode to simulate a hardwired connection. This method was simple but it had several drawbacks:
- Poor performance, because MAVLink messages were randomly split
- If the connection was lost it would not be restored

This solution maintains full control of the cellular module all the time; broken connections are actively attempted restored and one can even request SMS messages from the UAV as a last resort.

Features:
- Compatible with SimCom SIM900, SIM900D (DroneCell) and SIM700. Support for further additions.
- Full MAVLink transmit and receive to any IP address and UDP/TCP port.
- Intelligent monitoring (state machine) of modem state.
- Configuration via SIM card phone book.
- Limited support for status information and control via SMS messages.


How to get it to work
There are three different challenges in getting a mobile TCP/IP connection working from a UAV controller:
1) Getting the hardware wired up correctly
2) Getting the UAV computer to properly configure and control the cellular module
3) Getting IP messages routed between the UAV and the ground station

To succeed with setting up this, it is necessary to have some knowledge about
- Serial communications and hardware (what is TxD and RxD, what is baud rate and flow control, using a terminal emulation program, what is 3.3V logic level and why must the GND wire also be connected)
- Arduino and Ardupilot programming - how to modify and compile ArduPilot and how to upload to the APM/PX4
- Internet technologies, such as what TCP and UDP ports are, what NATs and firewalls do.
- An USB-to-serial adapter (eg. FTDIChip cable)  is necessary for testing.

1) Getting the hardware wired up correctly
The GND wire of the cellular modem must always be connected to that of the UAV controller.
A serial port is used for the modem connection. APM1 and APM2 use 5V logic, while eg. DroneCell uses 3.3V logic. Take care not to overload anyting via the data connetions becuase of different logic levels (DroneCell is protected and should be OK to use with APM).

For DroneCell, the connections are:
TxD on APM to RxI on DroneCell
RxD on APM to TxO on DroneCell
GND to GND
For other modems (which may be wired as DCEs) the connections may be different.

For APM2, please use the USART2 connection on the side of the board. Do not!! use the "telemetry port". This connector uses USART0, which is shared with the onboard USB connection. Since the cellular modem speaks a different language than the MAVLink of the USB serial, this cannot work.

Please perform these initial steps:
- Connect the modem to the PC serial port (TxD to RxD, RxD to TxD, GND to GND) and verify from within a terminal program that the modem is working. It has to respond OK when entering AT<enter>. If not, check that the baud rate is set correctly in the terminal program and that flow control is off. This has to work before continuing.
- Set the baud rate of the modem to 57600 and store the setting: 
AT+IPR=57600<enter>
Re-set the baud rate of the terminal program to 57600 and do
AT+IPC=0<enter>
AT&W<enter>
- Verify connection still working: AT<enter> should again result in OK.
- Now connect the APM and the cellular modem together and connect the APM's USB serial interface to a PC. Use Arduino to upload the sketch "" to the APM.
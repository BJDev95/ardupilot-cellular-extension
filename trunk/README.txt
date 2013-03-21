dongfang Ardupilot Cellular Extension
=====================================

Soren Kuula
soren.kuula@gmail.com

Description
-----------
This is an extension to ArdupilotMega that allows a serial cellular modem to be connected to the 2nd telemetry port of ArdupilotMega and used for telemetry.

Other cellular telemetry projects only more or less blindly send commands to the modem to establish a data connection with a remote host, and then switch into "transparent mode" to forward all forthcoming data the the connection. This has the disadvantages that MAVLink messages are not aligned to IP ditto, and that if the connection gets interrupted somewhere in flight, it will probably never get re-established.

This is a solution to these problems: The command-response communication with the cellular module works as a state machine, and the response messages from it are read and interpreted. The state machine stays conscious about whether the modem has a data connection, and keeps taking steps to establish one if not. One or more MAVLink messages is packed into each IP frame, but not partial messages.

Current Limitations
-------------------
- Support only for SIM900(D). That is e.g. DroneCell.
- There is no configuration implemented yet. One has to modify source code and recompile.
- Memory usage could be improved, but it will be slightly complicated.
- Not yet tested on PX4. However this is no special case, and there should be no reason that it would not work as on APM.

Status
------
I have integrated this into ArduPlane 2.65 and 2.68 and flown an APM2.0 on 30km missions with it. It worked great and appeared very robust! One can reset the APM and/or cycle power on the cellular module anytime, and the data connection resumes.
Three other ArduPlane pilots have tested it with positive results, too.

Contribution
------------
Testing in other environments than mine is needed.
Configuration of remote address, network access point etc also needs to be done.
I would be happy to welcome testers and coders to join!

Hardware
--------
APM1, APM2 or PX4.
DroneCell or similar.
A cable that connects the APM with the DroneCell. On APM1, UART3 is used and on APM2, UART2 is used.
On APM2, the telemetry "MUX" (an electronic switch between the on-board USB serial port and the UART0/2 connector on the board) keeps working as before. That means, you can have an XBee, 3DR radio or other radio data gadget and the DroneCell attached and even connected at the same time (was tested once and it worked).
For APM2, please use the UART2 connector at the edge of the board. The "telemetry port" with its multiplexer and 4 solder jumpers may be confusing to get to work, and it makes no sense to multiplex the cellular module (using AT commands) with the USB UART connection (not using AT commands).
Notice that the APM and DroneCell must be connected: 
Tx(APM) to Rx(DroneCell)
Rx(APM) to Tx(DroneCell)
GND(APM) to GND(DroneCell)
Do not use +5V(APM) to power the cellular module. It needs its own supply. A single cell LiPo battery is ideal.

How to try it out
-----------------
One will need to have a working Arduino programming environment and hopefully a little experience with hacking the ArdupilotMega code. 
Some knowledge about serial hardware, about the meaning the "AT" commands to the cellular module and how to use them to build a data connection also helps. A terminal emulation program on a PC and a serial port or two to watch the command and response traffic between APM and cellular module might also be needed.
Read the README.txt file in the ArduPlane-2.xx directory (xx is your preferred sub-version number. Currently there's only 68).

Regards
Soren

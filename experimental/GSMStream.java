import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class GSMStream {
	private SerialPort serialPort;

	private static final int RESPONSE_BUFLEN = 32;

	static class BufferedStream {
		static class Buffer {
			int head, tail; // /< head and tail pointers
			int mask; // /< buffer size mask for pointer wrap
			char[] bytes;

			Buffer(int size) {
				bytes = new char[size];
				mask = size - 1;
			}

			int available() {
				return (head - tail) & mask;
			}

			int freeSpace() {
				return mask - ((head - tail) & mask);
			}

			int read() {
				if (head == tail)
					return -1;
				int result = bytes[tail];
				tail = (tail + 1) & mask;
				return result;
			}

			int write(char data) {
				int i = (head + 1) & mask;

				if (i == tail) {
					return 0;
				}

				// while (i == _txBuffer->tail)
				// ;

				// add byte to the buffer
				bytes[head] = data;
				head = i;

				// return number of bytes written (always 1)
				return 1;
			}
		}

		Buffer _rxBuffer = new Buffer(1024);
		Buffer _txBuffer = new Buffer(1024);

		int available() {
			synchronized (_rxBuffer) {
				return _rxBuffer.available();
			}
		}

		int availableForTx() {
			synchronized (_txBuffer) {
				return _txBuffer.available();
			}
		}

		int txspace() {
			synchronized (_txBuffer) {
				return _txBuffer.freeSpace();
			}
		}

		int rxspace() {
			synchronized (_rxBuffer) {
				return _rxBuffer.freeSpace();
			}
		}

		int read() {
			synchronized (_rxBuffer) {
				int i = _rxBuffer.read();
				return i;
			}
		}

		int reverseWrite(char data) {
			synchronized (_rxBuffer) {
				int i =_rxBuffer.write(data);
				return i; 
			}
		}

		void flush() {
			_txBuffer.head = _txBuffer.tail = _rxBuffer.head = _rxBuffer.tail = 0;
		}

		int write(char data) {
			synchronized (_txBuffer) {
				return _txBuffer.write(data);
			}
		}

		int reverseRead() {
			synchronized (_txBuffer) {
				return _txBuffer.read();
			}
		}

		/*
		int readBlock(char[] data, int offset, int length) {
			synchronized (_rxBuffer) {
				// Input considered linear! And if offset+length exceeds the
				// allocated length of data, caller's problem.
				// Cases:
				length = Math.min(available(), length);

				if (_rxBuffer.head > _rxBuffer.tail) {
					System.arraycopy(_rxBuffer.bytes, _rxBuffer.tail, data, offset, length);
				} else {
					int firstChunk = Math.min(length, _rxBuffer.mask + 1 - _rxBuffer.tail);
					System.arraycopy(_rxBuffer.bytes, _rxBuffer.tail, data, offset, firstChunk);
					int secondChunk = length - firstChunk;
					System.arraycopy(_rxBuffer.bytes, 0, data, firstChunk + offset, secondChunk);
				}
				_rxBuffer.tail = (_rxBuffer.tail + length) & _rxBuffer.mask;
				return length;
			}
		}
		*/

		/*
		int writeBlock(char[] data, int offset, int length) {
			synchronized (_txBuffer) {
				length = Math.min(length, txspace());

				// Case 1: head<tail. Copy from head, and length bytes forward.
				// Copy
				// to offset of offset.
				// Case 2: tail<head. Copy (end-tail) to tail (source offset
				// offset),
				// and from zero to head (offset offset + (end-tail))
				// Case 1:[dddHfffffffTdddddd]
				// Case 2:[ffffTddddddddHffff]
				// Ex.: Length=8, mask=7.

				if (_txBuffer.head < _txBuffer.tail) {
					System.arraycopy(data, offset, _txBuffer.bytes, _txBuffer.head, length);
				} else {
					// The number of bytes to copy from head to end of array.
					// WRONG: Source array may be shorter than this.
					// int j = _txBuffer.mask+1 - _txBuffer.head;
					int firstChunk = Math.min(length, _txBuffer.mask + 1 - _txBuffer.head);
					System.arraycopy(data, offset, _txBuffer.bytes, _txBuffer.head, firstChunk);
					int secondChunk = length - firstChunk;
					System.arraycopy(data, firstChunk + offset, _txBuffer.bytes, 0, secondChunk);
				}
				_txBuffer.head = (_txBuffer.head + length) & _txBuffer.mask;
				return length;
			}
		}
		*/

		int writeBlock(char[] data, int offset, int length) {
			int result=0;
			for (int i=0; i<length; i++) {
				result += write(data[offset+i]);
			}
			return result;
		}

		/*
		static int copy(Buffer source, Buffer target, int length) {
			synchronized (source) {
				synchronized (target) {
					length = Math.min(length, source.available());
					length = Math.min(length, target.freeSpace());
					if (source.head > source.tail) {
						// Source is a : [ffffTdddddHffff]
						writeBlock(source._rxBuffer.bytes, source._rxBuffer.tail, length);
					} else {
						// Source is a : [ddddHfffffTdddd]
						int firstChunk = Math.min(length, source._rxBuffer.mask + 1 - source._rxBuffer.tail);
						writeBlock(source._rxBuffer.bytes, source._rxBuffer.tail, firstChunk);
						int secondChunk = length - firstChunk;
						writeBlock(source._rxBuffer.bytes, 0, secondChunk);
					}
					return length;
				}
			}
		}
		*/
	}

	public void init(String id) throws IOException {
		// CommPortIdentifier.getPortIdentifier("COM1");
		// Just take the 1st available port.
		CommPortIdentifier portIdentifier = null;

		try {
			if (id == null) {
				portIdentifier = (CommPortIdentifier) CommPortIdentifier
						.getPortIdentifiers().nextElement();
				if (portIdentifier == null) {
					throw new IOException(
							"No serial port found (in search for any serial port).");
				}
			} else
				portIdentifier = CommPortIdentifier.getPortIdentifier(id);
		} catch (NoSuchPortException ex) {
			throw new IOException(ex.getMessage());
		}

		System.out.println("Using " + portIdentifier.getName());

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
			return;
		} else {
			try {
				CommPort commPort = portIdentifier.open(this.getClass()
						.getName(), 2001);
				if (commPort instanceof SerialPort) {
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(57600,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				}
			} catch (Exception ex) {
				throw new IOException(ex.getMessage());
			}
		}

		new ModemInputDriver().start();
		new ModemOutputDriver().start();
		new UserDataConsumer().start();
		
		ParseState ps = new ParseState();
		ps.state = initialState;
		ps.progress = 0;
		ps.bufptr = 0;
		ps.deferredSuccessorState = null;
		parseState = ps;
	}

	public InputStream getInputStream() throws IOException {
		return serialPort.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return serialPort.getOutputStream();
	}

	static class Transition {
		String token;
		State state;

		Transition(String token, State state) {
			this.token = token;
			this.state = state;
		}
	}

	ParseState parseState;
	State initialState;
	Transition[] URCTransitions = new Transition[20];
	int numberOfURCs = 0;

	BufferedStream userData = new BufferedStream();
	BufferedStream modem = new BufferedStream();

	private static final int GSM_P_START = 0;
	private static final int GSM_P_COMMAND_SENT = 10;
	private static final int GSM_P_ARGUMENT_SENT = 20;
	private static final int GSM_P_TRANSMITTING = 25;
	// KILLING_ECHO=10,
	// KILLED_ECHO=20.
	private static final int GSM_P_INITIAL_WS = 30;
	private static final int GSM_P_INTITAL_RESPONSE = 40;
	// For queries, one can add state for parsing the 2nd line.

	private static final int GSM_P_RECEIVING_DATALENGTH = 50;
	private static final int GSM_P_RECEIVING_DATA = 60;

	private static final int GSM_P_TRAILING_WS = 70;
	private static final int GSM_P_DONE = 80;

	void writeSerial(char c) throws IOException {
		getOutputStream().write(c);
		System.out.write(c);
		System.out.flush();
	}

	void writeSerial(byte[] bs) throws IOException {
		for (int i = 0; i < bs.length; i++) {
			int asChar = bs[i];
			if (asChar < 0)
				asChar += 256;
			writeSerial((char) asChar);
		}
	}

	int readSerial() throws IOException {
		int i = getInputStream().read();
		if (i >= 0) {
			System.err.print((char) i);
			System.err.flush();
		}
		return i;
	}

	class ModemInputDriver extends Thread {
		public void run() {
			while (true) {
				try {
					int input = readSerial();
					if (input >= 0) {
						GSMStream.this.modem.reverseWrite((char) input);
					}
				} catch (IOException ex) {
					System.err.println(ex);
				}
			}
		}
	}

	class ModemOutputDriver extends Thread {
		public void run() {
			while (true) {
				try {
					int output;
					output = GSMStream.this.modem.reverseRead();
					if (output >= 0) {
						writeSerial((char) output);
					}
				} catch (IOException ex) {
					System.err.println(ex);
				}
			}
		}
	}

	class UserDataConsumer extends Thread {
		public void run() {
			while (true) {
				int data = GSMStream.this.userData.read();
			}
		}
	}

	// Make a partial string match: Whether the characters match through the
	// length of the key
	// (excessive chars at input are ignored). '?' in a key will match any
	// character in input.
	// impl.
	static boolean match(String key, char[] input, int length) {
		if (length < key.length())
			return false;
		for (int idx = 0; idx < key.length(); idx++) {
			char k = key.charAt(idx);
			char i = input[idx];
			if (k != i && k != '?')
				return false;
		}
		return true;
	}

	// Assume leading and trailing CRLF has been dealt with (that is, this
	// prodecure should have
	// returned a match already before trailing CRLF and the caller must consume
	// the rest until CRLF then.
	// And assume echo is not an issue here (already removed).
	// Will be used for OK/ERROR distinction, normal query result distinction
	// and URC distinction.
	// All separate from each other (hopefully)
	static int match(Transition[] transitions, int numPatterns, char[] input, int length) {
		for (int i = 0; i < numPatterns; i++) {
			if (match(transitions[i].token, input, length)) {
				return i;
			}
		}
		return -1;
	}

	static class ParseState {
		State state;
		int progress;
		char[] responseBuffer = new char[RESPONSE_BUFLEN];
		int bufptr;
		State deferredSuccessorState;
		State savedState;
		// int savedProgress;

		int numRxBytes;
		int numTxBytes;

		void add(char data) {
			responseBuffer[bufptr++] = data;
			if (bufptr == RESPONSE_BUFLEN)
				bufptr = 0;
		}

		void saveState() {
			savedState = state;
			// savedProgress = progress;
		}

		void restoreState() {
			state = savedState;
			progress = GSM_P_START; // savedProgress;
		}

		void useDeferredState() {
			state = deferredSuccessorState;
			deferredSuccessorState = null;
			progress = GSM_P_START;
		}
	}

	static abstract class State {
		String name;

		State(String name) {
			this.name = name;
		}

		abstract void task(GSMStream outer, ParseState ps) throws IOException;

		boolean isImmediate() {
			return false;
		}

		// impl.
		void checkSuccessor(GSMStream outer, ParseState ps) {
			if (ps.progress == GSM_P_DONE) {
				if (ps.deferredSuccessorState != null) {
					ps.saveState();
					ps.useDeferredState();
				} else {
					ps.progress = GSM_P_INITIAL_WS;
				}
				ps.bufptr = 0;
			}
		}
	}

	static class DecisionState extends State {
		Transition[] transitions = new Transition[10];
		int numTransitions = 0;

		DecisionState(String name) {
			super(name);
		}

		void addTransition(String token, State nextState) {
			Transition t = new Transition(token, nextState);
			transitions[numTransitions++] = t;
		}

		// impl.
		void receive(GSMStream outer, ParseState ps) throws IOException {
			int data = outer.modem.read();
			if (data < 0)
				return;

			// Absorb whitespace until first real text.
			if (ps.progress == GSM_P_INITIAL_WS) {
				if (data != 13 && data != 10) {
					//System.out.println("Switch to initial_response on a " + data + " " + (char)data);
					ps.progress = GSM_P_INTITAL_RESPONSE;
				}
			}

			if (ps.progress == GSM_P_INTITAL_RESPONSE) {
				if (data != 13 && data != 10) {
					ps.add((char) data);
					int matchResult = GSMStream.match(transitions,
							numTransitions, ps.responseBuffer, ps.bufptr);
					if (matchResult >= 0) {
						State nextState = transitions[matchResult].state;
						ps.deferredSuccessorState = nextState;
						// System.out.println("Ordinary match: " +
						// nextState.name);
					} else {
						matchResult = outer.matchURCs(ps.responseBuffer,
								ps.bufptr);
						if (matchResult >= 0) {
							State nextState = outer.URCTransitions[matchResult].state;
							if (nextState.isImmediate()) {
								// change state immediately.
								// nextState.setOrigin(ps);
								ps.saveState();
								// strictly not necessary!
								ps.deferredSuccessorState = null;
								ps.state = nextState;
								ps.progress = GSM_P_START;
								ps.bufptr = 0;
							} else { // defer until tail is consumed.
								ps.deferredSuccessorState = nextState;
							}
						}
					}
				} else {
					if (data == 13)
						ps.progress = GSM_P_TRAILING_WS;
					else if (data == 10)
						ps.progress = GSM_P_DONE;
				}
			}

			else if (ps.progress == GSM_P_TRAILING_WS) {
				ps.progress = GSM_P_DONE;
			}
		}

		// impl.
		void task(GSMStream outer, ParseState ps) throws IOException {
			// TODO: Is command send and initial WS the same really?
			if (ps.progress == GSM_P_COMMAND_SENT) {
				ps.progress = GSM_P_INITIAL_WS;
			}

			if (ps.progress > GSM_P_COMMAND_SENT) {
				receive(outer, ps);
			}

			checkSuccessor(outer, ps);
		}
	}

	static class CommandState extends DecisionState {
		String command;

		CommandState(String name, String command) {
			super(name);
			this.command = command;
		}

		// Impl.
		void task(GSMStream outer, ParseState ps) throws IOException {
			if (ps.progress == GSM_P_START) {
				if (outer.modem.txspace() > command.length()) {
					char[] asChars = (command + '\r').toCharArray();
					outer.modem.writeBlock(asChars, 0, asChars.length);
					ps.progress = GSM_P_COMMAND_SENT;
				}
			}

			super.task(outer, ps);
		}
	}

	static class DeadState extends DecisionState {
		DeadState(String name) {
			super(name);
		}

		// impl.
		void task(GSMStream outer, ParseState ps) throws IOException {
			// There is no transmit in a dead state.
			if (ps.progress == GSM_P_START) {
				ps.progress = GSM_P_INITIAL_WS;
			}
			// With a mechanism similar to immediate states do we switch to transmit if necessary.
			int bytesToTransmit;
			
			if(ps.progress == GSM_P_INITIAL_WS && ps.bufptr == 0 && (bytesToTransmit = outer.userData.availableForTx()) > 0) {
				ps.saveState();
				ps.state = outer.transmitState;
				ps.progress = GSM_P_START;
				// start immediately.
				ps.numTxBytes = bytesToTransmit;
				ps.state.task(outer, ps);
			}
			
			else {
				receive(outer, ps);
				checkSuccessor(outer, ps);
			}
		}
	}

	static class KeepAliveState extends DeadState {
		long lastTime = 0;

		KeepAliveState(String name) {
			super(name);
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			super.task(outer, ps);
			/*
			 * if (ps.progress == GSM_P_INITIAL_WS) { // Apparently not
			 * receiving anything. if (System.currentTimeMillis() > lastTime +
			 * 30000) { lastTime = System.currentTimeMillis();
			 * transmitState.setOrigin(ps);
			 * transmitState.setData("hey!".getBytes()); ps.state =
			 * transmitState; ps.progress = GSM_P_START; ps.bufptr = 0; } }
			 */
		}
	}

	static class TransmitState extends CommandState {
		TransmitState() {
			super("TRANSMIT", "AT+CIPSEND=");
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			if (ps.progress == GSM_P_START) {
				if (outer.modem.txspace() >= command.length()) {
					char[] asChars = command.toCharArray();
					outer.modem.writeBlock(asChars, 0, asChars.length);
					ps.progress = GSM_P_COMMAND_SENT;
				}
			}

			if (ps.progress == GSM_P_COMMAND_SENT) {
				if (outer.modem.txspace() >= 4) {
					char[] size = Integer.toString(ps.numTxBytes).toCharArray();
					outer.modem.writeBlock(size, 0, size.length);
					outer.modem.write('\r');
					ps.progress = GSM_P_ARGUMENT_SENT;
				}
			}

			if (ps.progress == GSM_P_ARGUMENT_SENT) {
				int i = outer.modem.read();
				if (i < 0) {
					return;
				}
				if (i == ' ') {
					//System.out.println("Got mark.");
					ps.progress = GSM_P_TRANSMITTING;
				}
			}
			
			if (ps.progress == GSM_P_TRANSMITTING) {
				int numBytes = Math.min(ps.numTxBytes, outer.userData.availableForTx());
				numBytes = Math.min(numBytes, outer.modem.txspace());
				for (int i=0; i<numBytes; i++) {
					outer.modem.write((char)outer.userData.reverseRead());
				}
				ps.numTxBytes -= numBytes;
				if (ps.numTxBytes == 0) {
					//ps.progress = GSM_P_INITIAL_WS;
					ps.restoreState();
				}
			} 

			/*
			if(ps.progress >= GSM_P_INITIAL_WS) {
				// Until transmit finished we dont even listen.
				receive(outer, ps);
				checkSuccessor(outer, ps);
			}
			*/
		}
	}

	TransmitState transmitState = new TransmitState();

	static class DataReceptionState extends State {
		// int byteIdx;
		String numberOfBytesString = "";

		// byte[] data = new byte[1500];
		// int[] distribution = new int[256];

		DataReceptionState(String name) {
			super(name);
		}

		boolean isImmediate() {
			return true;
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			if (ps.progress == GSM_P_START) {
				// lick up the byte count.
				ps.numRxBytes = 0;
				ps.progress = GSM_P_RECEIVING_DATALENGTH;
			}

			if (ps.progress == GSM_P_RECEIVING_DATALENGTH) {
				int data = outer.modem.read();
				if (data < 0)
					return;
				if (data == ':') {
					ps.progress = GSM_P_RECEIVING_DATA;
					// System.out.println("About to receive " + ps.numRxBytes + " bytes.");
				} else {
					ps.numRxBytes *= 10;
					ps.numRxBytes += (data - '0');
				}
			}
			
			if (ps.progress == GSM_P_RECEIVING_DATA) {
				int a = outer.modem.available();
				int numBytes = Math.min(ps.numRxBytes, a);
				numBytes = Math.min(numBytes, outer.userData.rxspace());

				for (int i=0; i<numBytes; i++) {
					outer.userData.reverseWrite((char)outer.modem.read());
				}
				
				ps.numRxBytes -= numBytes;
				if (ps.numRxBytes == 0) {
					// actually we are done.
					// System.out.println("Got " + numberOfBytes +
					// " bytes of data: " + new String(data, 0, numberOfBytes));
					// for (int j = 0; j < numberOfBytes; j++) {
					// int b = data[j];
					// if(b<0) b+=256;
					// System.out.print(b + " ");
					// }
					// There is no post-data state. We return immediately.
					// ps.restoreState();
					// Dont restore state. Restart it. It's probably just a deadstate anyway.
					ps.state = ps.savedState;
					ps.progress = GSM_P_START;
				}
			}
		}
	}

	static class SMSReceptionState extends State {
		SMSReceptionState() {
			super("SMS_RECEPTION");
		}

		boolean isImmediate() {
			return true;
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			ps.restoreState();
		}
	}

	static class CallReceptionState extends State {
		CallReceptionState() {
			super("CALL_RECEPTION");
		}

		boolean isImmediate() {
			return true;
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			ps.restoreState();
		}
	}

	static class FailedState extends State {
		String message;

		FailedState(String message) {
			super("FAIL");
			this.message = message;
		}

		void task(GSMStream outer, ParseState ps) throws IOException {
			System.err.println("FAIL: " + message);
			System.exit(-1);
		}
	}

	// int singleMessageSize;

	void markMessageStart() {
		// singleMessageSize =
	}

	void markMessageEnd() {
		// DANGER: This assumes that the was empty at messageStart.
		// numMessageBytes = userData.txAvailable();
	}

	void addURCTransition(String token, State nextState) {
		Transition t = new Transition(token, nextState);
		URCTransitions[numberOfURCs++] = t;
	}

	int matchURCs(char[] input, int length) {
		return match(URCTransitions, numberOfURCs, input, length);
	}

	void push() throws IOException {
		// The current state will get one chance to do things even if there is
		// no
		// input data. But if there is input data, it will all be consumed.
		do {
			parseState.state.task(this, parseState);
		} while (modem.available() > 0);
	}

	State evenSimplerSetup() {
		// All the error messages basically..
		FailedState configurationError = new FailedState("configuration error.");

		CommandState entry = new CommandState("ENTRY",
				"\rATV1 E0 X1 S0=0 +CMEE=0");
		CommandState configuration = new CommandState("CONFIGURATION",
				"AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPHEAD=1;+CIPSPRT=1;+CIPCSGP=1,\"internet\"");

		CommandState start1 = new CommandState("START1", "AT+CSTT");
		CommandState start2 = new CommandState("SETTINGS2", "AT+CIICR");
		CommandState start3 = new CommandState("SETTINGS2", "AT+CIFSR");
		// DeadState waitForGPRSReg = new DeadState("WAIT_GPRS");
		CommandState dataConnection = new CommandState("DATA_CONNECTION",
				"AT+CIPSTART=\"udp\",\"www.sky-cam.dk\",\"10000\"");

		CommandState reset = new CommandState("RESET", "AT+CFUN=1,1");
		CommandState reset_IP = new CommandState("RESTART", "AT+CIPSHUT");

		// The URC based states may appear any time...
		State incomingData = new DataReceptionState("RECEIVING_DATA");


		reset_IP.addTransition("SHUT OK", configuration);
		reset_IP.addTransition("ERROR", configuration);

		entry.addTransition("OK", configuration);
		entry.addTransition("ERROR", reset);

		reset.addTransition("OK", entry);
		reset.addTransition("ERROR", entry);

		configuration.addTransition("OK", start1);
		configuration.addTransition("ERROR", reset_IP);

		start1.addTransition("OK", start2);
		start1.addTransition("ERROR", reset_IP);

		start2.addTransition("OK", start3);
		start2.addTransition("ERROR", reset_IP);

		start3.addTransition("", dataConnection); // any IP address is valid for
													// us...
		start3.addTransition("ERROR", reset_IP);

		State dataConnectionOpening = new DeadState("DATA_CONNECTION_STARTING");
		State dataConnectionOpen = new KeepAliveState("DATA_CONNECTION_OPEN");

		transmitState.addTransition("SEND OK", dataConnectionOpen);
		transmitState.addTransition("SEND FAIL", dataConnectionOpen);
		
		transmitState.addTransition("ERROR", dataConnectionOpen);

		dataConnection.addTransition("OK", dataConnectionOpening);
		dataConnection.addTransition("ERROR", entry); // ???

		// All URCs should be represented!
		addURCTransition("RDY", entry); // We still do the alive
										// test, just to
		addURCTransition("+CFUN: ?", entry);
		addURCTransition("+CGREG: 1", entry); // GPRS registered, so (re)start
		addURCTransition("+CGREG: 5", entry); // GPRS registered, so (re)start
		addURCTransition("+CGREG: 3", entry); // GPRS denied. This is issued at
												// shutdown. We use this to
												// detect that we are offline.
		addURCTransition("+IPD,", incomingData);
		addURCTransition("CONNECT OK", dataConnectionOpen);
		addURCTransition("ALREADY CONNECT", reset_IP);
		
		addURCTransition("SEND OK", dataConnectionOpen);
		addURCTransition("SEND FAIL", dataConnectionOpen);

		return entry;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println("\\r is (13) " + (int)'\r' + " and \\l is (10) " +
		// (int)'\n');
		GSMStream m = new GSMStream();
		m.initialState = m.evenSimplerSetup();
		m.init("COM4");
		
		char[] data = "The quick brown fox jumps over lazy dogs".toCharArray();
		int start = 0;
		long time = System.currentTimeMillis();
		
		while(true) {
			m.push();
			if (System.currentTimeMillis() > time + 10000) {
				time = System.currentTimeMillis();
				int length = data.length - (start % data.length);
				int offset = start % 40;
				start += m.userData.writeBlock(data, offset, length);
			}
		}
	}
}
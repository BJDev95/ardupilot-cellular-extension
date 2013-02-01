import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StupidSIM900StateMachineNonblocking {
	private SerialPort serialPort;

    static class BufferedStream {
	static class Buffer {
	    int head, tail;	///< head and tail pointers
	    int mask;					///< buffer size mask for pointer wrap
	    char[] bytes;

	    Buffer(int size) {
		bytes = new char[size];
		mask = size - 1;
	    }
	}

	Buffer _rxBuffer = new Buffer(128);
	Buffer _txBuffer = new Buffer(128);

	//void end(void);
	int available() {
	    return (_rxBuffer.head - _rxBuffer.tail) & _rxBuffer.mask;
	}

	int txspace() {
	    return (_txBuffer.mask+1) - ((_rxBuffer.head - _rxBuffer.tail) & _txBuffer.mask);
	}

	int read() {
	    if (_rxBuffer.head == _rxBuffer.tail) return -1;
	    int result = _rxBuffer.bytes[_rxBuffer.tail];
	    _rxBuffer.tail++;
	    _rxBuffer.tail &= _rxBuffer.mask;
	    return result;
	}

	int peek() {
	    if (_rxBuffer.head == _rxBuffer.tail) return -1;
	    int result = _rxBuffer.bytes[_rxBuffer.tail];
	    return result;
	}
	
	void flush() {
	    _txBuffer.head = _txBuffer.tail = _rxBuffer.head = _rxBuffer.tail = 0;
	}

	int write(char data) {
	    int i = (_txBuffer.head + 1) & _txBuffer.mask;

	    if (i == _txBuffer.tail) {
		return 0;
	    }
	    
	    //	    while (i == _txBuffer->tail)
	    //		;

	    // add byte to the buffer
	    _txBuffer.bytes[_txBuffer.head] = data;
	    _txBuffer.head = i;
	    
	    // return number of bytes written (always 1)
	    return 1;
	}

	int readBlock(char[] data, int offset, int length) {
	    // Input considered linear! And if offset+length exceeds the allocated length of data, caller's problem.
	    // Cases:
	    length = Math.min(available(), length);
	    
	    // Case 1: head<tail.
	    // Case 2: tail<head.
	    // and from zero to head (offset offset + (end-tail))
	    // Case 1:[dddHfffffffTdddddd]
	    // Case 2:[ffffTddddddddHffff]
	    // Case 1 ex.: H=2, T=5, l=4
	    // available() = (2-5)&7 = (0b111111101)&7 = 5.
	    // length = 4.
	    // firstChunk = min(4, 8-5) = 3
	    // So positions 5,6,7 are copied to
	    // secondChunk = 4-3 = 1
	    // One more:
	    // H=2, T=5, l=1
	    // available() = 5
	    // length = 1
	    // firstChunk = 1
	    // secondChunk = 1-1 = 0.
	    if (head < tail) {
		int firstChunk = Math.min(length, _rxBuffer.mask+1 - _rxBuffer.tail);
		System.arraycopy(_rxBuffer.data, _rxBuffer.tail, buf, offset, firstChunk);
		int secondChunk = length - firstChunk;
		System.arraycopy(_rxBuffer.data, 0, buf, firstChunk+offset, secondChunk);
	    } else if (head > tail) {
		System.arraycopy(_rxBuffer.data, _rxBuffer.tail, buf, offset, length);
	    }
	    _rxBuffer.tail = (_rxBuffer.tail + length) & _rxBuffer.mask;
	}


	int writeBlock(byte[] data, int offset, int length) {
	    length = Math.min(length, txspace());

	    // Case 1: head<tail. Copy from head, and length bytes forward. Copy to offset of offset.
	    // Case 2: tail<head. Copy (end-tail) to tail (source offset offset), 
	    // and from zero to head (offset offset + (end-tail))
	    // Case 1:[dddHfffffffTdddddd]
	    // Case 2:[ffffTddddddddHffff]
	    // Ex.: Length=8, mask=7.

	    if (head < tail) {
		System.arraycopy(buf, offset, _txBuffer.bytes, _txBuffer.head, length);
	    } else if (head > tail) {
		// The number of bytes to copy from head to end of array.
		// WRONG: Source array may be shorter than this.
		// int j = _txBuffer.mask+1 - _txBuffer.head;
		int firstChunk = Math.min(length, _txBuffer->mask+1 - _txBuffer->head);
		System.arraycopy(buf, offset, _txBuffer.bytes, _txBuffer.head, firstChunk);
		int secondChunk = length - firstChunk;
		System.arraycopy(buf, firstChunk + offset, _txBuffer.bytes, 0, secondChunk);
	    }
	    _txBuffer.head = (_txBuffer.head + length) & _txBuffer.mask;
	    return length;
	}
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
	}

	public InputStream getInputStream() throws IOException {
		return serialPort.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return serialPort.getOutputStream();
	}

    class Transition {
	String token;
	State state;
    }

    State initialState;
    Transition[] URCTransitions = new Transition[20];
    int numberOfURCs = 0;
    BufferedStream userData = new BufferedStream();
    BufferedStream modem = new BufferedStream();

    int bufptr;
    char[] buffer = new char[1000];
    State nextState;
    

    private static final int		GSM_P_START = 0;
    private static final int		GSM_P_COMMAND_SENT = 10;
    private static final int		GSM_P_ARGUMENT_SENT = 20;
		// KILLING_ECHO=10,
		// KILLED_ECHO=20.
    private static final int		GSM_P_INITIAL_WS = 30;
    private static final int		GSM_P_INTITAL_RESPONSE = 40;
		// For queries, one can add state for parsing the 2nd line.
    private static final int		GSM_P_TRAILING_WS = 70;
    private static final int		GSM_P_DONE = 80;

    void write(char c) throws IOException {
		getOutputStream().write(c);
		//System.out.write(c);
		//System.out.flush();
	}

	void write(String cs) throws IOException {
		char[] chars = cs.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			write(chars[i]);
		}
	}

	void write(byte[] bs) throws IOException {
		for (int i = 0; i < bs.length; i++) {
			int asChar = bs[i];
			if (asChar < 0)
				asChar += 256;
			write((char) asChar);
		}
	}

	int read() throws IOException {
	    int i = getInputStream().read();
	    if (i >= 0) {
			//System.err.print((char) i);
			//System.err.flush();
		}
		return i;
	}

	// Make a partial string match: Whether the characters match through the
	// length of the key
	// (excessive chars at input are ignored). '?' in a key will match any
	// character in input.
    // impl.
	boolean doMatch(String key, char[] input, int length) {
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
	int doMatch(String[] patterns, int numPatterns, char[] input, int length) {
		for (int i = 0; i < numPatterns; i++) {
			if (doMatch(patterns[i], input, length)) {
				return i;
			}
		}
		return -1;
	}

	static class ParseState {
		State state;
		int progress;
 	}

	static abstract class State {
		String name;
		boolean isConnected;
		
		State(String name) {
			this.name = name;
		}
		
		boolean isConnected() {return isConnected;}

	    abstract void task(GSMStream outer, ParseState ps) throws IOException;

	    // impl.
	    void checkSuccessor(GSMStream outer, ParseState ps) {
		if (ps.progress == DONE_TRAILING_WS) {
		    if (ps.nextState != null) {
			ps.nextState.setOrigin(ps);
			ps.state = ps.nextState;
			ps.nextState = null;
			ps.progress = START;
			ps.bufptr = 0;
		    } else {
			ps.progress = INITIAL_WS;
			ps.bufptr = 0;
		    }
		}
		}
	}
    
	static class DeadState extends DecisionState {
	    DeadState(String name) {
		super(name);
	    }
	    
	    //impl.
	    void task(GSMStream outer, ParseState ps) throws IOException {
		// There is no transmit in a dead state.
		if (ps.progress == START) {
		    ps.progress = INITIAL_WS;
			}
		receive(outer, ps);
		checkSuccessor(outer, ps);
	    }
	}

	static class KeepAliveState extends DeadState {
	    long lastTime = 0;
	    
		KeepAliveState(String name) {
			super(name);
		}

	    void task(GSMStream outer, ParseState ps) throws IOException {
		super.task(outer, ps);
		if (ps.progress == INITIAL_WS) {
		    // Apparently not receiving anything.
		    if (System.currentTimeMillis() > lastTime + 30000) {
			lastTime = System.currentTimeMillis();
			transmitState.setOrigin(ps);
			transmitState.setData("hey!".getBytes());
			ps.state = transmitState;
			ps.progress = START;
			ps.bufptr = 0;
		    }
		}
	    }
	}

	static class DecisionState extends State {
	    String[] responseTokens = new String[10];
	    State[] nextStates = new State[10];
	    int numNextStates = 0;
	    
	    DecisionState(String name) {
		super(name);
	    }
	    
	    void addTransition(String token, State nextState) {
		responseTokens[numNextStates] = token;
		nextStates[numNextStates] = nextState;
		numNextStates++;
	    }
	    
	    // impl.
	    void receive(GSMStream outer, ParseState ps) throws IOException {
		int i = read();
		if (i < 0)
		    return;
		
		// Absorb whitespace until first real text.
		if (ps.progress == INITIAL_WS) {
		    if (i != 13 && i != 10) {
			ps.progress = INITIAL_TEXT;
		    }
		}
		
		if (ps.progress == INITIAL_TEXT) {
		    if (i != 13 && i != 10) {
			ps.buffer[ps.bufptr++] = (char) i;
			int match = doMatch(responseTokens, numNextStates, ps.buffer, ps.bufptr);
			if (match >= 0) {
			    State nextState = nextStates[match];
			    ps.nextState = nextState;
			    //System.out.println("Ordinary match: " + nextState.name);
			} else {
			    match = doURCMatch(ps.buffer, ps.bufptr);
			    if (match >= 0) {
				State nextState = URCNextStates[match];
				if (nextState instanceof TemporaryState) {
				    // change state immediately.
				    nextState.setOrigin(ps);
				    ps.state = nextState;
				    ps.progress = START;
				    ps.bufptr = 0;
				} else { // defer until tail is consumed.
				    ps.nextState = nextState;
				}
			    }
			}
		    } else {
			if (i == 13)
			    ps.progress = TRAILING_WS;
			else if (i == 10)
			    ps.progress = DONE_TRAILING_WS;
		    }
		}
		
		else if (ps.progress == TRAILING_WS) {
		    ps.progress = DONE_TRAILING_WS;
		}
	    }
	    
	    // impl.
	    void task(GSMStream outer, ParseState ps) throws IOException {
		if (ps.progress == START) {
		    ps.progress = INITIAL_WS;
		}
		
		if (ps.progress > START) {
		    receive(ps);
		}
		checkSuccessor(ps);
	    }
	}

	static class CommandState extends AutomaticState {
	    String command;
		
		CommandState(String name, String command) {
			super(name);
			this.command = command;
		}
		
	    // Impl.
	    void task(GSMStream outer, ParseState ps) throws IOException {
		if (ps.progress == START) {
		    write(command + '\r');
		}
		
		super.doStuff(ps);
	    }
	}

        static class TransmitState extends CommandState {

	    TransmitState() {
		super("TRANSMIT", "AT+CIPSEND=");
	    }
	    
	    void task(GSMStream outer, ParseState ps) throws IOException {
		if (ps.progress == START) {
		    write(command + Integer.toString(data.length) + "\r");
		    ps.progress = 1;
		}

			if (ps.progress < INITIAL_WS) {
				int i = read();
				if (i < 0) {
					return;
				}
				if (i == ' ') {
					write(data);
					ps.progress = INITIAL_WS;
				}
			} else
				receive(ps);
			checkSuccessor(ps);
		}
	}

	TransmitState transmitState = new TransmitState();

    static class DataReceptionState extends State {
		int numberOfBytes;
		int byteIdx;
		String numberOfBytesString = "";

	//		byte[] data = new byte[1500];
		//int[] distribution = new int[256];

		DataReceptionState(String name) {
			super(name);
		}

	void task(GSMStream outer, ParseState ps) throws IOException {
			int i = read();
			if (i < 0)
				return;
			if (ps.progress == START) {
				// lick up the byte count.
				numberOfBytes = 0;
				ps.progress = START + 1;
			}
			if (ps.progress == START + 1) {
				if (i != ':') {
					numberOfBytes *= 10;
					numberOfBytes += (i - '0');
				} else {
					ps.progress = START + 2;
					byteIdx = 0;
				}
			} else if (ps.progress == START + 2) {
				// do something with the data.
				data[byteIdx++] = (byte) i;
				if (byteIdx == numberOfBytes) {
					// actually we are done.
					System.out.println("Got " + numberOfBytes + " bytes of data: " + new String(data, 0, numberOfBytes));
					for (int j = 0; j < numberOfBytes; j++) {
						int b = data[j];
						if(b<0) b+=256;
						System.out.print(b + " ");
					}
					ps.state = origin.state;
					ps.bufptr = 0;
					ps.progress = 0;
				}
			}
		}
	}

	static class SMSReceptionState extends State {
		SMSReceptionState() {
			super("SMS_RECEPTION");
		}

	    void task(GSMStream outer, ParseState ps) throws IOException {
			// At end:
			ps.state = origin.state;
			ps.progress = START;
		}
	}

	static class CallReceptionState extends State {
		CallReceptionState() {
			super("CALL_RECEPTION");
		}

	    void task(GSMStream outer, ParseState ps) throws IOException {
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

	void addURCTransition(String token, State nextState) {
	    Transition t = new Transition(token, nextState);
	    URCTransitions[numberOfURCs] = t;
	    numberOfURCs++;
	}

	int doURCMatch(char[] input, int length) {
		return doMatch(URCTokens, numberOfURCs, input, length);
	}

	State simpleSetup() {
		// All the error messages basically..
		// FailedState configurationError = new FailedState("configuration error.");

		// The URC based states may appear any time...
		State dataReceptionState = new DataReceptionState("RECEIVING_DATA");

		CommandState reset = new CommandState("RESET", "AT+CFUN=1,1");

		// These are all allowed to be skipped by URCs.
		CommandState entry = new CommandState("ENTRY",
				"ATV1 E0 X1 S0=0 +CMEE=0");

		CommandState detach = new CommandState("REGISTERED", "AT+CGATT=0");
		// CommandState configWhileDetached = new CommandState("Cwd",
		// "AT+CIPMODE=0; +CIPMUX=0");
		CommandState configuration1 = new CommandState("SETTINGS1",
				"AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPHEAD=1;+CIPSPRT=0");
		CommandState configuration2 = new CommandState("SETTINGS2",
				"AT+CSTT=\"internet\"");
		CommandState reattach = new CommandState("DETACHED", "AT+CGATT=1");

		CommandState dataConnection = new CommandState("DATA_CONNECTION",
				"AT+CIPSTART=\"udp\",\"www.sky-cam.dk\",\"10000\"");

		entry.addTransition("OK", detach);
		entry.addTransition("ERROR", reset);
		// entry.setTimeoutTransition(localCommunicationsError);

		reset.addTransition("OK", entry);
		reset.addTransition("ERROR", entry);

		detach.addTransition("OK", configuration1);
		detach.addTransition("ERROR", reset);

		// configWhileDetached.addTransition("OK",detached);
		// configWhileDetached.addTransition("ERROR",registered);

		reattach.addTransition("OK", dataConnection);
		reattach.addTransition("ERROR", reset);

		configuration1.addTransition("OK", configuration2);
		configuration1.addTransition("ERROR", reset);

		configuration2.addTransition("OK", reattach);
		configuration2.addTransition("ERROR", reset);

		State dataConnectionStarting = new DeadState("DATA_CONNECTION_STARTING");
		State dataConnectionOpen = new DeadState("DATA_CONNECTION_OPEN");
		
		dataConnection.addTransition("OK", dataConnectionStarting);
		dataConnection.addTransition("ERROR", dataConnection);

		// All URCs should be represented!
		addURCTransition("RDY", entry); // We still do the alive
										// test, just to
		// check the TxD line

		// This is mostly here so that we at least understand the URC when it
		// comes.
		addURCTransition("+CFUN: ?", entry);
		addURCTransition("+CREG: 1", detach);
		addURCTransition("+CREG: 5", detach);
		addURCTransition("+CGREG: 0", reattach);
		addURCTransition("+CGREG: 1", configuration1); // GPRS
		addURCTransition("+CGREG: 5", configuration1); // GPRS
		addURCTransition("+IPD,", dataReceptionState);
		addURCTransition("CONNECT OK", dataConnectionOpen);
		addURCTransition("ALREADY CONNECT", dataConnectionOpen);

		dataConnectionOpen.isConnected = true;
		transmitState.isConnected = true;
		dataReceptionState.isConnected = true;
		
		return entry;
	}

	State evenSimplerSetup() {
		// All the error messages basically..
		FailedState configurationError = new FailedState("configuration error.");

		// The URC based states may appear any time...
		State incomingData = new DataReceptionState("RECEIVING_DATA");

		CommandState reset = new CommandState("RESET", "AT+CFUN=1,1");

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

		CommandState reset_IP = new CommandState("RESTART", "AT+CIPSHUT");
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
		transmitState.addTransition("ERROR", dataConnectionOpen);

		dataConnection.addTransition("OK", dataConnectionOpening);
		dataConnection.addTransition("ERROR", entry); // ???

		// All URCs should be represented!
		addURCTransition("RDY", entry); // We still do the alive
										// test, just to
		addURCTransition("+CFUN: ?", entry);
		addURCTransition("+CGREG: 1", entry); // GPRS registered, so (re)start
		addURCTransition("+CGREG: 5", entry); // GPRS registered, so (re)start
		addURCTransition("+CGREG: 3", entry); // GPRS denied.  This is issued at shutdown. We use this to detect that we are offline.
		addURCTransition("+IPD,", incomingData);
		addURCTransition("CONNECT OK", dataConnectionOpen);
		addURCTransition("ALREADY CONNECT", reset_IP);

		return entry;
	}

	void run() throws IOException {
		ParseState ps = new ParseState();
		ps.state = initialState;
		ps.progress = 0;
		ps.bufptr = 0;

		boolean isConnected = false;
		
		while (true) {
			if(isConnected != ps.state.isConnected) {
				isConnected = ps.state.isConnected;
				if(isConnected) System.err.println("CONNECTED");
				else System.err.println("DISCONNECTED");
			}
			ps.state.doStuff(ps);
		}
	}

	public static void main(String[] args) throws IOException {
		// System.out.println("\\r is (13) " + (int)'\r' + " and \\l is (10) " +
		// (int)'\n');
		StupidSIM900StateMachineNonblocking m = new StupidSIM900StateMachineNonblocking();
		m.init("COM4");
		m.initialState = m.evenSimplerSetup();
		m.run();
	}
}
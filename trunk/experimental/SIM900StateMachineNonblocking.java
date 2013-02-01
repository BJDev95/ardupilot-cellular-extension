import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SIM900StateMachineNonblocking {
	private SerialPort serialPort;

	public void init(String id) throws IOException {
		// CommPortIdentifier.getPortIdentifier("COM1");
		// Just take the 1st available port.
		CommPortIdentifier portIdentifier = null;

		try {
			if (id == null) {
				portIdentifier = (CommPortIdentifier) CommPortIdentifier.getPortIdentifiers().nextElement();
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
				CommPort commPort = portIdentifier.open(this.getClass().getName(), 2001);
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

	List<Transition> URCTransitions = new ArrayList<Transition>();
	State initialState;

	private static final int START = 0;
	private static final int RESOLVING_IF_URC = 10;
	private static final int RESOLVED_IF_URC = 20;
	private static final int READING_RESPONSE = 30;
	private static final int DONE_READING_RESPONSE_AND_WS = 40;

	void write(char c) throws IOException {
		System.out.write(c);
		System.out.flush();
		getOutputStream().write(c);
	}

	void write(String cs) throws IOException {
		char[] chars = cs.toCharArray();
		for (int i=0; i<chars.length; i++) {
			write(chars[i]);
		}
	}

	int read() throws IOException {
		int i = getInputStream().read();
		if (i>=0) {
			System.err.print((char)i);
			System.err.flush();
		}
		return i;
	}
	
	/*
	 * State handleURCOrJunk(String line, State sender) { for (Transition
	 * transition : URCTransitions) { if (doMatch(transition.token, line)) {
	 * State nextState = transition.nextState; nextState.setReturnState(sender);
	 * return nextState; } } return null; }
	 */

	// Make a partial string match: Whether the characters match through the
	// length of the key
	// (excessive chars at input are ignored). '?' in a key will match any
	// character in input.
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

	class Transition {
		String token;
		State nextState;

		Transition(String token, State nextState) {
			this.token = token;
			this.nextState = nextState;
		}
	}

	class ParseState {
		State state;
		int progress;
		int bufptr;
		char[] buffer = new char[1000];
	}

	abstract class State {
		String name;

		State(String name) {
			this.name = name;
		}

		abstract void doStuff(ParseState ps) throws IOException;

		void setOrigin(ParseState origin) { /* throw away */
		}
	}

	class DeadState extends State {
		DeadState(String name) {
			super(name);
		}

		void doStuff(ParseState ps) throws IOException {
			// We do nothing until an URC comes our way.
			// Well .. then we actually have to check for URCs.
		}
	}

	class CommandState extends State {
		String command;
		State OKNextState;
		State errorNextState;

		String[] options = new String[] { "OK", "ERROR" };

		CommandState(String name, String command) {
			super(name);
			this.command = command;
		}

		void doStuff(ParseState ps) throws IOException {
			if (ps.progress == START) {
				write(command + '\r');
				ps.progress = RESOLVING_IF_URC;
			}

			if (ps.progress > START) {
				int i = read();
				if (i < 0)
					return;

				if (ps.progress == RESOLVING_IF_URC) {
					// Determine echo or URC
					if (i == 'A') {
						// echo.
						ps.progress = RESOLVED_IF_URC;
					} else {
						// URC! Its leading CR will be missing....
						urcParsingState.setOrigin(ps);
						ps.progress = ps.bufptr = 0;
						ps.state = urcParsingState;
						return;
					}
				}

				if (ps.progress == RESOLVED_IF_URC) {
					// Eat echo until CR or LF
					if (i == 13 || i == 10) {
						ps.progress = RESOLVED_IF_URC + 1;
					}
				}

				else if (ps.progress == RESOLVED_IF_URC + 1) {
					// Eat CR or LF until response.
					if (i != 13 && i != 10) {
						ps.progress = READING_RESPONSE;
					}
				}

				if (ps.progress == READING_RESPONSE) {
					if (i == 13 || i == 10) {
						ps.progress = DONE_READING_RESPONSE_AND_WS;
					} else {
						ps.buffer[ps.bufptr++] = (char) i;
					}
				}

				else if (ps.progress == DONE_READING_RESPONSE_AND_WS) {
					int match = doMatch(options, 2, ps.buffer, ps.bufptr);
					if (match < 0) {
						System.err.println("Strange, neither OK nor ERROR returned from command: " + new String(ps.buffer, 0, ps.bufptr));
						match = 0;
					}
					if (match == 0) {
						ps.state = OKNextState;
						ps.bufptr = 0;
						ps.progress = 0;
					} else {
						ps.state = errorNextState;
						ps.bufptr = 0;
						ps.progress = 0;
					}
				}
			}
		}

		void setOKTransition(State nextState) {
			this.OKNextState = nextState;
		}

		void setErrorTransition(State nextState) {
			this.errorNextState = nextState;
		}
	}

	class QueryState extends State {
		String query;
		String[] responseTokens = new String[10];
		State[] nextStateOptions = new State[10];
		int numNextStates = 0;

		QueryState(String name, String query) {
			super(name);
			this.query = query;
		}

		void addResponseTransition(String token, State nextState) {
			responseTokens[numNextStates] = token;
			nextStateOptions[numNextStates] = nextState;
			numNextStates++;
		}

		void doStuff(ParseState ps) throws IOException {
			if (ps.progress == START) {
				write(query + '\r');
				ps.progress = RESOLVING_IF_URC;
			}

			if (ps.progress > START) {
				int i = read();
				if (i < 0)
					return;

				if (ps.progress == RESOLVING_IF_URC) {
					// Determine echo or URC
					if (i == 'A') {
						// echo.
						ps.progress = RESOLVED_IF_URC;
					} else {
						// URC! Its leading CR will be missing....
						urcParsingState.setOrigin(ps);
						ps.progress = ps.bufptr = 0;
						ps.state = urcParsingState;
						return;
					}
				}

				if (ps.progress == RESOLVED_IF_URC) {
					// Eat echo until CR or LF are met.
					if (i == 13 || i == 10) {
						ps.progress = RESOLVED_IF_URC + 1;
					}
				}

				if (ps.progress == RESOLVED_IF_URC + 1) {
					// Eat CRs and LFs between echo and response.
					if (i != 13 && i != 10) {
						ps.progress = READING_RESPONSE;
					}
				}

				if (ps.progress == READING_RESPONSE) {
					// Buffer response.
					if (i != 13 && i != 10) {
						ps.buffer[ps.bufptr++] = (char) i;
					} else {
						ps.progress = READING_RESPONSE + 1;
					}
				}

				if (ps.progress == READING_RESPONSE + 1) {
					// Eat CRs and LFs between response and OK/ERROR.
					if (i != 13 && i != 10) {
						ps.progress = READING_RESPONSE + 2;
					}
				}

				else if (ps.progress == READING_RESPONSE + 2) {
					// Eat OK until CR or LF are met.
					if (i == 13 || i == 10) {
						ps.progress = DONE_READING_RESPONSE_AND_WS;
					}
				}

				else if (ps.progress == DONE_READING_RESPONSE_AND_WS) {
					// expect one LF. Well its eaten already.
					int match = doMatch(responseTokens, numNextStates,
							ps.buffer, ps.bufptr);
					if (match < 0) {
						System.err.println("No good query response: "
								+ new String(ps.buffer, 0, ps.bufptr));
					} else {
						ps.state = nextStateOptions[match];
						ps.bufptr = 0;
						ps.progress = 0;
					}
				}
			}
		}
	}

	abstract class TemporaryState extends State {
		TemporaryState(String name) {
			super(name);
		}

		ParseState origin = new ParseState();

		void setOrigin(ParseState origin) {
			this.origin.state = origin.state;
			//this.origin.buffer = origin.buffer;
			this.origin.progress = origin.progress;
			this.origin.bufptr = origin.bufptr;
		}
	}

	class URCParsingState extends TemporaryState {
		String[] responseTokens = new String[20];
		State[] nextStateOptions = new State[20];
		int numNextStates = 0;

		State nextState;

		URCParsingState() {
			super("PARSING_URC");
		}

		void addURCTransition(String token, State nextState) {
			responseTokens[numNextStates] = token;
			nextStateOptions[numNextStates] = nextState;
			numNextStates++;
		}

		void doStuff(ParseState ps) throws IOException {
			int i = read();
			if (i < 0)
				return;

			if (ps.progress == 0) {
				// Eat CRs and LFs between echo and response.
				if (i != 13 && i != 10) {
					ps.progress = 1;
				}
			}

			if (ps.progress == 1) {
				// We will NOT wait for CRLF here. With incoming data, there
				// could be (too) many chars to buffer before that happens.
				// But if CRLF appears without a match, we know that the URC was
				// not recognized.
				if (i != 13 && i != 10) {
					ps.buffer[ps.bufptr++] = (char) i;
					int match = doMatch(responseTokens, numNextStates,
							ps.buffer, ps.bufptr);
					if (match >= 0) {
						// We replace ourselves with the state resolved.
						// Problem: The non temporary states should be started
						// with clean input (rest of line and trailing CRLF)
						// consumed.
						// Other states should have the partial buffer and
						// continue with that (data, SMS..).
						nextState = nextStateOptions[match];
						nextState.setOrigin(origin);

						if (nextState instanceof TemporaryState) {
							// We just flush everything. The temp. states will
							// start without having to parse the beginning of
							// their own URC.
							ps.progress = START;
							ps.bufptr = 0;
							ps.state = nextState;
						} else {
							// We are permanently switching to another state. We
							// need to clear rubbish first.
							ps.progress = 2;
						}
					}
				} else {
					// We have encountered ONE CR or LF.
					ps.progress = 4;
				}
			}

			else if (ps.progress == 2) {
				// stay here to consume all before the first CR or LF including.
				if (i == 10 || i == 13)
					ps.progress = 4;
			}

			else if (ps.progress == 4) {
				if (nextState != null) {
					ps.state = nextState;
					nextState = null;
				} else {
					ps.state = origin.state;
				}
				ps.progress = START;
				ps.bufptr = 0;
			}
		}
	}

	URCParsingState urcParsingState = new URCParsingState();

	class DataReceptionState extends TemporaryState {
		int numberOfBytes;
		String numberOfBytesString = "";

		DataReceptionState(String name) {
			super(name);
		}

		void doStuff(ParseState ps) throws IOException {
			int i = read();
			if (i < 0)
				return;
			if (ps.progress == 0) {
				// lick up the byte count.
				numberOfBytes = 0;
				ps.progress = 1;
			}
			if (ps.progress == 1) {
				if (i != ':') {
					numberOfBytes *= 10;
					numberOfBytes += (i - '0');
				} else {
					ps.progress = 2;
				}
			} else if (ps.progress == 2) {
				// do something with the data.
				numberOfBytes--;
				if (numberOfBytes == 0) {
					// actually we are done.
					ps.state = origin.state;
					ps.bufptr = 0;
					ps.progress = RESOLVING_IF_URC;
				}
			}
		}
	}

	class SMSReceptionState extends TemporaryState {
		SMSReceptionState() {
			super("SMS_RECEPTION");
		}

		boolean driveCommand() {
			// nothing to be done.
			return true;
		}

		void doStuff(ParseState ps) throws IOException {
			// At end:
			ps.state = origin.state;
			ps.progress = START;
		}
	}

	class CallReceptionState extends State {
		CallReceptionState() {
			super("CALL_RECEPTION");
		}

		boolean driveCommand() {
			// nothing to be done.
			return true;
		}

		void doStuff(ParseState ps) throws IOException {
		}
	}

	class FailedState extends State {
		String message;

		FailedState(String message) {
			super("FAIL");
			this.message = message;
		}

		boolean driveCommand() {
			// nothing to be done.
			return true;
		}

		void doStuff(ParseState ps) throws IOException {
			System.err.println("FAIL: " + message);
			System.exit(-1);
		}
	}

	void addURCTransition(String token, State nextState) {
		Transition transition = new Transition(token, nextState);
		URCTransitions.add(transition);
	}

	State setup() {
		// All the error messages basically..
		FailedState localCommunicationsError = new FailedState("local comms error (check wiring and baud rates).");
		FailedState configurationError = new FailedState("configuration error.");
		FailedState registrationDenied = new FailedState("registration denied.");
		FailedState networkError = new FailedState("network error.");
		FailedState SIMMissing = new FailedState("SIM missing.");
		FailedState PINError = new FailedState("PIN error");

		// The URC based states may appear any time...
		SMSReceptionState incomingSMS = new SMSReceptionState();
		CallReceptionState incomingCall = new CallReceptionState();
		State incomingData = new DataReceptionState("RECEIVING_DATA");

		CommandState reset = new CommandState("RESET", "AT+CFUN=1,1");
		
		// These are all allowed to be skipped by URCs.
		CommandState entry = new CommandState("ENTRY", "ATV1 E1 X1 S0=0 +CMEE=0");
		QueryState checksim = new QueryState("ALIVE", "AT+CSMINS?");
		QueryState simready = new QueryState("SIMREADY", "AT+CPIN?");
		QueryState PINOK = new QueryState("PINOK", "AT+CREG?");

		entry.setOKTransition(checksim);
		entry.setErrorTransition(reset);
		// entry.setTimeoutTransition(localCommunicationsError);

		reset.setOKTransition(entry);
		reset.setErrorTransition(entry);
		
		checksim.addResponseTransition("+CSMINS: ?,0", SIMMissing);
		checksim.addResponseTransition("+CSMINS: ?,1", simready);

		simready.addResponseTransition("+CPIN: READY", PINOK);
		simready.addResponseTransition("+CPIN: PIN", PINError); // anything that
																// begins with
																// PIN is a
																// prompt to
																// input a PIN.

		CommandState registered = new CommandState("REGISTERED", "AT+CGATT=0");
		// CommandState configWhileDetached = new CommandState("Cwd", "AT+CIPMODE=0; +CIPMUX=0");
		CommandState detached =  new CommandState("REGISTERED", "AT+CGATT=1");

		PINOK.addResponseTransition("+CREG: ?,0", PINOK); // entry if not(!)
															// searching.
		PINOK.addResponseTransition("+CREG: ?,1", registered); // home network
		PINOK.addResponseTransition("+CREG: ?,2", PINOK); // repeat while
															// searching.
		PINOK.addResponseTransition("+CREG: ?,3", registrationDenied);
		PINOK.addResponseTransition("+CREG: ?,5", registered); // roaming

		// These should NOT be skipped ever!! But they are advanced settings
		// that can all wait a little.
		// So, no URSs (at startup) should go beyond here.
		CommandState configuration1 = new CommandState("SETTINGS1", "AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPHEAD=1;+CIPSPRT=0");
		CommandState configuration2 = new CommandState("SETTINGS2", "AT+CSTT=\"internet\"");
		CommandState configurationDone = new CommandState("SETTINGSDONE", "AT+CIICR");
		CommandState configurationDone2 = new CommandState("SETTINGSDONE", "AT+CIFSR");

		registered.setOKTransition(detached);
		registered.setErrorTransition(reset);
		
		//configWhileDetached.setOKTransition(detached);
		//configWhileDetached.setErrorTransition(registered);
		
		detached.setOKTransition(configuration1);
		detached.setErrorTransition(reset);

		configuration1.setOKTransition(configuration2);
		configuration1.setErrorTransition(configurationError);

		configuration2.setOKTransition(configurationDone);
		configuration2.setErrorTransition(configurationError);

		CommandState dataConnection = new CommandState("DATA_CONNECTION",
				"AT+CIPSTART=\"udp\",\"www.sky-cam.dk\",\"10000\"");
		// CommandState alreadyDataConnection = new
		// CommandState("DATA_CONNECTION",
		// "AT+CIPSTART=\"UDP\",\"www.sky-cam.dk\",\"10000\"");
		State dataConnectionStarting = new DeadState("DATA_CONNECTION_STARTING");
		State dataConnectionOpen = new DeadState("DATA_CONNECTION_OPEN");

		configurationDone.setOKTransition(configurationDone2);
		
		configurationDone2.setOKTransition(dataConnection);
		configurationDone2.setErrorTransition(dataConnection);

		dataConnection.setOKTransition(dataConnectionStarting);
		dataConnection.setErrorTransition(dataConnection);

		// All URCs should be represented!
		urcParsingState.addURCTransition("RDY", entry); // We still do the alive
														// test, just to
		// check the TxD line

		// This is mostly here so that we at least understand the URC when it
		// comes.
		urcParsingState.addURCTransition("+CFUN: ?", entry);

		urcParsingState.addURCTransition("+CSMINS: ?,1", simready);
		urcParsingState.addURCTransition("+CPIN: READY", PINOK);

		urcParsingState.addURCTransition("+CREG: 2", PINOK); // not yet
																// registered
																// but trying.
		urcParsingState.addURCTransition("+CREG: 1", registered); // home
																	// network
		urcParsingState.addURCTransition("+CREG: 5", registered); // roaming

		urcParsingState.addURCTransition("+CGREG: 0", registered); // registered
																	// is
																	// considered
		// before GPRS registered.
		urcParsingState.addURCTransition("+CGREG: 1", configuration1); // GPRS
																		// home
		urcParsingState.addURCTransition("+CGREG: 5", configuration1); // GPRS
																		// roaming

		urcParsingState.addURCTransition("+IPD,", incomingData);
		urcParsingState.addURCTransition("+CRING:", incomingCall);
		urcParsingState.addURCTransition("+CMTI:", incomingSMS);

		urcParsingState.addURCTransition("CONNECT OK", dataConnectionOpen);
		urcParsingState.addURCTransition("ALREADY CONNECT", dataConnectionOpen);

		// TODO: We need:
		// State dependent data reading (so the receivingData state can eat up
		// its correct number of bytes)
		// TODO: Should receivingData simply return to the previous state when
		// done?

		// We also need: Partial response matching.
		return entry;
	}


	State simpleSetup() {
		// All the error messages basically..
		FailedState configurationError = new FailedState("configuration error.");

		// The URC based states may appear any time...
		State incomingData = new DataReceptionState("RECEIVING_DATA");

		CommandState reset = new CommandState("RESET", "AT+CFUN=1,1");
		
		// These are all allowed to be skipped by URCs.
		CommandState entry = new CommandState("ENTRY", "ATV1 E1 X1 S0=0 +CMEE=0");

		CommandState registered = new CommandState("REGISTERED", "AT+CGATT=0");
		// CommandState configWhileDetached = new CommandState("Cwd", "AT+CIPMODE=0; +CIPMUX=0");
		CommandState detached =  new CommandState("REGISTERED", "AT+CGATT=1");

		CommandState configuration1 = new CommandState("SETTINGS1"   , "AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPHEAD=1;+CIPSPRT=0;CIPCSGP=1,\"internet\"");
		CommandState configuration2 = new CommandState("STARTUP1"    , "AT+CSTT");
		CommandState configurationDone = new CommandState("STARTUP2" , "AT+CIICR");
		CommandState configurationDone2 = new CommandState("STARTUP3", "AT+CIFSR");

		CommandState restart1 = new CommandState("RESTART1", "AT+CIPSHUT");
		restart.
		
		entry.setOKTransition(registered);
		entry.setErrorTransition(reset);
		// entry.setTimeoutTransition(localCommunicationsError);

		reset.setOKTransition(entry);
		reset.setErrorTransition(entry);
		
		registered.setOKTransition(detached);
		registered.setErrorTransition(reset);
		
		//configWhileDetached.setOKTransition(detached);
		//configWhileDetached.setErrorTransition(registered);
		
		detached.setOKTransition(configuration1);
		detached.setErrorTransition(reset);

		configuration1.setOKTransition(configuration2);
		configuration1.setErrorTransition(configurationError);

		configuration2.setOKTransition(configurationDone);
		configuration2.setErrorTransition(configurationError);

		CommandState dataConnection = new CommandState("DATA_CONNECTION", "AT+CIPSTART=\"udp\",\"www.sky-cam.dk\",\"10000\"");
		State dataConnectionStarting = new DeadState("DATA_CONNECTION_STARTING");
		State dataConnectionOpen = new DeadState("DATA_CONNECTION_OPEN");

		configurationDone.setOKTransition(configurationDone2);
		
		configurationDone2.setOKTransition(dataConnection);
		configurationDone2.setErrorTransition(dataConnection);

		dataConnection.setOKTransition(dataConnectionStarting);
		dataConnection.setErrorTransition(dataConnection);

		// All URCs should be represented!
		urcParsingState.addURCTransition("RDY", entry); // We still do the alive
														// test, just to
		// check the TxD line

		// This is mostly here so that we at least understand the URC when it
		// comes.
		urcParsingState.addURCTransition("+CFUN: ?", entry);
		urcParsingState.addURCTransition("+CREG: 1", registered); 
		urcParsingState.addURCTransition("+CREG: 5", registered);
		urcParsingState.addURCTransition("+CGREG: 0", registered); // registered
		urcParsingState.addURCTransition("+CGREG: 1", configuration1); // GPRS
		urcParsingState.addURCTransition("+CGREG: 5", configuration1); // GPRS
		urcParsingState.addURCTransition("+IPD,", incomingData);
		urcParsingState.addURCTransition("CONNECT OK", dataConnectionOpen);
		urcParsingState.addURCTransition("ALREADY CONNECT", dataConnectionOpen);

		return entry;
	}

void run() throws IOException {
		ParseState ps = new ParseState();
		ps.state = initialState;
		ps.progress = 0;
		ps.bufptr = 0;

		while (true) {
			ps.state.doStuff(ps);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("\\r is " + (int)'\r' + " and \\l is " + (int)'\n');
		SIM900StateMachineNonblocking m = new SIM900StateMachineNonblocking();
		m.init("COM4");
		m.initialState = m.simpleSetup();
		m.run();
	}
}
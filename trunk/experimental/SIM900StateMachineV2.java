import java.io.*;
import java.util.*;

/*
 * Echoing: Trailing CRs in input ARE echoed. Trailing line feeds not.
 * There is a leading CRLF before responses.
 *
 * Simple OK response: <CR><LF>OK<CR><LF>
 *
 * URC: Apparently <CR><LF>+text<CR><LF>
 *
 * Query response: (<CR><LF>data)* <CR><LF> <CR><LF>OK<CR><LF>
 *
 * How to parse this wihtout echo:
 * 1) Wait for <CR><LF>
 * 2) s = string until next <CR><LF>
 * 3) if s = OK or s = ERROR then we have a completed command response
 * 4) if s matches a known URC then we have a URC (go digest it)
 * 5) else we need more input
 *
 * How to parse this with echo:
 * Command mode:
 * 0) Send command
 * 1) Wait for <CR> received (echo of sent command)
 * 2) Accumulate till <CR><LF>OK<CR><LF> or <CR><LF>ERROR<CR><LF>
 * Check-URC-mode:
 * Wait for <CR><LF>text<CR><LF>
 *
 * Restricted simplified mode:
 * We can deal with the different response types:
 * 1) OK or ERROR
 * 2) One line query responses
 * 3) Known URCs
 * We know whether we are expecting a query response or not.
 */

public class SIM900StateMachineV2 {
    Reader reader = new InputStreamReader(System.in);
    BufferedReader breader = new BufferedReader(reader);
    List<Transition> URCTransitions = new ArrayList<Transition>();
    State currentState;
   
    
    String read() throws IOException {
	return breader.readLine();
    }

    void write(String s) throws IOException {
	System.out.println(s);
    }

    // Make a partial string match: Whether the characters match through the length of the key 
    // (excessive chars at input are ignored). '?' in a key will match any character in input.
    boolean doMatch(String key, String input) {
	if(input.length() < key.length()) return false;
	for (int idx=0; idx<key.length(); idx++) {
	    char k = key.charAt(idx);
	    char i = input.charAt(idx);
	    if (k!=i && k!='?')
		return false;
	}
	return true;
    }

    class Transition {
	String token;
	State nextState;

	Transition(String token, State nextState) {
	    this.token = token;
	    this.nextState = nextState;
	}
    }
    
    abstract class State {
	String name;
	
	State(String name) {
	    this.name = name;
	}
	
	abstract void doAction() throws IOException;
	abstract State expectResponse() throws IOException;
	//	boolean isTemporary() {return false;}
	void setReturnState(State returnState) { /* throw away */ }
    }

    State handleURCOrJunk(String line, State sender) {
	for (Transition transition : URCTransitions) {
	    if(doMatch(transition.token, line)) {
		State nextState = transition.nextState;
		nextState.setReturnState(sender);
		return nextState;
	    }
	}
	return null;
    }

    class DeadState extends State {
	DeadState(String name) {
	    super(name);
	}

	void doAction() {}
	State expectResponse() {
	    return this;
	}
    }

    class CommandState extends State {
	String command;
	State OKNextState;
	State errorNextState;

	CommandState(String name, String command) {
	    super(name);
	    this.command = command;
	}

	void doAction() throws IOException {
	    write(command);
	}

	State expectResponse() throws IOException {
	    String wastedCRLF = read();
	    String line = read();
	    
	    if("OK".equals(line))
		return OKNextState;
	    
	    // TODO: Must work for verbose error messages too.
	    if ("ERROR".equals(line))
		return errorNextState;
	    
	    return handleURCOrJunk(line, this);
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
	List<Transition> responseTransitions = new ArrayList<Transition>();

	QueryState(String name, String query) {
	    super(name);
	    this.query = query;
	}

	void doAction() throws IOException {
	    write(query);
	}

	State expectResponse() throws IOException {
	    String line = read();
	    if (!("".equals(line)))
		System.err.println("Strange! A query response did not begin with CRLF!");
	    State nextState = null;

	    while (nextState == null) {
		line = read();
		// if (isURC == null) {
		// System.out.println("Received response to query: " + line);
		// now await <CR><LF>OK<CR><LF>; if not arriving do what?
		for (Transition transition : responseTransitions) {
		    if(doMatch(transition.token, line)) {
			nextState = transition.nextState;
			System.out.println("Found a match: " + nextState.name);
			break;
		    }
		}

		if(nextState==null) {
		    // check for URC and return directly if one found.
		}
	    }

	    // After a query we expect OK. ERROR could also happen but as far as I know only at errors in the query.
	    // We assume there are none.
	    // We also assume all queries are the kind that return only one data line.
	    line = read();
	    if (!("".equals(line)))
		System.err.println("Strange! A response did not have CRLFCRLF after data line!");
	    line = read();
	    if("OK".equals(line)) {
		return nextState;
	    } else {
		System.err.println("Strange: Non-OK query result!");		
		return this;
	    }
	}
	
	void addResponseTransition(String token, State nextState) {
	    Transition t = new Transition(token, nextState);
	    responseTransitions.add(t);
	}
    }

    abstract class TemporaryState extends State {
	State returnState;

	TemporaryState(String name) {
	    super(name);
	}
    
	void setReturnState(State returnState) {
	    this.returnState = returnState;
	}
    }

    class DataReceptionState extends TemporaryState {
	int numberOfBytes;
	String numberOfBytesString = "";

	DataReceptionState(String name) {
	    super(name);
	}
	
	void doAction() {
	    // nothing to be done.
	}

	State expectResponse() throws IOException {
	    int character;
	    while((character = breader.read()) != ':') {
		numberOfBytesString += (char)character;
	    }

	    numberOfBytes = Integer.parseInt(numberOfBytesString);
	    
	    for (int i=0; i<numberOfBytes; i++) {
		breader.read(); // waste the data in this dummy test implementation.
	    }

	    return returnState;
	}
    }

    class SMSReceptionState extends TemporaryState {
	SMSReceptionState() {
	    super("SMS_RECEPTION");
	}

	void doAction(){}
	State expectResponse(){return null;}
    }

    class CallReceptionState extends TemporaryState {
	CallReceptionState() {
	    super("CALL_RECEPTION");
	}
	void doAction(){}
	State expectResponse(){return null;}
    }

    class FailedState extends State {
	String message;

	FailedState(String message) {
	    super("FAIL");
	    this.message = message;
	}
	
	void doAction() {
	    System.err.println(message);
	    System.exit(-1);
	}

	State expectResponse() {
	    return null;
	}
    }

    void addURCTransition(String token, State nextState) {
	Transition transition = new Transition(token, nextState);
	URCTransitions.add(transition);
    }

    void setup() {
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

	// These are all allowed to be skipped by URCs.
	CommandState entry = new CommandState("ENTRY", "ATV1;E1;X1;S0=0");
	QueryState checksim = new QueryState("ALIVE", "AT+CSMINS?");
	QueryState simready = new QueryState("SIMREADY", "AT+CPIN?");
	QueryState PINOK = new QueryState("PINOK", "AT+CREG?");

	entry.setOKTransition(checksim);
	entry.setErrorTransition(localCommunicationsError);
	// entry.setTimeoutTransition(localCommunicationsError);

	checksim.addResponseTransition("+CSMINS: ?,0", SIMMissing);
	checksim.addResponseTransition("+CSMINS: ?,1", simready);

	simready.addResponseTransition("+CPIN: READY", PINOK);
	simready.addResponseTransition("+CPIN: PIN", PINError); // anything that begins with PIN is a prompt to input a PIN.

	QueryState registered = new QueryState("REGISTERED", "AT+CGREG?"); // as soon as we are registered, we ask if we are also GPRS registered.
	CommandState attach = new CommandState("ATTACH", "AT+CGATT=1");

	PINOK.addResponseTransition("+CREG: ?,0", PINOK);   // entry if not(!) searching.
	PINOK.addResponseTransition("+CREG: ?,1", registered); // home network
	PINOK.addResponseTransition("+CREG: ?,2", PINOK);   // repeat while searching.
	PINOK.addResponseTransition("+CREG: ?,3", registrationDenied);
	PINOK.addResponseTransition("+CREG: ?,5", registered); // roaming

	// These should NOT be skipped ever!! But they are advanced settings that can all wait a little.
	// So, no URSs (at startup) should go beyond here.
	CommandState configuration1 = new CommandState("SETTINGS1", "AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPMUX=0;+CIPMODE=0;+CIPHEAD=1;+CIPSPRT=0");
	CommandState configuration2 = new CommandState("SETTINGS2", "AT+CSTT=\"internet\"");
	CommandState configurationDone = new CommandState("SETTINGSDONE", "AT+CIICR");

	registered.addResponseTransition("+CGREG: ?,0", registered);
	registered.addResponseTransition("+CGREG: ?,1", configuration1);
	registered.addResponseTransition("+CGREG: ?,2", registered); // repeat while searching.

	attach.setOKTransition(configuration1);
	attach.setErrorTransition(registered); // panic, try again!

	configuration1.setOKTransition(configuration2);
	configuration1.setErrorTransition(configurationError);

	configuration2.setOKTransition(configurationDone);
	configuration2.setErrorTransition(configurationError);

	CommandState dataConnection = new CommandState("DATA_CONNECTION", "start IP connection...");
	State dataConnectionStarting = new DeadState("DATA_CONNECTION_STARTING");
	State dataConnectionOpen = new DeadState("DATA_CONNECTION_OPEN");
	
	configurationDone.setOKTransition(dataConnection);

	dataConnection.setOKTransition(dataConnectionStarting);
	
	
	currentState = entry;
	

	// All URCs should be represented!
	addURCTransition("RDY", entry); // We still do the alive test, just to check the TxD line

	// This is mostly here so that we at least understand the URC when it comes.
	addURCTransition("+CFUN: ?", entry);

	addURCTransition("+CSMINS: ?,1", simready);
	addURCTransition("+CPIN: READY", PINOK);

	addURCTransition("+CREG: 2", PINOK); // not yet registered but trying.
	addURCTransition("+CREG: 1", registered); // home network
	addURCTransition("+CREG: 5", registered); // roaming

	addURCTransition("+CGREG: 0", registered); // registered is considered before GPRS registered.
	addURCTransition("+CGREG: 1", configuration1); // GPRS home
	addURCTransition("+CGREG: 5", configuration1); // GPRS roaming
	
	addURCTransition("+CRING:", incomingCall);
	
	//+CMTI: "SM",1	
	addURCTransition("+CMTI:", incomingSMS);

	addURCTransition("CONNECT OK", dataConnectionOpen);

	// TODO: We need:
	// State dependent data reading (so the receivingData state can eat up its correct number of bytes)
	// TODO: Should receivingData simply return to the previous state when done?

	// We also need: Partial response matching.

	addURCTransition("+IPD,", incomingData);
    }

    void run() throws IOException {
	while(true) {
	    currentState.doAction();
	    State nextState = currentState.expectResponse();
	    if (nextState != null) {
		currentState = nextState;
	    }
	}
    }

    public static void main(String[] args) throws IOException {
	SIM900StateMachineV2 m = new SIM900StateMachineV2();
	m.setup();
	m.run();
    }
}
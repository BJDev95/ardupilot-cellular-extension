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

public class SIM900StateMachine {
    /*
    static class Symbol {
    }

    abstract static class AutoPromote {
	String command;
	boolean test(String response);
    }

    class OKAutoPromote extens AutoPromote {
	boolean test(String response) {
	    return "OK".equals(response);
	}
    }
    */
    Reader reader = new InputStreamReader(System.in);
    BufferedReader breader = new BufferedReader(reader);
    
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
    
    class State {
	String name;
	Map<String, State> transitions = new HashMap<String, State>();
	NextAction nextAction;
	State(String name) {
	    this.name = name;
	}
	void addTransition(String token, State nextState) {
	    this.transitions.put(token, nextState);
	}
	String read() throws IOException {
	    return SIM900StateMachine.this.read();
	}
    }
    
    class DataReceptionState extends State {
	int numberOfBytes;
	String numberOfBytesString = "";

	DataReceptionState(String name) {
	    super(name);
	}
	
	String read() throws IOException {
	    int character;
	    while((character = breader.read()) != ':') {
		numberOfBytesString += (char)character;
	    }

	    numberOfBytes = Integer.parseInt(numberOfBytesString);
	    
	    for (int i=0; i<numberOfBytes; i++) {
		breader.read(); // waste the data in this dummy test implementation.
	    }

	    return super.read();
	}
    }

    abstract class NextAction {
	abstract void execute() throws IOException;
    }

    class CommandAction extends NextAction {
	String command;
	CommandAction(String command) {
	    this.command = command;
	}
	void execute() throws IOException {
	    write(command);
	}
    }

    class FailAction extends NextAction {
	String message;
	FailAction(String message) {
	    this.message = message;
	}
	void execute() {
	    System.err.println(message);
	    System.exit(-1);
	}
    }

    Set<State> states = new HashSet<State>();
    Map<String, State> globalTransitions = new HashMap<String, State>();
    State currentState;
    
    void addGlobalTransition(String token, State nextState) {
	globalTransitions.put(token, nextState);
    }

    void setup() {
	State entry = new State("ENTRY");
	entry.nextAction = new CommandAction("AT");
	states.add(entry);

	State alive = new State("ALIVE");
	alive.nextAction = new CommandAction("ATV1;E1;X1;S0=0");  // Standard set up: Echo enabled, verbose replies, dont answer calls.
	states.add(alive);

	State settings1  = new State("SETTINGS1");
	settings1.nextAction = new CommandAction("AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPMUX=0;+CIPMODE=0;+CIPHEAD=1;+CIPSPRT=0"); // store URCs and send them to term when not sending data.
	states.add(settings1);

	State settingsDone = new State("SETTINGSDONE");
	settingsDone.nextAction = new CommandAction("AT&CPIN?");
	states.add(settingsDone);

	State simready = new State("SIMREADY");
	simready.nextAction = new CommandAction("AT+CREG?");
	states.add(simready);

	State registered = new State("REGISTERED");
	registered.nextAction = new CommandAction("AT+CGREG?");
	states.add(registered);

	State registration_denied = new State("REGISTRATION_DENIED");
	registration_denied.nextAction = new FailAction("registration denied.");
	states.add(registration_denied);

	State tell_to_attach = new State("TELL_TO_ATTACH");
	tell_to_attach.nextAction = new CommandAction("AT+CGATT=1");
	states.add(tell_to_attach);

	State GPRSRegistered = new State("GPRS_REGISTERED");
	GPRSRegistered.nextAction = new CommandAction("AT+CSTT=\"internet\"");
	states.add(GPRSRegistered);

	State wirelessConnection = new State("WIRELESS_CONNECTION");
	wirelessConnection.nextAction = new CommandAction("AT+CIICR");
	states.add(wirelessConnection);

	State wirelessConnectionUp = new State("WIRELESS_CONNECTION_UP");
	wirelessConnectionUp.nextAction = new CommandAction("start IP connection...");
	states.add(wirelessConnectionUp);

	State receivingData = new DataReceptionState("RECEIVING_DATA");
	// There is no next-action!
	states.add(receivingData);

	entry.addTransition("OK", alive);

	alive.addTransition("OK", settings1);

	settings1.addTransition("OK", settingsDone);

	settingsDone.addTransition("+CPIN: READY", simready);

	simready.addTransition("+CREG: ?,1", registered); // home network
	simready.addTransition("+CREG: ?,5", registered); // roaming
	simready.addTransition("+CREG: ?,2", simready);   // repeat while searching.
	simready.addTransition("+CREG: ?,0", simready);   // entry if not(!) searching.
	simready.addTransition("+CREG: ?,3", registration_denied);
	
	registered.addTransition("+CGREG: ?,0", tell_to_attach);
	registered.addTransition("+CGREG: ?,1", GPRSRegistered);
	registered.addTransition("+CGREG: ?,2", registered); // repeat while searching.

	tell_to_attach.addTransition("+CGATT:1", GPRSRegistered);

	// Failure not too likely. It's just setting a name.
	GPRSRegistered.addTransition("OK", wirelessConnection);
       
	// ERROR? How far to back off? We could (?) have lost network
	// but probably not SIM. Go to SIM ready state.
	wirelessConnection.addTransition("OK", wirelessConnectionUp);
	wirelessConnection.addTransition("ERROR", simready);

	currentState = entry;
	
	// All URCs should be represented!
	addGlobalTransition("RDY", entry); // We still do the alive test, just to check the TxD line

	addGlobalTransition("+CFUN: ?", alive);

	addGlobalTransition("+CSMINS: ?,1", simInserted);
	addGlobalTransition("+CPIN: READY", simready);

	addGlobalTransition("+CREG: 2", simready); // not yet registered but trying.
	addGlobalTransition("+CREG: 1", registered); // home network
	addGlobalTransition("+CREG: 5", registered); // roaming

	addGlobalTransition("+CGREG: 1", GPRSRegistered); // GPRS home
	addGlobalTransition("+CGREG: 5", GPRSRegistered); // GPRS roaming

	addGlobalTransition("+CGREG: 1", GPRSRegistered);
	addGlobalTransition("+CGREG: 0", registered); // registered is considered before GPRS registered.
	
	addGlobalTransition("+CRING:", incomingCall);
	
	//+CMTI: "SM",1	
	addGlobalTransition("+CMTI:", incomingSMS);

	// TODO: We need:
	// State dependent data reading (so the receivingData state can eat up its correct number of bytes)
	// TODO: Should receivingData simply return to the previous state when done?

	// We also need: Partial response matching.

	addGlobalTransition("+IPD,", incomingData);
    }

    void run() throws IOException {
	while(true) {
	    NextAction nextAction = currentState.nextAction;
	    if(nextAction != null)
		nextAction.execute();
	    String result = currentState.read();
	    boolean didMatch = false;

	    for (Map.Entry<String, State> t : currentState.transitions.entrySet()) {
		if(doMatch(t.getKey(),result)) {
		    currentState = t.getValue();
		    didMatch = true;
		    break;
		}
	    }
	    if (!didMatch) {
		for (Map.Entry<String, State> t : globalTransitions.entrySet()) {
		    if(doMatch(t.getKey(),result)) {
			currentState = t.getValue();
			didMatch = true;
			break;
		    }
		}
	    }
	}
    }

    public static void main(String[] args) throws IOException {
	SIM900StateMachine m = new SIM900StateMachine();
	m.setup();
	m.run();
    }
}
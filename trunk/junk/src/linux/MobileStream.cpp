#include "MobileStream.h"

BufferedStream::Buffer __MobileStream__rxBuffer;
BufferedStream::Buffer __MobileStream__txBuffer;

MobileStream::ParseState parseState;

const char C_INIT0[]  = "\r\nATV1 E0 X1 S0=0 +CMEE=0";
const char C_CONFIGURATION1[]  = "AT+CGEREP=2,0;+CIURC=0;+CFGRI=1;+CIPHEAD=1;+CIPSPRT=1;+CIPCSGP=1,\"internet\"";
const char C_START1[]  = "AT+CSTT";
const char C_START2[]  = "AT+CIICR";
const char C_START3[]  = "AT+CIFSR";
const char C_DATA_CONNECTION[]  = "AT+CIPSTART=\"udp\",\"www.sky-cam.dk\",\"10000\"";
const char C_RESET[]  = "AT+CFUN=1,1";
const char C_RESET_IP[]  = "AT+CIPSHUT";

const char R_OK[]  = "OK";
const char R_ERROR[]  = "ERROR";
const char R_SHUT_OK[]  = "SHUT OK";
const char R_ANYTHING[]  = "";

const char R_RDY[]  = "RDY";
const char R_ANY_CFUN[]  = "+CFUN: ?";
const char R_CGREG_1[]  = "+CGREG: 1";
const char R_CGREG_3[]  = "+CGREG: 3";
const char R_CGREG_5[]  = "+CGREG: 5";
const char R_IPD[]  = "+IPD,";
const char R_CONNECT_OK[]  = "CONNECT OK";
const char R_ALREADY_CONNECT[]  = "ALREADY CONNECT";
const char R_SEND_OK[]  = "SEND OK";
const char R_SEND_FAIL[]  = "SEND FAIL";
//const char R_[]  = "";

const MobileStream::Transition MobileStream::T_INITIAL[] = {
	Transition(R_OK, &S_CONFIGURATION1),
	Transition(R_ERROR, &S_RESET)
};

const MobileStream::CommandState MobileStream::S_INITIAL =
	MobileStream::CommandState(
		C_INIT0,
		2,
		T_INITIAL
	);

const MobileStream::Transition MobileStream::T_RESET[] = {
	Transition(R_OK, &S_INITIAL),
	Transition(R_ERROR, &S_INITIAL)
};

const MobileStream::CommandState MobileStream::S_RESET =
	MobileStream::CommandState(
		C_RESET,
		2,
		T_RESET
	);

const MobileStream::Transition MobileStream::T_RESET_IP[] = {
	Transition(R_SHUT_OK, &S_CONFIGURATION1),
	Transition(R_ERROR, &S_CONFIGURATION1)
};

const MobileStream::CommandState MobileStream::S_RESET_IP =
	MobileStream::CommandState(
		C_RESET_IP,
		2,
		T_RESET_IP
	);

 const MobileStream::DeadState MobileStream::S_DATA_CONNECTION_OPENING = MobileStream::DeadState();
 const MobileStream::DeadState MobileStream::S_DATA_CONNECTION_OPEN = MobileStream::DeadState();

 const MobileStream::Transition MobileStream::T_DATA_CONNECTION[] = {
	Transition(R_OK, &S_DATA_CONNECTION_OPENING),
	Transition(R_ERROR, &S_INITIAL)
};

 const MobileStream::CommandState MobileStream::S_DATA_CONNECTION =
	MobileStream::CommandState(
		C_DATA_CONNECTION,
		2,
		T_DATA_CONNECTION
	);

 const MobileStream::Transition MobileStream::T_START3[] = {
	Transition(R_ANYTHING, &S_DATA_CONNECTION),
	Transition(R_ERROR, &S_RESET_IP)
};

 const MobileStream::CommandState MobileStream::S_START3 =
	MobileStream::CommandState(
		C_START3,
		2,
		T_START3
	);

const MobileStream::Transition MobileStream::T_START2[] = {
	Transition(R_OK, &S_START3),
	Transition(R_ERROR, &S_RESET_IP)
};

const MobileStream::CommandState MobileStream::S_START2 =
	MobileStream::CommandState(
		C_START2,
		2,
		T_START2
	);

const MobileStream::Transition MobileStream::T_START1[] = {
	Transition(R_OK, &S_START2),
	Transition(R_ERROR, &S_RESET_IP)
};

const MobileStream::CommandState MobileStream::S_START1 =
	MobileStream::CommandState(
		C_START1,
		2,
		T_START1
	);

const MobileStream::Transition MobileStream::T_CONFIGURATION1[] = {
	Transition(R_OK, &S_START1),
	Transition(R_ERROR, &S_RESET_IP)
};

const MobileStream::CommandState MobileStream::S_CONFIGURATION1 =
	MobileStream::CommandState(
		C_CONFIGURATION1,
		2,
		T_CONFIGURATION1
	);

const MobileStream::Transition MobileStream::T_DATA_TRANSMISSION[] = {
	Transition(R_SEND_OK, &S_DATA_CONNECTION_OPEN),
	Transition(R_SEND_FAIL, &S_DATA_CONNECTION_OPEN),
	Transition(R_ERROR, &S_DATA_CONNECTION_OPEN)
};

const MobileStream::DataTransmissionState MobileStream::S_DATA_TRANSMISSION =
	MobileStream::DataTransmissionState(3, T_DATA_TRANSMISSION);

const MobileStream::DataReceptionState MobileStream::S_DATA_RECEPTION =
	MobileStream::DataReceptionState();

/*
 * Responses to URCs (Unsolicited Return Codes) from modem.
 */
const MobileStream::Transition MobileStream::URCTransitions[] = {
		MobileStream::Transition(R_RDY, &S_INITIAL),
		MobileStream::Transition(R_ANY_CFUN, &S_INITIAL),
		MobileStream::Transition(R_CGREG_1, &S_INITIAL),
		MobileStream::Transition(R_CGREG_3, &S_INITIAL),
		MobileStream::Transition(R_CGREG_5, &S_INITIAL),
		MobileStream::Transition(R_IPD, &S_DATA_RECEPTION),
		MobileStream::Transition(R_CONNECT_OK, &S_DATA_CONNECTION_OPEN),
		MobileStream::Transition(R_ALREADY_CONNECT, &S_RESET_IP),
		MobileStream::Transition(R_SEND_OK, &S_DATA_CONNECTION_OPEN),
		MobileStream::Transition(R_SEND_FAIL, &S_DATA_CONNECTION_OPEN)
};

const uint8_t MobileStream::numURCTransitions = sizeof(MobileStream::URCTransitions) / sizeof(MobileStream::Transition);

MobileStream::MobileStream(BetterStream* modem) :
	modem(modem),
	userdata(&__MobileStream__rxBuffer, &__MobileStream__txBuffer),
	parseState()
{
	userdata.set_blocking_writes(false);
}

/*
 * By default states are not immediate.
 */
bool MobileStream::State::isImmediate(void) const {
	return false;
}

void MobileStream::State::checkSuccessor(MobileStream* const outer, ParseState& parseState) const {
	if(parseState.progress == P_DONE) {
		// Now we are done with the current state and we can use deferred next-state.
		if (parseState.deferredSuccessorState != NULL) {
			parseState.beginDeferredState();
		} else {
			// State received something in the expected format, but it did not match any
			// transition! Re-start current state's listening for a new response or URC.
			parseState.progress = P_WAITING_INITIAL_WS;
		}
		// In any case, reset response buffer counter.
		parseState.bufptr = 0;
	}
}

MobileStream::DecisionState::DecisionState() :
	_numTransitions(0),
	_transitions(NULL) {
}

MobileStream::DecisionState::DecisionState(size_t numTransitions, Transition const * transitions) :
	_numTransitions(numTransitions),
	_transitions(transitions) {
}

/*
 * Search for an applicable transition. 
 */
int8_t MobileStream::DecisionState::match(const ParseState& parseState) const {
	uint8_t i;
	for (i=0; i<_numTransitions; i++) {
		if (parseState.match(_transitions[i].token)) {
			return i;
		}
	}
	return -1;
}

/*
 * Receive response message.
 */
void MobileStream::DecisionState::receive(MobileStream* const outer, ParseState& parseState) const {
	int data;
	int8_t matchResult;

	// If there is no (more) data, quit.
	if ((data = outer->modem->read()) < 0) {
		if (parseState.timeWaited > TIMEOUT) {
			parseState.beginState(&S_DATA_CONNECTION);
			parseState.timeWaited = 0;
		}
		return;
	}
	
	parseState.timeWaited = 0;

	// If we are consuming initial WS and there is non-WS input, start buffering it as response text.
	if (parseState.progress == P_WAITING_INITIAL_WS || parseState.progress == P_SAW_INITIAL_WS) {
		if (data!=CR && data!=LF) {
			parseState.progress = P_INTITAL_RESPONSE;
		} else {
			// Has seen some whitespace but maybe not all.
			parseState.progress = P_SAW_INITIAL_WS;
		}
	}

	// Check if response text matches any transition.
	if (parseState.progress == P_INTITAL_RESPONSE) {
		if (data!=CR && data!=LF) {
			parseState.add((uint8_t)data);
			matchResult = match(parseState);
			// States switched to by normal response matching are never immediate but just deferred.
			if (matchResult >= 0) {
				parseState.deferredSuccessorState = _transitions[matchResult].state;
			} else {
				// Try if the response is really no response at all, but a known URC.
				// Unfortunately, the SIM900 does not hold back URCs between the start of an AT command
				// and the end of its response so we can expect them almost any time!!
				matchResult = outer->matchURCs(parseState);
				if (matchResult >= 0) {
					State const * URCResolvedState = outer->URCTransitions[matchResult].state;
					if (URCResolvedState->isImmediate()) {
						// Immediately switch to new state. This is used when receiving data or SMS in a URC.
						parseState.saveState();
						parseState.deferredSuccessorState = NULL;
						parseState.beginState(URCResolvedState);
					} else {
						parseState.deferredSuccessorState = URCResolvedState;
					}
				}
			}
		} else { 
			// Consume trailing whitespace. If an LF, then we are done already.
			// For States that expect responses of more than one line, something needs to be added
			// at this stage to consume that!
			if (data==CR) {
				parseState.progress = P_TRAILING_WS;
			} else {
				parseState.progress = P_DONE;
			}
		}
	} else if (parseState.progress == P_TRAILING_WS) {
		// In this state, a CR was already consumed and we now consume the LF following it.
		parseState.progress = P_DONE;
	}
}

bool MobileStream::DecisionState::task(MobileStream* const outer, ParseState& parseState) const {
	// When the command has been sent, we switch to waiting for <CR><LF>response<CR><LF>...
	if (parseState.progress == P_COMMAND_SENT) {
			parseState.progress = P_WAITING_INITIAL_WS;
	}

	if (parseState.progress > P_COMMAND_SENT) {
		receive(outer, parseState);
	}

	checkSuccessor(outer, parseState);
	return true;
}

/*
MobileStream::CommandState::CommandState(const char* command) :
		_command(command) {
}
*/

MobileStream::CommandState::CommandState(const char* command, size_t numTransitions, Transition const * transitions) :
		DecisionState(numTransitions, transitions),
		_command(command) {
}

bool MobileStream::CommandState::task(MobileStream* const outer, ParseState& parseState) const {
		if (parseState.progress == P_START) {
			// Make sure there is space for the command and a trailing CR.
			if ((unsigned int)(outer->modem->txspace()) > strlen(_command)) {
				// Don't use println because it probably sends a LF. Modem needs a CR.
				outer->modem->print(_command);
				outer->modem->print("\r\n");
				parseState.progress = P_COMMAND_SENT;
			}
		}
		return DecisionState::task(outer, parseState);
}

bool MobileStream::DeadState::task(MobileStream* const outer, ParseState& parseState) const {
	int numBytes;

	// Dead states never send any commands, so skip to waiting for whitespace from modem.
	if (parseState.progress == P_START) {
		parseState.progress = P_WAITING_INITIAL_WS;
	}

	// See if user application wanted to transmit data, and in that case do it.
	// A transmission is started only when an URC reception is not in progress. 
	// Because the GCS (currently) buffers data as whole MAVLink messages at a time, it is simple
	// to get whole messages i UDP package: Any data waiting to be sent is one or more complete
	// MAVLink messages.
	// TODO: Not all dead states should support transmit.
	
	// Balance between prioritizing input and prioritizing output. Mission Planner sends so few messages that we barely hear them
	// - QGroundStation sends so many that we have to ignore it sometimes...
	if ((parseState.txrxScheduling++) & 1) {
		receive(outer, parseState);
	}
	
	if (parseState.progress == P_WAITING_INITIAL_WS && (numBytes = outer->userdata.backend_available()) > 0 && parseState.bufptr == 0) {
		parseState.numTxBytes = (size_t)numBytes;
		parseState.saveState();
		parseState.beginState(&S_DATA_TRANSMISSION);
		parseState.state->task(outer, parseState);
	} else {
	// Normal stuff: Check for arriving URCs.
		receive(outer, parseState);
		checkSuccessor(outer, parseState);
	}
	return true;
}

MobileStream::DataTransmissionState::DataTransmissionState(size_t numTransitions, Transition const * transitions) :
	CommandState("AT+CIPSEND=", numTransitions, transitions) {}

bool MobileStream::DataTransmissionState::task(MobileStream* const outer, ParseState& parseState) const {
	// uint8_t prompt;
	int numBytes;
	int tmpNumBytes;
	size_t i;
	
	// First send the command..
	if (parseState.progress == P_START) {
		// Make sure there is space for the command and a trailing CR.
		if ((size_t)outer->modem->txspace() >= strlen(_command)) {
			outer->modem->print(_command);
			parseState.progress = P_COMMAND_SENT;
		}
	}

	// Then send the message length..
	if (parseState.progress == P_COMMAND_SENT) {
		// Enough space for 4 digits and a CR.
		if (outer->modem->txspace() > 5) {
			outer->modem->printf("%u", parseState.numTxBytes);
			// Don't use println because it probably sends an LF. Modem needs a CR.
			outer->modem->printf("\r\n");
			parseState.progress = P_ARGUMENT_SENT;
		}
	}

	// Then wait for "> " from modem (skipping this step caused the modem to lose data!)
	// UNFORTUNATELY (cheap SIM900 crap!) URCs may come mixed with the > and we are chanceless.
	/*
	prompt = outer->modem->read();
	if (prompt == '>') {
		parseState.progress = P_TRANSMITTING;
	}
	*/
	
	// This is a delay of 20 ms for each return false...
	if (parseState.progress == P_ARGUMENT_SENT) {
		parseState.progress = P_WAITING_TRANSMIT;
		return false;
	}

	if (parseState.progress == P_WAITING_TRANSMIT) {
		parseState.progress = P_WAITING_TRANSMIT_2;
		//parseState.progress = P_TRANSMITTING;
		return false;
	}

	if (parseState.progress == P_WAITING_TRANSMIT_2) {
		//parseState.progress = P_WAITING_TRANSMIT_3;
		parseState.progress = P_TRANSMITTING;
		return false;
	}

	if (parseState.progress == P_WAITING_TRANSMIT_3) {
		parseState.progress = P_TRANSMITTING;
		return false;
	}
	
	// Then send the data in the largest possible chunks.
	if (parseState.progress == P_TRANSMITTING) {
		numBytes = parseState.numTxBytes;

		tmpNumBytes = outer->userdata.backend_available();
		
		if (tmpNumBytes < numBytes) 
			numBytes = tmpNumBytes;

		tmpNumBytes = outer->modem->txspace();
		if (tmpNumBytes < numBytes) 
			numBytes = tmpNumBytes;
			
		if (numBytes > 0) {
			parseState.timeWaited = 0;
		} else {
			numBytes = 0;
		}
		
		// If we appear stuck, bail out.
		if (parseState.timeWaited > TIMEOUT) {
			parseState.beginState(&S_DATA_CONNECTION);
			parseState.timeWaited = 0;
			return false;
		}
		
		
		for (i=0; i<numBytes; i++) {
			outer->modem->write(outer->userdata.backend_read());
		}

		parseState.numTxBytes -= numBytes;
		if (parseState.numTxBytes == 0) {
			parseState.restoreState();
		}
	}
	
	return true;
}

/*
 * This URC-driven, interrupt-like state is immediate.
 */
bool MobileStream::DataReceptionState::isImmediate() const {
	return true;
}

bool MobileStream::DataReceptionState::task(MobileStream* const outer, ParseState& parseState) const {
	int numBytes;
	int tmpNumBytes;
	
	if (parseState.progress == P_START) {
		parseState.numRxBytes = 0;
		parseState.progress = P_RECEIVING_DATALENGTH;
	}

	// First, get the byte count terminated by ':' 
	if (parseState.progress == P_RECEIVING_DATALENGTH) {
		int data = outer->modem->read();
		if (data < 0)
			return false;
		if (data == ':') {
			parseState.progress = P_RECEIVING_DATA;
		} else {
			parseState.numRxBytes *= 10;
			parseState.numRxBytes += (data - '0');
		}
	}

	// Then get the data in the largest possible chunks.
	if (parseState.progress == P_RECEIVING_DATA) {
		numBytes = parseState.numRxBytes;
		
		tmpNumBytes = outer->modem->available();
			
		if (tmpNumBytes < numBytes)
			numBytes = tmpNumBytes;
			
		tmpNumBytes = outer->userdata.backend_rxspace();

		if (tmpNumBytes < numBytes)
			numBytes = tmpNumBytes;

		if(numBytes < 0)
			numBytes = 0;
		
		size_t i;
		for (i=0; i<numBytes; i++) {
			outer->userdata.backend_write(outer->modem->read());
		}
		parseState.numRxBytes -= numBytes;
		if (parseState.numRxBytes == 0) {
			parseState.restoreState();
		}
	}
	
	return true;
}

int8_t MobileStream::matchURCs(ParseState& parseState) const {
	uint8_t i;
	for (i=0; i<numURCTransitions; i++) {
		if (parseState.match(URCTransitions[i].token)) {
			return i;
		}
	}
	return -1;
}

void MobileStream::begin(unsigned int rxSpace, unsigned int txSpace) {
	userdata.begin(rxSpace, txSpace);
	// The application is expected to init the serial port! Not we. However it might
	// make sense to do it anyway, reducing buffer sizes if possible.
	// With 57600 baud and 50 invocations/sec, there should still be enough space for
	// receiving 116 bytes though.
	// modem->begin(rxSpace, txSpace);
	parseState.deferredSuccessorState = NULL;
	parseState.beginState(&S_INITIAL);
}

/*
 * Begin message and end message events may be invoked from GCS_MAVLink!
 * Right now, they seem not necessary.
 */
void MobileStream::beginTransmitMessage(int16_t length) {
}
void MobileStream::endTransmitMessage(int16_t length) {
}

/*
 * Available incoming data.
 */
int MobileStream::available(void) {
	return userdata.available();
}

/*
 * Main driver of the state machine.
 */
void MobileStream::task(void) {
	parseState.timeWaited++;
	do {
		if (!parseState.state->task(this, parseState))
			break;
	} while (modem->available() > 0);
}

#ifndef MobileStream_h
#define MobileStream_h

#include "BufferedStream.h"
#include <inttypes.h>
#include <stdlib.h>
#include <string.h>

class MobileStream : public BetterStream {
public:
	static const uint8_t RESPONSE_BUFLEN = 32;
	static const char CR = '\r';
	static const char LF = '\n';

	// After 10 seconds of no reception of any kind of response from modem, we restart.
	static const uint16_t TIMEOUT = 50 * 10;

	enum Progress_t {
		P_START = 0,
		P_COMMAND_SENT = 10,
		P_ARGUMENT_SENT = 20,
		P_WAITING_TRANSMIT = 23,
		P_WAITING_TRANSMIT_2 = 24,
		P_WAITING_TRANSMIT_3 = 25,
		P_TRANSMITTING = 28,
		// KILLING_ECHO,
		// KILLED_ECHO,
		P_WAITING_INITIAL_WS = 30,
		P_SAW_INITIAL_WS = 31,
		P_INTITAL_RESPONSE = 40,
		P_RECEIVING_DATALENGTH = 50,
		P_RECEIVING_DATA = 60,
		// For queries, one can add state for parsing the 2nd line.
		P_TRAILING_WS = 70,
		P_DONE = 80
	};

	class State;
	struct Transition {
		char const * token;
		State const * state;

		Transition() :
			token(NULL),
			state(NULL) {}

		Transition(const char * token, const State * state) :
			token(token),
			state(state) {}
	};

	/*
	 * Base class for States. Pretty much just an implementation of the State pattern.
	 */
	class ParseState;
	class State {
	public:
		virtual bool isImmediate(void) const;
		virtual bool task(MobileStream* const outer, ParseState& parseState) const = 0;
		// virtual void setOrigin(GSMParseState *parseState);
		void checkSuccessor(MobileStream* const outer, ParseState& parseState) const;
	};

	class ParseState {
	public:
		// The State that we are in now.
		State const * state;
		
		// How far we are through parsing input for the current state.
		Progress_t progress;
		
		// Small buffer for modem response, and index/length of same.
		uint8_t responseBuffer[RESPONSE_BUFLEN];
		uint16_t bufptr;
		
		// Some states are not executed immediately, but deferred until input of the previous state has been consumed.
		State const * deferredSuccessorState;
		
		// Some states act like interrupts. Here the previous state is stored to be returned to.
		State const * savedState;
		
		// for Recei8ve and Transmit Data states: Data length.
		size_t numRxBytes;
		size_t numTxBytes;

		// Distrubutes priority between tx and rx.
		uint8_t txrxScheduling;

		// The amount of time we have waited for some sort of response. If a limit is exceeded, a state or progress
		// reset is done to avoid getting stuck waiting.
		// The time is just the number of invocations of MobileStream::task().
		uint16_t timeWaited;

		void add(uint8_t data) {
			responseBuffer[bufptr++] = data;
			if (bufptr == RESPONSE_BUFLEN)
				bufptr = 0;
		}

		void saveState() {
			savedState = state;
		}

		void beginState(const State* _state) {
			state = _state;
			progress = P_START;
			bufptr = 0;
		}

		void restoreState() {
			beginState(savedState);
			savedState = NULL;
		}

		void beginDeferredState() {
			savedState = state;
			beginState(deferredSuccessorState);
			deferredSuccessorState = NULL;
		}

		bool match(char const * token) const {
			uint8_t tokenLength = strlen(token);
			if(bufptr < tokenLength)
				return false; //result = false;
			uint8_t idx;
			for (idx=0; idx<tokenLength; idx++) {
				//char t = token[idx]; // TODO: Does this work?
				char t = *(token+idx);
				char i = responseBuffer[idx];
				if (t != i && t != '?') {
					return false;
				}
			}
			return true;
		}
	};


	class DecisionState: public State {
	private:
		size_t _numTransitions;
		Transition const * _transitions;
	protected:
		void receive(MobileStream* const outer, ParseState& parseState) const;
		int8_t match(const ParseState& parseState) const;
	public:
		DecisionState();
		DecisionState(size_t numTransitions, Transition const * transitions);
		virtual bool task(MobileStream* const outer, ParseState& parseState) const;
		void setTransitions(size_t numTransitions, const Transition transitions[]) {
			_numTransitions = numTransitions;
			_transitions = transitions;
		}
	};

	/*
	 * A State that sends no commands or queries to the modem. It just waits for URCs or
	 * data to transmit.
	 */
	class DeadState: public DecisionState {
	public:
		virtual bool task(MobileStream* const outer, ParseState& parseState) const;
	};

	/*
	 * A DeadState which sends a keep-alive pseudo message every now and then to keep NATs
	 * going. When using MAVLink heartbeat messages, there is already enough link traffic
	 * to suppress keep-alives.
	 */
	class KeepAliveState: public DeadState {
	private:
		long lastTime;
	public:
		virtual bool task(MobileStream* const outer, ParseState& parseState) const;
	};

	class CommandState: public DecisionState {
	public:
//		CommandState(const char* command /*, int numTransitions, Transition const * transitions */);
		CommandState(const char* command,size_t numTransitions, Transition const * transitions);
		virtual bool task(MobileStream* const outer, ParseState& parseState) const;
	protected:
		const char* _command;
	};

	class DataTransmissionState: public CommandState {
	public:
		DataTransmissionState(size_t numTransitions, Transition const * transitions);
		virtual bool task(MobileStream* const outer, ParseState& parseState) const;
	};

	class DataReceptionState: public State {
	public:
		virtual bool isImmediate(void) const;
	virtual bool task(MobileStream* const outer, ParseState& parseState) const;
	};

	MobileStream(BetterStream* modem);
	void begin(unsigned int rxSpace, unsigned int txSpace);

	// Regular task that does all the work. Should be called frequently, like in fast_loop.
	void task(void);

	// At this message, we can mark the beginning of a new MAVLink message.
	void beginTransmitMessage(int16_t length);
	// At this message, we know that there is a complete MAVLink message in the buffer ready to be sent.
	void endTransmitMessage(int16_t length);

	virtual int available(void);

	virtual int read(void) {
		return userdata.read();
	}

	virtual int peek(void) {
		return userdata.peek();
	}

	virtual void flush(void) {
		userdata.flush();
	}

	int txspace(void) {
		return userdata.txspace();
	}

	size_t write(uint8_t c) {
		userdata.write(c);
		return 1;
	}

	int8_t matchURCs(ParseState& parseState) const;

	BetterStream * const modem;
	BufferedStream userdata;
	ParseState parseState;

	static const Transition T_INITIAL[];
	static const CommandState S_INITIAL;

	static const Transition T_RESET[];
	static const CommandState S_RESET;

	static const Transition T_RESET_IP[];
	static const CommandState S_RESET_IP;

	static const DeadState S_DATA_CONNECTION_OPENING;
	static const DeadState S_DATA_CONNECTION_OPEN;
	static const DataReceptionState S_DATA_RECEPTION;

	static const Transition T_DATA_CONNECTION[];
	static const CommandState S_DATA_CONNECTION;

	static const Transition T_START3[];
	static const CommandState S_START3;

	static const Transition T_START2[];
	static const CommandState S_START2;

	static const Transition T_START1[];
	static const CommandState S_START1;

	static const Transition T_CONFIGURATION1[];
	static const CommandState S_CONFIGURATION1;

	static const Transition T_DATA_TRANSMISSION[];
	static const DataTransmissionState S_DATA_TRANSMISSION;

	static const uint8_t numURCTransitions;
	static const Transition URCTransitions[];
};

/*
class SIM900Stream: public MobileStream {
};
*/

#endif

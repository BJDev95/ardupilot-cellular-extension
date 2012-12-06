#ifndef MobileStream_h
#define MobileStream_h

#include "BufferedStream.h"
#include <inttypes.h>
#include <stdlib.h>
#include <avr/pgmspace.h>
#include <AP_Common.h>

class MobileStream : public BetterStream {
public:
	static const uint8_t RESPONSE_BUFLEN = 32;

	// After 10 seconds of no reception of any kind of response from modem, we restart.
	static const uint16_t TIMEOUT = 50 * 45;

	enum Progress_t {
		P_START = 0,
		P_COMMAND_SENT = 10,
		//P_ARGUMENT_SENT = 20,
		P_WAITING_TRANSMIT_WS = 23,
		P_WAITING_TRANSMIT_SAW_WS = 24,
		P_PRETRANSMIT = 25,
		P_TRANSMITTING = 28,

		P_WAITING_INITIAL_WS = 30,
		P_SAW_INITIAL_WS = 31,

		P_BUFFERING_RESPONSE_LINE = 32,

		P_SAW_TRAILING_WS = 40,
		P_RESPONSE_LINE_COMPLETE = 41,

		P_RECEIVING_DATALENGTH = 50,
		P_RECEIVING_DATA = 60,
		// For queries, one can add state for parsing the 2nd line.
		P_TRAILING_WS = 70,
		P_DONE = 80,
		P_RETURNED_FROM_INTERRUPT = 100
	};

	class State;
	struct Transition {
		prog_char const * token;
		State const * state;

		Transition() :
			token(NULL),
			state(NULL) {}

		Transition(const prog_char * token, const State * state) :
			token(token),
			state(state) {}
	};

	/*
	 * Base class for States. Pretty much just an implementation of the State pattern.
	 */
	class ParseState;
	class State {
	public:
		virtual void task(MobileStream* const outer, ParseState& parseState) const = 0;
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
		
		// Some states act like interrupts. Here the previous state is stored to be returned to.
		State const * savedState;
		
		// for Receive and Transmit Data states: Data length.
		size_t numRxBytes;
		size_t numTxBytes;

		// The amount of time we have waited for some sort of response. If a limit is exceeded, a state or progress
		// reset is done to avoid getting stuck waiting.
		// The time is just the number of invocations of MobileStream::task().
		uint16_t responseTimeout;
		uint8_t txrxScheduling;

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
			state = savedState;
			savedState = NULL;
			progress = P_RETURNED_FROM_INTERRUPT;
			bufptr = 0;
		}

		// Pattern match of response string to tokens. There is a wildcard '?'.
		bool match(prog_char const * token) const {
			uint8_t tokenLength = strlen_P(token);
			if(bufptr < tokenLength)
				return false;
			uint8_t idx;
			for (idx=0; idx<tokenLength; idx++) {
				char t = pgm_read_byte(token+idx);
				char i = responseBuffer[idx];
				if (t != i && t != '?')
					return false;
			}
			return true;
		}
	};


	class DecisionState: public State {
	private:
		size_t numTransitions;
		Transition const * transitions;
	protected:
		void receiveResponseLine(MobileStream* const outer, ParseState& parseState) const;
		const State* match(const ParseState& parseState) const;
	public:
		DecisionState();
		DecisionState(size_t numTransitions, Transition const * transitions);
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
		const State* findSuccessor(MobileStream* const outer, ParseState& parseState) const;
	};

	/*
	 * A State that sends no commands or queries to the modem. It just waits for URCs or
	 * data to transmit.
	 */
	class DeadState: public DecisionState {
	public:
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
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
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
	};

	class CommandState: public DecisionState {
	public:
		CommandState(const prog_char_t* command,size_t numTransitions, Transition const * transitions);
		CommandState(const prog_char* command,size_t numTransitions, Transition const * transitions);
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
	protected:
		const prog_char_t* _command;
	};

	class DataTransmissionState: public CommandState {
	public:
		DataTransmissionState(size_t numTransitions, Transition const * transitions);
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
	};

	class DataReceptionState: public State {
	public:
		virtual void task(MobileStream* const outer, ParseState& parseState) const;
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
		return userdata.write(c);
	}

private:
	const State* matchURCs(ParseState& parseState) const;
	const State* matchInterrupts(ParseState& parseState) const;

	BetterStream* const modem;
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

	static const uint8_t numURCInterrupts;
	static const Transition URCInterrupts[];
};

/*
class SIM900Stream: public MobileStream {
};
*/

#endif

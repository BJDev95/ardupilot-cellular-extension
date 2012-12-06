// -*- Mode: C++; c-basic-offset: 8; indent-tabs-mode: nil -*-
//
// This is free software; you can redistribute it and/or modify it under
// the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 2.1 of the License, or (at
// your option) any later version.
//

#include <cstdlib>

#ifndef __BUFFEREDSTREAM_H
#define __BUFFEREDSTREAM_H

#define min(a,b) ((a)<(b) ? (a) : (b))

#include "BetterStream.h"

class BufferedStream: public BetterStream {
public:
	class Buffer {
	public:
		Buffer() :
		_bytes(NULL) {}

		bool resize(bool wasAlreadyOpen, unsigned int size) {
			uint16_t	mask;
			uint8_t		shift;

			if (wasAlreadyOpen) {
				if (size == 0)
					size = _mask + 1;
				if (size == (unsigned)(_mask + 1))
					return true; // same size already allocated; success.
			}

			// Not open or changed size. Allocate.
			_head = _tail = 0;

			// Compute the power of 2 greater or equal to the requested buffer size
			// and then a mask to simplify wrapping operations.  Using __builtin_clz
			// would seem to make sense, but it uses a 256(!) byte table.
			// Note that we ignore requests for more than BUFFER_MAX space.
			for (shift = 1; (1U << shift) < min(_max_buffer_size, size); shift++)
				;
			mask = (1 << shift) - 1;

			// If the descriptor already has a buffer allocated we need to take
			// care of it.
			if (_bytes) {
				// If the allocated buffer is already the correct size then
				// we have nothing to do
				if (_mask == mask)
						return true;

				// Dispose of the old buffer.
				free(_bytes);
			}

			_mask = mask;

			// allocate memory for the buffer - if this fails, we fail.
			_bytes = (uint8_t *) malloc(mask + 1);

			return (_bytes != NULL);
		}

		void dealloc(void) {
			_head = _tail = 0;
			_mask = 0;
			if (NULL != _bytes) {
				free(_bytes);
				_bytes = NULL;
			}
		}

		virtual size_t available(void) {
			return (_head - _tail) & _mask;
		}

		virtual size_t freespace(void) {
			return _mask - ((_head - _tail) & _mask);
		}

		// This does not check that data is actually available!
		virtual int read(void) {
			int result = _bytes[_tail];
			_tail = (_tail+1) & _mask;
			return result;
		}

		// This does not check that data is actually available!
		virtual int peek(void) {
			return _bytes[_tail];
		}

		// This does not check that there is buffer space available!
		virtual void write(uint8_t data) {
			_bytes[_head] = data;
			_head = (_head + 1) & _mask;
		}

		volatile uint16_t _head, _tail;	///< head and tail pointers
		uint16_t _mask;						///< buffer size mask for pointer wrap
		uint8_t* _bytes;					///< pointer to allocated buffer

		static const unsigned int _max_buffer_size = 512;
	};

	BufferedStream(Buffer* const rxBuffer, Buffer* const txBuffer);

	/// @name 	Serial API
	//@{
	virtual void end(void);
	virtual int available(void);
	virtual int txspace(void);
	virtual int read(void);
	// This does not check that data is actually available!
	virtual int peek(void);
#define ARDUINO 100
#if defined(ARDUINO) && ARDUINO >= 100
	virtual size_t write(uint8_t c);
#else
	virtual void write(uint8_t c);
#endif
	virtual void flush(void);
	//using BetterStream::write;
	virtual void begin(unsigned int rxSpace, unsigned int txSpace);

	/// Transmit/receive buffer descriptor.
	///
	/// Public so the interrupt handlers can see it

	// ask for writes to be blocking or non-blocking
	void set_blocking_writes(bool blocking) {
		_nonblocking_writes = !blocking;
	}

	// return true if there are bytes pending transmission
	bool tx_pending(void) {
		return (_txBuffer->_head != _txBuffer->_tail);
	}

	// Writes to the read stream.
	int backend_write(uint8_t data) {
		if (_rxBuffer->freespace() == 0) return 0;
		_rxBuffer->write(data);
		return 1;
	}

	// Reads from the write stream.
	int backend_read(void) {
		if (_txBuffer->_head == _txBuffer->_tail) return -1;
		return _txBuffer->read();
	}

	// Number of bytes waiting for transmission.
	size_t backend_available(void) const {
		if (!_open)
			return (-1);
		return _txBuffer->available();
	}

	// Return how much _free space_ there is in the receive buffer
	size_t backend_rxspace(void) const {
		if (!_open)
			return (-1);
		return _rxBuffer->freespace();
	}

	// ring buffers
	Buffer * const _rxBuffer;
	Buffer * const _txBuffer;

	bool _open;

private:
	// whether writes to the port should block waiting
	// for enough space to appear
	bool _nonblocking_writes;

	/// Allocates a buffer of the given size
	///
	/// @param	buffer		The buffer descriptor for which the buffer will
	///						will be allocated.
	/// @param	size		The desired buffer size.
	/// @returns			True if the buffer was allocated successfully.
	///

	/// default receive buffer size
	static const unsigned int _default_rx_buffer_size = 128;

	/// default transmit buffer size
	static const unsigned int _default_tx_buffer_size = 16;

	/// maxium tx/rx buffer size
	/// @note if we could bring the max size down to 256, the mask and head/tail
	///       pointers in the buffer could become uint8_t.
	///
};

// Used by the per-port interrupt vectors
extern BufferedStream::Buffer __FastSerial__rxBuffer[];
extern BufferedStream::Buffer __FastSerial__txBuffer[];

#endif // __BUFFEREDSTREAM_H

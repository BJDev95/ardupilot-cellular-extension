// -*-  tab-width: 4; Mode: C++; c-basic-offset: 4; indent-tabs-mode: t -*-

#include "BufferedStream.h"

BufferedStream::BufferedStream(Buffer* const rxBuffer, Buffer* const txBuffer) :
		_rxBuffer(rxBuffer), _txBuffer(txBuffer), _open(false)
{
}

void BufferedStream::begin(unsigned int rxSpace, unsigned int txSpace) {
	bool wasopen = _open;
	_open = false;
	bool success;
	// If a buffer was not already allocated (open) then a size of 0 means use default size.
	// Else it means keep the old size (handled in Buffer::resize)
	if (!wasopen) {
		if (0 == rxSpace) {
			rxSpace = _default_rx_buffer_size;
		}
		if (0 == txSpace) {
			txSpace = _default_tx_buffer_size;
		}
	}
	success = _rxBuffer->resize(wasopen, rxSpace);
	success &= _txBuffer->resize(wasopen, txSpace);
	if (success)
		_open = true;
	else
		end();
}

void BufferedStream::end(void) {
	_rxBuffer->dealloc();
	_txBuffer->dealloc();
	_open = false;
}

int BufferedStream::available(void) {
	if (!_open)
		return (-1);
	return _rxBuffer->available();
}

int BufferedStream::txspace(void) {
	if (!_open)
		return (-1);
	return _txBuffer->freespace();
}

int BufferedStream::read(void) {
	if (_rxBuffer->_head == _rxBuffer->_tail) return -1;
	return _rxBuffer->read();
}

int BufferedStream::peek(void) {
	if (_rxBuffer->_head == _rxBuffer->_tail) return -1;
	return _rxBuffer->peek();
}

#if defined(ARDUINO) && ARDUINO >= 100
size_t BufferedStream::write(uint8_t c) {
	if (!_open) // drop bytes if not open
		return 0;

	// wait for room in the tx buffer
	uint16_t i = (_txBuffer->_head + 1) & _txBuffer->_mask;

	// if the port is set into non-blocking mode, then drop the byte
	// if there isn't enough room for it in the transmit buffer
	if (_nonblocking_writes && i == _txBuffer->_tail) {
		return 0;
	}

	while (i == _txBuffer->_tail)
	;

	_txBuffer->write(c);

	// return number of bytes written (always 1)
	return 1;
}

#else

void BufferedStream::write(uint8_t c) {
	uint16_t i;

	if (!_open) // drop bytes if not open
		return;

	// wait for room in the tx buffer
	i = (_txBuffer->_head + 1) & _txBuffer->_mask;
	while (i == _txBuffer->_tail)
		;

	_txBuffer->write(c);
}
#endif

void BufferedStream::flush(void) {
	// don't reverse this or there may be problems if the RX interrupt
	// occurs after reading the value of _rxBuffer->head but before writing
	// the value to _rxBuffer->tail; the previous value of head
	// may be written to tail, making it appear as if the buffer
	// don't reverse this or there may be problems if the RX interrupt
	// occurs after reading the value of head but before writing
	// the value to tail; the previous value of rx_buffer_head
	// may be written to tail, making it appear as if the buffer
	// were full, not empty.
	_rxBuffer->_head = _rxBuffer->_tail;

	// don't reverse this or there may be problems if the TX interrupt
	// occurs after reading the value of _txBuffer->tail but before writing
	// the value to _txBuffer->head.
	_txBuffer->_tail = _txBuffer->_head;
}

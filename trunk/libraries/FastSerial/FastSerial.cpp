// -*-  tab-width: 4; Mode: C++; c-basic-offset: 4; indent-tabs-mode: t -*-
//
// Interrupt-driven serial transmit/receive library.
//
//      Copyright (c) 2010 Michael Smith. All rights reserved.
//
// Receive and baudrate calculations derived from the Arduino
// HardwareSerial driver:
//
//      Copyright (c) 2006 Nicholas Zambetti.  All right reserved.
//
// Transmit algorithm inspired by work:
//
//      Code Jose Julio and Jordi Munoz. DIYDrones.com
//
//      This library is free software; you can redistribute it and/or
//      modify it under the terms of the GNU Lesser General Public
//      License as published by the Free Software Foundation; either
//      version 2.1 of the License, or (at your option) any later version.
//
//      This library is distributed in the hope that it will be useful,
//      but WITHOUT ANY WARRANTY; without even the implied warranty of
//      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//      Lesser General Public License for more details.
//
//      You should have received a copy of the GNU Lesser General Public
//      License along with this library; if not, write to the Free Software
//      Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

#include "FastSerial.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#if   defined(UDR3)
# define FS_MAX_PORTS   4
#elif defined(UDR2)
# define FS_MAX_PORTS   3
#elif defined(UDR1)
# define FS_MAX_PORTS   2
#else
# define FS_MAX_PORTS   1
#endif

// Just set aside space for 4 buffers in and 4 out. This space is for the 
// buffer structs and does not include the data buffers proper.
BufferedStream::Buffer __FastSerial__rxBuffer[FS_MAX_PORTS];
BufferedStream::Buffer __FastSerial__txBuffer[FS_MAX_PORTS];

uint8_t FastSerial::_serialInitialized = 0;

// Constructor /////////////////////////////////////////////////////////////////

FastSerial::FastSerial(const uint8_t portNumber, volatile uint8_t *ubrrh, volatile uint8_t *ubrrl,
					   volatile uint8_t *ucsra, volatile uint8_t *ucsrb, const uint8_t u2x,
					   const uint8_t portEnableBits, const uint8_t portTxBits) :
					   BufferedStream(&__FastSerial__rxBuffer[portNumber], &__FastSerial__txBuffer[portNumber]),
					   _ubrrh(ubrrh),
					   _ubrrl(ubrrl),
					   _ucsra(ucsra),
					   _ucsrb(ucsrb),
					   _u2x(u2x),
					   _portEnableBits(portEnableBits),
					   _portTxBits(portTxBits)
{
	setInitialized(portNumber);
	begin(57600);
}

// Public Methods //////////////////////////////////////////////////////////////

void FastSerial::begin(long baud)
{
	begin(baud, 0, 0);
}

void FastSerial::begin(long baud, unsigned int rxSpace, unsigned int txSpace)
{
	uint16_t ubrr;
	bool use_u2x = true;

	BufferedStream::begin(rxSpace, txSpace);

	// If buffer allocation failed, stop port activity.
	if (!_open) {
		*_ucsrb &= ~(_portEnableBits | _portTxBits);
		return;
	}

	// If the user has supplied a new baud rate, compute the new UBRR value.
	if (baud > 0) {
#if F_CPU == 16000000UL
		// hardcoded exception for compatibility with the bootloader shipped
		// with the Duemilanove and previous boards and the firmware on the 8U2
		// on the Uno and Mega 2560.
		if (baud == 57600)
			use_u2x = false;
#endif

		if (use_u2x) {
			*_ucsra = 1 << _u2x;
			ubrr = (F_CPU / 4 / baud - 1) / 2;
		} else {
			*_ucsra = 0;
			ubrr = (F_CPU / 8 / baud - 1) / 2;
		}

		*_ubrrh = ubrr >> 8;
		*_ubrrl = ubrr;
	}

	*_ucsrb |= _portEnableBits;
}

void FastSerial::end()
{
	*_ucsrb &= ~(_portEnableBits | _portTxBits);
	BufferedStream::end();
}

#if defined(ARDUINO) && ARDUINO >= 100
size_t FastSerial::write(uint8_t c)
{
	if (!BufferedStream::write(c))
		return 0;

	// enable the data-ready interrupt, as it may be off if the buffer is empty
	*_ucsrb |= _portTxBits;

	// return number of bytes written (always 1)
	return 1;
}

/*
size_t FastSerial::writeBlock(uint8_t *buf, uint16_t length) {
	if (!_open)
		return 0;

	uint16_t i = BufferedStream::writeBlock(buf, length);

	// enable the data-ready interrupt, as it may be off if the buffer is empty
	if(i) {
		*_ucsrb |= _portTxBits;
	}

	return i;
}
*/

#else
void FastSerial::write(uint8_t c)
{
	uint16_t i;

	if (!_open) // drop bytes if not open
		return;

	// This does not respect nonblocking I/O anyway, and thus always gets the data written.
	BufferedStream::write(c);

	// enable the data-ready interrupt, as it may be off if the buffer is empty
	*_ucsrb |= _portTxBits;
}

/*
void FastSerial::writeBlock(uint8_t *buf, uint16_t length) {
	if (!_open)
		return 0;

	BufferedStream::writeBlock(buf, length);

	// enable the data-ready interrupt, as it may be off if the buffer is empty
	*_ucsrb |= _portTxBits;
}
*/
#endif

// -*- Mode: C++; c-basic-offset: 8; indent-tabs-mode: nil -*-
//
//      Copyright (c) 2010 Michael Smith. All rights reserved.
//
// This is free software; you can redistribute it and/or modify it under
// the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 2.1 of the License, or (at
// your option) any later version.
//

//
// Enhancements to the Arduino Stream class.
//

#include <limits.h>
#include <stdarg.h>
#include "BetterStream.h"

// Stream extensions////////////////////////////////////////////////////////////

void
BetterStream::print(const char *s) {
        //        char    c;
        //while ('\0' != (c = *(s++)))
        //                write(c);
        //        printf("%s", s);
        printf("%s", s);
}

void
BetterStream::printf(const char *fmt, ...) {
        char temp[1000];
        int length, i;
        va_list args;
        va_start(args, fmt);
        // vsprintf(char * restrict str, const char * restrict format, va_list ap);
        length = vsprintf(temp, fmt, args);
        va_end(args);
        for (i=0; i<length; i++)
                write(temp[i]);
}

int
BetterStream::txspace(void) {
        // by default claim that there is always space in transmit buffer
        return(INT_MAX);
}

// -*- Mode: C++; c-basic-offset: 8; indent-tabs-mode: nil -*-
//
//      Copyright (c) 2010 Michael Smith. All rights reserved.
//
// This is free software; you can redistribute it and/or modify it under
// the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 2.1 of the License, or (at
// your option) any later version.
//

#ifndef __BETTERSTREAM_H
#define __BETTERSTREAM_H

#include "Stream.h"

class BetterStream : public Stream {
public:
        BetterStream(void) {
        }
        
        void print(const char* str);
        void printf(const char *fmt, ...);
        virtual int     txspace(void);
};

#endif // __BETTERSTREAM_H


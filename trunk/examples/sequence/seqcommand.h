/***************************************************************************
 *   Copyright (C) 2009 by sb   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

#ifndef _SEQCOMMAND_
#define _SEQCOMMAND_

#include "seqengine.h"

class SequenceCommand {

	friend void SequenceEngine::runSequence(SequenceCommand *command, int N);

protected:
	const double a, b;

	SequenceCommand() :
		a(0), b(0) {
	}

	SequenceCommand(const double parama) :
		a(parama), b(0) {
	}

	SequenceCommand(const double parama, const double paramb) :
		a(parama), b(paramb) {
	}

private:
	virtual double getFirst() = 0;
	virtual double getNext() = 0;
	virtual double getNext2() {
		return -1;
	}
	virtual bool is2Seq() {
		return false;
	}

	virtual double getLimit() const = 0;

public:
	virtual ~SequenceCommand() {
	}
};

#endif


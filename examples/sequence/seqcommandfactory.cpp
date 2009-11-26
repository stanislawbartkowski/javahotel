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

#include "seqcommandfactory.h"
#include "seqcommand.h"
#include "seqharmcommand.h"
#include "seq23command.h"
#include "seqpower2.h"
#include "seqavharm.h"
#include "seqc2.h"
#include "seqfih60.h"

SequenceCommand *SequenceFactory::createCommand(const std::string commandname) {
	if (commandname == SIMPLESEQ) {
		return new HarmSequence();
	}
	if (commandname == SEQPOWER2) {
		return new SeqPower2();
	}
	return NULL;
}

SequenceCommand *SequenceFactory::createCommand(const std::string commandname, const double a,
		const double b) {
	if (commandname == SEQ23SEQ) {
		return new Sequence23(a, b);
	}
	if (commandname == SEQGEOAR) {
		return new SeqAvHarm(a, b);
	}
	return NULL;
}

SequenceCommand *SequenceFactory::createCommand(const std::string commandname, const double a) {
	if (commandname == SEQC2) {
		return new SeqC2(a);
	}
	if (commandname == SEQFIH60) {
		return new SeqFih60(a);
	}
	return NULL;
}


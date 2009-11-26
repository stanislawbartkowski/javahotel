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

#ifndef SEQCOMMANDFACTORY
#define SEQCOMMANDFACTORY

#include <iostream>
#include <cstdlib>

class SequenceCommand;

namespace SequenceFactory {

#define SIMPLESEQ "SimpleSeq"
#define SEQPOWER2 "SeqPower2"
#define SEQ23SEQ "23Seq"
#define SEQGEOAR "SeqGeoAr"
#define SEQC2 "Seqc2"
#define SEQFIH60 "SeqFih60"

  SequenceCommand *createCommand(const std::string commandname);
  SequenceCommand *createCommand(const std::string commandname,const double a,const double b);
  SequenceCommand *createCommand(const std::string commandname,const double a);
};

#endif

/***************************************************************************
 *   Copyright (C) 2009 by sb   *
 *   hotel@sbartkowski.krakow.pl.ibm.com   *
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
#include "seqharmcommand.h"
#include "seq23command.h"
#include "seqpower2.h"
#include "seqavharm.h"
#include "seqc2.h"

SequenceCommand *createCommand(std::string commandname) {
  if (commandname == "SimpleSeq") {
     return new HarmSequence();
  }
  if (commandname == "SewPower2") {
     return new SeqPower2();
  }
  return NULL; 
}

SequenceCommand *createCommand(const std::string commandname,int a,int b) {
  if (commandname == "23Seq") {
    return new Sequence23(a,b);
  }
  if (commandname == "SeqGeoAr") {
    return new SeqAvHarm(a,b);
  }
  return NULL;
}

SequenceCommand *createCommand(const std::string commandname,double a) {
  if (commandname == "Seqc2") {
     return new SeqC2(a);
  }
  return NULL;
}



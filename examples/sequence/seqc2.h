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

#ifndef _SEQC2_COMMAND_
#define _SEQC2_COMMAND_

#include <complex>

// Fih: 63

class SeqC2 : public SequenceCommand {

   friend SequenceCommand *createCommand(const std::string commandname,double a);

   // Fichtenholz 40
   const double c;

   double an1;

   SeqC2(double paramc) : c(paramc) {
   }

   double getFirst() {
     an1 = (double)c/2;
     return an1;
   }

   double getNext() {
     double d = an1*an1 /2;
     d += ((double)c / 2);
     an1 = d;
     return an1;
   }

   double getLimit() {
     double d = sqrt(1-c);
     return 1-d;
   }

};

#endif

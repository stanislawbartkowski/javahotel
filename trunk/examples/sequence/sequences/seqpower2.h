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

#ifndef _SEQPOWER2_
#define _SEQPOWER2_

#include <complex>

class SeqPower2 : public SequenceCommand {

   friend SequenceCommand *SequenceFactory::createCommand(const std::string commandname,const double a);

   // Fichtenholz 52
   SeqPower2(const double parama) : SequenceCommand(parama) {
   }


   int no;

   double getNum() {
     double po = pow(no+1,a);
     double po1 = pow(no,a);
     return po - po1;
   } 
    

   double getFirst() {
     no = 1;
     return getNum();
   }

   double getNext() {
     no++;
     return getNum();
   }

   double getLimit() const {
     return 0;
   }

};

#endif

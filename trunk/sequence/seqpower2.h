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

#ifndef _SEQPOWER2_
#define _SEQPOWER2_

#include <complex>

class SeqPower2 : public SequenceCommand {

   // Fichtenholz 52

   int no;

   double getNum() {
     double k = 0.5;
     double po = pow(no+1,k);
     double po1 = pow(no,k);
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

   double getLimit() {
     return 0;
   }


};

#endif

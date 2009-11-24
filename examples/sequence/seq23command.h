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

#ifndef _SEQ23_COMMAND_
#define _SEQ23_COMMAND_

class Sequence23 : public SequenceCommand {

   friend SequenceCommand *createCommand(const std::string commandname,int a,int b);

   // Fichtenholz 40
   const double a,b;

   double an1,an2;

   Sequence23(const double parama, const double paramb) : a(parama), b(paramb) {
   }

   int no;

   double getFirst() {
     no = 1;
     return a;
   }

   double getNext() {
     no++;
     if (no == 2) { return b; }
     if (no == 3) {
       an2 = a;
       an1 = b;
     }
     double d = (an1 + an2)/2;
     an2 = an1;
     an1 = d;
     return d;
   }
   
   double getLimit() {
     return (a + 2*b) / 3;
   }

};

#endif

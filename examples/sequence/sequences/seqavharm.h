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

#ifndef _SEQ_AV_HARM_
#define _SEQ_AV_HARM_

class SeqAvHarm : public SequenceCommand {

   friend SequenceCommand *SequenceFactory::createCommand(const std::string commandname,const double a,const double b);

   // Fichtenholz 40

   double an1,bn1;

   SeqAvHarm(const double parama,const double paramb) : SequenceCommand(parama,paramb) {
   }

   double getFirst() {
     an1 = a;
     bn1 = b;
     return getNext();
   }

   double getNext() {
     double sa = (an1 + bn1) / 2;
     double sg = (2 * an1 * bn1) / (an1 + bn1);
     an1 = sa;
     bn1 = sg;
     return sa;
   }

   double getNext2()  { return bn1; }
   bool is2Seq() { return true; } 
   
   double getLimit() {
     double po = sqrt(a*b);
     return po;
   }

};


#endif

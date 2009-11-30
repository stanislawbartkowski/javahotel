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

#ifndef _SEQFIH60_
#define _SEQFIH60_

// Fih 60
// xn+1 = xn(2-xn)

class SeqFih60 : public SequenceCommand {

   friend SequenceCommand *SequenceFactory::createCommand(const std::string commandname,const double a);

   // Fichtenholz 40

   double xn1;

   SeqFih60(const double parama0) : SequenceCommand(parama0) {
   }

   double getFirst() {
      xn1=a;
      return getNext();
   }

   double getNext() {
      double xn = xn1 * (2-xn1);
      xn1 = xn;
      return xn;
   }

   double getLimit() const {
    if ((a>0) && (a<1)) {
      return 1;
    }
    return -1;
   }

};

#endif

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

#include <iostream>
#include <cstdlib>

using namespace std;

#include "seqengine.h"

void runSequence(SequenceCommand *command, int N) {
  double d;
  for (int i = 0; i<N; i++) {
    if (i == 0) { d = command->getFirst(); }
    else { d = command->getNext(); }
    if (command->is2Seq()) {
        cout << i+1 << " : " << d << " " << command->getNext2() << endl;
    }
     else {
        cout << i+1 << " : " << d << endl;
     }
    }
    cout << "-----" << command->getLimit() << endl;
}

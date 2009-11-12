/***************************************************************************
 *   Copyright (C) 2009 by ,,,   *
 *   sb@sbb   *
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

#include "runflav.h"

#include <iostream>
#include <cstdlib>
#include <vector>

using namespace std;

namespace {

  int mark(int start, vector<int> &SS) {
    int i;
    for (i=start; i<SS.size(); i+=2) {
       SS[i] = -1;
    }
    return i % SS.size();
  }

  void kill(vector<int> &SS) {
    vector<int>::iterator iter = SS.begin();
    while (iter != SS.end()) {
      if (*iter == -1) {
          iter = SS.erase(iter);
        }
      else {
        iter++;
    }
  } // while
}

}

int CountFlavByExclusion(int no) {

  vector<int> SS(no);

  for (int i=0; i<no; i++) {
    SS[i] = i+1;
  }
  int nextstart = 1;
  while (SS.size() > 1) {
    nextstart = mark(nextstart,SS);
    kill(SS);
  }
  return *SS.begin();
}

int CountFlavByFormula(int no) {
  int m = 1;
  while (m <= no) {
     m = m<<1;
  }
  m = m>>1;
  int l = no - m;
  return (l<<1) + 1;
}

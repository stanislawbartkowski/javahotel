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


#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <iostream>
#include <cstdlib>

using namespace std;

#include "seqcommandfactory.h"
#include "seqengine.h"
#include "seqcommand.h"

using namespace SequenceFactory;

int main(int argc, char *argv[])
{
  cout << "Hello, world!" << endl;
//  SequenceCommand *com = createCommand(SEQ23SEQ,1,4);
//  SequenceCommand *com = createCommand(SEQFIH60,0.45);
//  SequenceCommand *com = createCommand(SIMPLESEQ);

//  SequenceCommand *com = createCommand(SEQGEOAR,1,1000);
//  SequenceCommand *com = createCommand(SEQC2,-3.1);
//  SequenceCommand *com = createCommand(SEQPOWER2,.5);

  SequenceCommand *com = createCommand(SEQGEOAR,1,98);

  SequenceEngine::runSequence(com,100);

  return EXIT_SUCCESS;
}

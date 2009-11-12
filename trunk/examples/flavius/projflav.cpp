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


#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <iostream>
#include <cstdlib>

#include "runflav.h"

using namespace std;

int no_main(int argc, char *argv[])
{
  cout << "Hello, world!" << endl;

//  cout << "live:" << CountFlavByExclusion(9) << endl;
  for (int i=1; i<=16; i++) {
    cout << i << " : " << CountFlavByExclusion(i) << " " << CountFlavByFormula(i) << endl;
  }

  return EXIT_SUCCESS;
}

#define BOOST_TEST_MODULE MyTest
#include <boost/test/unit_test.hpp>

int add( int i, int j ) { return i+j; }

BOOST_AUTO_TEST_CASE( test1 )
{
  BOOST_CHECK(CountFlavByExclusion(1) == 1);
  BOOST_CHECK(CountFlavByExclusion(2) == 1);
  BOOST_CHECK(CountFlavByExclusion(3) == 3);
  BOOST_CHECK(CountFlavByExclusion(4) == 1);
  BOOST_CHECK(CountFlavByExclusion(5) == 3);
  BOOST_CHECK(CountFlavByExclusion(6) == 5);
  BOOST_CHECK(CountFlavByExclusion(7) == 7);
  BOOST_CHECK(CountFlavByExclusion(8) == 1);
  BOOST_CHECK(CountFlavByExclusion(9) == 3);
  BOOST_CHECK(CountFlavByExclusion(10) == 5);
  BOOST_CHECK(CountFlavByExclusion(11) == 7);
  BOOST_CHECK(CountFlavByExclusion(12) == 9);
  BOOST_CHECK(CountFlavByExclusion(13) == 11);
  BOOST_CHECK(CountFlavByExclusion(14) == 13);
  BOOST_CHECK(CountFlavByExclusion(15) == 15);
  BOOST_CHECK(CountFlavByExclusion(16) == 1);
}

BOOST_AUTO_TEST_CASE( test2 )
{
  for (int i=1; i<=16; i++) {
    BOOST_CHECK(CountFlavByExclusion(i) == CountFlavByFormula(i));
  }
}

BOOST_AUTO_TEST_CASE( test3 )
{
    BOOST_CHECK(CountFlavByExclusion(100) == 73);
    BOOST_CHECK(CountFlavByExclusion(100) == CountFlavByFormula(100));

    BOOST_CHECK(CountFlavByExclusion(42) == 21);
    BOOST_CHECK(CountFlavByExclusion(42) == CountFlavByFormula(42));

    BOOST_CHECK(CountFlavByExclusion(170) == 85);
    BOOST_CHECK(CountFlavByExclusion(170) == CountFlavByFormula(170));
}

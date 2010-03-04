/***************************************************************************
 *   Copyright (C) 2010 by Stanislaw Bartkowski   *
 *   sbartkowski@sbartkowski-linux.krakow.pl.ibm.com   *
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


#include <gtest/gtest.h>
#include <iostream>

#include "odbctest.h"

using namespace std;

#define EXIT_ERROR 4

int main(int argc, char *argv[])
{
  std::cout << "Start tester\n" << std::endl;
  // analize parameters
  bool runSqlTest = false;
  std::string dsnName;
  std::string userName;
  std::string password;

  for (int i=1; i<argc; i++) {
    int testN = -1;
    // 0 - noDrivers, 1 - noDataSources 
    const char *par = argv[i];
    if (strcmp(par,"-testDrivers") == 0) { 
      testN = 0;
    }
    if (strcmp(par,"-testDataSources") == 0) {
      testN = 1;
    }
    if (strcmp(par,"-testDSNVals") == 0) {
      if (i >= argc-1) {
        std::cout << "ERROR 1: invalid parameters" << std::endl;
        return EXIT_ERROR;
      }
      dsnName = argv[++i];
	  if (i < argc-1) {
        userName = argv[++i];
		if (i < argc - 1) {
			  password = argv[++i];
		}
	  }
      setDSNConnection(dsnName,userName,password);
    } 
    if (testN != -1) {
      if (i == argc-1) {
        std::cout << "ERROR 0: invalid parameters" << std::endl;
        return EXIT_ERROR;
      }
      std::istringstream buffer(argv[++i]);
      int no;
      buffer >> no;
      switch (testN) {
       case 0: setDriverNumberTested(no); break;
       case 1: setDataSourcesNumberTested(no); break;
      }
      testN = -1;
    }
  }

  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}

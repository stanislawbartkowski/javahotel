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

using namespace std;

int main(int argc, char *argv[])
{
  std::cout << "Start tester\n" << std::endl;
  // analize parameters
  int noDrivers = -1;
  int noDataSource = -1;
  bool runSqlTest = false;
  std::string dsnName;
  std::string userName;
  std::string password;

  for (int i=1; i<argc; i++) {
    int testN = -1;
    // 0 - noDrivers, 1 - noDataSources 
    const char *par = argv[i];
    if (strcmp    

  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}

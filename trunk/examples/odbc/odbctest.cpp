/***************************************************************************
 *   Copyright (C) 2010 by sb   *
 *   hotel@sbartkowski-linux.krakow.pl.ibm.com   *
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

#ifdef WIN32
#include <windows.h>
#endif

#include <stdio.h>
#include <stdlib.h>

#include <iostream>
#include <cstdlib>
#include <sstream>

#include <sql.h>
#include <sqlext.h>
#include <stdlib.h>

#include "ODBCDrivers.h"

namespace {
  void runSql(ODBCConnection &conn , const char *sqlstament) {
    std::vector<ODBCConnection::SelectValue> res;
    std::cout << "--------------" << std::endl;
    std::cout << sqlstament << std::endl;
    if (!conn.executeSelect(sqlstament,res)) { throw 1; }
    std::vector<ODBCConnection::SelectValue>::iterator ite;
    for (ite = res.begin(); ite != res.end(); ite++) {
       std::string col = ite->getCol("symbol");
       std::string val = ite->getCol("name");
       std::cout << col << " : " << val << std::endl;
    } 

 }
}

int Xmain(int argc, char *argv[])
{
  std::cout << "List of all drivers" << std::endl;
  std::vector<ODBCInfo> dList = getODBCDrivers();
  std::vector<ODBCInfo>::iterator i;
  for (i = dList.begin(); i != dList.end(); i++) {
    std::cout << i->getName() << "  " << i->getDescr() << std::endl;
  }

  std::cout << std::endl << "List of all data sources" << std::endl;
  std::vector<ODBCInfo> dsList = getODBCDataSources();
  std::vector<ODBCInfo>::iterator ids;
  int no; 
  for (no=0 , ids = dsList.begin(); ids != dsList.end(); ids++, no++) {
    std::cout << no << ": " << ids->getName() << "  " << ids->getDescr() << std::endl;
  }

  std::cout << std::endl << "Open rybka" << std::endl;
  ODBCConnection conn;
  if (!conn.open("rybka","postgres","postgres123")) { 
//  if (!conn.open("rybka","db2admin","db2admin")) { 
      std::cout << " Failed: " << conn.getError() << std::endl;
      return 4;
  }
  try {
    std::cout << "Connected " << std::endl;
    conn.execute("DROP TABLE TESTEXAMPLE");
    std::cout << "Dropped " << std::endl;
    if (!conn.execute(" CREATE TABLE TESTEXAMPLE (id INT,symbol VARCHAR(100), name VARCHAR(100) )")) { throw 1; }
    std::cout << "Created " << std::endl;
    for (int i=0; i<100; i++) {

      std::stringstream Num;
      Num << i;
      std::string si = Num.str();

      std::string sta = "INSERT INTO TESTEXAMPLE VALUES ( " + si + ",\'SYM" +si+ "\',\'NAME"+si +"\')";
      std::cout << sta << std::endl;
      if (!conn.execute(sta)) { throw 1; }
    }
    runSql(conn,"SELECT * FROM TESTEXAMPLE");
    runSql(conn,"SELECT * FROM TESTEXAMPLE WHERE symbol = 'SYM0'");
    runSql(conn,"SELECT * FROM TESTEXAMPLE WHERE symbol = 'SYM51'");
   }
   catch(...) {
      std::cout << " Failed: " << conn.getError() << std::endl;
    }

    conn.close();
 }
 
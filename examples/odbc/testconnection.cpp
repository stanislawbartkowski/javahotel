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
#include "ODBCDrivers.h"

namespace {

std::string dsnName;
std::string userName;
std::string passwordVal;

TEST(Connection, RunningSomeActions) {
  std::cout << "Connect using " << dsnName << " " << userName << " " << passwordVal << std::endl;
  ODBCConnection conn;
  conn.setErrorVerbose();
  ASSERT_TRUE(conn.open(dsnName,userName,passwordVal));
  conn.execute("DROP TABLE TESTEXAMPLE"); // is expected to be false if not exist
  ASSERT_TRUE(conn.execute(" CREATE TABLE TESTEXAMPLE (id INT,symbol VARCHAR(100), name VARCHAR(100))"));
  for (int i=0; i<100; i++) {

      std::stringstream Num;
      Num << i;
      std::string si = Num.str();

      std::string sta = "INSERT INTO TESTEXAMPLE VALUES ( " + si + ",\'SYM" +si+ "\',\'NAME"+si +"\')";
      std::cout << sta << std::endl;
      ASSERT_TRUE(conn.execute(sta));
    }
    std::vector<ODBCConnection::SelectValue> res;
    ASSERT_TRUE(conn.executeSelect("SELECT * FROM TESTEXAMPLE",res));
    ASSERT_EQ(100,res.size());
    res.clear();
    ASSERT_TRUE(conn.executeSelect("SELECT * FROM TESTEXAMPLE WHERE symbol = 'SYM0'",res));
    ASSERT_EQ(1,res.size());
    ASSERT_EQ("SYM0",res[0].getCol("symbol"));
    ASSERT_EQ("NAME0",res[0].getCol("name"));
    res.clear();
    ASSERT_TRUE(conn.executeSelect("SELECT * FROM TESTEXAMPLE WHERE symbol = 'SYM50'",res));
    ASSERT_EQ(1,res.size());
    ASSERT_EQ("SYM50",res[0].getCol("symbol"));
    ASSERT_EQ("NAME50",res[0].getCol("name"));

    conn.close();

}

}

void setDSNConnection(const std::string &dsn, const std::string &user, const std::string &password) {
  dsnName = dsn;
  userName = user;
  passwordVal = password;
}

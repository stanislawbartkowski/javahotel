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

#ifndef _ODBC_Drivers_
#define _ODBC_Drivers_

#ifdef WIN32
#include <windows.h>
#endif
#include <sql.h>
#include <sqlext.h>
#include <string>
#include <vector>
#include <exception>
#include <map>
#include <algorithm>

class ODBCInfo {
  std::string name;
  std::string descr;

 public:

  ODBCInfo(const char *pname, const char *pdescr) : name(pname) , descr(pdescr)  {
  }

  const std::string getName() const {
    return name;
  }

  const std::string getDescr() const {
   return descr;
  }
};


class ODBCConnection {

 std::string errs;
 SQLHENV env;
 SQLHDBC dbc;
 bool errorverbose;

 void release();
 
 void retrieveError(std::string where, SQLSMALLINT type, SQLHANDLE handle);
 bool open(const char *connectionstring);


public:
 
 class SelectValue : public std::map<std::string,std::string> {   
   public:
     const std::string getCol(const char *colName) const {
       std::string col(colName);
	   std::transform(col.begin(),col.end(),col.begin(),(int(*)(int))toupper);
       std::map<std::string,std::string> ma = *this;
	   std::string val = ma[col];
	   return val;
     }
 };

  ODBCConnection() {
    errorverbose = false;
  }
 
  void setErrorVerbose() {
    errorverbose = true;
  }

  bool open(const std::string dsnname,const std::string user,const std::string password) {
     char dsnstring[1024];

    sprintf(dsnstring,"DSN=%s;UID=%s;PWD=%s",dsnname.c_str(),user.c_str(),password.c_str());
    return open(dsnstring);
  }

  bool open(const std::string connectionstring) {
    return open(connectionstring.c_str());
  }

  void close();

  bool execute(std::string sqlstatement) {
     std::vector<SelectValue> res;
     return executeSelect(sqlstatement,res);
  }  

  bool executeSelect(std::string sqlstatement,std::vector<SelectValue> &res);

  const std::string getError() const {
    return errs;
  }
 
};



std::vector<ODBCInfo> getODBCDrivers() throw();

std::vector<ODBCInfo> getODBCDataSources() throw();

#endif






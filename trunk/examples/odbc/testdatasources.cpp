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

int noDataSources;

TEST(getODBCDataSources, TestNumberOfDataSources) {
  std::cout << "List of all data sources" << std::endl;
  std::vector<ODBCInfo> dList = getODBCDataSources();
  std::vector<ODBCInfo>::iterator i;
  for (i = dList.begin(); i != dList.end(); i++) {
    std::cout << i->getName() << "  " << i->getDescr() << std::endl;
  }
  EXPECT_EQ(noDataSources,dList.size());
}
}

void setDataSourcesNumberTested(int no) {
  noDataSources = no;
}
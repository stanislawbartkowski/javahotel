import os
import sys
import unittest
import decimal
import datetime

import Test2Case
import ODBCHelper
import TestCaseHelper

_SOURCEDSN="sourcedsn"

def setHe(he):
     Test2Case.helper = he

def injectParam(param,tepar) :
     Test2Case.param = param
     Test2Case.teparam = tepar
    
class TestSuite(unittest.TestCase):

     def setUp(self):
         self.param = Test2Case.param
         self.teparam = Test2Case.teparam
         self.odbcH = ODBCHelper.ODBCHelper(self, self.param, self.teparam)
         nextdsn=self.odbcH.getPar(_SOURCEDSN, True)
         self.sourceH = ODBCHelper.ODBCHelper(self, self.param, self.teparam, nextdsn)
        
     def tearDown(self):
         self.odbcH.close()
         self.sourceH.close()
         
     def testCase(self):
         self.sourceH.executeSection('start')
#         self.odbcH.executeSection('verifyperson')
         for i in range(100) :
             self.sourceH.execute("INSERT INTO PERSON VALUES(?,?)", ('John ' + str(i), i))
         self.sourceH.commit()
         
         sum = None
         for i in range(100) :
             self.odbcH.execute("SELECT SUM(RANGE) AS S FROM F_PERSON")
             row = self.odbcH.cursor.fetchone()
             dec = row[0]
             if sum == None : sum = dec
             else : sum = sum + dec
         print sum
         self.assertEqual(495000, sum)
         
         da = datetime.date.today()
         oneday = datetime.timedelta(1)
         
         for i in range(100) :
             self.sourceH.execute("INSERT INTO PERSON_DATE VALUES(?,?)", ('John ' + str(i), da))
             da = da + oneday
         self.sourceH.commit()
         
         da = None
         for i in range(100) :
             self.odbcH.execute("SELECT MIN(DT) AS D FROM F_PERSONDATE")
             row = self.odbcH.cursor.fetchone()
             if da == None : da = row[0]
         print da
         self.assertEqual(datetime.date.today(), da)
         
    

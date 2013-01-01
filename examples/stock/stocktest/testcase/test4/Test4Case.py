import os
import sys
import unittest
import TestMass

import Test4Case
import ODBCHelper

def setHe(he):
     Test4Case.helper = he

def injectParam(param,tepar) :
     Test4Case.param = param
     Test4Case.teparam = tepar
    
class TestSuite(unittest.TestCase):

     def setUp(self):
         self.param = Test4Case.param
         self.teparam = Test4Case.teparam
         self.odbcH = ODBCHelper.ODBCHelper(self, self.param, self.teparam)
         self.testM = TestMass.TestStock (self.odbcH.cnxn)
        
     def tearDown(self):
         self.testM.stop()
         self.odbcH.close()
         
     def testCase(self):
         self.testM.clearstores()
         self.odbcH.executeSection("start")
         session = self.odbcH.getPar('sessid').replace('\'','')
         noT = self.odbcH.getPar('nomesstest')
         if noT == None : noTest = 10
         else :  noTest=int(noT)
         self.testM.runTest(session, noTest)
         self.odbcH.executeSection("verify")
         
         

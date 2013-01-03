import os
import sys
import unittest

import Test1Case
import ODBCHelper
import TestCaseHelper

_SOURCEDSN="sourcedsn"

def setHe(he):
     Test1Case.helper = he

def injectParam(param,tepar) :
     Test1Case.param = param
     Test1Case.teparam = tepar
    
class TestSuite(unittest.TestCase):

     def setUp(self):
         self.param = Test1Case.param
         self.teparam = Test1Case.teparam
         self.odbcH = ODBCHelper.ODBCHelper(self, self.param, self.teparam)
         nextdsn=self.odbcH.getPar(_SOURCEDSN, True)
         self.sourceH = ODBCHelper.ODBCHelper(self, self.param, self.teparam, nextdsn)
        
     def tearDown(self):
         self.odbcH.close()
         self.sourceH.close()
         
     def testCase(self):
         self.sourceH.executeSection('createsource')
         self.odbcH.executeSection('verifyperson')

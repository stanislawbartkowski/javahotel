import sys
#print sys.path
import TestBoa
from TestBoa import *
from TestCaseHelper import *
import logging

#LOG_FILENAME = '/tmp/testlogging.out'
#logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)
#logging.basicConfig(level=logging.DEBUG)
logging.basicConfig(level=logging.INFO)

def printhelp():
     print "Usage:"
     print "prog /res dir/ /spec/ /testid/"

if __name__ == "__main__":
     if len(sys.argv) == 1:
         resdir = '/home/hotel/tboa/testsuite'
         testspec = 'one'
         testid = '11'
     elif len(sys.argv) == 4:    
         resdir = sys.argv[1]
         testspec = sys.argv[2]
         testid = sys.argv[3]
     else:
         printhelp()
         exit()
     resource = resdir
     rundir = None
#    globresource = resource + "/resource"
 #   propfile = globresource + "/testcommon.dat"
     globresource= resdir
     propfile = resdir+"/test.properties"
     suiteparam = RunSuiteParam(None, None, propfile, globresource, resource, rundir)
     try:
         runSuite(suiteparam, testspec, testid)
     except Exception, e:
         print e

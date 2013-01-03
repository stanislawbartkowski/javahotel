import logging
from TestBoa import *
from TestCaseHelper import *

#LOG_FILENAME = '/tmp/testlogging.out'
#logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)
#logging.basicConfig(level=logging.INFO)
logging.basicConfig(level=logging.DEBUG)

def printhelp():
    print "Usage:"
    print "runtest /res dir/ /run dir/ /spec/ /testid/"

if __name__ == "__main__":
#    if len(sys.argv) != 5:
#       printhelp()
#        sys.exit(4)
    DIR="/home/perseus/projects/db2fdw/boatest/"
    globresource = DIR + "resources/";
    
    propfile = globresource + 'testodbc.properties';
    resource = DIR  + "testcase";
    rundir = '/home/sbartkowski/testdir'
    
    testspec = 'one'
    testid = '2'
    
    factory = TestCaseFactory()
    suiteparam = RunSuiteParam(factory, None, propfile, globresource, resource, rundir)
    try:
        runSuite(suiteparam, testspec, testid)
    except Exception, e:
        print e

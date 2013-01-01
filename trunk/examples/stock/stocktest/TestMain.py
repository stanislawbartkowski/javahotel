import TestMass
import logging
import sys


#LOG_FILENAME = '/tmp/testlogging.out'
#logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)
#logging.basicConfig(level=logging.INFO)
logging.basicConfig(level=logging.DEBUG)

def printhelp():
    print "Usage:"
    print "runtest sessionid,numberoftest"


if __name__ == "__main__":

     if len(sys.argv) != 3  and  len(sys.argv) != 4:
         printhelp()
         sys.exit(4)

     
     session = sys.argv[1]
     noftest = int(sys.argv[2])
     print "Start"
     te = TestMass.TestStock()
     if len(sys.argv) == 4 :
         te.clearstores()
         
     te.runTest(session, noftest)
     te.stop()  
     print "Finished"





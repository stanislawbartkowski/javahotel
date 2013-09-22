from util.util import getHotel
import cutil

def startaction(action,var):
  cutil.printVar("start action",action,var)
  
  if action == "before" :
    var["JMAIN_DIALOG"] = "hotel/reservation/reservation.xml"  
    cutil.setStatusMessage(var,"DATA",getHotel(var))
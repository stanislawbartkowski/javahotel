from util.util import printvar

def startaction(action,var):
  printvar("start action",action,var)
  
  if action == "before" :
    var["JMAIN_DIALOG"] = "hotel/reservation/reservation.xml"  

      

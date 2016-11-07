from cutil import printVar
from cutil import setStatusMessage

def dialogaction(action,var) :
   printVar("changestatus",action,var)
   if action == "change1" :
      setStatusMessage(var,var["combof"],var["textf"])
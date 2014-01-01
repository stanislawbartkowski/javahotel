import cutil
from util import rutil
from util import util

def showreseraction(action,var):
   cutil.printVar("show reservaction",action,var)
   if action == "before" :
     rutil.setvarBefore(var)
     
   if action == "cancelreserv" and var["JYESANSWER"] :
     res = util.getReservForDay(var)
     resname = res[0].getResId()
     R = util.RESOP(var)
     R.changeStatusToCancel(resname)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
   if action == "aftercheckin" and var["JUPDIALOG_BUTTON"] == "makecheckin" :
       var["JCLOSE_DIALOG"] = True
       
   if action == "guestdesc" :
       util.showCustomerDetails(var,var["cust_name"])

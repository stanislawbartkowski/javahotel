from util.util import RESOP
from util.util import createArrayList
from util.util import createResQueryElem

def getReservForDay(var):
   R = RESOP(var)
   room = var["JDATELINE_LINE"]
   day = var["JDATELINE_DATE"]
   query=createArrayList()
   q = createResQueryElem(room,day)
   query.add(q)
   res = R.queryReservation(query)
   return res

def showCustomerDetails(var,custid):
    var["JUP_DIALOG"] = "hotel/reservation/showcustomerdetails.xml"
    print "details",custid
    var["JUPDIALOG_START"] = custid
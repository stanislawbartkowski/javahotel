from util.util import printvar
from cutil import setCopy
from util.util import ROOMLIST
from util.util import PRICELIST
from util.util import PRICEELEM
import datetime
from util.util import getServicesForRoom
from util.util import createEnumFromList
from util.util import emptyS
from util.util import SUMBDECIMAL
from util.util import RESOP
from util.util import createArrayList
from util.util import createResQueryElem
from util.util import eqDate
from util.util import newCustomer
from util.util import CUSTOMERLIST
from util.util import getCustFieldId
from util.util import newResForm
from util.util import createResFormElem
from util.util import RESFORM
from util.util import getReservForDay
from com.gwthotel.hotel.reservation import ResStatus
from util.util import getCustFieldId
from util.util import mapToXML
from util.util import xmlToVar
from util.util import showCustomerDetails
from util.util import getPriceForPriceList
from cutil import checkGreaterZero
from cutil import setFooter
from cutil import setJMapList
from util.util import BILLLIST
from util.util import getReseName
from util.util import setCustData
from util.util import getPayments
from cutil import addDecimal
from cutil import BigDecimalToDecimal
from util.util import newBill
from con import eqUL
from util.util import PAYMENTOP
from util import rutil
from util.util import HOTELTRANSACTION
from util.util import MESS
from util.util import customerFromVar
from util.util import getCustFieldIdAll
from util.util import setDefaCustomer
from util.util import saveDefaCustomer

M = MESS()

CUST="cust_"

def _createResData(var):
  service = var["roomservice"]
  if emptyS(service) : return None
  pricelist = var["roompricelist"]
  if emptyS(pricelist) : return None
  roomname = var["name"]
  date = var["datecol"]
  resdays = var["resdays"]
  dl = datetime.timedelta(1)
  list = []
  dt = date
  sum = SUMBDECIMAL()
  PE = PRICEELEM(var)
  
  query = createArrayList()
  RES = RESOP(var)
  qelem = createResQueryElem(roomname,date,date+datetime.timedelta(resdays))
  query.add(qelem)
  rList = RES.queryReservation(query)
  allavail = True
  
  pPrice = getPriceForPriceList(var,pricelist,service)
# getResDate      
  for i in range(resdays) :
      price = pPrice.getWorkingPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if eqDate(dt,rdata) : avail = allavail = False
      map = { "avail" : avail, "name" : roomname, "resday" : dt, "price" : price}
      list.append(map)
      dt = dt + dl
      sum.add(price)
    
  return [list,sum.sum,allavail]    

def _createListOfDays(var):
  rData = _createResData(var)
  if rData == None : return
  setJMapList(var,"reslist",rData[0])
  setFooter(var,"reslist","price",rData[1])
  return rData[2]
 
def _checkRese(var):
  if not checkGreaterZero(var,"resdays") : return False
  if _createListOfDays(var) : return True
  var["JERROR_MESSAGE"] = "Already reserved"
  var["JMESSAGE_TITLE"] = "Cannot make reservation"  
  return False
           
class MAKERESE(HOTELTRANSACTION) :
  
   def __init__(self,var) :
     HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :
      res = _createResData(var)
      if not res[2] :
          var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
          var["JMESSAGE_TITLE"] = M("ALREADYRESERVEDTITLE") 
          return
      # customer firstly
      cust = customerFromVar(var,CUST)
      R = CUSTOMERLIST(var)
      name = var["cust_name"]
      if not emptyS(name) :
          cust.setName(name)
          R.changeElem(cust)
      else :
          cust.setGensymbol(True);
          name = R.addElem(cust).getName()
      saveDefaCustomer(var,CUST)               
      # --- customer added
      reservation = newResForm(var)
      reservation.setGensymbol(True);
      reservation.setCustomerName(name)
      service = var["roomservice"]
      nop = var["nop"]
      reselist = reservation.getResDetail()
      for re in res[0] :
          roomname = re["name"]
          price = re["price"]
          dt = re["resday"]
          ele = createResFormElem(roomname,service,dt,nop,price)
          reselist.add(ele)
      RFORM = RESFORM(var)
      added = RFORM.addElem(reservation)
      resename = added.getName()
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
  
  
def reseraction(action,var):
    printvar("reseraction",action,var)
    
    if action == "signalchange" :
        if var["changefield"] == "roomservice" : _createListOfDays(var)
        if var["changefield"] == "roompricelist" : _createListOfDays(var)
    
    if action=="before" :
        rutil.setvarBefore(var)
        
    if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" :
        xml = var["JUPDIALOG_RES"]
        xmlToVar(var,xml,getCustFieldIdAll(),CUST)
        setCopy(var,getCustFieldIdAll(),None,CUST)
        
    if action=="custdetails" :
        var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        var["JUPDIALOG_START"] = mapToXML(var,getCustFieldIdAll(),CUST)
            
    if action == "checkaval" :
        _checkRese(var)

    if action == "askforreservation" :
      if not _checkRese(var) : return
      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" and var["JYESANSWER"] :
      TRAN = MAKERESE(var)
      TRAN.doTrans()                   
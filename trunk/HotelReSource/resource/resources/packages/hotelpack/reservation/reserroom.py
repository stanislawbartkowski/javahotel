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
from util.util import getReservForDay
from com.gwthotel.hotel.reservation import ResStatus
from util.util import getCustFieldId
from util.util import mapToXML
from util.util import xmlToVar
from util.util import showCustomerDetails
from util.util import getPriceForPriceList
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
from util.util import HOTELTRANSACTION
from util.util import customerFromVar
from util.util import getCustFieldIdAll
from util.util import setDefaCustomer
from util.util import saveDefaCustomer

from util import rutil
from util import util
import cutil
import con

M = util.MESS()

CUST="cust_"
RESLIST="reslist"

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
  resnop = var["resnop"]
  perperson = var["serviceperperson"]
  priceroom = var["respriceperroom"]
  priceperson = var["respriceperson"]
  pricechildren = None
  priceextra = None
  resnoc = var["resnochildren"]
  if resnoc : pricechildren = var["respricechildren"]
  resextra = var["resnoextrabeds"]
  if resextra : priceextra = var["respriceextrabeds"]
  
  #calculate
  if perperson :
    price = con.mulIntDecimal(resnop,priceperson)
    price = con.addDecimal(price,con.mulIntDecimal(resnoc,pricechildren))
    price = con.addDecimal(price,con.mulIntDecimal(resextra,priceextra))
  else : price = priceroom    
  
  query = createArrayList()
  RES = RESOP(var)
  qelem = createResQueryElem(roomname,date,date+datetime.timedelta(resdays))
  query.add(qelem)
  rList = RES.queryReservation(query)
  allavail = True
  
#  pPrice = getPriceForPriceList(var,pricelist,service)
# getResDate      
  for i in range(resdays) :
#      price = pPrice.getPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if eqDate(dt,rdata) : avail = allavail = False
      
      map = { "avail" : avail, "resroomname" : roomname, "resday" : dt, "rlist_pricetotal" : price, "rline_nop" : resnop,"rlist_priceperson" : priceperson,
              "rlist_noc" : resnoc, "rlist_pricechildren" : pricechildren, "rlist_noe" : resextra, "rlist_priceextra" : priceextra}
      list.append(map)
      dt = dt + dl
      sum.add(price)
    
  return [list,sum.sum,allavail]    


def _getPriceList(var) :
  pricelist = var["roompricelist"]
  price = None
  pricechild = None
  priceextra = None
  serv = var["roomservice"]
  pricelist = var["roompricelist"]
  if serv != None and proclist != None return  
    P = PRICEELEM(var)
    prices = P.getPricesForPriceList(pricelist)
    for s in prices :
      id = s.getService()
      if id == serv :
        price = s.getPrice()
        pricechild = s.getChildrenPrice()
        priceextra = s.getExtrabedsPrice()
  return (price,pricechild,proceextra)

def _setAfterServiceName(var) :
  S = util.SERVICES(var)
  serv = S.findElem(var["roomservice"])
  if serv == None : return
  var["serviceperperson"] = serv.isPerperson()
  cutil.setCopy(var,"serviceperperson")  

def _setAfterPriceList(var) :
  (price,pricechild,proceextra) = _getPriceList(var)
  var["respriceperson"] = price
  var["respricechildren"] = pricechild
  var["respriceextrabeds"] = priceextra    
  var["respriceperroom"] = price
  cutil.setCopy(var,["respriceperson","respricechildren","respriceextrabeds"])

def _setAfterPerPerson(var) :  
  var["JSETATTR_FIELD_respriceperson_enable"] = var["serviceperperson"]  
  var["JSETATTR_FIELD_respricechildren_enable"] = var["serviceperperson"]  
  var["JSETATTR_FIELD_respriceextrabeds_enable"] = var["serviceperperson"]  
  var["JSETATTR_FIELD_respriceperroom_enable"] = not var["serviceperperson"]  

def _createListOfDays(var): 
  rData = _createResData(var)
  if rData == None : return
  setJMapList(var,"reslist",rData[0])
  setFooter(var,"reslist","rlist_pricetotal",rData[1])
  return rData[2]

def _checkNoAndPrice(var,no,price) :
  if not cutil.checkGreaterZero(var,no) : return False
  if var[no] == None : return True
  if cutil.checkEmpty(var,price) : return False
  return True
 
def _checkRese(var):
  if not cutil.checkGreaterZero(var,"resdays") : return False
  if not var["serviceperperson"] :
    if cutil.checkEmpty(var,["respriceperroom"]) : return False
  if not _checkNoAndPrice(var,"resnop","respriceperson") : return False
  if not _checkNoAndPrice(var,"resnochildren","respricechildren") : return False
  if not _checkNoAndPrice(var,"resnoextrabeds","respriceextrabeds") : return False
    
  if _createListOfDays(var) : return True
  var["JERROR_MESSAGE"] = M("alreadyreserved")
  var["JMESSAGE_TITLE"] = M("cannotmakereservation")  
  return False
  
def _checkAvailibity(var) :
  list = var["JLIST_MAP"][RESLIST]
  if not var["avail"] :
     var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
     var["JMESSAGE_TITLE"] = "Not everything is available"
     return False
  RES = RESOP(var)
  query = createArrayList()
  for p in list :
    dat = p["resday"]
    roomname = p["resroomname"]
    qelem = createResQueryElem(roomname,date,date+datetime.timedelta(1))
    query.add(qelem)
  rList = RES.queryReservation(query)
  if len(rList) > 0 :
     var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
     var["JMESSAGE_TITLE"] = M("ALREADYRESERVEDTITLE") 
     return False
  return True       
  
class MAKERESE(HOTELTRANSACTION) :
  
   def __init__(self,var) :
     HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :     
      if not _checkAvailibity(var) :
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
      
      # --- pricelist
      # important: current pricelist and service is taken
      (price,pricechild,proceextra) = _getPriceList(var)
      
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
          ele = rutil.createResFormElem(roomname,service,dt,nop,price)
          reselist.add(ele)
      RFORM = util.RESFORM(var)
      added = RFORM.addElem(reservation)
      resename = added.getName()
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
        
def _checkCurrentRese(var) :
  list = var["JLIST_MAP"][RESLIST]
  if len(list) == 0:
      var["JERROR_MESSAGE"] = M("alreadyreserved")
      var["JMESSAGE_TITLE"] = "No single room selected !"
      return False
  for elem in list :
    avail = elem["avail"]
    if not avail :
      var["JERROR_MESSAGE"] = M("alreadyreserved")
      var["JMESSAGE_TITLE"] = "Not all room are available"
      return False
  return True          
    
def reseraction(action,var):
    printvar("reseraction",action,var)
    
    if action == "signalchange" :
        if var["changefield"] == "serviceperperson" :
          _setAfterPerPerson(var)
          
        if var["changefield"] == "roomservice" : 
            _setAfterServiceName(var)
            if not var["changeafterfocus"] : 
               _createListOfDays(var)
               _setAfterPriceList(var)
               _setAfterPerPerson(var)
        if var["changefield"] == "roompricelist" : 
            _setAfterPriceList(var)
            if not var["changeafterfocus"] : 
              _createListOfDays(var)
              _setAfterServiceName(var)
              _setAfterPerPerson(var)
    
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
#      if not _checkRese(var) : return

      if not _checkCurrentRese(var) : return
      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" and var["JYESANSWER"] :
      TRAN = MAKERESE(var)
      TRAN.doTrans()                   
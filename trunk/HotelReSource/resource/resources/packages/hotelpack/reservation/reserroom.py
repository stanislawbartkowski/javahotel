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
from util.util import eqDate
from util.util import newCustomer
from util.util import CUSTOMERLIST
from util.util import getCustFieldId
from util.util import newResForm
from com.gwthotel.hotel.reservation import ResStatus
from util.util import getCustFieldId
from util.util import showCustomerDetails
from util.util import getPriceForPriceList
from cutil import setFooter
from cutil import setJMapList
from util.util import BILLLIST
from util.util import setCustData
from util.util import getPayments
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

def showButton(var,buttonid,show=True) :
   var["JSETATTR_BUTTON_" + buttonid + "_hidden"] = not show

def _newRese(var) :
  return rutil.getReseName(var) == None

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
  
  query = cutil.createArrayList()
  RES = RESOP(var)
  qelem = rutil.createResQueryElem(roomname,date,date+datetime.timedelta(resdays))
  query.add(qelem)
  rList = RES.queryReservation(query)
  allavail = True
  
  (listprice,listpricechild,listpriceextra) = _getPriceList(var)
  resename = rutil.getReseName(var)

  for i in range(resdays) :
#      price = pPrice.getPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if eqDate(dt,rdata) : 
            if resename == None or resename != re.getResId() : 
              avail = allavail = False
      
      map = { "avail" : avail, "resroomname" : roomname, "resday" : dt, "rlist_pricetotal" : price, "rline_nop" : resnop,"rlist_priceperson" : priceperson,
              "rlist_noc" : resnoc, "rlist_pricechildren" : pricechildren, "rlist_noe" : resextra, "rlist_priceextra" : priceextra,
              "rlist_pricelistperson" : listprice, "rlist_pricelistchildren" : listpricechild, "rlist_pricelistextrabeds" : listpriceextra,
              "rlist_serviceperperson" : perperson, "rlist_roomservice" : service, "rlist_roompricelist" : pricelist}
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
  if serv != None and pricelist != None :
    P = PRICEELEM(var)
    prices = P.getPricesForPriceList(pricelist)
    for s in prices :
      id = s.getService()
      if id == serv :
        price = s.getPrice()
        pricechild = s.getChildrenPrice()
        priceextra = s.getExtrabedsPrice()
  return (price,pricechild,priceextra)

def _setAfterServiceName(var) :
  S = util.SERVICES(var)
  serv = S.findElem(var["roomservice"])
  if serv == None : return
  var["serviceperperson"] = serv.isPerperson()
  cutil.setCopy(var,"serviceperperson")  

def _setAfterPriceList(var) :
  (price,pricechild,priceextra) = _getPriceList(var)
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
  if rData == None : return None
#  assert rData != None
  setJMapList(var,RESLIST,rData[0])
  setFooter(var,RESLIST,"rlist_pricetotal",rData[1])
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
  _setAlreadyReserved(var)
  return False
  
def _setAlreadyReserved(var) :  
   var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
   var["JMESSAGE_TITLE"] = M("ALREADYRESERVEDTITLE") 

def _setAlreadyReservedNotAvailable(var) :  
    var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
    var["JMESSAGE_TITLE"] = "Not everything is available"
    
def _setAlreadyReservedNotSelected(var) :  
    var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
    var["JMESSAGE_TITLE"] = "No single room selected !"


def _checkAvailibity(var) :
  list = var["JLIST_MAP"][RESLIST]
  RES = RESOP(var)
  query = cutil.createArrayList()
  for p in list :
    if not p["avail"] :
      _setAlreadyReservedNotAvailable(var)
      return False
    dat = p["resday"]
    roomname = p["resroomname"]
    qelem = rutil.createResQueryElem(roomname,dat,dat)
    query.add(qelem)
  rList = RES.queryReservation(query)
  # analize if other reservation
  resename = rutil.getReseName(var)
  alreadyres = len(rList)
  if resename  :
    alreadyres = 0
    for r in rList :
      if r.getResId() != resename : alreadyres = alreadyres + 1
  if alreadyres :
     _setAlreadyReserved(var)
     return False
  return True       
  
class MAKERESE(HOTELTRANSACTION) :
  
   def __init__(self,var) :
     HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :     
      if not _checkAvailibity(var) : return
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
      
      resename = rutil.getReseName(var) 
      reservation = newResForm(var)
      if resename : reservation.setName(resename)
      else : reservation.setGensymbol(True);
      reservation.setCustomerName(name)
      service = var["roomservice"]
      nop = var["nop"]
      reselist = reservation.getResDetail()
      rlist = var["JLIST_MAP"][RESLIST]
      for re in rlist :
          r = util.newResAddPayment()
          r.setRoomName(re["resroomname"])
          r.setService(re["rlist_roomservice"])
          r.setResDate(con.toDate(re["resday"]))
          r.setPerperson(re["rlist_serviceperperson"])
          r.setPriceListName(re["rlist_roompricelist"])
          
          r.setNoP(re["rline_nop"])
          r.setPrice(con.toB(re["rlist_priceperson"]))
          r.setPriceList(con.toB(re["rlist_pricelistperson"]))
          
          util.setIntField(re,"rlist_noc",lambda v : r.setNoChildren(v))
          r.setPriceChildren(con.toB(re["rlist_pricechildren"]))
          r.setPriceListChildren(con.toB(re["rlist_pricelistchildren"]))
          
          util.setIntField(re,"rlist_noe",lambda v : r.setNoExtraBeds(v))
          r.setPriceExtraBeds(con.toB(re["rlist_priceextra"]))
          r.setPriceListExtraBeds(con.toB(re["rlist_pricelistextrabeds"]))
          
          r.setPriceTotal(con.toB(re["rlist_pricetotal"]))
          
          reselist.add(r)
          
      RFORM = util.RESFORM(var)
      if resename : RFORM.changeElem(reservation)
      else : RFORM.addElem(reservation)
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
        
def _checkCurrentRese(var) :
  list = var["JLIST_MAP"][RESLIST]
  if len(list) == 0:
    _setAlreadyReservedNotSelected(var)  
    return False
  for elem in list :
    avail = elem["avail"]
    if not avail :
     _setAlreadyReserved(var)
     return False
  return True          
    
def reseraction(action,var):
    printvar("reseraction",action,var)
    
    if action == "cancelreserv" and var["JYESANSWER"] :
     resname = rutil.getReseName(var)
     R = util.RESOP(var)
     R.changeStatusToCancel(resname)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
    if action == "aftercheckin" and var["JUPDIALOG_BUTTON"] == "makecheckin" :
       var["JCLOSE_DIALOG"] = True        
    
    if action == "signalchange" :
        if var["changefield"] == "serviceperperson" :
          _setAfterPerPerson(var)
          
        if var["changefield"] == "roomservice" : 
            _setAfterServiceName(var)
            if not var["changeafterfocus"] and _newRese(var) : 
               _setAfterPriceList(var)
               _setAfterPerPerson(var)
               _createListOfDays(var)
        if var["changefield"] == "roompricelist" : 
            _setAfterPriceList(var)
            if not var["changeafterfocus"] and _newRese(var): 
              _setAfterServiceName(var)
              _setAfterPerPerson(var)
              _createListOfDays(var)
    
    if action=="before" :
        rutil.setvarBefore(var)
        if not _newRese(var) :
          showButton(var,"cancelres")
          showButton(var,"checkin")
        
    if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" :
        xml = var["JUPDIALOG_RES"]
        util.xmlToVar(var,xml,getCustFieldIdAll(),CUST)
        setCopy(var,getCustFieldIdAll(),None,CUST)
        
    if action=="custdetails" :
        var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        var["JUPDIALOG_START"] = util.mapToXML(var,getCustFieldIdAll(),CUST)
            
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
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
from hotelpack.reservation.resutil import getReservForDay
from com.gwthotel.hotel.reservation import ResStatus
from util.util import getCustFieldId
from util.util import mapToXML
from util.util import xmlToVar
from hotelpack.reservation.resutil import showCustomerDetails
from util.util import getPriceForPriceList
from cutil import checkGreaterZero

CLIST = ["name","descr"] + getCustFieldId()

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
  var["JLIST_MAP"] = { "reslist" : rData[0]}
  var["JFOOTER_COPY_reslist_price"] = True
  var["JFOOTER_reslist_price"] = rData[1]
  return rData[2]
 
def _checkRese(var):
  if not checkGreaterZero(var,"resdays") : return False
  if _createListOfDays(var) : return True
  var["JERROR_MESSAGE"] = "Already reserved"
  var["JMESSAGE_TITLE"] = "Cannot make reservation"  
  return False
     
     
def _setvarBefore(var):
    R = ROOMLIST(var)
    roomname = var["JDATELINE_LINE"]
    res = getReservForDay(var)
    room = R.findElem(roomname)
    assert room != None
    nop = room.getNoPersons()
    var["name"] = roomname
    var["desc"] = room.getDescription()
    var["nop"] = nop
    if len(res) == 0 :
      setCopy(var,["resename","name","datecol","nop","desc","resdays"])
      date = var["JDATELINE_DATE"]
      var["datecol"] = date
      var["resdays"] = 1
      var["resename"] = None
      return
  
    setCopy(var,["resename","name","nop"])
    assert len(res) == 1
    resname = res[0].getResId()
    assert resname != None
    print "resname",resname,"!!!!"
    RFORM = RESFORM(var)
    reservation = RFORM.findElem(resname)
    assert reservation != None
    custname = reservation.getCustomerName()
    assert custname != None
    var["resename"] = resname
    CU = CUSTOMERLIST(var)
    customer = CU.findElem(custname)
    assert customer != None
    li = []
    for s in getCustFieldId() :
      deid = "cust_" + s
      li.append(deid)
      val = customer.getAttr(s)
      var[deid] = val
    setCopy(var,li)
    setCopy(var,["cust_name","cust_descr"])
    var["cust_name"] = customer.getName()
    var["cust_descr"] = customer.getDescription()
    list = []
    sum = SUMBDECIMAL()
    for r in reservation.getResDetail() :
         map = { "name" : r.getRoom(), "resday" : r.getResDate(), "price" : r.getPrice(), "service" : r.getService() }
         list.append(map)
         sum.add(r.getPrice())

    var["JLIST_MAP"] = { "reslist" : list}
    var["JFOOTER_COPY_reslist_price"] = True
    var["JFOOTER_reslist_price"] = sum.sum
  
def reseraction(action,var):
    printvar("reseraction",action,var)
    if action == "signalchange" :
        if var["changefield"] == "roomservice" : _createListOfDays(var)
        if var["changefield"] == "roompricelist" : _createListOfDays(var)
    
    if action=="before" :
        _setvarBefore(var)
        
    if action == "acceptdetails" :
        xml = var["JUPDIALOG_RES"]
        xmlToVar(var,xml,CLIST,"cust_")
        setCopy(var,CLIST,None,"cust_")
        
    if action=="custdetails" :
        var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        var["JUPDIALOG_START"] = mapToXML(var,CLIST,"cust_")
        print "start",var["JUPDIALOG_START"]
            
    if action == "checkaval" :
        _checkRese(var)

    if action == "askforreservation" :
      if not _checkRese(var) : return
      var["JYESNO_MESSAGE"] = "Make reservation ?"
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" :
      if not var["JYESANSWER"] : return  
      # TODO: fix !!! not transactional  
      res = _createResData(var)
      if not res[2] :
          var["JERROR_MESSAGE"] = "Already reserved"
          var["JMESSAGE_TITLE"] = "Cannot make reservation"  
          return
      # customer firstly
      cust = newCustomer(var)
      R = CUSTOMERLIST(var)
      name = var["cust_name"]
      for s in getCustFieldId() :
          deid = "cust_" + s
          val = var[deid]
          cust.setAttr(s,val)
      desc = var["cust_descr"]
      cust.setDescription(desc)
      if not emptyS(name) :
          cust.setName(name)
          R.changeElem(cust)
      else :
          cust.setGensymbol(True);
          name = R.addElem(cust).getName()
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
      # --- 
      var["JOK_MESSAGE"] = "Done. Reservation id " + resename
      var["JMESSAGE_TITLE"] = "Reservation" 
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
      
def showreseraction(action,var):
   printvar("show reservaction",action,var)
   if action == "before" :
     _setvarBefore(var)
     
   if action == "cancelreserv" and var["JYESANSWER"] :
     res = getReservForDay(var)
     resname = res[0].getResId()
     R = RESOP(var)
     R.changeStatusToCancel(resname)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
   if action == "aftercheckin" and var["JUPDIALOG_BUTTON"] == "makecheckin" :
       var["JCLOSE_DIALOG"] = True
       
   if action == "guestdesc" :
       showCustomerDetails(var,var["cust_name"])
           
     
def showstay(action,var):     
   printvar("show stay",action,var)
   if action == "before" :
     _setvarBefore(var)
     
   if action == "changetoreserv" and var["JYESANSWER"] :
     res = getReservForDay(var)
     resname = res[0].getResId()
     R = RESOP(var)
     R.changeStatusToReserv(resname)
     a = createArrayList()
     R.setResGuestList(resname,a)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
   if action == "guestdesc" :
       showCustomerDetails(var,var["cust_name"])
  


     
      

     
   
      
      
      
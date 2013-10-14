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
from util.util import BILLPOSADD
from util.util import newBill
from con import eqUL
from util.util import PAYMENTOP
from util import rutil
from util.util import HOTELTRANSACTION
from util.util import MESS

M = MESS()

CLIST = ["name","descr"] + getCustFieldId()

BILLIST="billlist"

def _listOfPayments(var) :
  rese = getReseName(var)
  li = RESOP(var).getResAddPaymentList(rese)
  seq = []
  sum = SUMBDECIMAL()  
  CU = CUSTOMERLIST(var)
  for e in li :
    g = e.getGuestName()    
    customer = CU.findElem(g)
    fName = customer.getAttr("firstname")
    map = { "roomid" : e.getRoomName(), "guestid" :g, "guestdescr" : customer.getDescription(), "guestfirstname" : fName, "total" : e.getPriceTotal(), "price" : e.getPrice(), "servdescr" : e.getDescription(), "quantity" : e.getQuantity() }
    seq.append(map)
    sum.add(e.getPriceTotal())
  setJMapList(var,"paymentlist",seq)
  setFooter(var,"paymentlist","total",sum.sum)

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
    RFORM = RESFORM(var)
    reservation = RFORM.findElem(resname)
    assert reservation != None
    custname = reservation.getCustomerName()
    assert custname != None
    var["resename"] = resname
        
    setCustData(var,custname,"cust_")
    
    list = []
    sum = SUMBDECIMAL()
    for r in reservation.getResDetail() :
         map = { "name" : r.getRoomName(), "resday" : r.getResDate(), "price" : r.getPrice(), "service" : r.getService() }
         list.append(map)
         sum.add(r.getPrice())

    var["JLIST_MAP"] = { "reslist" : list}
    var["JFOOTER_COPY_reslist_price"] = True
    var["JFOOTER_reslist_price"] = sum.sum
  
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
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
  
  
def reseraction(action,var):
    printvar("reseraction",action,var)
    if action == "signalchange" :
        if var["changefield"] == "roomservice" : _createListOfDays(var)
        if var["changefield"] == "roompricelist" : _createListOfDays(var)
    
    if action=="before" :
        _setvarBefore(var)
        
    if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" :
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
      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" and var["JYESANSWER"] :
      TRAN = MAKERESE(var)
      TRAN.doTrans()
      
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
     
def _countTotal(var,b,pli) :
  
  total = 0.0
  for idp in b.getPayList() :
    for pa in pli :
       if eqUL(idp,pa.getId()) :
         to = BigDecimalToDecimal(pa.getPriceTotal())
         total = addDecimal(total,to)
         
  return total       
     
def _ListOfBills(var) :
   rese = getReseName(var)
   bList = RESOP(var).findBillsForReservation(rese)
   seq = []
   pli = getPayments(var)
   sumtotal = 0.0
   footerpayments = 0.0
   for b in bList :
     id = b.getName()
     payer = b.getPayer()
     da = b.getIssueDate()
     tot = _countTotal(var,b,pli)
     sumtotal = addDecimal(sumtotal,tot)
     paysum = rutil.countPayments(var,id)
     footerpayments = addDecimal(footerpayments,paysum)
     ma = { "billname" : id, "payerid" : payer, "payerdescr" : b.getDescription(), "billtotal" : tot, "payments" : paysum }
     seq.append(ma)
   setJMapList(var,BILLIST,seq)
   setFooter(var,BILLIST,"billtotal",sumtotal) 
   setFooter(var,BILLIST,"payments",footerpayments) 
  
def _setChangedFalse(var) :
   var["billlistwaschanged"] = False
   setCopy(var,["billlistwaschanged",])
  
  
def showstay(action,var):     
   printvar("show stay",action,var)
   if action == "before" :
     _setvarBefore(var)
     # after 
     _listOfPayments(var)
     _ListOfBills(var)
     _setChangedFalse(var)
     return
   
   if var["billlistwaschanged"] :
    _setChangedFalse(var)   
    _ListOfBills(var)
     
   if action == "crud_readlist" and var["JLIST_NAME"] == BILLIST :
     _ListOfBills(var)     
     
   if action == "afterbill" and var["JUPDIALOG_BUTTON"] == "acceptafteryes" :
     _ListOfBills(var)
     
   if action == "payerdetail" :
      showCustomerDetails(var,var["payerid"])
     
   if action == "changetoreserv" and var["JYESANSWER"] :
     res = getReservForDay(var)
     resname = res[0].getResId()
     R = RESOP(var)
     R.changeStatusToReserv(resname)
     a = createArrayList()
     R.setResGuestList(resname,a)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
   if action == "addpayment" :
      var["JUP_DIALOG"]="hotel/reservation/addpayment.xml" 
      var["JAFTERDIALOG_ACTION"] = "afteraddpayment" 
      
   if action == "paymentslist" :
      var["JUP_DIALOG"]="hotel/reservation/listofpayment.xml" 
      var["JAFTERDIALOG_ACTION"] = "afterlistpayments"
      var["JUPDIALOG_START"] = var["billname"]
      
   if action == "afteraddpayment" and var["JUPDIALOG_BUTTON"] == "addpayment" :
     _listOfPayments(var)
        
   if action == "guestdesc" :
       showCustomerDetails(var,var["cust_name"])
       
   if action == "guestdetail" :
       showCustomerDetails(var,var["guestid"])
         
def billdesc(action,var) :
   printvar("bill desc",action,var)
   
   if action == "before" :
     payername = var["payerid"]
     setCustData(var,payername,"payer_")
     LI = BILLPOSADD(var,"billlist")
     billname = var["billname"]
     b = BILLLIST(var).findElem(billname)
     pli = getPayments(var)
     for idp in b.getPayList() :
       print idp
       for pa in pli :
         print "a=   " + str(pa.getId())
         if eqUL(idp,pa.getId()) : 
            print "add"
            LI.addMa({},pa,idp)
     LI.close()     
     
   if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = "Are you sure that you want to remove this bill ?"
      var["JMESSAGE_TITLE"] = ""  
      return

   if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
     billname = var["billname"]
     BILLLIST(var).deleteElemByName(billname)
     var["JCLOSE_DIALOG"] = True
           


     


  

     
      

     
   
      
      
      
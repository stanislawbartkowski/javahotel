from cutil import printVar
from util.util import RESFORM
from util.util import ROOMLIST 
from util.util import RESOP
from util.util import CUSTOMERLIST
from hotelpack.reservation.resutil import showCustomerDetails
from cutil import setCopy
from cutil import today
from util.util import getPriceForPriceList
from util.util import SERVICES
from cutil import mulIntDecimal
from cutil import BigDecimalToDecimal
from cutil import checkGreaterZero
from util.util import newResAddPayment
from cutil import toDate
from cutil import toB

RLIST = "roomlist"

def _createList(var):
   resName = var["resename"]
   ROOM = ROOMLIST(var)
   ROP = RESOP(var)
   CU = CUSTOMERLIST(var)
   list = []
   gList = ROP.getResGuestList(resName)
   for g in gList :
       room = g.getRoomName()
       guest = g.getGuestName()
       rdescr = ROOM.findElem(room).getDescription() 
       c = CU.findElem(guest)
       descr = c.getDescription()
       firstname = c.getAttr("firstname")
       list.append({"roomid" : room,"roomdesc" : rdescr, "name" : guest, "descr" : descr,"firstname" : firstname})
   var["JLIST_MAP"] = { RLIST : list}

LI = ["paymentdate","pricelist","service","pricefromlist","price","descr","quantity","total"]

def _setPrice(var,val=None) :
    setCopy(var,["pricefromlist","price"])
    var["pricefromlist"] = val
    if val : var["price"] = val
    _setTotal(var)

def _setDescr(var,desc=None) :
    setCopy(var,["descr"])
    var["descr"] = desc
  
def _setPriceAfterRating(var) :
    price = var["pricelist"]
    service = var["service"]
    if price == None or service == None:
      _setPrice(var)
      return
    pr = getPriceForPriceList(var,price,service)
    if pr == None :
      _setPrice(var)
      return
    
    _setPrice(var,BigDecimalToDecimal(pr.getWeekendPrice()))

def _setAfterPriceList(var) :
    _setPriceAfterRating(var)

def _setAfterService(var) :
      _setPriceAfterRating(var)
      service = var["service"]
      if service == None :
          _setDescr(var)
          return
      serv = SERVICES(var).findElem(service)
      _setDescr(var,serv.getDescription())
      setCopy(var,["vat"])
      var["var"] = serv.getVat()

def _setAfterQuantity(var) :
    _setTotal(var)
     
def _setAfterPrice(var) :
    _setTotal(var)

def _setTotalVal(var,val=None) :
      setCopy(var,["total"])
      var["total"] = val
    
def _setTotal(var) :
      qua = var["quantity"]
      price = var["price"]
      if qua == None or price == None :
        _setTotalVal(var)
        return
      if not checkGreaterZero(var,"quantity") : return
      _setTotalVal(var,mulIntDecimal(qua,price))

def _addPayment(var) :
     ROP = RESOP(var)
     r = newResAddPayment()
     quantity = var["quantity"]
     da = toDate(var["paymentdate"])
     descr = var["descr"]
     price = toB(var["price"])
#     price = toB(1.00)     
     pricelist = toB(var["pricefromlist"])
     total = toB(var["total"])
#     total = toB(1.01)
     room = None
     guest = None
     rese = var["resename"]
     vat = var["vat"]
     serv = var["service"]
     if var[RLIST+"_lineset"] :
       room = var["roomid"]
       guest = var["name"]
     if room == None :
       room = var["JDATELINE_LINE"]
     if guest == None :
       guest = RESFORM(var).findElem(rese).getCustomerName()
     r.setQuantity(quantity)
     r.setPrice(price)
     r.setPriceList(pricelist)
     r.setPriceTotal(total)
     r.setServDate(da)
     r.setDescription(descr)
#     r.setVat(vat) 
#    TODO: addvar
     r.setServiceName(serv)
     r.setGuestName(guest)
     r.setRoomName(room)
     ROP.addResAddPayment(rese,r)


def doaction(action,var):
    printVar("addpayment",action,var)
    
    if action == "before" :
        _createList(var)
        setCopy(var,LI)
        for l in LI :
          var[l] = None
        var["pricelist"] = ""
        var["service"] = ""
        var["quantity"] = 1
        var["paymentdate"] = today()
        
    if action == "signalchange" :
        if var["changefield"] == "pricelist" : _setAfterPriceList(var)
        if var["changefield"] == "service" : _setAfterService(var)
        if var["changefield"] == "quantity" : _setAfterQuantity(var)
        if var["changefield"] == "price" : _setAfterPrice(var)
        
    if action == "guestdetail" and var[RLIST+"_lineset"] :
        showCustomerDetails(var,var["name"])
        
    if action == "addpayment" :
      if not checkGreaterZero(var,"quantity") : return
      _addPayment(var)
      var["JCLOSE_DIALOG"] = True
              
            
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
    var["price"] = val
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
      _setDescr(var,SERVICES(var).findElem(service).getDescription())
    
def _setTotalVal(var,val=None) :
      setCopy(var,["total"])
      var["total"] = val
    
def _setTotal(var) :
      qua = var["quantity"]
      price = var["price"]
      if qua == None or price == None :
        _setTotalVal(var)
        return
      _setTotalVal(var,mulIntDecimal(qua,price))

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
        
        
    if action == "guestdetail" and var[RLIST+"_lineset"] :
        showCustomerDetails(var,var["name"])
        
    if action == "addpayment" :
      pass
      
        
        

#addpayment
        
            
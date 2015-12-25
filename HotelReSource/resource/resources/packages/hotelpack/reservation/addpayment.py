import cutil,con

from util import util,cust

RLIST = "roomlist"

def _createList(var):
   resName = var["resename"]
   ROOM = util.ROOMLIST(var)
   ROP = util.RESOP(var)
   CU = util.CUSTOMERLIST(var)
   list = []
   gList = ROP.getResGuestList(resName)
   for g in gList :
       room = g.getRoomName()
       guest = g.getGuestName()
       rdescr = ROOM.findElem(room).getDescription() 
       c = CU.findElem(guest)
       map = {"roomid" : room, "roomdesc" : rdescr }
       cust.toCustomerVar(map,c,"guest_")
       list.append(map)
   var["JLIST_MAP"] = { RLIST : list}

LI = ["paymentdate","service","pricefromlist","price","descr","quantity","total"]

def _setPrice(var,val=None) :
    cutil.setCopy(var,["pricefromlist","price"])
    var["pricefromlist"] = val
    if val : var["price"] = val
    _setTotal(var)

def _setDescr(var,desc=None) :
    cutil.setCopy(var,["descr"])
    var["descr"] = desc
  
def _setPriceAfterRating(var) :
    price = var["pricelist"]
    service = var["service"]
    if price == None or service == None:
      _setPrice(var)
      return
    pr = util.getPriceForPriceList(var,price,service)
    if pr == None :
      _setPrice(var)
      return
    
    _setPrice(var,con.BigDecimalToDecimal(pr.getPrice()))

def _setAfterPriceList(var) :
    _setPriceAfterRating(var)

def _setAfterService(var) :
      _setPriceAfterRating(var)
      service = var["service"]
      if service == None :
          _setDescr(var)
          return
      serv = util.SERVICES(var).findElem(service)
      _setDescr(var,serv.getDescription())
      cutil.setCopy(var,["vat"])
      var["var"] = serv.getVat()

def _setAfterQuantity(var) :
    _setTotal(var)
     
def _setAfterPrice(var) :
    _setTotal(var)

def _setTotalVal(var,val=None) :
      cutil.setCopy(var,["total"])
      var["total"] = val
    
def _setTotal(var) :
      qua = var["quantity"]
      price = var["price"]
      if qua == None or price == None :
        _setTotalVal(var)
        return
      if not cutil.checkGreaterZero(var,"quantity") : return
      _setTotalVal(var,con.mulIntDecimal(qua,price))

def _addPayment(var) :
     ROP = util.RESOP(var)
     r = util.newResAddPayment()
     quantity = var["quantity"]
     da = con.toDate(var["paymentdate"])
     descr = var["descr"]
     price = con.toB(var["price"])
#     price = toB(1.00)     
     pricelist = con.toB(var["pricefromlist"])
     total = con.toB(var["total"])
#     total = toB(1.01)
     room = None
     guest = None
     rese = var["resename"]
     vat = var["vat"]
     serv = var["service"]
     if var[RLIST+"_lineset"] :
       room = var["roomid"]
       guest = var["guest_name"]
     if room == None :
       room = var["JDATELINE_LINE"]
     if guest == None :
       guest = util.RESFORM(var).findElem(rese).getCustomerName()
     r.setQuantity(quantity)
     r.setPrice(price)
     r.setPriceList(pricelist)
     r.setPriceTotal(total)
     r.setServDate(da)
     r.setDescription(descr)
     r.setService(serv)
     r.setGuestName(guest)
     r.setRoomName(room)
     if serv == None :
       r.setVat(vat)
     ROP.addResAddPayment(rese,r)


def doaction(action,var):
    cutil.printVar("addpayment",action,var)
    
    if action == "before" :
        _createList(var)
        cutil.setCopy(var,LI)
        for l in LI :
          var[l] = None
#        var["pricelist"] = ""
        var["service"] = ""
        var["quantity"] = 1
        var["paymentdate"] = cutil.today()
        
    if action == "signalchange" :
        if var["changefield"] == "pricelist" : _setAfterPriceList(var)
        if var["changefield"] == "service" : _setAfterService(var)
        if var["changefield"] == "quantity" : _setAfterQuantity(var)
        if var["changefield"] == "price" : _setAfterPrice(var)
        
    if action == "guestdetail" and var[RLIST+"_lineset"] :
        util.showCustomerDetails(var,var["guest_name"])
        
    if action == "addpayment" and var["JYESANSWER"] :
      if not cutil.checkGreaterZero(var,"quantity") : return
      _addPayment(var)
      var["JCLOSE_DIALOG"] = True
              
            
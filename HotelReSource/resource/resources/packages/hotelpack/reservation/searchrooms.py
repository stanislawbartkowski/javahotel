import cutil
import con
from util import rutil
from util import util
import xmlutil

LISTID="roomlist"

# =================
#def nofDays(dfrom,dto) :
#  return (dto-dfrom).days

#def addRoom(var,roomid,map,capa,descr) :
#    RO = util.ROOMLIST(var).findElem(roomid)
#    map[capa] = RO.getNoPersons()
#    map[descr] = RO.getDescription()
    
    
#def getPriceList(var,pricelist,serv) :
#  pricelist = var["roompricelist"]
#  price = None
#  pricechild = None
#  priceextra = None
#  serv = var["roomservice"]
#  pricelist = var["roompricelist"]
#  if serv != None and pricelist != None :
#    P = util.PRICEELEM(var)
#    prices = P.getPricesForPriceList(pricelist)
#    for s in prices :
#      id = s.getService()
#      if id == serv :
#        price = s.getPrice()
#        pricechild = s.getChildrenPrice()
#        priceextra = s.getExtrabedsPrice()
#  return (price,pricechild,priceextra)

#def calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra) :
  
#  if perperson :
#    price = con.mulIntDecimal(resnop,priceperson)
#    price = con.addDecimal(price,con.mulIntDecimal(resnoc,pricechildren))
#    price = con.addDecimal(price,con.mulIntDecimal(resextra,priceextra))
#  else : price = priceroom    
  
#  return price

# ====================    

def _validate(var) :
  if var["searchby_days"] :
    if cutil.checkEmpty(var,"search_days") : return False
    if not cutil.checkGreaterZero(var,"search_days") : return False
    var["search_to"] = con.incDays(var["search_from"],var["search_days"])
    cutil.setCopy(var,"search_to")
    return True
  if cutil.checkEmpty(var,"search_to") : return False
  da = nofDays(var["search_from"],var["search_to"])
  if da <= 0 :
    cutil.setErrorField(var,"search_to","@datefromlessto")
    return False
  var["search_days"] = da
  cutil.setCopy(var,"search_days")
  return True

def _goto(var) :
    var["JDATELINE_GOTO_reservation"] = var["search_from"]
    var["JSEARCH_LIST_SET_reservation_name"] = var["search_roomid"]
    
def dialogaction(action,var) :
  cutil.printVar("search for rooms",action,var)
  
  if action == "before" :
    var["search_from"] = con.today()
    var["searchby_days"] = True
    var["search_nop"] = 2
    cutil.setCopy(var,["search_from","searchby_days","search_nop"])
    
  if action=="search" :
    if not _validate(var) : return
    roo = rutil.searchForRooms(var,var["search_from"],var["search_to"])
    li = []
    for l in roo :
      map = {"search_roomid" : l}
      util.addRoom(var,l,map,"search_roomcapa","search_roomdesc")
      ss = util.getServicesForRoom(var,l)
      if ss != None:
        map["search_roomservice"] = ss[0][0].getName() + " " + ss[0][0].getDescription()
        map["search_roompricelist"] = ss[1][0]
        (price,pricechild,priceextra) = rutil.getPriceList(var,ss[1][0],ss[0][0].getName())
        perperson = ss[0][0].isPerperson()
        map["search_price"] = price
        total= rutil.calculatePrice(perperson,var["search_nop"],0,0,price,pricechild,priceextra) 
        map["search_priceperson"] = total
        map["search_pricetotal"] = con.mulIntDecimal(var["search_days"],total)

      li.append(map)
    cutil.setJMapList(var,LISTID,li)  
    
  if action == "seton" and var[LISTID+"_lineset"] : 
     _goto(var)

  if action == "tores" and var[LISTID+"_lineset"] :
     _goto(var)
     var["JCLOSE_DIALOG"] = True
     var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
     m = {}
     m["roomname"] = var["search_roomid"]
     m["firstday"] = var["search_from"]
     m["nodays"] = var["search_days"]
     m["nop"] = var["search_nop"]
     var["JUPDIALOG_START"] = xmlutil.toXML(m)
     
     
import cutil
import con
import xmlutil

from util import rutil
from util import util

LISTID="roomlist"

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
    
def _setStartRes(var) :    
     m = {}
     m["roomname"] = var["search_roomid"]
     m["firstday"] = var["search_from"]
     m["nodays"] = var["search_days"]
     m["nop"] = var["search_nop"]
     m["roomservice"] = var["search_roomservice"]
     m["roompricelist"] = var["search_roompricelist"]
     return xmlutil.toXML(m)    
   
def dialogaction(action,var) :
  cutil.printVar("search for rooms",action,var)
  
  if action == "before" :
    var["search_from"] = con.today()
    var["searchby_days"] = True
    var["search_nop"] = 2
    var["search_days"] = 1
    cutil.setCopy(var,["search_from","searchby_days","search_nop","search_days"])
    if var["JUPDIALOG_START"] != None :
      html = var["JUPDIALOG_START"]
      var["search_html"] = html
      cutil.setCopy(var,"search_html")
      cutil.hideButton(var,"tores",True)
      cutil.hideButton(var,"toresrese",False)
          
  if action=="search" :
    if not _validate(var) : return
    roo = rutil.searchForRooms(var,var["search_from"],var["search_to"])
    alist = None
    if var["search_html"] != None :
      (m,alist) = xmlutil.toMap(var["search_html"])
    print alist  
            
    li = []
    for l in roo :
      found = False
      if alist != None :
        for re in alist :
          if re["resroomname"] != l : continue
          da = re["resday"]
          if da < var["search_from"] : continue
          if da >= var["search_to"] : continue
          print found
          found = True
          break
      if found : continue    
      map = {"search_roomid" : l}
      util.addRoom(var,l,map,"search_roomcapa","search_roomdesc")
      ss = util.getServicesForRoom(var,l)
      if ss != None:
        map["search_roomservice"] = ss[0][0].getName()
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
     if not _validate(var) : return
     _goto(var)
     var["JCLOSE_DIALOG"] = True
     var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
     var["JUPDIALOG_START"] = _setStartRes(var)
  
  if action == "toresrese" and var[LISTID+"_lineset"] :
    if not _validate(var) : return
    _goto(var)
    var["JCLOSE_DIALOG"] = _setStartRes(var)
     
     
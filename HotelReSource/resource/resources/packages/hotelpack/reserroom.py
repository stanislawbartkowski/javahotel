from util.util import printvar
from util.util import setCopy
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
  
  peList = PE.getPricesForPriceList(pricelist)
  pPrice = None
  for p in peList :
      if service == p.getService() :
          pPrice = p
          break
# getResDate      
  for i in range(resdays) :
      price = pPrice.getWorkingPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if eqDate(dt,rdata) : avail = allavail = False
      map = { "avail" : True, "name" : roomname, "resday" : dt, "price" : price}
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
 
  
def reseraction(action,var):
    printvar("reseraction",action,var)
    if action == "signalchange" :
        if var["changefield"] == "roomservice" : _createListOfDays(var)
        if var["changefield"] == "roompricelist" : _createListOfDays(var)
    
    if action=="before" :
      R = ROOMLIST(var)
      roomname = var["JDATELINE_LINE"]
      services = R.getRoomServices(roomname)
      date = var["JDATELINE_DATE"]
      setCopy(var,["name","datecol","nop","desc","resdays"])
      var["name"] = roomname
      var["datecol"] = date
      room = R.findElem(roomname)
      nop = room.getNoPersons()
      var["nop"] = nop
      var["desc"] = room.getDescription()
      var["resdays"] = 1
      
    if action == "checkaval" :
        resdays = var["resdays"]
        if resdays <= 0 :
            var["JERROR_resdays"] = "Should be greater then 0"
            return
        _createListOfDays(var)
              

def serviceenum(action,var):
  printvar ("serviceenum", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  f = lambda elem : [elem.getName(), elem.getDescription()]
  var["JLIST_MAP"] = { "roomservice" : createEnumFromList(li[0],f)}
      
def pricelistenum(action,var):
  printvar ("pricelistenum,", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  PR = PRICELIST(var)
  f = lambda elem : [elem, PR.findElem(elem).getDescription()]
  var["JLIST_MAP"] = { "roompricelist" : createEnumFromList(li[1],f)}

CLIST=["custname","custdescr","firstname","companyname","street","zipcode","email","phone"]

def custdetails(action,var):
  printvar ("custdetails,", action,var)
  if action == "before" :
      li = []
      for s in CLIST :
          deid = "de_" + s
          var[deid] = var[s]
          li.append(deid)
      setCopy(var,li)
      
  if action == "accept" :
      setCopy(var,CLIST)
      for s in CLIST :
          deid = "de_" + s
          var[s] = var[deid]
      var["JCLOSE_DIALOG"] = True
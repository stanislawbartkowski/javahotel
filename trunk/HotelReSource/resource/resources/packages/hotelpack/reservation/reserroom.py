import datetime

from util import rutil
from util import util
import cutil
import con
import xmlutil

M = util.MESS()

CUST="cust_"
RESLIST="reslist"
            
# --------------------          
def _newRese(var) :
  return rutil.getReseName(var) == None

def _createResData(var,new):
  service = var["roomservice"]
  if util.emptyS(service) : return None
  pricelist = var["roompricelist"]
  if util.emptyS(pricelist) : return None
  roomname = var["name"]
  date = var["datecol"]
  resdays = var["resdays"]
  dl = datetime.timedelta(1)
  dt = date
  sum = util.SUMBDECIMAL()
  if new :
    list = []
  else :
    list = var["JLIST_MAP"][RESLIST]
    sum.add(var["JFOOTER_reslist_rlist_pricetotal"])
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
    
  price = rutil.calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra)
  
  query = cutil.createArrayList()
  RES = util.RESOP(var)
  qelem = rutil.createResQueryElem(roomname,date,con.incDays(date,resdays))
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
          if con.eqDate(dt,rdata) : 
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
  serv = var["roomservice"]
#  print "!!!!", pricelist,serv, "!!!"
  return rutil.getPriceList(var,pricelist,serv)

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
#  print "set after price" + str(var["respriceperson"])
  cutil.setCopy(var,["respriceperson","respricechildren","respriceextrabeds"])

def _setAfterPerPerson(var) :  
  cutil.enableField(var,["respriceperson","respricechildren","respriceextrabeds"],var["serviceperperson"])
  cutil.enableField(var,"respriceperroom",not var["serviceperperson"])

def _createListOfDays(var,new): 
  rData = _createResData(var,new)
  if rData == None : return None
#  assert rData != None
  cutil.setJMapList(var,RESLIST,rData[0])
  cutil.setFooter(var,RESLIST,"rlist_pricetotal",rData[1])
  return rData[2]

def _checkNoAndPrice(var,no,price) :
  if not cutil.checkGreaterZero(var,no) : return False
  if var[no] == None : return True
  if cutil.checkEmpty(var,price) : return False
  return True
 
def _checkRese(var,new=True):
  if not cutil.checkGreaterZero(var,"resdays") : return False
  if not var["serviceperperson"] :
    if cutil.checkEmpty(var,["respriceperroom"]) : return False
  if not _checkNoAndPrice(var,"resnop","respriceperson") : return False
  if not _checkNoAndPrice(var,"resnochildren","respricechildren") : return False
  if not _checkNoAndPrice(var,"resnoextrabeds","respriceextrabeds") : return False
    
  if _createListOfDays(var,new) : return True
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
  RES = util.RESOP(var)
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
  
class MAKERESE(util.HOTELTRANSACTION) :
  
   def __init__(self,var) :
     util.HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :     
      if not _checkAvailibity(var) : return
      # customer firstly
      cust = util.customerFromVar(var,CUST)
      R = util.CUSTOMERLIST(var)
      name = var["cust_name"]
      if not util.emptyS(name) :
          cust.setName(name)
          R.changeElem(cust)
      else :
          cust.setGensymbol(True);
          name = R.addElem(cust).getName()
      util.saveDefaCustomer(var,CUST)               
      # --- customer added
      
      resename = rutil.getReseName(var) 
      reservation = util.newResForm(var)
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
    cutil.printVar("reseraction",action,var)
    
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
            _setAfterPriceList(var)
            if not var["changeafterfocus"] and _newRese(var) : 
               _setAfterPerPerson(var)
               _createListOfDays(var,True)
        if var["changefield"] == "roompricelist" : 
            _setAfterPriceList(var)
            if not var["changeafterfocus"] and _newRese(var): 
              _setAfterServiceName(var)
              _setAfterPerPerson(var)
              _createListOfDays(var,True)
    
    if action=="before" :
        rutil.setvarBefore(var)
        if not _newRese(var) :          
          cutil.hideButton(var,["cancelres","checkin"],False)
          util.enableCust(var,CUST,False)
        
    if action == "acceptdetails" and (var["JUPDIALOG_BUTTON"] == "accept" or var["JUPDIALOG_BUTTON"] == "acceptask"):
        xml = var["JUPDIALOG_RES"]
        util.xmlToVar(var,xml,util.getCustFieldIdAll(),CUST)
        cutil.setCopy(var,util.getCustFieldIdAll(),None,CUST)
        if not _newRese(var) :          
          name = var[CUST+"name"]
          resename = rutil.getReseName(var)          
          RFORM = util.RESFORM(var)
          r = RFORM.findElem(resename)
          r.setCustomerName(name)
          RFORM.changeElem(r)
        
    if action=="custdetails" :
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        if _newRese(var) : util.customerDetailsActive(var,CUST)
        else : util.showCustomerDetailstoActive(var,var[CUST+"name"])
            
    if action == "checkaval" :
        _checkRese(var)

    if action == "askforreservation" :

      if not _checkCurrentRese(var) : return
      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" and var["JYESANSWER"] :
      TRAN = MAKERESE(var)
      TRAN.doTrans()
      
    if action == "morereservation" :
        l = var["JLIST_MAP"][RESLIST]
        xml = xmlutil.toXML({},l)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="hotel/reservation/searchrooms.xml" 
        var["JAFTERDIALOG_ACTION"] = "morereservationaccept" 
      
    if action == "morereservationaccept" and var["JUPDIALOG_BUTTON"] == "toresrese" :
        var["JUPDIALOG_START"] = var["JUPDIALOG_RES"]        
        rutil.setvarBefore(var)
        _checkRese(var,False)

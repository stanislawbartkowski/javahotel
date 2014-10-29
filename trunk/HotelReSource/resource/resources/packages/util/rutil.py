import sets

from com.gwthotel.hotel.reservationop import ResQuery

import cutil,con,util,clog,xmlutil

M=util.MESS()

def getReseName(var) :
  return var["resename"]

def setServicePriceList(var,roomservice,pricelist) :
  var["roompricelist"] = pricelist
  var["roomservice"] = roomservice
#  print pricelist,roomservice
  cutil.setCopy(var,["roompricelist","roomservice"])

def createResQueryElem(roomname,dfrom,dto):
    q = ResQuery()
    q.setFromRes(cutil.toDate(dfrom))
    q.setToRes(cutil.toDate(dto))
    q.setRoomName(roomname)
    return q

def getReservForDay(var):
   R = util.RESOP(var)
   room = var["JDATELINE_LINE"]
   day = var["JDATELINE_DATE"]
   query=cutil.createArrayList()
   q = createResQueryElem(room,day,day)
   query.add(q)
   res = R.queryReservation(query)
   return res

def getPayments(var,rese=None) :    
  if rese == None : rese = getReseName(var)
  pli = util.RESOP(var).getResAddPaymentList(rese)
  R = util.RESFORM(var)
  pli.addAll(R.findElem(rese).getResDetail())
  return pli

def countPaymentsA(var,billName) :
  pList = util.PAYMENTOP(var).getPaymentsForBill(billName)
  suma = 0.0
  isadvance = False
  for p in pList :
    total = p.getPaymentTotal()
    suma = cutil.addDecimal(suma,con.BigDecimalToDecimal(total))
    if p.isAdvancepayment() : isadvance = True
  return (suma,isadvance)  


def countPayments(var,billName) :
  (suma,isadvance) = countPaymentsA(var,billName)
  return suma  

def countTotal(var,b,pli) :
  """ Counts total sum of services for bill
  
  Args: 
    var
    b CustomerBill
    pli List of services (return result of getPayments)   
    
  Returns: sum of services for b (CustomerBill)  
  """
  total = 0.0
  for idp in b.getPayList() :
    for pa in pli :
       if con.eqUL(idp,pa.getId()) :
         to = con.BigDecimalToDecimal(pa.getPriceTotal())
         total = con.addDecimal(total,to)
         
  return total

class BILLPOSADD :
  
  def __init__(self,var,liname) :
    self.sumf = 0.0
    self.var = var
    self.liname = liname
    self.li = []
    
  def addMa(self,ma,r,idp) :
    se = r.getServiceType()
    resdate = None
    servdate= None
    if util.isRoomService(se) : resdate = r.getResDate()
    else : servdate = r.getResDate()
    total = r.getPriceTotal()
    self.sumf = cutil.addDecimal(self.sumf,cutil.BigDecimalToDecimal(total))
    guest = r.getGuestName()
    room = r.getRoomName()
    service = r.getService()
    ma1 = { "idp" : idp, "room" : room, "resday" : resdate, "service" : service, "servday":servdate, "servdescr" : r.getDescription(),"guest_name" : guest, "total" : total }
    ma.update(ma1)
    self.li.append(ma)
    
  def close(self) :
    cutil.setJMapList(self.var,self.liname,self.li)
    cutil.setFooter(self.var,self.liname,"total",self.sumf)

def searchForRooms(var,dfrom, dto):
    R = util.RESOP(var)
    q = createResQueryElem(None,dfrom,dto)
    l = R.searchReservation(q)
    res = []
    for r in l :
        res.append(r.getRoomName())
    return res
  
def getPriceList(var,pricelist,serv) :
  """ Get prices for pricelist name and service given
  Args:
    var 
    pricelist Price list name
    serv Service name
  Returns :
    Sequence (price, pricechild, priceextreabdes)
    In not found return sequence of three Nones
  """
#  clog.info("getPricList",pricelist,serv)
  price = None
  pricechild = None
  priceextra = None
  if serv != None and pricelist != None :
    P = util.PRICEELEM(var)
    prices = P.getPricesForPriceList(pricelist)
    for s in prices :
      id = s.getService()
#      clog.info("id=",id,"serv=",serv)
      if id == serv :
        price = s.getPrice()
        pricechild = s.getChildrenPrice()
        priceextra = s.getExtrabedsPrice()
  return (price,pricechild,priceextra)
  
def calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra,priceroom=None) :  
  if perperson :
    price = con.mulIntDecimal(resnop,priceperson)
    price = con.addDecimal(price,con.mulIntDecimal(resnoc,pricechildren))
    price = con.addDecimal(price,con.mulIntDecimal(resextra,priceextra))
  else : 
    if priceeroom : price = priceroom    
    else : price = priceperson
  return price

def getAttrS(r,attr) :
  if r.getAttr(attr) == None : return ""
  return r.getAttr(attr)

def rescustInfo(var,res) :
   custName = res.getCustomerName()
   cust = util.CUSTOMERLIST(var).findElem(custName)
   assert cust != None
   dName = getAttrS(cust,"surname") + " " + getAttrS(cust,"firstname")
   if cutil.emptyS(dName) : dName = cust.getName()
   return dName + " , " + getAttrS(cust,"country")


def setAlreadyReserved(var) :  
   var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
   var["JMESSAGE_TITLE"] = M("ALREADYRESERVEDTITLE")

def setAlreadyReservedNotAvailable(var) :  
    var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
    var["JMESSAGE_TITLE"] = "Not everything is available"
    
def setAlreadyReservedNotSelected(var) :  
    var["JERROR_MESSAGE"] = M("ALREADYRESERVEDMESSAGE")
    var["JMESSAGE_TITLE"] = "No single room selected !"
    
def _setDuplicatedReservation(var,resday,roomname) :  
    var["JERROR_MESSAGE"] = M("reservationduplicated").format(str(resday),roomname)
    
def checkReseAvailibity(var,list,avail,resday,resroomname) :
  """ Validate reservation
  Args:
    var
    list List of reservations
    avail If not none field with 'available' information
    resday Field with reservation day
    resroomname Field with room name to reserve
  Returns: 
    None Reservation OK
    (date,roomname) Not valid and position of error
  """
  
  # check if reservation not doubled  
  b = sets.Set()
  for p in list :
    dat = p[resday]
    roomname = p[resroomname]
    if (dat,roomname) in b :
        _setDuplicatedReservation(var,dat,roomname)
        return (dat,roomname)
    b.add((dat,roomname))
    
  RES = util.RESOP(var)
  query = cutil.createArrayList()
  for p in list :
    dat = p[resday]
    roomname = p[resroomname]
    if avail != None and not p[avail] :
      setAlreadyReservedNotAvailable(var)
      return (dat,roomname)
    qelem = createResQueryElem(roomname,dat,dat)
    query.add(qelem)
  rList = RES.queryReservation(query)
  # analize if other reservation
  resename = getReseName(var)
  alreadyres = len(rList)
  dat = None
  roomname = None
  if resename  :
    alreadyres = 0
    for r in rList :
      if r.getResId() != resename : 
        dat = r.getResDate()
        roomname = r.getRoomName()
        alreadyres = alreadyres + 1
  if alreadyres :
     setAlreadyReserved(var)
     return (dat,roomname)
  return None

# ------------------------------

RESLIST = ["resday","resroomname","rlist_roomservice","rlist_roompricelist","rlist_serviceperperson","rline_nop","rlist_priceperson","rlist_noc","rlist_pricechildren","rlist_noe","rlist_priceextra","rlist_pricetotal","rlist_pricetotal"]

class RELINE :
  
  def __init__(self,li,night,roomname,servicename,pricelist,perpers,nop,pop,noc,poc,noe,poe,priceroom,total) :
    self.li = li
    self.night = night
    self.roomname = roomname
    self.servicename = servicename
    self.pricelist = pricelist
    self.perpers = perpers
    self.nop = nop
    self.pop = pop
    self.noc = noc
    self.poc = poc
    self.noe = noe
    self.poe = poe
    self.priceroom = priceroom
    self.total = total
    
  def _testp(self,var,nop,pop) :
    if var[nop] == None: return True
    return not cutil.checkEmpty(var,pop) 
    
  def validate(self,var) :
    if cutil.checkEmpty(var,[self.night,self.roomname,self.nop]) : return False
    if var[self.perpers] :
      if cutil.checkEmpty(var,self.pop) : return False
      if not cutil.checkGreaterZero(var,self.nop) : return False
      if not cutil.checkGreaterZero(var,self.noc) : return False
      if not cutil.checkGreaterZero(var,self.noe) : return False
      if not self._testp(var,self.noc,self.poc) : return False
      if not self._testp(var,self.noe,self.poe) : return False
    else :
      if cutil.checkEmpty(var,self.priceroom) : return False
    room = var[self.roomname]
    service = var[self.servicename]
    if room != None and service != None :
      slist = util.ROOMLIST(var).getRoomServices(room)
      exist = util.findElemInSeq(service,slist)
      if not exist :
        cutil.setErrorField(var,self.servicename,"@theserviceisnotthisroom")
        return False
        
    return True
    
  def setPrices(self,var) :
    pservice = var[self.servicename]
    plist = var[self.pricelist]
    if plist != None and pservice != None :
      (price,pricechild,priceextra) = getPriceList(var,plist,pservice)
      (var[self.pop],var[self.poc],var[self.poe]) = (price,pricechild,priceextra)
      cutil.setCopy(var,[self.pop,self.poc,self.poe],self.li)
      
  def __calculate(self,var) :
    total = calculatePrice(var[self.perpers],var[self.nop],var[self.noc],var[self.noe],var[self.pop],var[self.poc],var[self.poe],var[self.priceroom])
    return total
      
  def calculatePrice(self,var) :
    total = self.__calculate(var)
    var["JVALBEFORE"] = var[self.total]
    var[self.total] = total
    cutil.setCopy(var,self.total,self.li)    
    cutil.modifDecimalFooter(var,self.li,self.total)
    
  def removePricesFromMap(self,var) :
    print var[self.noc]
    if var[self.noc] == 0 or var[self.noc] == None : var[self.poc] = None
    if var[self.noe] == 0 or var[self.noe] == None : var[self.poe] = None
    
  def initsum(self) :
    self.sum = 0

  def addsum(self,var) :
    total = self.__calculate(var)
    self.sum = con.addDecimal(self.sum,total)
    
  def tofooter(self,var) :
    cutil.setFooter(var,self.li,self.total,self.sum)
    
  def calculatePriceAterRemove(self,var) :
    cutil.removeDecimalFooter(var,self.li,self.total) 

def calculateJDates(arrival,departure,da):    
     if arrival == None : arrival = da
     elif da < arrival : arrival = da
     if departure == None : departure = da
     elif da > departure : departure = da
     return (arrival,departure)
    
def calculateDates(arrival,departure,l):    
     return calculateJDates(arrival,departure,con.toJDate(l.getServDate()))
    
def getReseDateS(var,sym) :
    """ Gather data related to reservation
    Args:
      var 
      sym : reservation symbol
    Returns:
     (arrival,departure,roomname,rate)
     arrival : arrival date, checkin
     departure : departure date, checkout, next day after last reservation date
     roomname : room reserved
     rate : daily rate    
    """
    li = getPayments(var,sym)
    roomname = li[0].getRoomName()
    rate = li[0].getPrice()
    arrival = None
    departure = None
    for l in li : (arrival,departure) = calculateDates(arrival,departure,l)
    return (arrival,con.incDays(departure),roomname,rate)     
    
def getReseDate(var,r) :
  return getReseDateS(var,r.getName())

# ----------------------

def refreshPanel(var) :
   var["JREFRESH_DATELINE_reservation"] = ""

def afterCheckIn(var) :
   if var["JUPDIALOG_BUTTON"] == "makecheckin" :
       util.RESOP(var).changeStatusToStay(getReseName(var))
       refreshPanel(var)
       var["JCLOSE_DIALOG"] = True        

import cutil
import con
import util
from com.gwthotel.hotel.reservationop import ResQuery
import xmlutil

def getReseName(var) :
  return var["resename"]

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

def countPayments(var,billName) :
  pList = util.PAYMENTOP(var).getPaymentsForBill(billName)
  suma = 0.0
  for p in pList :
    total = p.getPaymentTotal()
    suma = cutil.addDecimal(suma,con.BigDecimalToDecimal(total))
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

def setvarBefore(var,cust="cust_"):
    nop = None
    resdays = 1
    
    if var.has_key("JUPDIALOG_START") and var["JUPDIALOG_START"] != None :
      xml = var["JUPDIALOG_START"]
      m = {}
      (m,li) = xmlutil.toMap(xml)
      nop = m["nop"]
      var["JDATELINE_LINE"] = m["roomname"]
      var["JDATELINE_DATE"] = m["firstday"]
      resdays = m["nodays"]
      
    R = util.ROOMLIST(var)
    roomname = var["JDATELINE_LINE"]
    room = R.findElem(roomname)
    assert room != None
    if nop == None : nop = room.getNoPersons()
    var["name"] = roomname
    var["desc"] = room.getDescription()
    var["nop"] = nop
    var["noextrabeds"] = util.getIntField(room.getNoExtraBeds())
    var["nochildren"] = util.getIntField(room.getNoChildren())
    var["resnop"] = util.getIntField(nop)
    util.setCopy(var,["resename","name","datecol","desc","resdays","noextrabeds","nochildren","resnop","nop"])
    res = getReservForDay(var)
    if len(res) == 0 :
      date = var["JDATELINE_DATE"]
      var["datecol"] = date
      var["resdays"] = resdays
      var["resename"] = None
      util.setDefaCustomer(var,cust)
      return
  
    assert len(res) == 1
    resname = res[0].getResId()
    assert resname != None
    RFORM = util.RESFORM(var)
    reservation = RFORM.findElem(resname)
    assert reservation != None
    custname = reservation.getCustomerName()
    assert custname != None
    var["resename"] = resname
        
    util.setCustData(var,custname,cust)
    
    list = []
    sum = util.SUMBDECIMAL()
    S = util.SERVICES(var)
    mindate = None
    for r in reservation.getResDetail() :
                 
         map = { "avail" : True, "resroomname" : r.getRoomName(), "resday" : r.getResDate(), "rlist_pricetotal" : con.BigDecimalToDecimal(r.getPriceTotal()), 
              "rline_nop" : r.getNoP(),"rlist_priceperson" : con.BigDecimalToDecimal(r.getPrice()),
              "rlist_noc" : util.getIntField(r.getNoChildren()), "rlist_pricechildren" : con.BigDecimalToDecimal(r.getPriceChildren()),
              "rlist_noe" : util.getIntField(r.getNoExtraBeds()), "rlist_priceextra" : con.BigDecimalToDecimal(r.getPriceExtraBeds()),
              "rlist_pricelistperson" : con.BigDecimalToDecimal(r.getPriceList()), "rlist_pricelistchildren" : con.BigDecimalToDecimal(r.getPriceListChildren()), 
              "rlist_pricelistextrabeds" :  con.BigDecimalToDecimal(r.getPriceListExtraBeds()), "rlist_serviceperperson" : r.isPerperson(), 
              "rlist_roomservice" : r.getService(), "rlist_roompricelist" : r.getPriceListName()}
              
         if mindate == None : mindate = r.getResDate()
         elif mindate > r.getResDate() : mindate = r.getResDate()

         list.append(map)
         sum.add(r.getPrice())

    print mindate
    var["datecol"] = mindate
    var["resdays"] = len(reservation.getResDetail())
    cutil.setJMapList(var,"reslist",list)
    cutil.setFooter(var,"reslist","rlist_pricetotal",sum.sum)

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
  price = None
  pricechild = None
  priceextra = None
  if serv != None and pricelist != None :
    P = util.PRICEELEM(var)
    prices = P.getPricesForPriceList(pricelist)
    for s in prices :
      id = s.getService()
      if id == serv :
        price = s.getPrice()
        pricechild = s.getChildrenPrice()
        priceextra = s.getExtrabedsPrice()
  return (price,pricechild,priceextra)
  
def calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra) :  
  if perperson :
    price = con.mulIntDecimal(resnop,priceperson)
    price = con.addDecimal(price,con.mulIntDecimal(resnoc,pricechildren))
    price = con.addDecimal(price,con.mulIntDecimal(resextra,priceextra))
  else : price = priceroom    
  return price


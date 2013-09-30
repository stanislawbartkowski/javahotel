from com.gwthotel.hotel.server.service import H
from java.util import ArrayList
from java.util import Date
from java.util import Calendar
from java.math import BigDecimal
from com.gwthotel.hotel import HotelObjects
from com.gwthotel.hotel.reservationop import ResQuery
from com.gwthotel.hotel.reservation import ReservationPaymentDetail
import cutil
from com.gwthotel.hotel.reservation import ResStatus
from cutil import removeDuplicates
from com.gwthotel.hotel.stay import ResGuest
from cutil import createArrayList
from com.gwthotel.hotel import ServiceType
from com.gwthotel.hotel.services import HotelServices
from com.gwthotel.hotel.payment import PaymentBill

class MESS :

  def __init__(self):
      self.M = H.getM()

  def __call__(self,key) :
      return self.M.getMessN(key)
  
class HotelAdmin :
    
    def __init__(self,var) :
      self.adminI = H.getHotelAdmin()
      self.app = getAppId(var)
      
    def getListOfPersons(self) :
      return self.adminI.getListOfPersons(self.app)

    def getListOfHotels(self) :
      return self.adminI.getListOfHotels(self.app)

    def getListOfRolesForPerson(self,person):
      return self.adminI.getListOfRolesForPerson(self.app,person)
  
    def getListOfRolesForHotel(self,hotel):
      return self.adminI.getListOfRolesForHotel(self.app,hotel)
  
    def addOrModifHotel(self,hotel,roles):
      self.adminI.addOrModifHotel(self.app,hotel,roles)
      H.invalidateHotelCache()   

    def addOrModifPerson(self,person,roles):
      self.adminI.addOrModifPerson(self.app,person,roles)  


    def changePasswordForPerson(self,person,password):
      self.adminI.changePasswordForPerson(self.app,person,password)  

    def validatePasswordForPerson(self,person,password):
        return self.adminI.validatePasswordForPerson(self.app,person,password)        

    def getPassword(self,person):
        return self.adminI.getPassword(self.app,person)

    def clearAll(self):
        self.adminI.clearAll(self.app)

    def removePerson(self,person):
        self.adminI.removePerson(self.app,person)
        
    def removeHotel(self,hotel):
        self.adminI.removeHotel(self.app,hotel)
        H.invalidateHotelCache()         

class CRUDLIST :
    def __init__(self,var):
        self.serviceS = None
        self.var = var
        
    def getList(self):
        return self.serviceS.getList(getHotelName(self.var)) 
    
    def getModifiedList(self,f):
        li = self.getList()
        nli = createArrayList()
        for l in li :
            if not f(l) : continue
            nli.add(l)
        return nli    
    
    def addElem(self,elem):
        return self.serviceS.addElem(getHotelName(self.var),elem)
       
    def changeElem(self,elem):
        self.serviceS.changeElem(getHotelName(self.var),elem)
            
    def deleteElem(self,elem):
        self.serviceS.deleteElem(getHotelName(self.var),elem)
        
    def findElem(self,name):
        return self.serviceS.findElem(getHotelName(self.var),name)    
      
    def deleteElemByName(self,name) :
        elem = self.findElem(name)
        self.deleteElem(elem)

  
class SERVICES(CRUDLIST) :
    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getHotelServices()
        
    def getOtherServices(self):
        return self.getModifiedList(lambda e: e.getServiceType() == ServiceType.OTHER);                    

    def getRoomServices(self):
        return self.getModifiedList(lambda e: e.getServiceType() == ServiceType.HOTEL);
     
class PRICELIST(CRUDLIST) :

    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getHotelPriceList()
        
class CUSTOMERLIST(CRUDLIST) :

    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getHotelCustomers()

class BILLLIST(CRUDLIST) :

    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getCustomerBills()

                
class ROOMLIST(CRUDLIST):        
    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getHotelRooms()
  
    def setRoomServices(self,roomName,services):
        self.serviceS.setRoomServices(getHotelName(self.var),roomName,services)
        
    def getRoomServices(self,roomName):
        return self.serviceS.getRoomServices(getHotelName(self.var),roomName)
    
        
class PRICEELEM :
    
    def __init__(self,var) :
        self.var = var
        self.service = H.getHotelPriceElem() 
    
    def getPricesForPriceList(self,pricelist)  :
        return self.service.getPricesForPriceList(getHotelName(self.var),pricelist)
             
    def savePricesForPriceList(self,pricelist,list) :
        self.service.savePricesForPriceList(getHotelName(self.var),pricelist,list)
        
class PAYMENTOP :        

     def __init__(self,var):
         self.var = var
         self.service = H.getPaymentsOp()

     def getPaymentsForBill(self,billName) :
         return self.service.getPaymentsForBill(getHotelName(self.var),billName)
       
     def addPaymentForBill(self,billName,p) :
         self.service.addPaymentForBill(getHotelName(self.var),billName,p)
         
     def removePaymentForBill(self,billName,id) :
         self.service.removePaymentForBill(getHotelName(self.var),billName,id)
        
class RESOP :
     def __init__(self,var):
         self.var = var
         self.service = H.getResOp()
         
     def queryReservation(self,query):
         return self.service.queryReservation(getHotelName(self.var),query)
     
     def changeStatus(self,resId,status):
         self.service.changeStatus(getHotelName(self.var),resId,status)
         
     def changeStatusToStay(self,resId):
         self.changeStatus(resId,ResStatus.STAY)    

     def changeStatusToCancel(self,resId):
         self.changeStatus(resId,ResStatus.CANCEL)    

     def changeStatusToReserv(self,resId):
         self.changeStatus(resId,ResStatus.OPEN)    
         
     def setResGuestList(self,resId,list):
         self.service.setResGuestList(getHotelName(self.var),resId,list)
         
     def getResGuestList(self,resId):
         return self.service.getResGuestList(getHotelName(self.var),resId)
       
     def addResAddPayment(self,resId,a) :  
         self.service.addResAddPayment(getHotelName(self.var),resId,a)
         
     def getResAddPaymentList(self,resId) :
         return self.service.getResAddPaymentList(getHotelName(self.var),resId)
       
     def findBillsForReservation(self,resId) :
         return self.service.findBillsForReservation(getHotelName(self.var),resId)
       
class RESFORM(CRUDLIST) :

    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = H.getResForm()
                
                
def isRoomService(service) :
  return service == ServiceType.HOTEL
        
def resStatus(rform):
    status = rform.getStatus()
    if status == ResStatus.STAY : return 1
    return 0       
                
def xmlToVar(var,xml,list,pre=None) :
    iXML = H.getXMLMap()
    prop = iXML.readXML(xml,"root","elem")
    for l in list :
        val = prop.getAttr(l)
        if pre : k = pre + l
        else : k = l
        var[k] = val
        
def mapToXML(map,list,pre=None):
    s = "<root><elem>"
    for e in list :
        val = ""
        if pre : k = pre + e
        else : k = e
        if map.has_key(k) :
            if map[k] : val = str(map[k])
        s = s + "<" + e + ">" + val + "</" + e + ">"
    s = s + "</elem></root>"
    return s 
  
def listNumberToCVS(li) :  
  s = None
  for l in li :
    vals = str(l)
    if s : s = s + "," + vals
    else : s = vals
  if s : return s
  return ""

def CVSToListNumber(s) :
  if s == None : return []
  li = []
  for n in s.split(",") :
    num = long(n)
    li.append(num)
  return li  
  
def printvar(method,action,var): 
    cutil.printVar(method,action,var)
#  return  
 
def createArrayList() :
  return ArrayList()   
  
def getHotelName(var):
    """ Get com.gwthotel.admin.HotelId class for current session
    
    Args: var
      
    Returns:
      com.gwthotel.admin.HotelId clas
    """
    token = var["SECURITY_TOKEN"]
    return H.getHotelName(token)

def getHotel(var) :
    """ Get hotel name for current session
    Args: var
    
    Returns: hotel name
    """
    return getHotelName(var).getHotel()

def getAppId(var):
    token = var["SECURITY_TOKEN"]
    return H.getInstanceId(token)

def copyNameDescr(desc,var):
    desc.setName(var["name"])
    desc.setDescription(var["descr"])
    
def toVarNameDesc(var,sou):
    var["name"] = sou.getName()
    var["descr"] = sou.getDescription()    
      
def findElemInSeq(pname,seq):
    for s in seq : 
       name = s.getName()
       if name == pname : return s
    return None  
          

def toDate(value):
    return cutil.toDate(value)

# d1 : datetime
# d2 : java.util.Date
def eqDate(d1,d2):
    return cutil.eqDate(d1,d2)

def toB(value):
    return cutil.toB(value)

def createSeq(list,addName=False):    
    seq = []
    for s in list :
        m = {}
        m["id"] = s.getName()
        if addName :  m["displayname"] = s.getName() + " " + s.getDescription()
        else : m["displayname"] = s.getDescription()
        seq.append(m)
    return seq    

def toVar(var,sou,list):
    for s in list :
        var[s] = sou.getAttr(s)
        
def toP(dest,var,list):
    for s in list :
        dest.setAttr(s,var[s])   
        
def duplicatedName(var,S,duplicateM):    
    seq = S.getList()
    if findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = duplicatedM
      return True
    return False

def createResQueryElem(roomname,dfrom,dto=None):
    if dto == None : dto = dfrom
    q = ResQuery()
    q.setFromRes(toDate(dfrom))
    q.setToRes(toDate(dto))
    q.setRoomName(roomname)
    return q

def createResFormElem(roomname,service,date,nop,price):
    r = ReservationPaymentDetail()
    r.setRoomName(roomname)
    r.setNoP(nop)
    r.setPrice(price)
    r.setService(service)
    r.setResDate(toDate(date))
    return r

class ConstructObject :
    
    def __init__(self,var):
        self.factory = H.getObjectFactory()
        self.hotel = getHotelName(var)
               
    def getO(self,t) :
        oType = None
        if t == 0 : oType = HotelObjects.CUSTOMER
        if t == 1 : oType = HotelObjects.RESERVATION
        if t == 2 : oType = HotelObjects.BILL
        return self.factory.construct(self.hotel,oType)
    
def newCustomer(var) :
    c = ConstructObject(var)
    return c.getO(0)  

def newResForm(var):
    c = ConstructObject(var)
    return c.getO(1)   
  
def newBill(var) :
    c = ConstructObject(var)
    return c.getO(2)  
  
def newBillPayment() :
   return PaymentBill()

def newResGuest(var):
    return ResGuest()

def newOtherService(var):
    se = HotelServices()
    se.setServiceType(ServiceType.OTHER)
    se.setNoPersons(-1)
    return se

def newHotelService(var):
    return HotelServices()
  
def newResAddPayment() :
    return ReservationPaymentDetail()
  
def setCopy(var,li) :
  cutil.setCopy(var,li)  
    
def getCustFieldId():
    return ["firstname","companyname","street","zipcode","email","phone"]
    
def createCustomerList(var):
    C = CUSTOMERLIST(var)
    CLIST = getCustFieldId()
    seq = []
    for c in C.getList() :
        v = {}
        toVarNameDesc(v,c)
        toVar(v,c,CLIST)
        seq.append(v)
    return seq    
    
def getServicesForRoom(var,room):
  RO = ROOMLIST(var)
  services = RO.getRoomServices(room)
  if len(services) == 0 : return None
  PR = PRICELIST(var)
  PE = PRICEELEM(var)
  pList = PR.getList()
  li = []
  liList = []
  for p in pList :
      prList = PE.getPricesForPriceList(p.getName())
      for price in prList :
          service = price.getService()
          prWE = price.getWeekendPrice()
          prWO = price.getWorkingPrice()
          if prWE == None or prWO == None : continue
          for s in services :
              if s.getName() == service : 
                li.append(s) 
                liList.append(p.getName())
  if len(li) == 0 : return None
  f = lambda(e) : e[0] == e[1]
  liList = removeDuplicates(liList,f)
  return [li,liList]
  
  
def createEnumFromList(li, f = lambda elem : [elem.getName(), elem.getDescription()]):
    seq= []
    for elem in li :
        e = f(elem)
        id = e[0]
        name = e[1]
        if emptyS(name): name = id
        seq.append({"id" : id, "displayname" : name })
    return seq

def emptyS(name):
    return name == None or name == ""

def isResOpen(res):
    return res.getStatus() == ResStatus.OPEN

class SUMBDECIMAL :
    
    def __init__(self):
        self.sum = 0.0
       
    def add(self,b):
        if b : self.sum = self.sum + b.floatValue()    
                
def duplicateService(var):    
    serv = SERVICES(var)
    seq = serv.getList()
    if findElemInSeq(var["name"],seq) != None :
      M = MESS()
      var["JERROR_name"] = M("DUPLICATEDSERVICENAME")
      return True
    return False

def getVatName(vat):
  taxList = H.getVatTaxes()
  list = taxList.getList()
  vat = findElemInSeq(vat,list)
  if vat == None : return ""
  return vat.getDescription()

def getPriceForPriceList(var,pricelist,service) :
  PE = PRICEELEM(var)
  peList = PE.getPricesForPriceList(pricelist)
  pPrice = None
  for p in peList :
      if service == p.getService() :
          pPrice = p
          break
  return pPrice   

def getReseName(var) :
  return var["resename"]

def setCustData(var,custname,prefix) :
    CU = CUSTOMERLIST(var)
    customer = CU.findElem(custname)
    assert customer != None
    li = []
    for s in getCustFieldId() :
      deid = prefix + s
      li.append(deid)
      val = customer.getAttr(s)
      var[deid] = val
    setCopy(var,li)
    setCopy(var,[prefix+"name",prefix+"descr"])
    var[prefix+"name"] = customer.getName()
    var[prefix+"descr"] = customer.getDescription()

def getReservForDay(var):
   R = RESOP(var)
   room = var["JDATELINE_LINE"]
   day = var["JDATELINE_DATE"]
   query=createArrayList()
   q = createResQueryElem(room,day)
   query.add(q)
   res = R.queryReservation(query)
   return res

def showCustomerDetails(var,custid):
    var["JUP_DIALOG"] = "hotel/reservation/showcustomerdetails.xml"
    print "details",custid
    var["JUPDIALOG_START"] = custid
    
def getPayments(var) :    
  rese = getReseName(var)
  pli = RESOP(var).getResAddPaymentList(rese)
  R = RESFORM(var)
  # java.util.List
  pli.addAll(R.findElem(rese).getResDetail())
  return pli

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
    if isRoomService(se) : resdate = r.getResDate()
    else : servdate = r.getResDate()
    total = r.getPriceTotal()
    self.sumf = cutil.addDecimal(self.sumf,cutil.BigDecimalToDecimal(total))
    guest = r.getGuestName()
    room = r.getRoomName()
    service = r.getService()
    ma1 = { "idp" : idp, "room" : room, "resday" : resdate, "service" : service, "servday":servdate, "servdescr" : r.getDescription(),"guest" : guest, "total" : total }
    ma.update(ma1)
    self.li.append(ma)
    
  def close(self) :
    cutil.setJMapList(self.var,self.liname,self.li)
    cutil.setFooter(self.var,self.liname,"total",self.sumf)

class HOTELTRANSACTION(cutil.SEMTRANSACTION) :
  
    def __init__(self,semid,var) :
      semname = None
      if semid == 0 : semname = "HOTELBILLSAVE"
      cutil.SEMTRANSACTION.__init__(self,semname,var)


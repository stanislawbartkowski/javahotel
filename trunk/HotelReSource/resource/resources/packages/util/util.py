from com.gwthotel.hotel.server.service import H
from java.util import ArrayList
from java.util import Date
from java.util import Calendar
from java.math import BigDecimal
from com.gwthotel.hotel import HotelObjects
from com.gwthotel.hotel.reservation import ReservationPaymentDetail
import cutil
from com.gwthotel.hotel.reservation import ResStatus
from cutil import removeDuplicates
from com.gwthotel.hotel.stay import ResGuest
from cutil import createArrayList
from com.gwthotel.hotel import ServiceType
from com.gwthotel.hotel.services import HotelServices
from com.gwthotel.hotel.payment import PaymentBill
from com.gwthotel.hotel import HUtils
import rutil
from com.gwthotel.hotel.rooms import HotelRoom
from com.gwthotel.shared import IHotelConsts
import xmlutil

def setIntField(var,key,setF) :
  if var[key] == None : setF(IHotelConsts.PERSONIDNO)
  else : setF(var[key])
  
def getIntField(val) :
  if val == IHotelConsts.PERSONIDNO : return None
  return val

class HOTELDEFADATA(cutil.DEFAULTDATA) :
  
  def __init__(self) :
    cutil.DEFAULTDATA.__init__(self)
    
  def __getV(self,what) :
    
    if what == 0 : return "defsex"
    elif what == 1 : return "defcountry"
    elif what == 2 : return "defidcard"
    elif what == 3 : return "defpayment"
    
    elif what == 10 : return "lastnopersons"
    elif what == 11 : return "lastnoextrabeds"
    elif what == 12 : return "lastnochildren"
    elif what == 13 : return "lastperperson"
    elif what == 14 : return "lastvatused"
    elif what == 15 : return "lastvatusedextra"
    
    elif what == 20 : return "lastroomservices"
    elif what == 21 : return "lastroomnopersons"
    elif what == 22 : return "lastroomnochildren"
    elif what == 23 : return "lastroomnoextrabeds"
    
  def getDataH(self,what,defa=None) :
    return self.getData(self.__getV(what),defa)
  
  def putDataH(self,what,value) :
    self.putData(self.__getV(what),value)

  def getDataHI(self,what) :
    return self.getDataI(self.__getV(what))
  
  def putDataHI(self,what,v) :
    self.putDataI(self.__getV(what),v)
    
  def getDataHB(self,what) :
    return self.getDataB(self.__getV(what)) 
    
  def putDataHB(self,what,val) :
    self.putDataB(self.__getV(what),val)

class MESS :

  def __init__(self):
      self.M = H.getM()

  def __call__(self,key) :
      return self.M.getMessN(key)
    
def clearHotel(var,hotel) :
   iGet = H.getInstanceObjectId()
   aid = getAppId(var)
   instance = aid.getInstanceName()
   hi = iGet.getOObject(instance, hotel, aid.getPerson());
   H.getClearHotel().clearObjects(hi)

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
     
     def searchReservation(self,query):
         return self.service.searchReservation(getHotelName(self.var),query)
       
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
    return getHotelName(var).getObject()

def getPerson(var):
    token = var["SECURITY_TOKEN"]
    return H.getInstanceId(token).getPerson()

def getAppId(var):
    token = var["SECURITY_TOKEN"]
    return H.getInstanceId(token)

def __toV(s,prefix) :
  if prefix : return prefix + s
  return s

def copyNameDescr(desc,var,prefix=None):
    desc.setName(var[__toV("name",prefix)])
    desc.setDescription(var[__toV("descr",prefix)])
    
def toVarNameDesc(var,sou,prefix=None):
    var[__toV("name",prefix)] = sou.getName()
    var[__toV("descr",prefix)] = sou.getDescription()    
      
def findElemInSeq(pname,seq, getN = None):
    if getN == None : getN = lambda s : s.getName()
    for s in seq : 
       name = getN(s)
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

def createSeq(list,addName=False, displayname=None):    
    seq = []
    for s in list :
        m = {}
        m["id"] = s.getName()
        if displayname : m["displayname"] = displayname(s)
        else :
          if s.getDescription() == None : m["displayname"] = m["id"]
          else :
            if addName :  m["displayname"] = s.getName() + " " + s.getDescription()
            else : m["displayname"] = s.getDescription()
        seq.append(m)
    return seq    

def toVar(var,sou,list,prefix=None):
    for s in list :
        var[__toV(s,prefix)] = sou.getAttr(s)
        
def toP(dest,var,list,prefix=None):
    for s in list :
        dest.setAttr(s,var[__toV(s,prefix)])   
        
def duplicatedName(var,S,duplicateM):    
    seq = S.getList()
    if findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = duplicatedM
      return True
    return False

class ConstructObject :
    
    def __init__(self,var):
        self.factory = H.getObjectFactory()
        self.hotel = getHotelName(var)
               
    def getO(self,t) :
        oType = None
        if t == 0 : oType = HotelObjects.CUSTOMER
        if t == 1 : oType = HotelObjects.RESERVATION
        if t == 2 : oType = HotelObjects.BILL
        o = self.factory.construct(self.hotel,oType)
        o.setGensymbol(True)
        return o
    
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

def newHotelRoom() :
    return HotelRoom()

def newHotelService(var):
    return HotelServices()
  
def newResAddPayment() :
    return ReservationPaymentDetail()
  
def setCopy(var,li) :
  cutil.setCopy(var,li)  
        
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
          pr = price.getPrice()
          if pr == None : continue
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
        if b : 
          if type(b) == BigDecimal : self.sum = self.sum + b.floatValue()    
          else : self.sum = self.sum + b
                
def duplicateService(var):    
    serv = SERVICES(var)
    seq = serv.getList()
    if findElemInSeq(var["name"],seq) != None :
      M = MESS()
      var["JERROR_name"] = M("DUPLICATEDSERVICENAME")
      return True
    return False

def getVatName(vat):
  list = cutil.getDict("vat")
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
#  return var["resename"]
  return rutil.getReseName(var)
    
def getPayments(var) :    
  return rutil.getPayments(var)

class HOTELTRANSACTION(cutil.SEMTRANSACTION) :
  
    def __init__(self,semid,var) :
      semname = None
      if semid == 0 : semname = "HOTELBILLSAVE"
      if semid == 1 : semname = "HOTELMAKERESERVATION"
      if semid == 2 : semname = "HOTELCHECKIN"
      cutil.SEMTRANSACTION.__init__(self,semname,var)

# ---------- CUSTOMER ---------

def newCustomer(var) :
    c = ConstructObject(var)
    return c.getO(0)  

def __toS(ch) :
   print ch
   return ch
 
def __toCh(s) :
   return s[0]

def customerDataFromVar(c,var,prefix=None) :
    copyNameDescr(c,var,prefix)
    toP(c,var,getCustFieldId(),prefix)
    c.setSex(__toCh(var[__toV("title",prefix)]))
    c.setDoctype(__toCh(var[__toV("doctype",prefix)]))

def customerFromVar(var,prefix=None) :
    c = newCustomer(var)
    customerDataFromVar(c,var,prefix)
    return c
  
def customerToVar(v,c,prefix=None) :
    toVarNameDesc(v,c,prefix)
    toVar(v,c,getCustFieldId(),prefix)
    v[__toV("title",prefix)] = __toS(c.getSex())
    v[__toV("doctype",prefix)] = __toS(c.getDoctype())

def getCustFieldId():
  sL = HUtils.getCustomerFields()
  seq = []
  for s in sL : seq.append(s)
  return seq

def getCustFieldIdAll() :
  l = getCustFieldId() + ["title","doctype","name","descr"]
  return l

def getCustFieldIdWithout() :
  l = getCustFieldId() + ["name","descr"]
  l.remove("country")
  return l  
    
def createCustomerList(var):
    C = CUSTOMERLIST(var)
    CLIST = getCustFieldId()
    seq = []
    for c in C.getList() :
        v = {}
        customerToVar(v,c)
        seq.append(v)
    return seq
  
def toCustomerVar(var,c,prefix,clist = ["name","surname","firstname"]) :
    toVar(var,c,clist,prefix)

def setCustVarCopy(var,prefix) :
    cutil.setCopy(var,getCustFieldIdAll(),None,prefix)

def setCustData(var,custname,prefix=None) :
    CU = CUSTOMERLIST(var)
    customer = CU.findElem(custname)
    assert customer != None
    customerToVar(var,customer,prefix)
    setCustVarCopy(var,prefix)

def showCustomerDetails(var,custid):
    var["JUP_DIALOG"] = "hotel/reservation/showcustomerdetails.xml"
#    print "details",custid
    var["JUPDIALOG_START"] = custid

def setDefaCustomerNotCopy(var,prefix=None) :
   D = HOTELDEFADATA()
   title = D.getDataH(0)
   country = D.getDataH(1)
   doctype = D.getDataH(2)
   var[__toV("title",prefix)] = title
   var[__toV("country",prefix)] = country
   var[__toV("doctype",prefix)] = doctype

def setDefaCustomer(var,prefix=None) :
   setDefaCustomerNotCopy(var,prefix)
   cutil.setCopy(var,["title","country","doctype"],None,prefix)
   
def saveDefaCustomer(var,prefix=None) :
   D = HOTELDEFADATA()
   title = var[__toV("title",prefix)]
   country = var[__toV("country",prefix)]
   doctype = var[__toV("doctype",prefix)]
   D.putDataH(0,title)
   D.putDataH(1,country)
   D.putDataH(2,doctype)
  
# ------------------------------

def xmlToVar(var,xml,list,pre=None) :
  xmlutil.xmlToVar(var,xml,list,pre)
        
def mapToXML(map,list=None,pre=None):
  return xmlutil.mapToXML(map,list,pre) 

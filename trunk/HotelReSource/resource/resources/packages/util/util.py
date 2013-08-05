from com.gwthotel.hotel.server.service import H
from java.util import ArrayList
from java.util import Date
from java.util import Calendar
from java.math import BigDecimal
from com.gwthotel.hotel import HotelObjects
from com.gwthotel.hotel.reservationop import ResQuery
from com.gwthotel.hotel.reservation import ReservationDetail
import cutil

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
  
class SERVICES :
    def __init__(self,var):
        self.serviceS = H.getHotelServices()
        self.var = var
        
    def getList(self):
        return self.serviceS.getList(getHotelName(self.var))    
    
    def addElem(self,elem):
        return self.serviceS.addElem(getHotelName(self.var),elem)
       
    def changeElem(self,elem):
        self.serviceS.changeElem(getHotelName(self.var),elem)
            
    def deleteElem(self,elem):
        self.serviceS.deleteElem(getHotelName(self.var),elem)
        
    def findElem(self,name):
        return self.serviceS.findElem(getHotelName(self.var),name)    
            
  
class PRICELIST(SERVICES) :

    def __init__(self,var):
        SERVICES.__init__(self,var)
        self.serviceS = H.getHotelPriceList()
        
class CUSTOMERLIST(SERVICES) :

    def __init__(self,var):
        SERVICES.__init__(self,var)
        self.serviceS = H.getHotelCustomers()
        
        
class ROOMLIST(SERVICES):        
    def __init__(self,var):
        SERVICES.__init__(self,var)
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
        
class RESOP :
     def __init__(self,var):
         self.var = var
         self.service = H.getResOp()
         
     def queryReservation(self,query):
         return self.service.queryReservation(getHotelName(self.var),query)
     
     def changeStatus(self,resId,status):
         self.service.changeStatus(getHotelName(self.var),resId,status)
         
     def setResGuestList(self,resId,list):
         self.service.setResGuestList(getHotelName(self.var),resId,list)
         
     def getResGuestList(self,resId):
         return self.service.getResGuestList(getHotelName(self.var),resId)
         
         
class RESFORM(SERVICES) :

    def __init__(self,var):
        SERVICES.__init__(self,var)
        self.serviceS = H.getResForm()
  
def printvar(method,action,var): 
    cutil.printVar(method,action,var)
#  return  
 
def createArrayList() :
  return ArrayList()   
  
def getHotelName(var):
    token = var["SECURITY_TOKEN"]
    return H.getHotelName(token)

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
    dd1 = toDate(d1)
    return dd1.equals(d2)

def toB(value,afterdot=2):
    if value == None : return None
    b = BigDecimal(value)
    return b

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
    r = ReservationDetail()
    r.setRoom(roomname)
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
        return self.factory.construct(self.hotel,oType)
    
def newCustomer(var) :
    c = ConstructObject(var)
    return c.getO(0)  

def newResForm(var):
    c = ConstructObject(var)
    return c.getO(1)   

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
  return [li,liList]
  
  
def createEnumFromList(li, f):
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

class SUMBDECIMAL :
    
    def __init__(self):
        self.sum = 0.0
       
    def add(self,b):
        if b : self.sum = self.sum + b.floatValue()    
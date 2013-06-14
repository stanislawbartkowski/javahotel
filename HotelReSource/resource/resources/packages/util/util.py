from com.gwthotel.hotel.server.service import H
from java.util import ArrayList
from java.util import Date
from java.math import BigDecimal

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
        self.serviceS.addElem(getHotelName(self.var),elem)
       
    def changeElem(self,elem):
        self.serviceS.changeElem(getHotelName(self.var),elem)
            
    def deleteElem(self,elem):
        self.serviceS.deleteElem(getHotelName(self.var),elem)
  
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
  
def printvar(method,action,var): 
  return  
  print method, action
  for k in var.keys() : 
    print k, var[k]
 
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
    if value == None : return None
    d = Date()
    d.setYear(value.year - 1900)
    d.setMonth(value.month-1)
    d.setDate(value.day)
    return d

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
        
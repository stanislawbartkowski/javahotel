from com.gwthotel.hotel.server.service import H
from java.util import ArrayList
from java.util import Date
from java.math import BigDecimal

class MESS :

  def __init__(self):
      self.M = H.getM()

  def __call__(self,key) :
      return self.M.getMessN(key)
  
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

def copyNameDescr(desc,var):
    desc.setName(var["name"])
    desc.setDescription(var["descr"])
      
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
    

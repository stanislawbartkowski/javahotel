from com.jython.ui.server.guice import ServiceInjector
import datetime
import time
from java.util import Calendar
from cutil import toDate
from cutil import modifDecimalFooter
from cutil import setStandEditMode
from cutil import StorageRegistry
from cutil import printVar
from cutil import addDecimal

class Registry(StorageRegistry) :
     
     def __init__(self): 
        StorageRegistry.__init__(self,ServiceInjector.getStorageRegistryFactory(),"PAYMENT")
              
     def getListOfEntries(self):
         l = self.getKeys();
         li = []
         for key in l :
             val = self.getEntryS(key)
             da = self.__tod(val)
             li.append((val,da))
         return li  
     
     def __tostr(self,da):
         return da.strftime("%Y-%m-%d")
     
     def __tod(self,s):
             ti = time.strptime(s,"%Y-%m-%d")
             da = datetime.date(ti.tm_year,ti.tm_mon,ti.tm_mday)
             return da
    
     def __getV(self,s):
         V = StorageRegistry(ServiceInjector.getStorageRegistryFactory(),"PAYMENT-" + s)
         return V
     
     def addList(self,da,li):
         s = self.__tostr(da)
         self.putEntry(s,s)
         V = self.__getV(s)
         for d in li :
             da = d["date"]
             pa = d["pay"]
             s = None
             if pa : s = str(pa)
             V.putEntry(self.__tostr(da),s)
             
     def changeList(self,da,li):
         self.addList(da,li)
         
     def removeList(self,da):        
        if da == None : return
        key = self.__tostr(da)
        self.removeEntry(key)
        V = self.__getV(key)
        for k in V.getKeys() :
            V.removeEntry(k)
        
             
     def getList(self,da):
        if da == None : return []
        key = self.__tostr(da)
        val = self.getEntryS(key)
        if val == None : return []
        li = []
        V = self.__getV(key)
        for k in V.getKeys() :
            pa = V.getEntryS(k)
            date = self.__tod(k)
            b = None
            if pa != None and pa != "" : 
                 b = float(pa)
            li.append({"date" : date, "pay" : b })
        return li    
         
R = Registry()    
YEAR=2013     
         
def __getListOfWeeks(var):
    list = []
    for da in R.getListOfEntries() :
        elem = {"key" : da[0], "week" : da[1]}
        list.append(elem)
    
    var["JLIST_MAP"] = { "listweek" : list}

def dialogaction(action,var) :

  print action
  for k in var.keys() : 
    print k
    print var[k]
 
  if action == "before" or action == "crud_readlist":
      __getListOfWeeks(var)
                          
def elemaction(action,var):      
      
  printVar("elem action",action,var)    
      
  if action == "before" :
      da = var["week"]
      li = R.getList(da)
      var["JLIST_MAP"]  = { "listpay" : li}
      sum = None 
      for l in li :
          b = l["pay"]
          sum = addDecimal(sum,b)
              
      var["JFOOTER_COPY_listpay_pay"] = True
      var["JFOOTER_listpay_pay"] = sum
      if var["JCRUD_DIALOG"] != "crud_remove" :
        setStandEditMode(var,"listpay",["pay"])
  
  if action == "signalchange" and var["JCRUD_DIALOG"] == "crud_add":
    da = var["week"]
    if da == None : return
    if da.weekday() != 0 :
        var["JERROR_week"] = "Date should be the Monday"
        return
    li = R.getListOfEntries()
    for l in li :
        if l[1] == da :
            var["JERROR_week"] = "Already used"
            return
    list = []
    td = datetime.timedelta(1)

    for i in range(5) :
       list.append({"date" : da, "pay" : None})     
       da = da + td
    var["JLIST_MAP"]  = { "listpay" : list}
    setStandEditMode(var,"listpay",["pay"])
    var["JFOOTER_COPY_listpay_pay"] = True
    var["JFOOTER_listpay_pay"] = None
    
  if action == "columnchangeaction" :
     modifDecimalFooter(var,"listpay","pay")
     
  if action == "crud_add" :
      da = var["week"]
      R.addList(da, var["JLIST_MAP"]["listpay"])
      var["JCLOSE_DIALOG"] = True    
      
  if action == "crud_change" :
      da = var["week"]
      R.changeList(da, var["JLIST_MAP"]["listpay"])
      var["JCLOSE_DIALOG"] = True    

  if action == "crud_remove" :
      da = var["week"]
      R.removeList(da)
      var["JCLOSE_DIALOG"] = True    

def weekhelper(action,var):
    
    if action == "select" and var["list_lineset"] :
      var["week"] = var["weekbeg"]
      var['JCOPY_week'] = True
      var["JCLOSE_DIALOG"] = True
    
    if action == "before" :
        da = datetime.date(YEAR,1,1)
        td = datetime.timedelta(1)
        while da.weekday() != 0 :
            da = da - td
        list= []
        while True :
            if da.weekday() == 0 : 
                list.append({"weekbeg" : toDate(da)})
                if da.year > YEAR : break
            da = da + td
        var["JLIST_MAP"] = {"list" : list}  
            
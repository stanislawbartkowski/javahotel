from com.jython.ui.server.guice import ServiceInjector
import datetime
import time
from java.util import Calendar
from cutil import toDate
from cutil import modifDecimalFooter
from cutil import setStandEditMode
from cutil import StorageRegistry

class Registry(StorageRegistry) :
     
     def __init__(self):
        StorageRegistry.__init__(self,ServiceInjector.getStorageRegistryFactory(),"PAYMENT")
              
     def getListOfEntries(self):
         l = self.getKeys();
         li = []
         for key in l :
             val = self.__getEntry(key)
             ti = time.strptime(val,"%y-%m-%d")
             da = datetime.date(ti.tm_year,tm_mon,tm_mday)
             l.append((val,da))
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
 
  if action == "before" :
      __getListOfWeeks(var)
                          
def elemaction(action,var):      
      
  if action == "before" :
      var["JLIST_MAP"]  = { "listpay" : []}
#      var["JLIST_EDIT_listpay_MODE"] = "NORMALMODE"
#          var["JLIST_EDIT_list_MODE"] = "CHANGEMODE" 
 
  if action == "signalchange" :
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
#    var["JLIST_EDIT_listpay_pay"] = ""
#    var["JLIST_EDIT_listpay_MODE"] = "NORMALMODE"
    setStandEditMode(var,"listpay",["pay"])
    var["JFOOTER_COPY_listpay_pay"] = True
    var["JFOOTER_listpay_pay"] = None
    
  if action == "columnchangeaction" :
#      currentfooter = var["JFOOTER_listpay_pay"]
#      prevval = var["JVALBEFORE"]
#      val = var["pay"]
#      if currentfooter == None : currentfooter = val
     modifDecimalFooter(var,"listpay","pay")
    

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
            
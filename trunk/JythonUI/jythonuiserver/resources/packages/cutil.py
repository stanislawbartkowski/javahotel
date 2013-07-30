from java.util import Calendar
from org.python.core.util import StringUtil

def toDate(value):
    if value == None : return None
    ca = Calendar.getInstance()
    ca.clear()
    ca.set(value.year,value.month - 1,value.day)
    return ca.getTime()

def setCopy(var,li) :
  for l in li :
    var["JCOPY_"+l] = True

def addDecimal(sum1,sum2,afterdot=2):
   if sum1 == None : return sum2
   if sum2 == None : return sum1
   return sum1 + sum2

def minusDecimal(sum1,sum2,afterdot=2):
   if sum1 == None : 
       if sum2 == None : return None
       return 0 - sum2
   if sum2 == None : return sum1
   return sum1 - sum2


def modifDecimalFooter(var,list,name):
    footerdef = "JFOOTER_"+list+"_"+name
    currentfooter = var[footerdef]
    prevval = var["JVALBEFORE"]
    val = var[name]
    currentfooter = minusDecimal(currentfooter,prevval)
    currentfooter = addDecimal(currentfooter,val)
    var[footerdef] = currentfooter
    var["JFOOTER_COPY_"+list+"_"+name] = True

def __defineEditMode(var,list,li,mode):      
    for l in li :
      var["JLIST_EDIT_"+list+"_"+l] = ""
    var["JLIST_EDIT_"+list+"_MODE"] = mode
    
def setStandEditMode(var,list,li):
    __defineEditMode(var,list,li,"NORMALMODE")
    
def setAddEditMode(var,list,li):
    __defineEditMode(var,list,li,"CHANGEMODE")  

class StorageRegistry() :
     
     def __init__(self,fa,realm):
         self.r = fa.construct(realm)
         
     def putEntry(self,key,value):
         self.r.putEntry(key,value)
         
     def getEntry(self,key):
         return self.r.getEntry(key)
     
     def getEntryS(self,key):
         val = self.getEntry(key)
         if val == None : return None
         return StringUtil.fromBytes(val)
         
     def removeEntry(self,key):
         self.r.removeEntry(key)    
         
     def getKeys(self):
         return self.r.getKeys()    

def printVar(name,action,var):
  print "================ " + name
  print "action = " + action
  for k in var.keys() : 
    print k + " = " + str(var[k])

from java.util import Calendar
from org.python.core.util import StringUtil
import datetime
import time
from java.util import ArrayList

def createArrayList() :
  return ArrayList()   

def copyVarToProp(var,prop,list):
    for l in list :
        val = var[l]
        prop.setAttr(l,val)

def toDate(value):
    if value == None : return None
    ca = Calendar.getInstance()
    ca.clear()
    ca.set(value.year,value.month - 1,value.day)
    return ca.getTime()

def setCopy(var,li, list=None,prefix=None) :
  for l in li :
    if list : c = "JROWCOPY_" + list + "_"  
    else : c = "JCOPY_"
    if prefix : k = prefix + l
    else : k = l
    var[c+k] = True

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

class RegistryFile:
    
    def __init__(self,fa,realM,seqGen,map, listid,id):
        self.__fa = fa
        self.__realM = realM
        self.__seqGen = seqGen
        self.__key = "GEN_KEY";
        self.__map = map
        self.__listid = listid
        self.__id = id
        
    def nextKey(self):
        key = self.__seqGen.genNext(self.__realM, self.__key)
        return key 
    
    def __getR(self):
        return StorageRegistry(self.__fa,self.__realM)
    
    def __getRR(self,k):
        return StorageRegistry(self.__fa,self.__realM + "---" + k)
    
    def __tostr(self,da):
         return da.strftime("%Y-%m-%d")
     
    def __tod(self,s):
             ti = time.strptime(s,"%Y-%m-%d")
             da = datetime.date(ti.tm_year,ti.tm_mon,ti.tm_mday)
             return da
         
    def __getMapRR(self,k):
        RR = self.__getRR(k)
        elem = {}
        for id in self.__map.keys() :
            type = self.__map[id]
            val = RR.getEntryS(id)
            if val == None: valt = None 
            else :
               if type == "long" :
                   valt = long(val)
               elif type == "date" :
                   valt = self.__tod(val)
               else : valt = val
            elem[id] = valt
        print elem    
        return elem           
                       
    
    def readList(self,var):
        R = self.__getR()
        l = R.getKeys()
        li = []
        for k in l : 
            if k != self.__key : li.append(self.__getMapRR(k))
        if var.has_key("JLIST_MAP") : var["JLIST_MAP"][self.__listid] = li
        else: var["JLIST_MAP"] = { self.__listid : li }
        
    def __removeEntries(self,R):
        for key in R.getKeys() :
            R.removeEntry(key)                
        
    def addMap(self,var): 
        id = var[self.__id]
        if id == None : return
        k = str(id)
        R = self.__getR()
        RR = self.__getRR(k)
        self.__removeEntries(RR)
            
        for id in self.__map.keys() :
            type = self.__map[id]
            if var.has_key(id) :
                val = var[id]
                if type == "long" :
                    vals = str(val)
                elif type == "date" :
                    vals = self.__tostr(val)
                else : vals = val
                RR.putEntry(id,vals)
        R.putEntry(k,"")
        
    def removeMap(self,var):
        id = var[self.__id]
        if id == None : return
        k = str(id)
        R = self.__getR()
        RR = self.__getRR(k)
        for key in RR.getKeys() :
            RR.removeEntry(key)
        R.removeEntry(k)    
        
    def removeAll(self):
        R = self.__getR()
        for k in R.getKeys() :
           RR = self.__getRR(k)
           self.__removeEntries(RR)
        self.__removeEntries(R)


def allEmpty(var,list):
    for l in list :
        if not var.has_key(l) : continue
        if var[l] == None : continue
        return False
    return True

def printVar(name,action,var):
  print "================ " + name
  print "action = " + action
  for k in var.keys() : 
    print k + " = " + str(var[k])

def removeDuplicates(li,thesame):
    newli = []
    for elem in li :
        notfound = True
        for e in newli :
            if thesame([e,elem]) :
                notfound = False
                break
        if notfound : newli.append(elem)
        
    return newli
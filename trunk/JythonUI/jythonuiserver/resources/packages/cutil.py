from java.util import Calendar
from org.python.core.util import StringUtil
import datetime
import time

from java.util import ArrayList
from java.math import BigDecimal
from com.jythonui.server import MUtil
from com.gwtmodel.table.common.dateutil import DateFormatUtil
from com.jythonui.server.holder import Holder
from com.jythonui.server.holder import SHolder
from com.gwtmodel.table.common import CUtil
from com.jythonui.server.semaphore import ISemaphore

import con
import clog


LONG="long"
DECIMAL="decimal"
DATE="date"     
BOOL="boolean"
STRING="string"

PDFTEMPORARY="TEMPORARY"

class MESS :

  def __init__(self):
      self.M = Holder.getAppMess()

  def __call__(self,key) :
      return self.M.getCustomMess().getAttr(key)

def enableField(var,li,enable=True) :
    if type(li) != list : li = [li]
    for fieldid in li : 
        var["JSETATTR_FIELD_"+fieldid+"_enable"] = enable
        
def enableButton(var,li,enable=True) :
    if type(li) != list : li = [li]
    for buttid in li :
        var["JSETATTR_BUTTON_"+buttid+"_enable"] = enable
        
def setSpinnerMin(var,fie,val) :
    var["JSETATTR_FIELD_"+fie +"_spinnermin"] = val

def setSpinnerMax(var,fie,val) :
    var["JSETATTR_FIELD_"+fie +"_spinnermax"] = val        
     
def hideButton(var,li,hide=True) :
    if type(li) != list : li = [li]
    for buttid in li :
      var["JSETATTR_BUTTON_"+buttid+"_hidden"] = hide  

def setEditListActionOk(var,li,ok=True) :
   var["JLIST_EDIT_ACTIONOK_" + li] = ok

def getCookie(var,name):
    cname="JCOOKIE_" + name
    if var.has_key(cname) : return var[cname]
    return None

def setCookie(var,name,val=None):
    cname="JCOOKIESET_" + name
    var[cname] = True
    if val : var["JCOOKIE_" + name] = val
    else : var["JCOOKIE_" + name] = ""
    
def setJChartList(var,list,seq):
  if var.has_key("JCHART_MAP") : var["JCHART_MAP"][list] = seq
  else : var["JCHART_MAP"] = { list : seq }
    
def setJMapList(var,list,seq):
  if var.has_key("JLIST_MAP") : var["JLIST_MAP"][list] = seq
  else : var["JLIST_MAP"] = { list : seq }

def setJCheckList(var,list,seq):
  if var.has_key("JCHECK_MAP") : 
    if var["JCHECK_MAP"].has_key(list) : var["JCHECK_MAP"][list].update(seq)
    else : var["JCHECK_MAP"][list] = seq
  else : var["JCHECK_MAP"] = { list : seq }

def disableJCheckList(var,id,disable=True) :
       var["JSETATTR_CHECKLIST_" + id +"_readonly"] = disable
       var["JVALATTR_CHECKLIST_"+ id +"_readonly"] = "" 

def createArrayList() :
  return ArrayList()   
  
def setFooter(var,list,column,val):
    var["JFOOTER_COPY_"+list+"_"+column] = True
    var["JFOOTER_"+list+"_"+column] = val
    
def setStatus(var,typ,val):
  var["JSTATUS_SET_" + typ] = True
  var["JSTATUS_TEXT_" + typ] = val  
  
def setErrorField(var,f,mess="") :
   var["JERROR_"+f] = mess

def checkEmpty(var,li) :
  if type(li) != list : li = [li]    
  isempty = False
  for f in li :
    if var[f] : continue
    isempty=True
    setErrorField(var,f,"@cannotbeempty")
  return isempty  
  
def checkGreaterZero(var,key,empty=True):
  if var[key] == None : return empty  
  val = var[key]
  if val <= 0 :
     setErrorField(var,key,"@numbergreater")
     return False
  return True

def createEnum(list,getname,getdisplay=None,addid=True):    
    seq = []
    for s in list :
        id = getname(s)
        if id == None or id.strip() == "": continue
        m = {"id" : id }
        if getdisplay :  
          name = getdisplay(s)
          if name == None : name = ""
          if addid : m["name"] = id + " " + name 
          else : m["name"] = name
        else : m["name"] = id
        seq.append(m)
    return seq 

def copyVarToProp(var,prop,list):
    for l in list :
        val = var[l]
        prop.setAttr(l,val)
        
def copyPropToVar(var,prop,list,prefix=None):
    for l in list :
        val = prop.getAttr(l)
        if prefix : key = prefix + l
        else : key = l
        var[key] = val

def toDate(value):
    return con.toDate(value)

def eqDate(d1,d2):
    return con.eqDate(d1,d2)

def toJDate(value):
    return con.toJDate(value)
    
def toJDateTime(value):
    return con.toJDateTime(value)

def toB(value):
    return con.toB(value)

def BigDecimalToDecimal(b):
    return con.BigDecimalToDecimal(b)

def mulIntDecimal(int,dec,afterdot=2):
    return con.mulIntDecimal(int,dec,afterdot)

def addDecimal(sum1,sum2,afterdot=2):
   return con.addDecimal(sum1,sum2,afterdot)

def minusDecimal(sum1,sum2,afterdot=2):
   return con.minusDecimal(sum1,sum2,afterdot) 
 
def emptyS(name):
    return name == None or name == "" or CUtil.EmptyS(name)

def ifnull(val1,val2) :
  if val1 == None : return val2
  return val1
  
def concatS(*arg) :
  res = None
  for r in arg :
    if not emptyS(r) :
      if res == None : res = r
      else : res = res + " " + r
  return res    
  
def setCopy(var,li, listt=None,prefix=None) :
  if type(li) != list :
      li = [li]  
  for l in li :
    if listt : c = "JROWCOPY_" + listt + "_"  
    else : c = "JCOPY_"
    if prefix : k = prefix + l
    else : k = l
    var[c+k] = True

def removeDecimalFooter(var,list,name) :
    footerdef = "JFOOTER_"+list+"_"+name
    currentfooter = var[footerdef]
    val = var[name]
    currentfooter = con.minusDecimal(currentfooter,val)
    setFooter(var,list,name,currentfooter)
    
def modifDecimalFooter(var,list,name):
    footerdef = "JFOOTER_"+list+"_"+name
    currentfooter = var[footerdef]
    prevval = var["JVALBEFORE"]
    val = var[name]
    currentfooter = minusDecimal(currentfooter,prevval)
    currentfooter = addDecimal(currentfooter,val)
    var[footerdef] = currentfooter
    var["JFOOTER_COPY_"+list+"_"+name] = True
    
def modifTextFooter(var,list,col,amountcol) :
   prevval=var["JVALBEFORE"]
   val = var[col]
   if prevval and val : return
   if prevval==None and val==None : return
   footerdef = "JFOOTER_"+list+"_"+col
   currentfooter=var[footerdef]
   amount = var[amountcol]
   if val : currentfooter=addDecimal(currentfooter,amount)
   else : currentfooter=minusDecimal(currentfooter,amount)
   var[footerdef] = currentfooter
   var["JFOOTER_COPY_"+list+"_"+col] = True
   
def modifTextDecimalFooter(var,list,col,amountcol):
    if var[col] == None : return
    footerdef = "JFOOTER_"+list+"_"+col
    currentfooter = var[footerdef]
    prevval = var["JVALBEFORE"]
    val = var[amountcol]
    currentfooter = minusDecimal(currentfooter,prevval)
    currentfooter = addDecimal(currentfooter,val)
    var[footerdef] = currentfooter
    var["JFOOTER_COPY_"+list+"_"+col] = True
    
def removeTextFooter(var,list,col,amountcol) :
    if var[col] == None : return
    footerdef = "JFOOTER_"+list+"_"+col
    currentfooter = var[footerdef]
    val = var[amountcol]
    currentfooter = minusDecimal(currentfooter,val)
    var[footerdef] = currentfooter
    var["JFOOTER_COPY_"+list+"_"+col] = True
   
   
def setFooter(var,list,col,val) :
    footerdef = "JFOOTER_"+list+"_"+col
    var[footerdef] = val
    var["JFOOTER_COPY_"+list+"_"+col] = True
    
def __defineEditMode(var,llist,li,mode):
    if type(li) != list :
      li = [li]       
    for l in li :
      var["JLIST_EDIT_"+llist+"_"+l] = ""
    var["JLIST_EDIT_"+llist+"_MODE"] = mode
    
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
        if fa == None :
            self.__fa = Holder.getRegFactory()
        self.__realM = realM
        self.__seqGen = seqGen
        if seqGen == None :
            self.__seqGen = SHolder.getSequenceRealmGen()
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
         
    def getMapRR(self,k):
        RR = self.__getRR(k)
        elem = {}
        for id in self.__map.keys() :
            type = self.__map[id]
            val = RR.getEntryS(id)
            if val == None: valt = None 
            else :
               if type == LONG :
                   valt = long(val)
               elif type == DATE :
                   valt = self.__tod(val)
               elif type == DECIMAL :
                   valt = float(val)
               else : valt = val
            elem[id] = valt
        return elem                                  
    
    def readList(self,var,setmap=True):
        R = self.__getR()
        l = R.getKeys()
        li = []
        for k in l : 
            if k != self.__key : li.append(self.getMapRR(k))
        if setmap : setJMapList(var,self.__listid,li)
        return li
        
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
            ttype = self.__map[id]
            if var.has_key(id) :
                val = var[id]
                if val == None : vals = None
                else :
                  if ttype == LONG :                
                      vals = str(val)
                  elif ttype == DATE :
                      vals = self.__tostr(val)
                  elif ttype == DECIMAL :
                      vals = con.toS(val)    
                  else : vals = val
                RR.putEntry(id,vals)

        R.putEntry(k,"")
        
    def saveList(self,var):
        self.removeAll()
        li = var["JLIST_MAP"][self.__listid]
        for m in li :
            self.addMap(m)    
        
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
  clog.info("==============",name)
  clog.info("action = ",action)
  for k in var.keys() :
      clog.info(k,"=",str(var[k]))
  

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

def concatDict(dic1,dic2):
    res = dic1.copy()
    for key in dic2.keys() :
        res[key] = dic2[key]
    return res    

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
    
def today():
    return con.today()

def getTypeUpList() :
    return ("USER", "DATA", "UPINFO", "OWNER", "PRODUCT", "TITLE")

def setStatusMessage(var,type,s = None):
    var["JSTATUS_SET_"+type] = True
    if s == None or s == "" : return
    var["JSTATUS_TEXT_"+type] = s

def setEditRowOk(var,li,stat=True) :
    var["JEDIT_ROW_OK_" + li] = stat  

class DLIST() :
  
  def __init__(self,var,list) :
    iServer = Holder.getiServer()
    r = Holder.getRequest() 
    d = iServer.findDialog(r,var["J_DIALOGNAME"])
    self.fList = d.getDialog().findList(list)
    assert self.fList != None
    
  def onList(self,name) :
    i = self.fList.getColumn(name)
    return i != None

class SEMTRANSACTION :
  
  def __init__(self,semname,var) :
    self.i = SHolder.getSem()
    self.semname = semname
    self.var = var
    
  def doTrans(self) :
    self.i.wait(self.semname,ISemaphore.DEFAULT) 
    try :
      self.run(self.var)
    finally :
      self.i.signal(self.semname)

  def doTransRes(self) :
      self.doTrans()
      return self.res

class GenSeq :
  
  def __init__(self,realM,keyM) :
    self.h = SHolder.getSequenceRealmGen()
    self.realM = realM
    self.keyM = keyM
    
  def nextKey(self) :
    return self.h.genNext(self.realM,self.keyM)
  
  def removeKey(self) :
    self.h.remove(self.realM,self.keyM)
    
def getDict(what) :
  if what == "countries" : return Holder.getListOfCountries().getList()
  if what == "titles" : return Holder.getListOfTitles().getList()
  if what == "idtypes" : return Holder.getListOfIdTypes().getList()
  if what == "payments" : return Holder.getListOfPayment().getList()
  if what == "roles" : return Holder.IGetListOfDefaultRoles().getList()
  if what == "vat" : return Holder.IGetListOfVat().getList()

def enumDictAction(action,var,what) :
  iC = getDict(what)
  seq = createEnum(iC,lambda c : c.getName(),lambda c : c.getDescription(), False)
  setJMapList(var,action,seq)      
  
def getDicName(what,id):
    if id == None : return None
    i = getDict(what)
    for c in i : 
        if c.getName().upper() == id.upper() : return c.getDescription()
    return None

def getDictFromFile(dire,dictname) :
  i = Holder.getReadDict()
  seq = i.getDict(dire, dictname)
  return seq

class DEFAULTDATA :
  
  def __init__(self) :
    self.h = Holder.getDefaultData()
    
  def getData(self,key,defa=None) :
    val = self.h.getValue(key)
    if val : return val
    return defa
  
  def putData(self,key,val) :
    self.h.putValue(key,val)     
    
  def getDataI(self,key) :
    v = self.getData(key)
    if v == None : return None
    return int(v)
  
  def putDataI(self,key,v) :
    if v == None : self.putData(key,None)
    else : self.putData(key,str(v))
    
  def getDataB(self,key,defa=True) :
    val = self.getData(key)
    if val == None : return defa 
    if val == "1" : return True
    return False
    
  def putDataB(self,key,val) :
    if val : self.putData(key,"1")
    else : self.putData(key,"0")    
       
# ==========================

def urlParList(var) :
  R = Holder.getRequest().getUrlParam()
  res = []
  for k in R : res.append(k)
  return res

def urlPar(var,k) :
  R = Holder.getRequest().getUrlParam()
  return R.get(k)
  
# =============================
def getMapFieldList(dialogName,list=None):
  """ Extract list of fields (columns) name from dialog
    Args:
      dialogName : dialog
      list : if None list of fields from dialog
             if not None list of columns from list 
  """    
  i = Holder.getiServer()
  dInfo =  i.findDialog(Holder.getRequest(), dialogName)
  assert dInfo != None
  dFormat = dInfo.getDialog()
  if list == None :
      flist = dFormat.getFieldList()
  else :
    lform = dFormat.findList(list)
    assert lform != None
    flist = lform.getColumns()  
          
  l = []
  for f in flist :
      name = f.getId()
      l.append(name)
  return l    
  
# =============================
def getPerson(var):
    token = var["SECURITY_TOKEN"]
    return Holder.getNameFromToken().getInstance(token).getPerson()

def getAppId(var):
    token = var["SECURITY_TOKEN"]
    return Holder.getNameFromToken().getInstance(token)
    
      
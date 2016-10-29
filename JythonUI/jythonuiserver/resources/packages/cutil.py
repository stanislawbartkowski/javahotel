from java.util import Calendar
from org.python.core.util import StringUtil
from java.util import ArrayList
from java.math import BigDecimal

from com.jythonui.server import MUtil
from com.jythonui.server.holder import Holder
from com.jythonui.server.holder import SHolder
from com.gwtmodel.table.common import CUtil
from com.jythonui.server.semaphore import ISemaphore
from com.jythonui.server.xmlmap import IMapValues
from com.jythonui.server.journal import JournalRecord
from com.gwtmodel.table.common import PersistTypeEnum
from com.jythonui.server import IConsts

import datetime,time

import con,clog

LONG=IMapValues.LONG
DECIMAL=IMapValues.DECIMAL
DATE=IMapValues.DATE
DATETIME="datetime"
BOOL=IMapValues.BOOL
STRING="string"

PDFTEMPORARY="TEMPORARY"

class MESS :

  def __init__(self):
      self.M = Holder.getAppMess()

  def __call__(self,key) :
      return self.M.getCustomMess().getAttr(key)
  
  def C(self,key):
      if key == None : return key
      if key[0] == '@' : return self.M.getCustomMess().getAttr(key[1:])
      return key

def enableField(var,li,enable=True) :
    if type(li) != list : li = [li]
    for fieldid in li : 
        var["JSETATTR_FIELD_"+fieldid+"_enable"] = enable

def setFocus(var,li,focus=True) :
    if type(li) != list : li = [li]
    for fieldid in li : 
        var["JSETATTR_FIELD_"+fieldid+"_focus"] = focus
        
def cellTitle(var,id,title) :
    var["JSETATTR_FIELD_" + id + "_celltitle"] = title        
        
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
      
def setBinderAttr(var,id,attr,val) :
    var["JSETATTR_BINDER_" + id + "_" + attr] = val
    
def setElevationAttr(var,id,val) :
    setBinderAttr(var,id,"elevation",val)

def setSpinnerActive(var,id,val=True) :
    setBinderAttr(var,id,"active",val)
    
def setLabelText(var,id,val=True) :
    setBinderAttr(var,id,"text",val)    

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
  
def setHeader(var,list,column,val):
    var["JSETHEADER_"+list+"_"+column] = val
    
def listColumnVisible(var,lis,cols,visible=True) :
    if type(cols) != list : cols = [cols]
    for column in cols :
      var["JSETCOLUMNVIS_"+lis+"_"+column] = visible    
  
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

def validatePercent(var,field) :
  if var[field] == None : return True
  pe = var[field]
  if pe <= 0 or pe >= 100 :
    setErrorField(var,field,"@percentinvalid")
    return False
  return True


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

def setCopyL(var,*arg) :
    l = []
    for e in arg : l.append(e)
    cutil.setCopy(var,l)

def setGlobCopy(var,li) :
  if type(li) != list :
      li = [li]  
  for l in li :
    c = "JCOPY_GLOBAL_"
    k = l
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

""" Set edit list in non-edit mode, switch off editing
  Args:
    var
    list list identifier
    li list of columns identifiers to remove editing    
"""    
def setStandEditMode(var,list,li):
    __defineEditMode(var,list,li,"NORMALMODE")

""" Set edit list in edit mode, switch on editing, editing with stable number of rows
  Args:
    var
    list list identifier
    li list of columns identifiers to be edited    
"""        
def setChangeEditMode(var,list,li):
    __defineEditMode(var,list,li,"CHANGEMODE")  

""" Set edit list in edit mode, switch on editing, +- buttons, can add or remove rows
  Args:
    var
    list list identifier
    li list of columns identifiers to be edited    
"""        
def setAddEditMode(var,list,li):
    __defineEditMode(var,list,li,"ADDCHANGEDELETEMODE")  
    

class StorageRegistry() :
     
     def __init__(self,fa,realm):
         if fa == None : fa = Holder.getRegFactory()
         self.r = fa.construct(realm,True,True)
         
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

def splitsubmitres(submitres) :
    elems = submitres.split(":")
    realm = elems[0]
    key = elems[1]
    filename = elems[2]
    return (realm,key,filename)

def printVar(name,action,var):
  clog.info("==============",name)
  clog.info("action = ",action)
  for k in var.keys() :
      clog.info(k,"=",con.toS(var[k]))
  

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
  
  def __init__(self,var,list=None) :
    iServer = Holder.getiServer()
    r = Holder.getRequest() 
    self.__d = iServer.findDialog(r,var["J_DIALOGNAME"])
    self.iN = Holder.getResolveName()
    if list == None : return
    self.fList = self.__d.getDialog().findList(list)
    assert self.fList != None,"Cannot find list " + list + " in the dialog " + var["J_DIALOGNAME"] 
    
  def onList(self,name) :
    i = self.fList.getColumn(name)
    return i != None

  def createNameMap(self):
    ma = {}
    for f in self.__d.getDialog().getFieldList() :
        id = f.getId();
        name = self.iN.resolveName(f.getDisplayName())
        ma[id] = name       
#        print id,name     
    return ma    

def verifyXML(xsdfile,xml):
    """ Verify xml file with xsd schema.
        Important: it is expected that schema match xml, used only for internal checking
    Args:
        xsdfile : the name of xsd (schme) file, file is read from dialog/xsd directory
        xml : xml string (important: string, not file name)
    
    Returns:
        returns if schema match the xml, if not breaks program
        
    Raise :
        run time exception if not match, not match is not expected
         
      
    """
    i = Holder.getXMLVerifies()
    ok = i.verify("xsd", xsdfile,xml)
    assert ok


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
def getPerson(var):
    token = var["SECURITY_TOKEN"]
#    return Holder.getNameFromToken().getInstance(token).getPerson()
    return Holder.getiSec().getUserName(token)

def getAppId(var):
    token = var["SECURITY_TOKEN"]
    return Holder.getNameFromToken().getInstance(token)

def getObject(var=None):
    return Holder.getO()
    
    
# ==========================

def createJournalRecord(type,typespec,elem1=None,elem2=None,descr=None):    
    r = JournalRecord()
    r.setJournalType(type)
    r.setJournalTypeSpec(typespec)
    r.setJournalElem1(elem1)
    r.setJournalElem2(elem2)
    r.setDescription(descr)
    return r

# ==================
class CRUDLIST :
    def __init__(self,var,recordid = None):
        self.serviceS = None
        self.var = var
        self.recordid = recordid
        if recordid : self.jserviceS = Holder.getiJournal()        
        
    def _getO(self):
        return getObject(self.var)        
        
    def getList(self):
        return self.serviceS.getList(self._getO()) 
    
    def getModifiedList(self,f):
        li = self.getList()
        nli = createArrayList()
        for l in li :
            if not f(l) : continue
            nli.add(l)
        return nli
        
    def _addJournal(self,typespec,r):
        if self.recordid == None : return
        re = createJournalRecord(self.recordid,str(typespec),r.getName(),r.getDescription())
        self.jserviceS.addElem(self._getO(),re)
                
    def addElem(self,elem):
        res = self.serviceS.addElem(self._getO(),elem)
        self._addJournal(PersistTypeEnum.ADD, res)
        return res
       
    def changeElem(self,elem):
        self.serviceS.changeElem(self._getO(),elem)
        self._addJournal(PersistTypeEnum.MODIF, elem)
            
    def deleteElem(self,elem):
        self.serviceS.deleteElem(self._getO(),elem)
        self._addJournal(PersistTypeEnum.REMOVE, elem)
        
    def findElem(self,name):
        return self.serviceS.findElem(self._getO(),name)    
      
    def deleteElemByName(self,name) :
        elem = self.findElem(name)
        self.deleteElem(elem)
           

# ------------------------
        
class JOURNAL(CRUDLIST):
    
    def __init__(self,var):
        CRUDLIST.__init__(self,var)
        self.serviceS = Holder.getiJournal()
        
    def addJournalElem(self,type,typespec,elem1=None,elem2=None,descr=None):
        re = createJournalRecord(type,typespec,elem1,elem2,descr)
        self.addElem(re) 
        
        
class JOURNALMESS():
    
    def __init__(self):
        self.M = MESS()
    
    def getEntryDescr(self,j):
        jtype = j.getJournalType()        
        if jtype == IConsts.LOGINJOURNAL : return self.M("loginjournalmess")
        if jtype == IConsts.LOGOUTJOURNAL : return self.M("logoutjournalmess")
        return None
    
    def standTypes(self):
        return [IConsts.LOGINJOURNAL,IConsts.LOGOUTJOURNAL]
                        
# -----------------
        
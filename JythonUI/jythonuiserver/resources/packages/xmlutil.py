from java.math import BigDecimal
from java.util import Properties
from com.jamesmurty.utils import XMLBuilder 
from javax.xml.transform.OutputKeys import INDENT
from java.util import Date

from com.jythonui.server.holder import Holder
from com.jythonui.server.holder import SHolder
from com.jythonui.shared import DialogVariables
from com.gwtmodel.table.common import TT
from com.jythonui.server import BUtil
from com.jythonui.shared import ButtonItem

import datetime

import cutil
import sys
import con
import miscutil

def __toBirt(d):
    if d == None : return None
    return d.strftime("%Y-%m-%d")

def getVar(map,dialogname,xml,listv):
    """ Set map with values read from xml string for dialog form.
    
    Retrieves values from XML string and set values to the map (key->value). 
    Retrieves values only for list of keys (not for all found in XML string)
    
    Args:
       map : A map being set. Can be empty or contains some values already. In case of
          conflict new value will overwrite existing value.
       dialogname : Dialog name where form is available.
       xml : XML string to be analyzed.
       listv : List of keys to be set to the map. Only keys found in the list are set. 
           Others are ignored.    
    """
    iXml = Holder.getXMLTransformer();
    v = DialogVariables();
    iXml.fromXML(dialogname,v,xml);
    for vname in listv :
        val = v.getValue(vname)
        if val == None or val.getValue() == None: 
            map[vname] = None
            continue
        if val.getType() == TT.STRING :
            map[vname] = val.getValueS()
            continue
        if val.getType() == TT.BIGDECIMAL :
            b = val.getValueBD()
            map[vname] = con.BigDecimalToDecimal(b)
            continue
        if val.getType() == TT.BOOLEAN :
            b = val.getValueB()
            map[vname] = b
            continue
        if val.getType() == TT.INT or val.getType() == TT.LONG :
            map[vname] = val.getValue()
            continue
        if val.getType() == TT.DATE :
            map[vname] = con.toJDate(val.getValue())
            continue
        if val.getType() == TT.DATETIME :
            map[vname] = con.toJDateTime(val.getValue())
            continue         

class TOXML :
    
    def __init__(self,tobirt=False):
        self.__tobirt = tobirt
        pass
    
    def getMap(self,first):
        pass
    
    def __toXML(self,ma):

      builder = self.__builder.e("elem")
    
      for k in ma :
          val = ma[k]
          vals = None
          atype = None
          if type(val) == int or type(val) == long : atype = cutil.LONG
          elif type(val) == float or type(val) == BigDecimal : atype = cutil.DECIMAL
          elif type(val) == bool : 
              atype = cutil.BOOL
              if val : val = 1
              else : val = 0
          elif type(val) == datetime.date : 
              atype = cutil.DATE
              if self.__tobirt : val = __toBirt(val)
              else : val = str(val).replace('-','/')
          elif type(val) == unicode :
              vals = val    
          builder = builder.e(k)    
          if atype : builder = builder.a("type",atype)
          if vals == None and val != None : vals = str(val)
          if vals != None : builder = builder.t(vals)
          builder = builder.up()
      self.__builder = builder.up()     

    
    def toXML(self,ma,isList):
        self.__builder = XMLBuilder.create("root")
        self.__toXML(ma)
        if isList :
            first = True
            self.__builder = self.__builder.e("list")
            while True :
              map = self.getMap(first)
              first = False
              if map == None : break
              self.__toXML(map)
            self.__builder = self.__builder.up()  
              
    def getS(self):
        outputProperties = Properties()
        outputProperties.put(INDENT, "yes")
        return self.__builder.root().asString(outputProperties)              

class MAPTOXML(TOXML):
    
    def __init__(self,l,tobirt=False):
        TOXML.__init__(self,tobirt)
        self.__l = l
        
    def getVal(self,map,k):
        return map[k]
    
    def getMap(self,first):
        if first : self.__start = 0
        else : self.__start = self.__start + 1
        if self.__start >= len(self.__l) : return None
        return self.__l[self.__start]
        
def toXML(ma,list = None, tobirt=False):
    X = MAPTOXML(list,tobirt)
    X.toXML(ma,list != None)
    return X.getS()              
   
def _toMap(map,li):
    ma = {}
    for e in map.entrySet() :
        k= e.getKey()
        if li != None and not k in li : continue 
        v = e.getValue()
        if type(v) == Date : v = con.toJDate(v)
        ma[k] = v
    return ma     
    
def _toMapF(xmls,lid,lil):
  x = Holder.getMapXML();
  iR = x.getMap(xmls)  
  rmap = _toMap(iR.getMap(),lid)
  li = []
  for m in iR.getList() :
      li.append(_toMap(m,lil))
  return (rmap,li)
     
def toMap(xmls):
  return _toMapF(xmls,None,None)
  
def _toMapFiltrL(xmls,lfiltr):
  return _toMapF(xmls,None,lfiltr)

def toMapFiltrDialL(xmls,dialogName,listname):
    return _toMapFiltrL(xmls,cutil.getMapFieldList(dialogName,listname))
                
# ---------------------------------------------------------------    
            
def listNumberToCVS(li,empty="") :
  """ Transforms list of number to csv line 
    
  Args:
    li : list of numbers (longs)
    empty (optional) : string representing empty list, default ""
    
  Returns:
    cvs line representing the input list     
  """  
  s = None
  for l in li :
    vals = str(l)
    if s : s = s + "," + vals
    else : s = vals
  if s : return s
  return empty

def CVSToListNumber(s) :
  """ Opposite to listNumberToCVS, transform csv line to list of number
      (Important: does not recognize empty parameter in listNumberTo CVS
  
  Args:
    s : csv line
  Returns: 
    List of numbers  
  """  
  if s == None : return []
  li = []
  for n in s.split(",") :
    num = long(n)
    li.append(num)
  return li  
  
# ----------------------------------------------------
def fileToS(dir,filename=None):
   if filename == None : 
       filename = dir 
       dir = None
#   iS = Holder.getIJython().getResource().getRes(filename)
   iS = Holder.getFindResource().getFirstURL(dir, filename)
   s = BUtil.readFromFileInput(iS.openStream())
   return s
  
# ---------------------

def xmlToVar(var,xml,list,pre=None) :
    """ Deprecated
    """
    iXML = SHolder.getToXMap()
    prop = ButtonItem()
    iXML.readXML(prop,xml,"root","elem")
    for l in list :
        val = prop.getAttr(l)
        if pre : k = pre + l
        else : k = l
        var[k] = val
        
def mapToXML(map,list=None,pre=None):
    iter = list
    if iter == None : iter = map
    demap = {}
    for e in iter :
        val = ""
        if pre : k = pre + e
        else : k = e
        if map.has_key(k) :
            if map[k] != None : val = map[k]
        demap[e] = val
            
    return toXML(demap)       
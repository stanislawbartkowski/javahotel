from com.jythonui.server.holder import Holder
from com.jythonui.server.holder import SHolder
from com.jythonui.shared import DialogVariables
from com.gwtmodel.table.common import TT
from com.jythonui.server import BUtil
from cutil import BigDecimalToDecimal
from cutil import toJDate
from cutil import toJDateTime
import cutil
import sys
import datetime
import con
from java.util import Date
from com.jythonui.shared import ButtonItem
from com.jamesmurty.utils import XMLBuilder 
from java.util import Properties
from javax.xml.transform.OutputKeys import INDENT

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
            map[vname] = BigDecimalToDecimal(b)
            continue
        if val.getType() == TT.BOOLEAN :
            b = val.getValueB()
            map[vname] = b
            continue
        if val.getType() == TT.INT or val.getType() == TT.LONG :
            map[vname] = val.getValue()
            continue
        if val.getType() == TT.DATE :
            map[vname] = toJDate(val.getValue())
            continue
        if val.getType() == TT.DATETIME :
            map[vname] = toJDateTime(val.getValue())
            continue         


def _toXML(builder,ma):

    builder = builder.e("elem")
    
    for k in ma :
        val = ma[k]
        atype = None
        if type(val) == int or type(val) == long : atype = cutil.LONG
        elif type(val) == float : atype = cutil.DECIMAL
        elif type(val) == bool : 
            atype = cutil.BOOL
            if val : val = 1
            else : val = 0
        elif type(val) == datetime.date : 
            atype = cutil.DATE
            val = str(val).replace('-','/')
        builder = builder.e(k)    
        if atype : builder = builder.a("type",atype)
        if val : builder = builder.t(str(val))
        builder = builder.up()
    return builder.up()     
    

def toXML(ma,list = None): 
    builder = XMLBuilder.create("root")
    builder = _toXML(builder, ma)
    if list :
      builder = builder.e("list")
      for m in list : builder = _toXML(builder,m)
      builder = builder.up() 
    outputProperties = Properties()
    outputProperties.put(INDENT, "yes")
    return builder.root().asString(outputProperties)              
   
def _toMap(map):
    ma = {}
    for e in map.entrySet() :
        k= e.getKey()
        v = e.getValue()
        if type(v) == Date : v = con.toJDate(v)
        ma[k] = v
    return ma     
    
def toMap(xmls):
  x = Holder.getMapXML();
  iR = x.getMap(xmls)  
  rmap = _toMap(iR.getMap())
  li = []
  for m in iR.getList() :
      li.append(_toMap(m))
  return (rmap,li)
    
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
def fileToS(filename):
   iS = Holder.getIJython().getResource().getRes(filename)
   s = BUtil.readFromFileInput(iS.openStream())
   return s
  
# ---------------------

def xmlToVar(var,xml,list,pre=None) :
    iXML = SHolder.getToXMap()
    prop = ButtonItem()
    iXML.readXML(prop,xml,"root","elem")
    for l in list :
        val = prop.getAttr(l)
        if pre : k = pre + l
        else : k = l
        var[k] = val
        
def mapToXML(map,list = None,pre=None):
    iter = list
    if iter == None : iter = map
    demap = {}
    for e in iter :
        val = ""
        if pre : k = pre + e
        else : k = e
        if map.has_key(k) :
            if map[k] : val = str(map[k])
        demap[e] = val
            
    return toXML(demap)       
from com.jythonui.server.holder import Holder
from com.jythonui.shared import DialogVariables
from com.gwtmodel.table.common import TT
from cutil import BigDecimalToDecimal
from cutil import toJDate
from cutil import toJDateTime
import cutil
import sys
import datetime
import con
from java.util import Date


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

def _toXML(ma):
    xml = "<elem>"
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
        x = '<' + k
        if atype : x = x + " type=\"" + atype + '\"'
        if val : x = x + '>' + str(val) + "</" + k + '>'
        else : x = x + "/>"
        xml = xml + '\n' + x
    xml = xml + "\n</elem>"    
    return xml    

def toXML(ma,list = None): 
    xml = _toXML(ma)
    if list :
      li = "\n<list>"
      for m in list :
          li = li + '\n' + _toXML(m)
      li = li + "\n</list>" 
      xml = "<root>\n" + xml + li + "\n</root>"
    return xml  
         
   
def _toMap(map):
    ma = {}
    for e in map.entrySet() :
        k= e.getKey()
        v = e.getValue()
        if type(v) == Date : v = con.toJDate(v)
        ma[k] = v
    return ma     
    
def toMap(xmls):
  print "toMap",xmls
  x = Holder.getMapXML();
  iR = x.getMap(xmls)  
  rmap = _toMap(iR.getMap())
  li = []
  for m in iR.getList() :
      li.append(_toMap(m))
  return (rmap,li)
    
from com.jythonui.server.holder import Holder
from com.jythonui.shared import DialogVariables
from com.gwtmodel.table.common import TT
from cutil import BigDecimalToDecimal
from cutil import toJDate
from cutil import toJDateTime

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

from com.jythonui.server.holder import Holder
from com.jythonui.shared import DialogVariables
from com.gwtmodel.table.common import TT
from cutil import BigDecimalToDecimal
from cutil import toJDate
from cutil import toJDateTime

def getVar(map,dialogname,xml,listv):
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

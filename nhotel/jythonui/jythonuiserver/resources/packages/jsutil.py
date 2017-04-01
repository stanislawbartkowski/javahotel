from com.google.gson import JsonObject
from com.google.gson import JsonParser

import cutil,con,miscutil

def toList(s,listid,dialname=None,listname=None) :
    elem = JsonParser().parse(s)
    object = elem.getAsJsonObject()
    array = object.getAsJsonArray(listid)
    list=[]
    lform = None
    if dialname != None : lform = miscutil.toListMap(dialname,listname) 
    for i in range (array.size()) :
        ma = {}
        o = array.get(i)
#        print i,o
        s = o.entrySet()
        for e in s :
#            print e,e.getKey(),e.getValue()
            if e.getValue().isJsonNull() : val = None
            else :
                if lform != None : (ttype,after) = miscutil.getColumnDescr(lform,e.getKey())
                p = o.getAsJsonPrimitive(e.getKey())           
                if p.isBoolean() : val = p.getAsBoolean()
                elif  p.isNumber() :
                    val = p.getAsDouble()
                    if lform != None :
                        if ttype == cutil.LONG : val = int(val)
                else : 
                    val = p.getAsString()
                    if lform != None : 
                        if ttype == cutil.DATE : val = con.StoDate(val)
                        if ttype == cutil.DATETIME : val = con.StoDate(val,True)                                        
            ma[e.getKey()] = val
        list.append(ma)
           
    return list
from com.jamesmurty.utils import XMLBuilder 
from javax.xml.transform.OutputKeys import INDENT
from java.util import Properties

import sets

import jsutil,xmlutil,con,miscutil,cutil,pdfutil

XML="xml"
JSON="json"
PDF="pdf"
CSV="csv"
HTML="html"

FID="id"
FVISIBLE="visible"
FCOLUMNNAME="columnname"

LIST="list"

def _toVisMap(listform):
    
    # create set of columns visible
    vis = sets.Set()
    for l in listform : 
        id = l[FID]
        if l[FVISIBLE] : vis.add(id)
    return vis
            
#    li = []
#    for j in jdata :
#        ma = {}
#        for k in j.keys() :
#            if k in vis : ma[k] = j[k]
#        li.append(ma)
        
#    return li

def _toCSV(jdata,lform,listform,vis):
    li = []
    for j in jdata :
        l = None
        for f in listform :
            id = f[FID]
            if not id in vis : continue
            val = con.toS(j[id])
            if val == None : val = ""
#            print val
#            print l
            if l == None : l = val
            else : l = l + "," + val
        li.append(l)
    
    line = None
    for l in li :
        if line == None : line = l
        else : line = line + "\n" + l
        
    return line


def _toHTML(jjdata,lform,listform,vis):
    styleborder = "border: 1px solid black;border-collapse: collapse"
    builder = XMLBuilder.create("table").a("style",styleborder)
#    builder = XMLBuilder.create("table")
    padding = "padding: 5px"
    
    # header
    builder = builder.e("tr")
    for l in listform : 
        if l[FID] in vis : builder = builder.e("th").a("style",styleborder+";" +padding).t(l[FCOLUMNNAME]).up()

    builder = builder.up()
    # content
    for d in jjdata :
        builder = builder.e("tr")
        for j in listform :
            id = j[FID]
            (ttype,tafter) = miscutil.getColumnDescr(lform,id)
#            print id,d[id],d,type(d[id])
            if not id in vis : continue
            if ttype == cutil.DECIMAL : val = con.fToSDisp(d[id],tafter)
            elif ttype == cutil.BOOL : val = con.toS(d[id])
            else: val = con.toS(d[id])
            if val == None : val = ""
            
            align = "left"
            if ttype == cutil.DATE : align = "center"
            elif ttype == cutil.DECIMAL or ttype == cutil.INT : align = "right"
            
            builder = builder.e("td").a("style",styleborder + ";" + padding + ";text-align: " + align).t(val).up()
        builder = builder.up()
    
    builder = builder.up()
    outputProperties = Properties()
    outputProperties.put(INDENT, "yes")
    return builder.root().asString(outputProperties)              

def toFormat(format,jsondata,jsonform,dialogname,listname):    
    lform = miscutil.toListMap(dialogname,listname)
    assert lform != None
    listform = jsutil.toList(jsonform,LIST)
    jdata = jsutil.toList(jsondata,LIST,dialogname,listname)
    vis = _toVisMap(listform)
    if format == XML :
        li = []
        for j in jdata :
            ma = {}
            for k in j.keys() :
                if k in vis : ma[k] = j[k]
            li.append(ma)
        return xmlutil.toXML({},li)
    if format == JSON : return jsondata
    if format == CSV : return _toCSV(jdata,lform,listform,vis)
    html = _toHTML(jdata,lform,listform,vis)
    if format == HTML : return html
    return pdfutil.createPDFH(html)
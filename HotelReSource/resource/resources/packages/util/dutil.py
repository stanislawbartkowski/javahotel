import cutil,xmlutil;

def doctoXML(ma,li):
    xml = xmlutil.toXML(ma,li,True)
    print xml
    cutil.verifyXML("fdocument.xsd",xml)
    return xml

def doctoMap(xml):
    cutil.verifyXML("fdocument.xsd",xml)
    return xmlutil.toMap(xml)  
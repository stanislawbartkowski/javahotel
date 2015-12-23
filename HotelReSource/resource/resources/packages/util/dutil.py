import cutil,xmlutil;

def doctoXML(ma,li,verify=True):
    xml = xmlutil.toXML(ma,li,True)
#    print xml
    if verify : cutil.verifyXML("fdocument.xsd",xml)
    return xml

def doctoMap(xml,verify=True):
    if verify: cutil.verifyXML("fdocument.xsd",xml)
    return xmlutil.toMap(xml)  
  
def getDocGrossValue(ma) :
    return ma["grossvalue"]  
  
def getIssueDate(ma) :
    return ma["issuedate"]  
  
def getGrossValueLine(l) :
    return l["grossvalue"]
  
def getNettoValueLine(l) :
    return l["netvalue"]

def getTaxValueLine(l) :
   return l["taxvalue"]  
  
def getVatLine(l) :
    return l["tax"]     
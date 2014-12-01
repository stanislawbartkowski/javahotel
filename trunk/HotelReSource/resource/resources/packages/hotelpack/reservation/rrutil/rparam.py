import xmlutil
import cutil

# =======================================
def setStartParam(var) :
  var["pstartparam"] = var["JUPDIALOG_START"]
  print "set py startparam ",var["pstartparam"]
  cutil.setCopy(var,"pstartparam")
  
def getStartParam(var) :
  return var["pstartparam"]

# =======================================

def resequeryXML(resroom,resday,resnodays,nop,roomservice,roompricelist) :
     m = {}
     m["roomname"] = resroom
     m["firstday"] = resday
     m["nodays"] = resnodays
     m["nop"] = nop
     m["roomservice"] = roomservice
     m["roompricelist"] = roompricelist
     return xmlutil.toXML(m)    

def XMLtoresquery(xml) :
    m = {}
    (m,li) = xmlutil.toMap(xml)
    return (m["roomname"],m["firstday"],m["nodays"],m["nop"],m["roomservice"],m["roompricelist"])

# ===================================

def roomdaytoXML(resname,resroom,resday) :
  m = {}
  m["resname"] = resname
  m["roomname"] = resroom
  m["resday"] = resday
  return xmlutil.toXML(m)

def XMLtoroomday(xml) :
    m = {}
    (m,li) = xmlutil.toMap(xml)
    return (m["resname"],m["roomname"],m["resday"])
  
import cutil
import tempfile
from com.jythonui.server.holder import Holder

def printBirt(var,rptfile,xml) :
  f = tempfile.NamedTemporaryFile(delete=False)
  f.write(xml.encode('utf8'))
  f.close()
  print f.name
  iGet = Holder.getBirtSearch()
  hName = Holder.getHostName()
#  print hName
  rfile = iGet.getName(rptfile)
  H = cutil.DEFAULTDATA()
  v = H.getData("birtviewer")
#  print v
  if v == None :
    var["JERROR_MESSAGE"] = "@birtviewernotdefined"
    return
  s = v.format(hName,rfile,f.name)
#  print s
  var["JURL_OPEN"] = s

import cutil
import tempfile

def printBirt(var,rptfile,xml) :
  f = tempfile.NamedTemporaryFile(delete=False)
  f.write(xml.encode('utf8'))
  f.close()
  print f.name
  H = cutil.DEFAULTDATA()
  v = H.getData("birtviewer")
#  print v
  if v == None :
    var["JERROR_MESSAGE"] = "@birtviewernotdefined"
    return
  s = v.format(rptfile,f.name)
  print s
  var["JURL_OPEN"] = s

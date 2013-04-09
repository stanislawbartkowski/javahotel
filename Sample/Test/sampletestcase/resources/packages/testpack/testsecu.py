from guice import ServiceInjector

def dialogaction(action,var) :
  print "packenum",action
  for k in var.keys() : 
    print k, var[k]
  iSec = ServiceInjector.constructSecurity()
  token = var["SECURITY_TOKEN"]
  ok = iSec.isAuthorized(token,"sec.u('darkhelmet')")
  var["OK"] = ok
  
import cutil

def dialogaction(action,var) :
  cutil.printVar("dialog action",action,var)
  
  if action == "res" :
    e = var["enter"]
    var["start"] = e
    cutil.setCopy(var,"start")
    
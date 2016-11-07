import cutil

def dialogaction(action,var) :
    cutil.printVar("Test big number",action,var)
    
    if action == "check" :
      var["tonumber"] = var["number"]
      f = var["number"]
      print f,type(f),f.real
      f
      cutil.setCopy(var,"tonumber")
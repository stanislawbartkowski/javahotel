import cutil

def dialogaction(action,var) :
  
  cutil.printVar("decimals",action,var)
  
  if action == "before" :
    var["number4"] = 123.4567
    var["number3"] = 123.4567
    var["number2"] = 123.4567
    var["number1"] = 123.4567
    var["number0"] = 123.4567
    cutil.setCopy(var,["number4","number3","number2","number1","number0"])
    
  if action == "res" :
    for no in ["0","1","2","3","4"] :
      var["numbero" + str(no)] = var["number" + str(no)]
    cutil.setCopy(var,["numbero4","numbero3","numbero2","numbero1","numbero0"])
      
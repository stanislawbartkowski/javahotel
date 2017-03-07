import datetime
import cutil

def dialogaction(action,var) :

  cutil.printVar("main",action,var)
  
  if action == "before" :
      var["glob1"] = "Hello"
      var["globint"] = 12;
      var["globdou"] = 123.45
      var["globchecked"] = True
      cutil.setCopy(var,["glob1","globint","globdou","globchecked"])

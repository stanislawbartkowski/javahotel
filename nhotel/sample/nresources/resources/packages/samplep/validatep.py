import cutil

def dialogaction(action,var) :

  cutil.printVar("validatep",action,var)

  if action == "before" :
    cutil.setCopy(var,["vstring","vint","vtest"])
    var["vstring"] = "Hello"
    var["vint"] = 99
    var["vtest"] = "C"


  if action == "click" :
    cutil.setOkMessage(var,"Ok, not empty")
    s = var["vtest"]
    if s != "A" and s != "B" :
        print "error"
        cutil.setErrorField(var,"vtest","Only A or B is allowed")

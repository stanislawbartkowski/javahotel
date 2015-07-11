import cutil,xmlutil,miscutil

def upaction(action,var) :
  
  cutil.printVar("download object",action,var)

  if action == "attach" :
     var["J_SUBMIT"] = True
     
  if action == "aftersubmit" :
      var["JCLOSE_DIALOG"] = var["JSUBMITRES"]

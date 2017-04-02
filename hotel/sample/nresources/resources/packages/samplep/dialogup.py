import cutil

def dialogaction(action,var) :

  cutil.printVar("dialogup",action,var)
  

def dpopaction(action,var) :

  cutil.printVar("dpop",action,var)
  
  if action == "before" :
      cutil.copyField(var,"dstring",var["vstring"])

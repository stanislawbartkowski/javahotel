import cutil


def dialogaction(action,var) :

  cutil.printVar("dialogup",action,var)
  

def dpopaction(action,var) :

  cutil.printVar("dpop",action,var)
  
  if action == "before" :
      cutil.copyField(var,"dstring",var["vstring"])
      
  if action == "copy" :
      cutil.copyField(var,"vstring",var["newstring"])

  if action == "ok" :
      cutil.setCloseDialog(var)
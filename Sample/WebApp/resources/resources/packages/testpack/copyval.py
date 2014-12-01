import cutil

def dialogaction(action,var) :
  
    cutil.printVar("copyval",action,var)
    
    if action=="clicklocal" :
       cutil.setCopy(var,"value")
       var["value"] = var["text"]

    if action=="clickglobal" :
       cutil.setGlobCopy(var,"value")
       var["value"] = var["text"]

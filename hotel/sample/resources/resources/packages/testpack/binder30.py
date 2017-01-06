import cutil

def dialogaction(action,var) :
  cutil.printVar("binder30",action,var)
  
  if action == "signalchange" :
      cutil.setToastText(var,"toast",var["changefield"] + " " + var[var["changefield"]])
  

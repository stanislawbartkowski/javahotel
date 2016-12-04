import util
import cutil

def dialogaction(action,var) :
  cutil.printVar("binder1",action,var)
  
  if action == "button1" :
    cutil.setBinderOpen(var,"toast1")

  if action == "button2" :
    cutil.setBinderOpen(var,"toast2")

import cutil

def dialogaction(action,var) :
  cutil.printVar("dialog panel",action,var)
  
  if action == "before" :
     var["html"] = "<H1> Hello </H1> How are you ? <p> I'm fine </p>"
     cutil.setCopy(var,"html")

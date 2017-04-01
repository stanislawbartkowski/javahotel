import datetime
import cutil

def dialogaction(action,var) :

  cutil.printVar("alertp",action,var)
  
  if action == "click" :
      cutil.setOkMessage(var,var["message"], var["title"],"after")
  
  if action == "clickyesno" :
      cutil.setYesNoMessage(var,var["message"], var["title"],"after")

  if action == "clickerror" :
      cutil.setErrorMessage(var,var["message"], var["title"],"after")

  if action == "copyfield" :
      cutil.copyField(var,"dispmessage",var["copymessage"])
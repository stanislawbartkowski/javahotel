import cutil

def dialogaction(action,var) :

  cutil.printVar("alertp",action,var)

  if action == "before" :
      cutil.copyField(var,"message","Hello, I am who I am")

  if action == "click" :
      cutil.setOkMessage(var,var["message"], var["title"],"afterok")

  if action == "clickyesno" :
      cutil.setYesNoMessage(var,var["message"], var["title"],"afteryesno")

  if action == "clickerror" :
      cutil.setErrorMessage(var,var["message"], var["title"],"aftererror")

  if action == "copyfield" :
      cutil.copyField(var,"dispmessage",var["copymessage"])

  if action == "afterok" :
      cutil.copyField(var,"dispmessage","You click OK")

  if action == "afteryesno" :
      if var["JYESANSWER"] : s = "Yes click YES"
      else : s = "You click NO"
      cutil.copyField(var,"dispmessage",s)

  if action == "aftererror" :
      cutil.copyField(var,"dispmessage","You click OK error message")

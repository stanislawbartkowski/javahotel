import cutil

# for parent dialog

def dialogaction(action,var) :

  cutil.printVar("dialogup",action,var)


# for child dialog

def dpopaction(action,var) :

  cutil.printVar("dpop",action,var)

  if action == "before" :
      cutil.copyField(var,"dstring",var["vstring"])
      cutil.copyField(var,"dint",var["vint"])
      cutil.copyField(var,"dchecked",var["vchecked"])

  if action == "copy" :
      cutil.copyField(var,"vstring",var["newstring"])

  if action == "ok" :
      cutil.setCloseDialog(var)

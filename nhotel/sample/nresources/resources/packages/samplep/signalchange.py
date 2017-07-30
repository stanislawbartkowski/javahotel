import cutil

def dialogaction(action,var) :

  cutil.printVar("signal change",action,var)

  if action == "signalchange" :
      v = var["changefield"]
      s = v + " = " + str(var[v])
      cutil.copyField(var,"vresult",s)

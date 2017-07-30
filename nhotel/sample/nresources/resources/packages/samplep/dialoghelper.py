import cutil

def dialogaction(action,var) :

  cutil.printVar("dialoghelper",action,var)

  if action == "helper" :
      cutil.callUpDialog(var,"helperstring.xml")

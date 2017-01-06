import cutil,xmlutil

def dialogaction(action,var) :
  cutil.printVar("binder31",action,var)
  
  if action == "before" :
     json = xmlutil.fileToS("docs/contacts.json")
     cutil.setBinderItemsAttr(var,"grid",json)


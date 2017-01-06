import cutil
import xmlutil

def dialogaction(action,var) :
  cutil.printVar("binder29",action,var)  
  
  if action == "before" :
    json = xmlutil.fileToS("docs/contacts.json")
#    print json
    cutil.setBinderItemsAttr(var,"list",json)
      
       
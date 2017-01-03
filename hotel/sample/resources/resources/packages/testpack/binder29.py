import cutil
import xmlutil

def setBinderItemsAttr(var,id,val=True) :
    cutil.setBinderAttr(var,id,"items",val)    


def dialogaction(action,var) :
  cutil.printVar("binder29",action,var)  
  
  if action == "before" :
    json = xmlutil.fileToS("docs/contacts.json")
#    print json
    setBinderItemsAttr(var,"list",json)
      
       
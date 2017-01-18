import cutil,xmlutil


def dialogaction(action,var) :    
    
   cutil.printVar("binder36",action,var)
   
   if action == "before" :
       cutil.setBinderListenOnAction(var,"basiccontext","opener")
       json = xmlutil.fileToS("docs/contacts.json")
       cutil.setBinderItemsAttr(var,"grid",json)
       cutil.setBinderListenOnAction(var,"gridcontext","grid")

       
   if action == "signalchange" and var["changefield"] == "basiccontext" :
       cutil.setToastText(var,"toast",var["basiccontext"])
       
   if action == "signalchange" and var["changefield"] == "gridcontext" :
       cutil.setToastText(var,"toast",var["gridcontext"])
       
import cutil,xmlutil

def dialogaction(action,var) :
  cutil.printVar("binder32",action,var)
  
  if action == "before" :
     json = xmlutil.fileToS("docs/elements.json")
     cutil.setBinderItemsAttr(var,"comboBox",json)
     cutil.setBinderItemsAttr(var,"comboBox2",json)
# comboBox2.setValue("Bohrium");     
     var["comboBox2"] = "Bohrium";
     cutil.setCopy(var,"comboBox2")
     
  if action == "signalchange" :
     cutil.setToastText(var,"toast","Value: " + var[var["changefield"]])


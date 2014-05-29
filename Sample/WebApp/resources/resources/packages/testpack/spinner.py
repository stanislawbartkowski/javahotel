import cutil


#def setSpinnerMin(var,fie,val) :
#    var["JSETATTR_FIELD_"+fie +"_spinnermin"] = val

#def setSpinnerMax(var,fie,val) :
#    var["JSETATTR_FIELD_"+fie +"_spinnermax"] = val
  

def dialogaction(action,var) :
  cutil.printVar("spinner",action,var)
  
  if action == "before" :
    var["number1"] = 4
    cutil.setCopy(var,"number1")
    
  if action == "setmin" and var["min"] != None :
#    var["JSETATTR_FIELD_number1_spinnermin"] = var["min"]
    cutil.setSpinnerMin(var,"number1",var["min"])

  if action == "setmax" and var["max"] != None :
#    var["JSETATTR_FIELD_number1_spinnermin"] = var["min"]
    cutil.setSpinnerMax(var,"number1",var["max"])

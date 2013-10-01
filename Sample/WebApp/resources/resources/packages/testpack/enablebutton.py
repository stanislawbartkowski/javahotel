import cutil

def clickaction(action,var) :
  
  cutil.printVar("enable",action,var)
  
  if action == "signalchange" :
    enable = var["enable"]
    var["JSETATTR_BUTTON_click_enable"] = enable
import cutil

def clickaction(action,var) :
  
  cutil.printVar("enable",action,var)
    
  if action=="before" :
    var["hide"] = False
    cutil.setCopy(var,"hide")
  
  if action == "signalchange" and var["changefield"] == "enable" :
    enable = var["enable"]
    cutil.enableButton(var,"click",enable)
    
  if action == "signalchange" and var["changefield"] == "hide" :
    hide = var["hide"]
    cutil.hideButton(var,"click",hide)
    
  if action == "signalchange" and var["changefield"] == "enableperson" :
    enable = var["enableperson"]
    cutil.enableField(var,"custid",enable)
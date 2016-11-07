import cutil

def dialogaction(action,var) :
  
  cutil.printVar("focus",action,var)
  
  if action == "fo1" :
    cutil.setFocus(var,"focus")
    
  if action == "fo2" :
    cutil.setFocus(var,"focus1")

  if action == "fo3" :
    cutil.setFocus(var,"focus3")
    
  if action == "fo4" :
    cutil.setFocus(var,"focus4")
    
  if action == "fo5" :
    cutil.setFocus(var,"focus5")

    
  if action == "signalchange" :  
      var["JYESNO_MESSAGE"] = "Set focus to FocusText1 ?"
      var["JAFTERDIALOG_ACTION"] = "setfocus1"      
      
  if action == "setfocus1" and var["JYESANSWER"] :  
      cutil.setFocus(var,"focus")

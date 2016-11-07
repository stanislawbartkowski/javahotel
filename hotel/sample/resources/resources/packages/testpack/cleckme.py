import cutil

def clickaction(action,var) :
   cutil.printVar("click action",action,var)
   
   if action == "click" :
     var["JUP_DIALOG"] = "buttonreturn.xml"
     var['JAFTERDIALOG_ACTION'] = "selectafter"
   
   if action == "selectafter" :
     res = var["JUPDIALOG_BUTTON"]
     var["JOK_MESSAGE"] = "Button : " + res
   
def returnaction(action,var) :
   cutil.printVar("return action",action,var)
   
   if action == "resign" : var["JCLOSE_DIALOG"] = True
   if action == "accept" and var['JYESANSWER'] : var["JCLOSE_DIALOG"] = True
     
import cutil 


def dialogaction(action,var) :
  
   cutil.printVar("image dialog",action,var)


   if action == "before" :
     cutil.setCopy(var,["imfield","imfieldd"])
     var["imfield"] = "1111"
     var["imfieldd"] = "ABC"
     
   if action == "copy" :
     cutil.setCopy(var,"imfieldd")
     var["imfieldd"] = var["imvalue"]
     
   if action == "clickimage" :
     var['JOK_MESSAGE'] = "Click " + var["changefield"] + " no:" + str(var["imagecolumn"])

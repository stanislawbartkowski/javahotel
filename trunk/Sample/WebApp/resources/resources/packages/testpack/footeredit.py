from cutil import printVar
import cutil
from cutil import modifDecimalFooter

def modifTextFooter(var,list,col,amountcol) :
   print "modiftext"
   prevval=var["JVALBEFORE"]
   val = var[col]
   if prevval and val : return
   if prevval==None and val==None : return
   footerdef = "JFOOTER_"+list+"_"+col
   currentfooter=var[footerdef]
   amount = var[amountcol]
   if val : currentfooter=cutil.addDecimal(currentfooter,amount)
   else : currentfooter=cutil.minusDecimal(currentfooter,amount)
   var[footerdef] = currentfooter
   var["JFOOTER_COPY_"+list+"_"+col] = True
   
def modifTextDecimalFooter(var,list,col,amountcol):
    print "modifdecimaltext"
    if var[col] == None : return
    footerdef = "JFOOTER_"+list+"_"+col
    currentfooter = var[footerdef]
    prevval = var["JVALBEFORE"]
    val = var[amountcol]
    currentfooter = cutil.minusDecimal(currentfooter,prevval)
    currentfooter = cutil.addDecimal(currentfooter,val)
    var[footerdef] = currentfooter
    var["JFOOTER_COPY_"+list+"_"+col] = True
   

def doaction(action,var) :
  printVar("footeredit",action,var)
  if action == "before" :
      cutil.setJMapList(var,"list",[])
      cutil.setAddEditMode(var,"list",["leftk","amount","rightk"])
      
  if action == "editlistrowaction"  :
    var["JLIST_EDIT_ACTIONOK_list"] = True
    
  if action == "columnchangeaction" and ((var["changefield"] == "leftk" or var["changefield"] == "rightk")) :
    modifTextFooter(var,"list",var["changefield"],"amount")

  if action == "columnchangeaction" and var["changefield"] == "amount" :
    modifTextDecimalFooter(var,"list","leftk","amount")
    modifTextDecimalFooter(var,"list","rightk","amount")


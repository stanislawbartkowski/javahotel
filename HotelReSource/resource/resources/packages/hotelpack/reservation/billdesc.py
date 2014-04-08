import cutil
from util import util
import con
from util import rutil

def billdesc(action,var) :
   cutil.printVar("bill desc",action,var)
   
   if action == "before" :
     payername = var["payer_name"]
     util.setCustData(var,payername,"payer_")
     LI = rutil.BILLPOSADD(var,"billlist")
     billname = var["billname"]
     b = util.BILLLIST(var).findElem(billname)
     pli = util.getPayments(var)
     for idp in b.getPayList() :
       for pa in pli :
         if con.eqUL(idp,pa.getId()) : 
            LI.addMa({},pa,idp)
     LI.close()     
     
   if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = "Are you sure that you want to remove this bill ?"
      var["JMESSAGE_TITLE"] = ""  
      return

   if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
     billname = var["billname"]
     util.BILLLIST(var).deleteElemByName(billname)
     var["JCLOSE_DIALOG"] = True      
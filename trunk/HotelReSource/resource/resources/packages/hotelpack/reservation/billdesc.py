import cutil,con

from util import util,rutil,cust

from rrutil import cbill

LIS="billlist"

class _BS(rutil.BILLSCAN) :
  
  def __init__(self,b,LI) :
    rutil.BILLSCAN.__init__(self, b.getPayList())
    self.LI = LI
    
  def walk(self,idp,pa) :
    self.LI.addMa({},pa,idp)

def billdesc(action,var) :
   cutil.printVar("bill desc",action,var)
   
   if action == "before" :
     payername = var["payer_name"]
     cust.setCustData(var,payername,"payer_")
     LI = cbill.BILLPOSADD(var,LIS)
     billname = var["billname"]
     b = util.BILLLIST(var).findElem(billname)
     
     B = _BS(b,LI)
     B.scan( rutil.getPayments(var))
     LI.close()     
     
   if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = "@doyouwantremovebill"
      return

   if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
     billname = var["billname"]
     util.BILLLIST(var).deleteElemByName(billname)
     var["JCLOSE_DIALOG"] = True      
     
   if action == "guestdetail" :
       cust.showCustomerDetails(var,var["guest_name"])
  
import cutil

from util import util,cust

M = util.MESS()
    
CLIST=cust.getCustFieldId()

def _createList(var):
    seq = cust.createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}    
    
def _duplicatedCustomerName(var):    
    R = util.CUSTOMERLIST(var)
    return util.duplicatedName(var,R,M("DUPLICATEDCUSTOMERNAME"))    
        
def customerlistaction(action,var):
  cutil.printVar("customerlistaction",action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
  if action == "info" :
    custname = var["name"]
    var["JUPDIALOG_START"] = custname
    var["JUP_DIALOG"]="hotel/reservation/customerresinfo.xml"
        
def elemcustomeraction(action,var):
  cutil.printVar("elemcustomeraction",action,var)
  R = util.CUSTOMERLIST(var)
    
  if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
    cust.setDefaCustomer(var)    
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedCustomerName(var) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWCUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      R.addElem(cust.customerFromVar(var))
      cust.saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      R.changeElem(cust.customerFromVar(var))      
      cust.saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      l = util.RESOP(var).getReseForCustomer(var["name"])
      l1 = util.RESOP(var).getReseForGuest(var["name"])
      l2 = util.RESOP(var).getReseForPayer(var["name"])
      if len(l) > 0 or len(l1) > 0 or len(l2) > 0 :
         var["JERROR_MESSAGE"] = M("cannotremovecustomer").format(len(l),len(l1),len(l2))
         return
      var["JYESNO_MESSAGE"] = M("REMOVECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      R.deleteElem(cust.customerFromVar(var))
      var["JCLOSE_DIALOG"] = True       
      
    
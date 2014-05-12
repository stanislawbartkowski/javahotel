import cutil

from util import util

M = util.MESS()
    
CLIST=util.getCustFieldId()

def createList(var):
    seq = util.createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}    
    
def _duplicatedCustomerName(var):    
    R = util.CUSTOMERLIST(var)
    return util.duplicatedName(var,R,M("DUPLICATEDCUSTOMERNAME"))    
        
def customerlistaction(action,var):
  cutil.printVar("customerlistaction",action,var)
  
  if action == "before" or action == "crud_readlist" :
    createList(var)
    
def elemcustomeraction(action,var):
  cutil.printVar("elemcustomeraction",action,var)
  R = util.CUSTOMERLIST(var)
    
  if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
    util.setDefaCustomer(var)    
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedCustomerName(var) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWCUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      R.addElem(util.customerFromVar(var))
      util.saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      R.changeElem(util.customerFromVar(var))      
      util.saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      R.deleteElem(util.customerFromVar(var))
      var["JCLOSE_DIALOG"] = True              
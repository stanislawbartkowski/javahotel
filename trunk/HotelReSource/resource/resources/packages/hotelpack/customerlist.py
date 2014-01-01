from util.util import printvar
from util.util import CUSTOMERLIST
from util.util import toVarNameDesc
from util.util import toVar
from util.util import duplicatedName
from util.util import copyNameDescr
from util.util import toP
from util.util import MESS
from util.util import newCustomer
from util.util import getCustFieldId
from util.util import createCustomerList
from util.util import customerFromVar
from util.util import setDefaCustomer
from util.util import saveDefaCustomer

M = MESS()
    
CLIST=getCustFieldId()

def createList(var):
    seq = createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}    
    
def _duplicatedCustomerName(var):    
    R = CUSTOMERLIST(var)
    return duplicatedName(var,R,M("DUPLICATEDCUSTOMERNAME"))    
        
def customerlistaction(action,var):
  printvar("customerlistaction",action,var)
  
  if action == "before" or action == "crud_readlist" :
    createList(var)
    
def elemcustomeraction(action,var):
  printvar("elemcustomeraction",action,var)
  R = CUSTOMERLIST(var)
    
  if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
    setDefaCustomer(var)    
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedCustomerName(var) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWCUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      R.addElem(customerFromVar(var))
      saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      R.changeElem(customerFromVar(var))      
      saveDefaCustomer(var)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      R.deleteElem(customerFromVar(var))
      var["JCLOSE_DIALOG"] = True              
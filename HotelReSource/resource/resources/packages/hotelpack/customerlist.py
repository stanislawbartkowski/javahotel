from util.util import printvar
from util.util import CUSTOMERLIST
from util.util import toVarNameDesc
from util.util import toVar
from util.util import duplicatedName
#from com.gwthotel.hotel.customer import HotelCustomer
from util.util import copyNameDescr
from util.util import toP
from util.util import MESS
from util.util import newCustomer

M = MESS()
    
CLIST=["firstname","companyname","street","zipcode","email","phone"]

def _createList(var):
    C = CUSTOMERLIST(var)
    seq = []
    for c in C.getList() :
        v = {}
        toVarNameDesc(v,c)
        toVar(v,c,CLIST)
        seq.append(v)
    var["JLIST_MAP"] = { "customerlist" : seq}    
    
def _duplicatedCustomerName(var):    
    R = CUSTOMERLIST(var)
    return duplicatedName(var,R,M("DUPLICATEDCUSTOMERNAME"))    
    
def _createCustomer(var):
    h = newCustomer(var)
    copyNameDescr(h,var)
    toP(h,var,CLIST)
    return h
    
def customerlistaction(action,var):
  printvar("customerlistaction",action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
def elemcustomeraction(action,var):
  printvar("elemcustomeraction",action,var)
  R = CUSTOMERLIST(var)
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedCustomerName(var) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWCUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      R.addElem(_createCustomer(var))
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      R.changeElem(_createCustomer(var))      
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVECUSTOMERASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      R.deleteElem(_createCustomer(var))
      var["JCLOSE_DIALOG"] = True              
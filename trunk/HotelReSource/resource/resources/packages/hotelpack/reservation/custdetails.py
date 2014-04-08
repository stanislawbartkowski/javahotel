from cutil import setCopy
from util.util import createCustomerList
from util.util import getCustFieldId
from util.util import CUSTOMERLIST
from cutil import copyPropToVar
from util.util import getCustFieldIdAll
from util.util import setCustVarCopy
from util.util import setCustData
import cutil
from util import util

CLIST = getCustFieldIdAll()

def custdetails(action,var):
  cutil.printVar ("custdetails", action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      util.xmlToVar(var,xml,CLIST,"de_")
      setCustVarCopy(var,"de_")
      
  if action == "accept" :
      var["JCLOSE_DIALOG"] = util.mapToXML(var,CLIST,"de_")
      
  if action == "selectcustomer" :
      c_name = var["JUPDIALOG_RES"]
      if c_name == None : return      
      setCustData(var,c_name,"de_")

def custlist (action,var):
  cutil.printVar ("list", action,var)
  if action == "before" :
    seq = createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}  
  if action == "select" :
      set = var["customerlist_lineset"]
      if not set : return
      var["JCLOSE_DIALOG"] = var["name"]
              
def showcustdetails(action,var):
  cutil.printVar ("showcustdetails", action,var)
  if action == "before" :
      custid =  var["JUPDIALOG_START"]
      setCustData(var,custid,"cust_")
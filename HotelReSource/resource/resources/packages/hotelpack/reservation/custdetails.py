from util.util import printvar
from cutil import setCopy
from util.util import createCustomerList
from util.util import getCustFieldId
from util.util import CUSTOMERLIST
from util.util import xmlToVar
from util.util import mapToXML

CLIST = ["name","descr"] + getCustFieldId()

def custdetails(action,var):
  printvar ("custdetails,", action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      xmlToVar(var,xml,CLIST,"de_")
      setCopy(var,CLIST,None,"de_")
      
  if action == "accept" :
      var["JCLOSE_DIALOG"] = mapToXML(var,CLIST,"de_")
      print "---",var["JCLOSE_DIALOG"]
      
  if action == "selectcustomer" :
      c_name = var["JUPDIALOG_RES"]
      if c_name == None : return
      
      C = CUSTOMERLIST(var)
      cu = C.findElem(c_name)
      print "name=!!!",cu.getName(),"!!",cu.getAttr("name"),"###"
      li = []
      for s in CLIST :
          deid = "de_" + s
          li.append(deid)
          var[deid] = cu.getAttr(s)
          print deid, var[deid]
      setCopy(var,li)    

def custlist (action,var):
  printvar ("list,", action,var)
  if action == "before" :
    seq = createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}  
  if action == "select" :
      set = var["customerlist_lineset"]
      if not set : return
      var["JCLOSE_DIALOG"] = var["name"]
              
def showcustdetails(action,var):
  printvar ("showcustdetails,", action,var)
  if action == "before" :
      li = []
      for s in CLIST :
          cuid = "cust_" + s
          li.append(cuid)
      setCopy(var,li)
      
    
      
 
from util.util import printvar
from util.util import setCopy
from util.util import createCustomerList
from util.util import getCustFieldId

CLIST = ["name","descr"] + getCustFieldId()

def custdetails(action,var):
  printvar ("custdetails,", action,var)
  if action == "before" :
      li = []
      for s in CLIST :
          deid = "de_" + s
          cuid = "cust_" + s
          var[deid] = var[cuid]
          li.append(deid)
      setCopy(var,li)
      
  if action == "accept" :
      li = []
      for s in CLIST :
          deid = "de_" + s
          cuid = "cust_" + s
          var[cuid] = var[deid]
          li.append(cuid)
      var["JCLOSE_DIALOG"] = True
      setCopy(var,li)

def custlist (action,var):
  printvar ("list,", action,var)
  if action == "before" :
    seq = createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}  
  if action == "select" :
      set = var["customerlist_lineset"]
      if not set : return
      var["JCLOSE_DIALOG"] = True
      li = []
      for s in CLIST :
          deid = "de_" + s
          li.append(deid)
          var[deid] = var[s]
      setCopy(var,li)    
              
def showcustdetails(action,var):
  printvar ("showcustdetails,", action,var)
  if action == "before" :
      li = []
      for s in CLIST :
          cuid = "cust_" + s
          li.append(cuid)
      setCopy(var,li)
      
    
      
 
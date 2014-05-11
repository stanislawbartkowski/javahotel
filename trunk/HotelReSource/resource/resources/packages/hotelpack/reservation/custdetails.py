import cutil

from util import util

CLIST = util.getCustFieldIdAll()

DE="de_"

def custdetails(action,var):
  cutil.printVar ("custdetails", action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      util.xmlToVar(var,xml,CLIST + [util.CUSTACTION],DE)
      action = var[DE + util.CUSTACTION]
      print action
      if action == util.CUSTSHOWTOACTIVE :
        custid = var[DE + "name"]
        custR = util.CUSTOMERLIST(var).findElem(custid)
        assert custR != None
        util.customerToVar(var,custR,DE)
        util.setCustVarCopy(var,DE)
#        util.enableCust(var,DE,False)
        cutil.hideButton(var,["accept","ok"])
      else : 
        util.setCustVarCopy(var,DE)
        cutil.hideButton(var,["acceptask","ok"])
        
      
  if action == "accept" :
      var["JCLOSE_DIALOG"] = util.mapToXML(var,CLIST,DE)

  if action == "acceptask" and var["JYESANSWER"] :
      util.CUSTOMERLIST(var).changeElem(util.customerFromVar(var,DE))
      var["JCLOSE_DIALOG"] = util.mapToXML(var,CLIST,DE)
      
  if action == "selectcustomer" :
      c_name = var["JUPDIALOG_RES"]
      if c_name == None : return      
      util.setCustData(var,c_name,DE)

def custlist (action,var):
  cutil.printVar ("list", action,var)
  if action == "before" :
    seq = util.createCustomerList(var)
    var["JLIST_MAP"] = { "customerlist" : seq}  
  if action == "select" :
      set = var["customerlist_lineset"]
      if not set : return
      var["JCLOSE_DIALOG"] = var["name"]
              
def showcustdetails(action,var):
  cutil.printVar ("showcustdetails", action,var)
  if action == "before" :
      custid =  var["JUPDIALOG_START"]
      util.setCustData(var,custid,"cust_")
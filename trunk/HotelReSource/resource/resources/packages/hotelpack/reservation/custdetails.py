import cutil

from util import util

CLIST = util.getCustFieldIdAll()

DE="de_"
CUSTLIST="customerlist"

def custdetails(action,var):
  cutil.printVar ("custdetails", action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      util.xmlToVar(var,xml,CLIST + [util.CUSTACTION],DE)
      action = var[DE + util.CUSTACTION]
      if action == util.CUSTSHOWTOACTIVE or action == util.CUSTSHOWONLY :
        custid = var[DE + "name"]
        custR = util.CUSTOMERLIST(var).findElem(custid)
        assert custR != None
        util.customerToVar(var,custR,DE)
        util.setCustVarCopy(var,DE)
        if action == util.CUSTSHOWTOACTIVE : cutil.hideButton(var,["accept","ok"])
        else : 
          cutil.hideButton(var,["accept","acceptask","resign","find"])
          util.enableCust(var,DE,False)
      else:
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
    var["JLIST_MAP"] = {CUSTLIST : seq}  
    
  if action == "select" and var[CUSTLIST +"_lineset"]:
      var["JCLOSE_DIALOG"] = var["name"]
              
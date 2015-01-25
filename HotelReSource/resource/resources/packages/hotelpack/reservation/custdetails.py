import cutil

from util import util,cust

CLIST = cust.getCustFieldIdAll()

DE="de_"
CUSTLIST="customerlist"

def custdetails(action,var):
  cutil.printVar ("custdetails", action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      util.xmlToVar(var,xml,CLIST + [cust.CUSTACTION],DE)
      action = var[DE + cust.CUSTACTION]
      if action == cust.CUSTSHOWTOACTIVE or action == cust.CUSTSHOWONLY :
        custid = var[DE + "name"]
        custR = util.CUSTOMERLIST(var).findElem(custid)
        assert custR != None
        cust.customerToVar(var,custR,DE)
        cust.setCustVarCopy(var,DE)
        if action == cust.CUSTSHOWTOACTIVE : cutil.hideButton(var,["accept","ok"])
        else : 
          cutil.hideButton(var,["accept","acceptask","resign","find"])
          cust.enableCust(var,DE,False)
      else:
        cust.setCustVarCopy(var,DE)
        cutil.hideButton(var,["acceptask","ok"])
      
  if action == "accept" :
      var["JCLOSE_DIALOG"] = util.mapToXML(var,CLIST,DE)

  if action == "acceptask" and var["JYESANSWER"] :
      util.CUSTOMERLIST(var).changeElem(cust.customerFromVar(var,DE))
      var["JCLOSE_DIALOG"] = util.mapToXML(var,CLIST,DE)
      
  if action == "selectcustomer" :
      c_name = var["JUPDIALOG_RES"]
      if c_name == None : return      
      cust.setCustData(var,c_name,DE)

def custlist (action,var):
  cutil.printVar ("list", action,var)
  
  if action == "before" :
    seq = cust.createCustomerList(var)
    cutil.setJMapList(var,CUSTLIST,seq)
    
  if action == "select" and var[CUSTLIST +"_lineset"]:
      var["JCLOSE_DIALOG"] = var["name"]
              
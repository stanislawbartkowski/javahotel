import cutil
from util.util import PAYMENTOP
import con
from util.util import BILLLIST
from util import rutil
from util import util


PAYLIST="paymentslist"
BILLNAME="paybillname"

def _getPayName(var) :
  return var[BILLNAME]

def _createList(var,billName) :
  pList = PAYMENTOP(var).getPaymentsForBill(billName)
  suma = 0.0
  li = []
  for p in pList :
    id = p.getId()
    total = p.getPaymentTotal()
    date = p.getDateOfPayment()
    method = p.getPaymentMethod()
    suma = con.addDecimal(suma,con.BigDecimalToDecimal(total))
    ma = { "id" : id, "paymethod" : method, "paymentdate" : date, "paymenttotal" : total }
    li.append(ma)
  cutil.setJMapList(var,PAYLIST,li)
  cutil.setFooter(var,PAYLIST,"paymenttotal",suma)
  
  
def _setChanged(var) :
  var["billlistwaschanged"] = True
  cutil.setCopy(var,["billlistwaschanged",])

def listofpayments(action,var) :
  cutil.printVar("listofpayments",action,var)
  
  if action == "before" or action == "crud_readlist" :
     if action == "before" :
        billname=var["JUPDIALOG_START"]
        var[BILLNAME] = billname
#        var["waschanged"] = False
        cutil.setCopy(var,[BILLNAME,])
        
     billname = _getPayName(var)   
     _createList(var,billname)
  
def elempayment(action,var) :  
    cutil.printVar("elempayment",action,var)
    
    if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
      var["paymentdate"] = cutil.today()
      b = BILLLIST(var).findElem(_getPayName(var))
      sumtotal = rutil.countTotal(var,b,rutil.getPayments(var))
      sumpay = rutil.countPayments(var,_getPayName(var))
      acc = con.minusDecimal(sumtotal,sumpay)
      if acc < 0 : acc = None
      var["paymenttotal"] = acc
      cutil.setCopy(var,["paymentdate","paymenttotal"])
      
    if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = "Do you want to add payment to this bill ?"
      var["JMESSAGE_TITLE"] = ""  
      return
      
    if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
       p = util.newBillPayment()
       p.setBillName(_getPayName(var))
       p.setPaymentMethod(var["paymethod"])
       p.setDateOfPayment(con.toDate(con.today()))
       p.setPaymentTotal(con.toB(var["paymenttotal"]))
       PAYMENTOP(var).addPaymentForBill(_getPayName(var),p)       
       var["JCLOSE_DIALOG"] = True
       _setChanged(var)
       return
      
    if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = "Are you sure to remove payment to this bill ?"
      var["JMESSAGE_TITLE"] = ""  
      return
  
    if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      id = var["id"]
      PAYMENTOP(var).removePaymentForBill(_getPayName(var),id)
      _setChanged(var)
      var["JCLOSE_DIALOG"] = True                

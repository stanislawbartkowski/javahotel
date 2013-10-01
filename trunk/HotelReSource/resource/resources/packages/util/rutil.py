#from util.util import PAYMENTOP
import cutil
import con
import util


def getReseName(var) :
  return var["resename"]

def getPayments(var) :    
  rese = getReseName(var)
  pli = util.RESOP(var).getResAddPaymentList(rese)
  R = util.RESFORM(var)
  # java.util.List
  pli.addAll(R.findElem(rese).getResDetail())
  return pli

def countPayments(var,billName) :
  pList = util.PAYMENTOP(var).getPaymentsForBill(billName)
  suma = 0.0
  for p in pList :
    total = p.getPaymentTotal()
    suma = cutil.addDecimal(suma,con.BigDecimalToDecimal(total))
  return suma  

def countTotal(var,b,pli) :
  """ Counts total sum of services for bill
  
  Args: 
    var
    b CustomerBill
    pli List of services (return result of getPayments)   
    
  Returns: sum of services for b (CustomerBill)  
  """
  total = 0.0
  for idp in b.getPayList() :
    for pa in pli :
       if con.eqUL(idp,pa.getId()) :
         to = con.BigDecimalToDecimal(pa.getPriceTotal())
         total = con.addDecimal(total,to)
         
  return total       

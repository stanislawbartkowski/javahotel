from cutil import printVar
from util.util import RESFORM
from util.util import ROOMLIST
from util.util import SERVICES
from util.util import RESOP
from cutil import setJMapList
from util.util import isRoomService
from util.util import getReseName
from cutil import setStandEditMode
from cutil import addDecimal
from cutil import minusDecimal
from cutil import setFooter
from cutil import BigDecimalToDecimal
from sets import Set
from util.util import BILLLIST
from util.util import newBill
from util.util import listNumberToCVS
from util.util import CVSToListNumber
from util.util import setCustData
from cutil import today
from cutil import toDate
from util.util import getPayments
from util.util import BILLPOSADD
from con import toL
from util.util import getCustFieldId
from util.util import mapToXML
from util.util import xmlToVar
from cutil import setCopy
from util.util import HOTELTRANSACTION
from cutil import setCopy
from util.util import MESS
from cutil import checkEmpty
from cutil import setErrorField
from util.util import PAYMENTOP
from util.util import newBillPayment
from con import toB

LIST="poslist"
NOPAID="billlist"
CLIST = ["name","descr"] + getCustFieldId()
M=MESS()

class PAID :
  
  def __init__(self,var) :
    rese = getReseName(var)
    bli = RESOP(var).findBillsForReservation(rese)
    self.se = Set()
    for b in bli :
      for l in b.getPayList() :
        self.se.add(toL(l))
 
  def onList(self,id) :
    return id in self.se
    
def _createPosList(var) :
  pli = getPayments(var)
  P = PAID(var)
  L1 = BILLPOSADD(var,LIST)
  L2 = BILLPOSADD(var,NOPAID)
  # list of bills
  sumf = 0.0
  for r in pli :
    idp = r.getId()
    if P.onList(idp) :
      ma = { "billed" : True }
    else :
      ma = { "billed" : False }
      L2.addMa( { "add" : True },r,idp)
      
    L1.addMa(ma,r,idp)  

  L1.close()
  L2.close()
  
  setStandEditMode(var,NOPAID,["add"])
                  
class HOTELBILLSAVE(HOTELTRANSACTION) :
  
    def __init__(self,var) :
      HOTELTRANSACTION.__init__(self,0,var)
    
    def run(self,var) :
     self.total = 0.0
     P = PAID(var)
     b = newBill(var)
     cust_name = var["payer_name"]
     b.setGensymbol(True);
     b.setPayer(cust_name)
     b.setReseName(getReseName(var))
     b.setIssueDate(toDate(today()))
     for m in var["JLIST_MAP"][NOPAID] :
       if m["add"] :
          idp = m["idp"]
          self.total = addDecimal(self.total,m["total"])
          if P.onList(idp) :
             var["JERROR_MESSAGE"] = "Trying to pay again. Check if someone else is billing just now !"
             return
          b.getPayList().add(idp)
         
     if b.getPayList().size() == 0 :
         var["JERROR_MESSAGE"] = "Nothing is checked"
         return
      
     self.billName = BILLLIST(var).addElem(b).getName()
     var["JCLOSE_DIALOG"] = True

def doaction(action,var) :
  printVar("create bill",action,var)
  
  if action == "before" :
    _createPosList(var)
    # payer
    rese = getReseName(var)
    R = RESFORM(var)
    r = R.findElem(rese)
    payername = r.getCustomerName()
    setCustData(var,payername,"payer_")
    var["paynow"] = True
    setCopy(var,["paynow"])

  if action == "columnchangeaction" :
     total = var["total"]
     footerf = var["JFOOTER_billlist_total"]
     if var["add"] : footerf = addDecimal(footerf,total)
     else : footerf = minusDecimal(footerf,total)
     setFooter(var,"billlist","total",footerf)

  if action == "accept" : 
    exist = False 
    for m in var["JLIST_MAP"][NOPAID] :
      if m["add"] : exist = True
    if not exist :
      var["JERROR_MESSAGE"] = "Nothing is checked"
      return
    
    if var["paynow"] :
      if checkEmpty(var,["paymethod"]): return
      
    if not var["paynow"] :  
      if checkEmpty(var,["paymethod","paymentdate"]): return
      
    var["JYESNO_MESSAGE"] = "Are you sure to issue this bill ?"
    var['JAFTERDIALOG_ACTION'] = "acceptafteryes"

  if action == "acceptafteryes" and var["JYESANSWER"] :    
     H = HOTELBILLSAVE(var)
     H.doTrans()
     # semaphore transction is not needed here
     if var["paynow"] :
       billName = H.billName
       p = newBillPayment()
       p.setBillName(billName)
       p.setPaymentMethod(var["paymethod"])
       p.setDateOfPayment(toDate(today()))
       p.setPaymentTotal(toB(H.total))
       PAYMENTOP(var).addPaymentForBill(billName,p)       
              
  if action == "payerdetails" :
      var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
      var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
      var["JUPDIALOG_START"] = mapToXML(var,CLIST,"payer_")

  if action == "acceptdetails" :
     xml = var["JUPDIALOG_RES"]
     xmlToVar(var,xml,CLIST,"payer_")
     setCopy(var,CLIST,None,"payer_")

         
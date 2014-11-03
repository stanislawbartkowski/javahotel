from sets import Set

import cutil
import con

from util import util
from util import rutil

from rrutil import resstat

LIST="poslist"
NOPAID="billlist"
CLIST=util.getCustFieldIdAll()
M=util.MESS()
PAY="payer_"

class PAID :
  
  def __init__(self,var) :
    rese = rutil.getReseName(var)
    bli = util.RESOP(var).findBillsForReservation(rese)
    self.se = Set()
    for b in bli :
      for l in b.getPayList() :
        self.se.add(con.toL(l))
 
  def onList(self,id) :
    return id in self.se
    
def _createPosList(var) :
  pli = rutil.getPayments(var)
  P = PAID(var)
  L1 = rutil.BILLPOSADD(var,LIST)
  L2 = rutil.BILLPOSADD(var,NOPAID)
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
  
  cutil.setStandEditMode(var,NOPAID,["add"])
                  
class HOTELBILLSAVE(util.HOTELTRANSACTION) :
  
    def __init__(self,var) :
      util.HOTELTRANSACTION.__init__(self,0,var)
    
    def run(self,var) :
     self.total = 0.0
     P = PAID(var)
     b = util.newBill(var)
     cust_name = var["payer_name"]
     b.setGensymbol(True);
     b.setPayer(cust_name)
     b.setReseName(rutil.getReseName(var))
     b.setIssueDate(cutil.toDate(cutil.today()))
     for m in var["JLIST_MAP"][NOPAID] :
       if m["add"] :
          idp = m["idp"]
          self.total = cutil.addDecimal(self.total,m["total"])
          if P.onList(idp) :
             var["JERROR_MESSAGE"] = "@billalreadypaid"
             return
          b.getPayList().add(idp)
         
     if b.getPayList().size() == 0 :
         var["JERROR_MESSAGE"] = "@nothingischecked"
         return
      
     self.billName = util.BILLLIST(var).addElem(b).getName()
     var["JCLOSE_DIALOG"] = True

def doaction(action,var) :
  cutil.printVar("create bill",action,var)
  
  if action == "before" :
    _createPosList(var)
    # payer
    rese = rutil.getReseName(var)
    R = util.RESFORM(var)
    r = R.findElem(rese)
    payername = r.getCustomerName()
    util.setCustData(var,payername,PAY)
    var["paynow"] = True
    cutil.setCopy(var,["paynow","paymethod"])
    var["paymethod"] = util.HOTELDEFADATA().getDataH(3)
    RR = resstat.getResStatusR(var,r)
    var["advance_pay_left"] = RR.advancepaymentleft
    var["advance_pay"] = RR.advancepayment
    cutil.setCopy(var,["advance_pay","advance_pay_left"])
    
  if action == "guestdetail" :
       util.showCustomerDetails(var,var["guest_name"])
    
  if action == "columnchangeaction" :
     total = var["total"]
     footerf = var["JFOOTER_billlist_total"]
     if var["add"] : footerf = cutil.addDecimal(footerf,total)
     else : footerf = cutil.minusDecimal(footerf,total)
     cutil.setFooter(var,"billlist","total",footerf)

  if action == "accept" : 
    exist = False 
    for m in var["JLIST_MAP"][NOPAID] :
      if m["add"] : exist = True
    if not exist :
      var["JERROR_MESSAGE"] = "@nothingischecked"
      return
    
    if var["paynow"] :
      if cutil.checkEmpty(var,["paymethod"]): return
      
    if not var["paynow"] :  
      if cutil.checkEmpty(var,["paymethod","paymentdate"]): return
      
    var["JYESNO_MESSAGE"] = "@areyousuretoissuebill"
    var['JAFTERDIALOG_ACTION'] = "acceptafteryes"

  if action == "acceptafteryes" and var["JYESANSWER"] :    
     util.HOTELDEFADATA().putDataH(3,var["paymethod"])
     H = HOTELBILLSAVE(var)
     H.doTrans()
     # semaphore transction is not needed here
     if var["paynow"] :
       billName = H.billName
       total = H.total
       # advance payment
       r = util.RESFORM(var).findElem(rutil.getReseName(var))       
       RR = resstat.getResStatusR(var,r)
       li = []
       if RR.advancepaymentleft > 0 :
	 payment = min(total,RR.advancepaymentleft) 
	 li.append((payment,True))
	 total = con.minusDecimal(total,payment)
       if total > 0 : li.append((total,False))
	
       for l in li :	
         p = util.newBillPayment()
         p.setBillName(billName)
         p.setPaymentMethod(var["paymethod"])
         p.setDateOfPayment(cutil.toDate(cutil.today()))
         p.setPaymentTotal(con.toB(l[0]))         
         if (l[1]) : p.setAdvancepayment(True)
         util.PAYMENTOP(var).addPaymentForBill(billName,p)       
              
  if action == "payerdetails" :
#      var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
      var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
      util.customerDetailsActive(var,PAY)
#      var["JUPDIALOG_START"] = util.mapToXML(var,CLIST,PAY)

  if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" :
     xml = var["JUPDIALOG_RES"]
     util.xmlToVar(var,xml,CLIST,PAY)
     cutil.setCopy(var,CLIST,None,PAY)

         
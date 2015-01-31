from sets import Set

import cutil,con,listbl

from util import util,rutil,rpdf,cust,diallaunch

from rrutil import resstat,cbill

LIST="poslist"
NOPAID="billlist"
CLIST=cust.getCustFieldIdAll()
M=util.MESS()
PAY="payer_"
DOCTYPE="documtype"
    
def _createPosList(var) :
  pli = rutil.getPayments(var)
  P = cbill.PAID(var,rutil.getReseName(var))
  L1 = cbill.BILLPOSADD(var,LIST)
  L2 = cbill.BILLPOSADD(var,NOPAID)
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
                  
class HOTELBILLSAVE(cbill.HOTELBILLSAVE) :
  
    def __init__(self,var,xml) :
      paym = None
      if var["paynow"] : paym = var["paymethod"]
      cbill.HOTELBILLSAVE.__init__(self,var,rutil.getReseName(var),var["payer_name"],xml,paym)
    
def doaction(action,var) :
  cutil.printVar("create bill",action,var)
  
  if action == "before" :
    _createPosList(var)
    # payer
    rese = rutil.getReseName(var)
    R = util.RESFORM(var)
    r = R.findElem(rese)
    payername = r.getCustomerName()
    cust.setCustData(var,payername,PAY)
    var["paynow"] = True
    cutil.setCopy(var,["paynow","paymethod",DOCTYPE])
    var["paymethod"] = util.HOTELDEFADATA().getDataH(3)
    var[DOCTYPE] = util.HOTELDEFADATA().getDataH(5)
    RR = resstat.getResStatusR(var,r)
    var["advance_pay_left"] = RR.advancepaymentleft
    var["advance_pay"] = RR.advancepayment
    cutil.setCopy(var,["advance_pay","advance_pay_left"])
    
  if action == "guestdetail" :
       cust.showCustomerDetails(var,var["guest_name"])
    
  if action == "columnchangeaction" :
     total = var["total"]
     footerf = var["JFOOTER_billlist_total"]
     if var["add"] : footerf = cutil.addDecimal(footerf,total)
     else : footerf = cutil.minusDecimal(footerf,total)
     cutil.setFooter(var,"billlist","total",footerf)

  if action == "accept" : 
    pli = []
    for m in var["JLIST_MAP"][NOPAID] :
      if m["add"] : 
	pli.append(m["idp"])
      
    if len(pli) == 0 :
      var["JERROR_MESSAGE"] = "@nothingischecked"
      return
    
    if var["paynow"] :
      if cutil.checkEmpty(var,["paymethod"]): return
      
    if not var["paynow"] :  
      if cutil.checkEmpty(var,["paymethod","paymentdate"]): return
    
    cust.saveCustomerData(var,var["payer_name"],PAY)
    
    xml = rpdf.buildXMLForStay(var,rutil.getReseName(var),var["payer_name"],pli)
    
    diallaunch.displayDocument(var,xml)
      
  if action == "acceptdocument" and var["JUPDIALOG_BUTTON"] == "accept" :    
     util.HOTELDEFADATA().putDataH(3,var["paymethod"])
     util.HOTELDEFADATA().putDataH(5,var[DOCTYPE])     
     xml = var["JUPDIALOG_RES"]
     H = HOTELBILLSAVE(var,xml)
     if H.doTransRes() : var["JCLOSE_DIALOG"] = True
              
  if action == "payerdetails" :
      var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
      cust.customerDetailsActive(var,PAY)

  if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" :
     xml = var["JUPDIALOG_RES"]
     util.xmlToVar(var,xml,CLIST,PAY)
     cutil.setCopy(var,CLIST,None,PAY)

         
import cutil
import con
from util import rutil
from util import util
from util import rpdf
import pdfutil

from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()

BILLIST="billlist"

def _listOfPayments(var) :
  rese = util.getReseName(var)
  li = util.RESOP(var).getResAddPaymentList(rese)
  seq = []
  sum = util.SUMBDECIMAL()  
  CU = util.CUSTOMERLIST(var)
  for e in li :
    g = e.getGuestName()    
    customer = CU.findElem(g)
    assert customer != None
    map = { "roomid" : e.getRoomName(), "total" : e.getPriceTotal(), "price" : e.getPrice(), "servdescr" : e.getDescription(), "quantity" : e.getQuantity() }
    util.toCustomerVar(map,customer,"guest_")
    seq.append(map)
    sum.add(e.getPriceTotal())
  cutil.setJMapList(var,"paymentlist",seq)
  cutil.setFooter(var,"paymentlist","total",sum.sum)

def _countTotal(var,b,pli) :  
  total = 0.0
  for idp in b.getPayList() :
    for pa in pli :
       if con.eqUL(idp,pa.getId()) :
         to = cutil.BigDecimalToDecimal(pa.getPriceTotal())
         total = cutil.addDecimal(total,to)
         
  return total       
     
def _ListOfBills(var) :
   rese = util.getReseName(var)
   bList = util.RESOP(var).findBillsForReservation(rese)
   seq = []
   pli = util.getPayments(var)
   sumtotal = 0.0
   footerpayments = 0.0
   CU = util.CUSTOMERLIST(var)
   for b in bList :
     bName = b.getName()
     assert bName != None
     payer = b.getPayer()
     customer = CU.findElem(payer)
     assert customer != None
     da = b.getIssueDate()     
     tot = _countTotal(var,b,pli)
     sumtotal = cutil.addDecimal(sumtotal,tot)
     paysum = rutil.countPayments(var,bName)
     footerpayments = cutil.addDecimal(footerpayments,paysum)
     ma = { "billname" : bName, "billtotal" : tot, "payments" : paysum }
     util.toCustomerVar(ma,customer,"payer_")
     seq.append(ma)
   cutil.setJMapList(var,BILLIST,seq)
   cutil.setFooter(var,BILLIST,"billtotal",sumtotal) 
   cutil.setFooter(var,BILLIST,"payments",footerpayments) 
  
def _setChangedFalse(var) :
   var["billlistwaschanged"] = False
   cutil.setCopy(var,"billlistwaschanged")  
  
def showstay(action,var):
   cutil.printVar("show stay",action,var)
   if action == "before" :
     rutil.setvarBefore(var)
     # after 
     _listOfPayments(var)
     _ListOfBills(var)
     _setChangedFalse(var)
   
   if var["billlistwaschanged"] :
    _setChangedFalse(var)   
    _ListOfBills(var)
     
   if action == "crud_readlist" and var["JLIST_NAME"] == BILLIST :
     _ListOfBills(var)     
     
   if action == "afterbill" and var["JUPDIALOG_BUTTON"] == "acceptafteryes" :
     _ListOfBills(var)
     
   if action == "payerdetail" :
      util.showCustomerDetails(var,var["payer_name"])
     
   if action == "changetoreserv" and var["JYESANSWER"] :
     res = getReservForDay(var)
     resname = res[0].getResId()
     R = util.RESOP(var)
     R.changeStatusToReserv(resname)
     a = cutil.createArrayList()
     R.setResGuestList(resname,a)
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
   if action == "addpayment" :
      var["JUP_DIALOG"]="hotel/reservation/addpayment.xml" 
      var["JAFTERDIALOG_ACTION"] = "afteraddpayment" 
      
   if action == "paymentslist" :
      assert var["billname"] != None
      var["JUP_DIALOG"]="hotel/reservation/listofpayment.xml" 
      var["JAFTERDIALOG_ACTION"] = "afterlistpayments"
      var["JUPDIALOG_START"] = var["billname"]
      
   if action == "printbill" and var["billlist_lineset"] :
      var["JUP_DIALOG"]="hotel/reservation/billprint.xml" 
      var["JUPDIALOG_START"] = var["billname"]
      
   if action == "afteraddpayment" and var["JUPDIALOG_BUTTON"] == "addpayment" :
     _listOfPayments(var)
        
   if action == "guestdesc" :
       util.showCustomerDetails(var,var["cust_name"])
       
   if action == "guestdetail" :
       util.showCustomerDetails(var,var["guest_name"])


def billprint(action,var) :
   cutil.printVar("bill print",action,var)
   
   if action == "before" :
     billname=var["JUPDIALOG_START"]
     xml = rpdf.buildXMLS(var,billname)
     b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",xml)
     html =  b.toString()
     var["html"] = html
     var["billno"] = billname
     cutil.setCopy(var,["html","billno","download"])
     pdf = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",xml)
     assert pdf != None
     bkey = ADDBLOB.addNewBlob("TEMPORARY","PDF",pdf)
     var["download"] = "TEMPORARY:" + bkey + ":print.pdf"

   
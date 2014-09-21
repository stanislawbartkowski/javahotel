import cutil
import con
import pdfutil
import xmlutil
from util import util
from util import rpdf
from util import hmail
from util import diallaunch
import cmail

M = util.MESS()
ALIST="atachlist"

def _getCust(var,ma) :
    cust = None
    rese = ma["resename"]
    if ma.has_key("custname") : cust = ma["custname"]
    rform = util.RESFORM(var).findElem(rese)
    assert rform != None
    if cust == None : cust = rform.getCustomerName()
    C = util.CUSTOMERLIST(var).findElem(cust)
    assert C != None
    return (C,rform)

def dialogaction(action,var) :
  cutil.printVar("rese mail",action,var)
  
  if action == "before" :
    xml = var["JUPDIALOG_START"]
    (ma,alist) = xmlutil.toMap(xml)
    rese = ma["resename"]
    mtype = ma["mailtype"]
#    cust = None
#    if ma.has_key("custname") : cust = ma["custname"]
#    rform = util.RESFORM(var).findElem(rese)
#    assert rform != None
#    if cust == None : cust = rform.getCustomerName()
#    C = util.CUSTOMERLIST(var).findElem(cust)
#    assert C != None
    (C,rform) = _getCust(var,ma)
    var["to"] = C.getAttr("email")
    var["from"] = M("confirmationfrom")
    var["xml"] = xml
    (arrival,departure,roomname,rate) = rpdf.getReseDate(var,rform)
    var["subject"] = M("confirmationmailsubject").format(con.toS(arrival),con.toS(departure))
    xml = rpdf.buildResXML(var,rese)
    if mtype == 0 :
      var["subject"] = M("confirmationmailsubject").format(con.toS(arrival),con.toS(departure))
      var["content"] = pdfutil.xsltHtml("mail/resconfirmation.xslt",xml).toString()
    elif mtype == 1: 
      var["subject"] = M("sendpdfmailsubject").format(con.toS(arrival),con.toS(departure))
      var["content"] = pdfutil.xsltHtml("mail/sendinvoicepdf.xslt",xml).toString()
    else : assert False
    cutil.setCopy(var,["subject","to","from","content","xml"])
    seq = []
    if alist != None :
      for l in alist : 
	 (realm,key,filename) = cutil.splitsubmitres(l["blobid"])
	 seq.append({"attachname" : filename, "blobid" : l["blobid"] })
    cutil.setJMapList(var,ALIST,seq)	 
    
  if action == "sendmail" and var["JYESANSWER"] :
    H = hmail.HotelMail(var)
    xml = var["xml"]
    (ma,alist) = xmlutil.toMap(xml)
    rese = ma["resename"]
#    custname = ma["custname"]
    (C,rform) = _getCust(var,ma)
    custname = C.getName()
    mtype = ma["mailtype"]
    to = var["to"]
    fromc = var["from"]
    content = var["content"]
    subject = var["subject"]
#    C = util.CUSTOMERLIST(var).findElem(custname)
    C.setAttr("email",to)
    util.CUSTOMERLIST(var).changeElem(C)
    attachL = None
    for l in alist : 
	 (realm,key,filename) = cutil.splitsubmitres(l["blobid"])
	 attachL = cmail.createAttachList(attachL,realm,key,filename)   
    hh = H.sendMail(mtype,rese,custname,subject,content,to,fromc,attachL)
    ressend = H.getCMail(hh.getName()).getSendResult()
    if ressend == None : 
       var["JOK_MESSAGE"] = "@okmailsent"
       var["JCLOSE_DIALOG"] = True
    else :
       var["JERROR_MESSAGE"] = ressend
       var["JMESSAGE_TITLE"] = "@errormailsent"
       
  if action == "download" :
    diallaunch.pdfdownload(var,var["blobid"])
      
    
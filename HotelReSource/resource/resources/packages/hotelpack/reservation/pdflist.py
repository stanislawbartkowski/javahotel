import cutil
import listbl
from util import rpdf
import pdfutil
from util import rutil
from util import diallaunch

from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()
BREG="BILLBLOB"
LIST="pdflist"
TEMPPDF="PDF"

def _constructB(billname) :
  return listbl.BLOBREGISTRY(BREG,billname,LIST)

def listaction(action,var) :
  
  cutil.printVar("list action",action,var)
  
  if action == "before" :
    billname = var["JUPDIALOG_START"]
    var["blob_billname"] = billname
    cutil.setCopy(var,"blob_billname")
    B = _constructB(billname)
    B.readBlobList(var)

  if action == "edit" :
     cutil.setAddEditMode(var,LIST,"blob_comment")
     
  if action == "editlistrowaction" :
     if var["JLIST_EDIT_ACTION_" + LIST] == "REMOVE" :
       var["JYESNO_MESSAGE"] = "@blobremovequestion"
       var["JMESSAGE_TITLE"] = "@Confirm"
       var["JAFTERDIALOG_ACTION"] = "removeyesno"
    
  if action == "removeyesno" :
    if var["JYESANSWER"] :
      billname = var["blob_billname"]
      pdf_id = var["id"]
      B = _constructB(billname)
      B.removeOneBlob(var,pdf_id)
      var["JLIST_EDIT_ACTIONOK_" + LIST] = True
    else : var["JLIST_EDIT_ACTIONOK_" + LIST] = False
    
  if action == "columnchangeaction" and not var["JLIST_EDIT_BEFORE"] :
     billname = var["blob_billname"]
     pdf_id = var["id"]
     B = _constructB(billname)
     B.changeBlobComment(var,pdf_id,var["blob_comment"])
     
  if action == "pdfdownload" :
      diallaunch.pdfdownload(var,listbl.constructPDFBLOB(var,var["blob_key"]))
      
  if action == "send" :
     custname = var["payer_name"]
     resename = rutil.getReseName(var)
     blobid=listbl.constructPDFBLOB(var,var["blob_key"])
     diallaunch.sendpdfmail(var,resename,custname,blobid)
    
def billprint(action,var) :
   cutil.printVar("bill print",action,var)
   
   if action == "before" :
     billname=var["JUPDIALOG_START"]
     xml = rpdf.buildXMLS(var,billname)
     b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",xml)
     html =  b.toString()
     var["html"] = html
     var["pdfbillno"] = billname     
     cutil.setCopy(var,["html","pdfbillno","download","tempkey"])
     pdf = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",xml)
     assert pdf != None
     bkey = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,TEMPPDF,pdf)
     var["tempkey"] = bkey
     var["download"] = cutil.PDFTEMPORARY + ":" + bkey + ":receipt.pdf"

   if action == "savepdf" and var["JYESANSWER"] :
     tempkey = var["tempkey"]
     comment = var["pdf_comment"]
     billno = var["pdfbillno"]
     B = _constructB(billno)
     B.addBlob(var,comment,tempkey)
     var["JOK_MESSAGE"] = "@oksavedconfirmation"
       
import cutil
from util import listbl
from util import rpdf
import pdfutil

from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()

LIST="pdflist"

def listaction(action,var) :
  
  cutil.printVar("list action",action,var)
  
  if action == "before" :
    billname = var["JUPDIALOG_START"]
    var["blob_billname"] = billname
    cutil.setCopy(var,"blob_billname")
    listbl.readBlobList(var,billname,LIST)

  if action == "edit" :
     cutil.setAddEditMode(var,LIST,"blob_comment")
     
  if action == "editlistrowaction" :
     if var["JLIST_EDIT_ACTION_" + LIST] == "REMOVE" :
       var["JYESNO_MESSAGE"] = "Do you want to remove this PDF ?"
       var["JMESSAGE_TITLE"] = "@Confirm"
       var["JAFTERDIALOG_ACTION"] = "removeyesno"
    
  if action == "removeyesno" :
    if var["JYESANSWER"] :
      billname = var["blob_billname"]
      pdf_id = var["id"]
      listbl.removeOneBlob(var,billname,LIST,pdf_id)
      var["JLIST_EDIT_ACTIONOK_" + LIST] = True
    else : var["JLIST_EDIT_ACTIONOK_" + LIST] = False
    
  if action == "columnchangeaction" and not var["JLIST_EDIT_BEFORE"] :
     billname = var["blob_billname"]
     pdf_id = var["id"]
     listbl.changeBlobComment(var,billname,LIST,pdf_id,var["blob_comment"])
     
  if action == "pdfdownload" :
      var["JUP_DIALOG"]="hotel/reservation/pdfprint.xml"       
      var["JUPDIALOG_START"] = listbl.constructPDFBLOB(var,var["blob_key"])
    
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
     bkey = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"PDF",pdf)
     var["tempkey"] = bkey
     var["download"] = cutil.PDFTEMPORARY + ":" + bkey + ":receipt.pdf"

   if action == "savepdf" and var["JYESANSWER"] :
     tempkey = var["tempkey"]
     comment = var["pdf_comment"]
     billno = var["pdfbillno"]
     listbl.addBlob(var,billno,LIST,comment,tempkey)
     var["JOK_MESSAGE"] = "@oksavedconfirmation"


def pdfprint(action,var) :
   cutil.printVar("pdf print",action,var)
   
   if action == "before" :
     var["download"] = var["JUPDIALOG_START"]
     cutil.setCopy(var,"download")
  
import cutil

import pdfutil
from util import rpdf
from util import listbl
from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()



def dialogaction(action,var) :
     cutil.printVar("doaction",action,var)
     
     if action == "clearlist" :
        billno = var["billno"]
        listbl.removeBlob(var,billno,"blist")
        var["OK"] = True
        billno = var["billno"]
     
     if action == "createlist" :
        var["OK"] = True
        billno = var["billno"]
        listbl.readBlobList(var,billno,"blist")
         
     if action == "changecomment" :
        billno = var["billno"]
        id = var["id"]
        listbl.changeBlobComment(var,billno,"blist",id,"new comment")             
        var["OK"] = True

     if action == "addpdf" :     
        name = var["billno"]
        s = rpdf.buildXMLS(var,name)
        pdf  = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",s)
        bkey = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"PDF",pdf)
        listbl.addBlob(var,name,"blist","Hello",bkey)
        var["OK"] = True
        
     if action == "removepdf" :
         id = var["id"]
         billno = var["billno"]
         listbl.removeOneBlob(var,billno,"blist",id)
         var["OK"] = True

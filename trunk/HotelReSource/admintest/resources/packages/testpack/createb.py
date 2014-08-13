import cutil

import pdfutil
from util import rpdf
import listbl
from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()

def __createB(var):
    return listbl.BLOBREGISTRY("BILLBLOB",var["billno"],"blist")

def dialogaction(action,var) :
    
     cutil.printVar("doaction",action,var)
     
     if action == "clearlist" :
        B = __createB(var)
        billno = var["billno"]
#        listbl.removeBlob(var,billno,"blist")
        B.removeBlob(var)
        var["OK"] = True
        billno = var["billno"]
     
     if action == "createlist" :
        B = __createB(var)
        var["OK"] = True
        billno = var["billno"]
#        listbl.readBlobList(var,billno,"blist")
        B.readBlobList(var)
         
     if action == "changecomment" :
        B = __createB(var)
        billno = var["billno"]
        id = var["id"]
#        listbl.changeBlobComment(var,billno,"blist",id,"new comment")
        B.changeBlobComment(var,id,"new comment")              
        var["OK"] = True

     if action == "addpdf" :     
        B = __createB(var)
        name = var["billno"]
        s = rpdf.buildXMLS(var,name)
        pdf  = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",s)
        bkey = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"PDF",pdf)
#        listbl.addBlob(var,name,"blist","Hello",bkey)
        B.addBlob(var,"Hello",bkey)
        var["OK"] = True
        
     if action == "removepdf" :
         B = __createB(var)
         id = var["id"]
         billno = var["billno"]
 #        listbl.removeOneBlob(var,billno,"blist",id)
         B.removeOneBlob(var,id)
         var["OK"] = True

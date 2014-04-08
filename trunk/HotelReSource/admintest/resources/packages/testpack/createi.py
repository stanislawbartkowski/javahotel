import cutil
from com.jythonui.server import Util
from java.io import FileInputStream

import pdfutil
from util import rpdf


def dialogaction(action,var) :
     cutil.printVar("doaction",action,var)
     
     if action == "runxslt" :
       s = Util.getResourceAsDirectory("invoice")
       s = s + "/test.xml"
       inS = FileInputStream(s)
       print inS
       xml = Util.readFromFileInput(inS)
       print xml
               
       b = pdfutil.xsltHtml("invoice/test.xslt",xml)
       assert b != None
       
       print b.toString()
       
     if action == "invoicehtml" :
       s = Util.getResourceAsDirectory("invoice")
       s = s + "/guest.xml"
       inS = FileInputStream(s)
       print inS
       xml = Util.readFromFileInput(inS)
       print xml
               
       b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",xml)
       assert b != None
       
       print b.toString()
       var["OK"] = True     

     if action == "invoicexmlforbill" :
       name = var["billno"]
       s = rpdf.buildXMLS(var,name)       
       print s
       var["OK"] = True     
       var["xml"] = s

     if action == "invoicehtmlforbill" :
       name = var["billno"]
       s = rpdf.buildXMLS(var,name)       
       b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",s)
       assert b != None       
       print b.toString()
       var["html"] = b.toString()
       var["OK"] = True     

     if action == "invoicepdfforbill" :
       name = var["billno"]
       s = rpdf.buildXMLS(var,name)
       b  = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",s)       
       assert b != None
       var["OK"] = True     
       
       


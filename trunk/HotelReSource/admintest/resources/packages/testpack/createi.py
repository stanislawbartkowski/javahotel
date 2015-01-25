import cutil
from com.jythonui.server import Util
from java.io import FileInputStream

import pdfutil
from util import rpdf
import xmlutil


def dialogaction(action,var) :
     cutil.printVar("doaction",action,var)
     
     if action == "runxslt" :
       xml = xmlutil.fileToS("invoice/test.xml")

               
       b = pdfutil.xsltHtml("invoice/test.xslt",xml)
       assert b != None
       
       print b.toString()
       
     if action == "invoicehtml" :
       xml = xmlutil.fileToS("invoice/guest.xml")
               
       b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",xml)
       assert b != None
       
       print b.toString()
       var["OK"] = True     

     if action == "invoicexmlforbill" :
       name = var["billno"]
       s = rpdf.buildXMLBill(var,name)       
       print s
       var["OK"] = True     
       var["xml"] = s

     if action == "invoicehtmlforbill" :
       name = var["billno"]
       s = rpdf.buildXMLBill(var,name)       
       b = pdfutil.xsltHtml("invoice/invoicestandard.xslt",s)
       assert b != None       
       print b.toString()
       var["html"] = b.toString()
       var["OK"] = True     

     if action == "invoicepdfforbill" :
       name = var["billno"]
       s = rpdf.buildXMLBill(var,name)
       b  = pdfutil.createPDFXSLT("invoice/invoicestandard.xslt",s)       
       assert b != None
       var["OK"] = True     
       
       


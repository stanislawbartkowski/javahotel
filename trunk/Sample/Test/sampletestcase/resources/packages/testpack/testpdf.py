import cutil
import pdfutil

from com.jythonui.server import Util
from java.io import FileInputStream
from com.jythonui.server.holder import Holder

def dialogaction(action,var):
    cutil.printVar("runpdf",action,var)
    
    if action == "runpdf" :
      s = "<H1> Hello </H1>"
      b = pdfutil.createPDFH(s)
      assert b != None

    if action == "runpdf1" :
      s = "<H1> Hello </H1>"
      map={}
      map[pdfutil.CREATOR] = "I am"
      map[pdfutil.PRODUCER] = ""
      map[pdfutil.AUTHOR] = "Author"
      map[pdfutil.TITLE] = "Title"
      b = pdfutil.createPDFH(s,map)
      assert b != None
      
    if action == "createxml" :
        s = "<?xml version=\"1.0\" ?>"
        s = s +  "<persons>"
        s = s +  "<person username=\"JS1\">"
        s = s + "<name>John</name>"
        s = s + "<family-name>Smith</family-name>"
        s = s + "</person>"
        s = s + "<person username=\"MI1\">"
        s = s + "<name>Morka</name>"
        s = s + "<family-name>Ismincius</family-name>"
        s = s + "</person>"
        s = s + "</persons>"
          
        
    if action == "runxslt" :
       s = Util.getResourceAsDirectory("dialogs")
       s = s + "/data.xml"
       inS = FileInputStream(s)
       print inS
       xml = Util.readFromFileInput(inS)
       print xml
               
       b = pdfutil.xsltHtml("dialogs/template.xsl",xml)
       assert b != None
       
       print b.toString()
       
    if action == "runxsltpdf" :   
#       s = Util.getResourceAsDirectory("dialogs")
       s = Holder.getIJython().getResourceDirectory() + "/dialogs";
       print s
       s = s + "/data.xml"
       print s
       inS = FileInputStream(s)
       print inS
       xml = Util.readFromFileInput(inS)
       print xml
               
       b = pdfutil.createPDFXSLT("dialogs/template.xsl",xml)
       assert b != None
            
    
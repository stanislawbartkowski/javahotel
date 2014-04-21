import cutil
from com.jython.ui.server.guice import ServiceInjector
from com.jythonui.server.holder import Holder

from com.itextpdf.text import Document
from java.io import ByteArrayOutputStream

from com.itextpdf.text import DocumentException
from com.itextpdf.text import Paragraph
from com.itextpdf.text.pdf import PdfWriter

from com.jythonui.server.holder import SHolder
from java.io import FileReader

from com.jythonui.server import Util

from com.itextpdf.text import Document
from com.itextpdf.text import DocumentException
from com.itextpdf.text import PageSize
from com.itextpdf.text.pdf import PdfWriter
from com.itextpdf.tool.xml import XMLWorkerHelper
from java.io import FileOutputStream
from java.io import FileInputStream

from javax.xml.transform import TransformerFactory
from javax.xml.transform import Transformer
from javax.xml.transform.stream import StreamSource
from javax.xml.transform.stream import StreamResult
from java.io import ByteArrayInputStream
import xmlutil

import pdfutil
  
def createPDF(var) :
    return pdfutil.createPDFH("",None,["Hello World !",])


def _toPDF(fis) :
    b = ByteArrayOutputStream()
         
    # create a new document
    document = Document();
    # get Instance of the PDFWriter
    pdfWriter = PdfWriter.getInstance(document, b)
          
    # document header attributes
    document.addAuthor("betterThanZero")
    document.addCreationDate()
    document.addProducer()
    document.addCreator("MySampleCode.com")
    document.addTitle("Demo for iText XMLWorker")
    document.setPageSize(PageSize.LETTER);
       
    # open document
    document.open();
          
    # To convert a HTML file from the filesystem
    # String File_To_Convert = "docs/SamplePDF.html";
#    //FileInputStream fis = new FileInputStream(File_To_Convert);
       
    # URL for HTML page
       
    # get the XMLWorkerHelper Instance
    worker = XMLWorkerHelper.getInstance();
    # convert to PDF
    worker.parseXHtml(pdfWriter, document, fis);
          
    # close the document
    document.close();
    # close the writer
    pdfWriter.close();
    return b.toByteArray()

def createPDF1(var) :
#    s = Util.getResourceAsDirectory("docs")
#    print s
#    fis = FileReader( s + "/Sample.html")
    iS = Holder.getIJython().getResource().getRes("docs/Sample.html")
 #   s = Util.readFromFileInput(iS.openStream())

    map = {}
    map[pdfutil.AUTHOR] = "betterThanZero"
    map[pdfutil.CREATOR] = ""
    map[pdfutil.TITLE] = "Demo for iText XMLWorker"
    return pdfutil.createPDF(iS.openStream(),map)
  

def createPDF2(var) :
#    s = Util.getResourceAsDirectory("docs")
#    inS = FileInputStream(s+"/data.xml")
#    xml = Util.readFromFileInput(inS)
    xml = xmlutil.fileToS("docs/data.xml")
    return pdfutil.createPDFXSLT("docs/template.xsl",xml)

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "firstname" : cutil.STRING, "lastname" : cutil.STRING,"realm" : cutil.STRING, "key" : cutil.STRING, "filename" : cutil.STRING, "info" : cutil.STRING }
      cutil.RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"LIST-ATTACH-PERSON-DEMO",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()

def readFileList(var) :
       F.readList(var)
       seq = var["JLIST_MAP"]["list"]
       for s in seq :
         realm = s["realm"]
         key = s["key"]
         filename=s["filename"]
         s["upload"] = "upload"
           

def dialogaction(action,var) :
  cutil.printVar("listattach",action,var)
  
  if action == "upload" and var["list_lineset"] :
     var["JUP_DIALOG"] = "attachsomething.xml"
    
  
  if action == "before" or action == "crud_readlist" :
       F.readList(var)
       readFileList(var)

  if action == "clearrecords" :
       F.removeAll()
       readFileList(var)
       
  if action == "attach" :
     var["JUP_DIALOG"] = "attachsomething.xml"
     
  if action == "download" :
     if var["realm"] : var["JUP_DIALOG"] = "download.xml"
       
  if action == "crud_add" :
      key = F.nextKey()
      var["id"] = key
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True
       
  if action == "crud_change" :
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True
                                        #<td><a class="attachment" href="/attach/Test/Zalozenia%20akcji%20Konspiratorze%20-%20odbierz%20swiadectwo%20pracy.odt" accesskey="" title="Zalozenia akcji Konspiratorze - odbierz swiadectwo pracy.odt">Zalozenia akcji Konspiratorze ...</a></td>

  if action == "crud_remove" :
     F.removeMap(var)
     var["JCLOSE_DIALOG"] = True
     
def _setVals(var,realm,key,filename) :
    var["realm"] = realm
    var["key"] = key
    var["filename"] = filename
    F.addMap(var)
    cutil.setCopy(var,["filename","realm","key","info"],"list")
    var["JCLOSE_DIALOG"] = True  

def attachaction(action,var) :
  cutil.printVar("attach action",action,var)
  
  if action == "submitaction" :
     var["JYESNO_MESSAGE"] = "Do you really want to submit this file ?"
     var["JMESSAGE_TITLE"] = "Confirm"
     var["JAFTERDIALOG_ACTION"] = "afteryesnosubmit"
     return
   
  if action == "afteryesnosubmit" and var["JYESANSWER"] :
     var["J_SUBMIT"] = True
     
  if action == "aftersubmit" :
    files = var["JSUBMITRES"]    
    elems = files.split(":")
    realm = elems[0]
    key = elems[1]
    filename = elems[2]
    _setVals(var,realm,key,filename)
    
  if action == "addpdf" and var["JYESANSWER"] :
    b = createPDF(var)
#    print "dok=",b.length
    i = SHolder.getAddBlob()
    realm = "TESTPDF"
    key = "PDF"
    bkey = i.addNewBlob(realm,key,b)
    _setVals(var,realm,bkey,"test.pdf")

  if (action == "addpdf1" or action == "addpdf2") and var["JYESANSWER"] :
    if action == "addpdf1" : b = createPDF1(var)
    else : b = createPDF2(var)
#    print "dok=",b.length
    i = SHolder.getAddBlob()
    realm = "TESTPDF"
    key = "PDF"
    bkey = i.addNewBlob(realm,key,b)
    _setVals(var,realm,bkey,"test.pdf")       
  
def downaction(action,var) :
  cutil.printVar("download action",action,var)
  
  if action == "before" :
#    var["download"] = "xxx:ddd:p.pdf"
    realm = var["realm"]
    key = var["key"]
    filename = var["filename"]
    var["download"] = realm + ":" + key + ":" + filename
    cutil.setCopy(var,"download")
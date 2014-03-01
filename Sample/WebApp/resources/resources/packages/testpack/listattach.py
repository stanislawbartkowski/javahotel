import cutil
from com.jython.ui.server.guice import ServiceInjector

from com.itextpdf.text import Document
from java.io import ByteArrayOutputStream

from com.itextpdf.text import DocumentException
from com.itextpdf.text import Paragraph
from com.itextpdf.text.pdf import PdfWriter

from com.jythonui.server.holder import SHolder

def createPDF(var) :
  
    # step 1
    document = Document()
    b = ByteArrayOutputStream()
    # step 2
    PdfWriter.getInstance(document, b)
    # step 3
    document.open()
    # step 4
    document.add(Paragraph("Hello World!"))
    #step 5
    document.close()
    return b.toByteArray()

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "firstname" : cutil.STRING, "lastname" : cutil.STRING,"realm" : cutil.STRING, "key" : cutil.STRING, "filename" : cutil.STRING, "info" : cutil.STRING }
      cutil.RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"LIST-ATTACH-PERSON-DEMO",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()

def readFileList(var) :
       F.readList(var)
       seq = var["JLIST_MAP"]["list"]
       for s in seq :
#         if s["filename"] == None : s["filename"] = "attach"
         realm = s["realm"]
         key = s["key"]
         filename=s["filename"]
         s["upload"] = "upload"
#         if realm == None : s["download"] = None
#         else : s["download"] = realm + ":" + key + ":" + filename
         
  
           

def dialogaction(action,var) :
  cutil.printVar("listattach",action,var)
  
  
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
    var["realm"] = realm
    var["key"] = key
    var["filename"] = filename
    F.addMap(var)
    var["JCLOSE_DIALOG"] = True
    
  if action == "addpdf" :
    b = createPDF(var)
#    print "dok=",b.length
    print b,len(b)
    i = SHolder.getAddBlob()
    realm = "TESTPDF"
    key = "PDF"
    bkey = i.addNewBlob(realm,key,b)
    var["realm"] = realm
    var["key"] = bkey
    var["filename"] = "test.pdf"
    F.addMap(var)
    var["JCLOSE_DIALOG"] = True     
  
def downaction(action,var) :
  cutil.printVar("download action",action,var)
  
  if action == "before" :
#    var["download"] = "xxx:ddd:p.pdf"
    realm = var["realm"]
    key = var["key"]
    filename = var["filename"]
    var["download"] = realm + ":" + key + ":" + filename
    cutil.setCopy(var,"download")
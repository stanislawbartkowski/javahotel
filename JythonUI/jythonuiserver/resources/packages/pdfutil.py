from com.itextpdf.text import Document
from java.io import ByteArrayOutputStream
from java.io import ByteArrayInputStream

from com.itextpdf.text import DocumentException
from com.itextpdf.text import Paragraph
from com.itextpdf.text.pdf import PdfWriter
from java.io import FileReader
from com.itextpdf.text import Document
from com.itextpdf.text import DocumentException
from com.itextpdf.text import PageSize
from com.itextpdf.text.pdf import PdfWriter
from com.itextpdf.tool.xml import XMLWorkerHelper
from java.io import FileOutputStream
from java.io import StringReader

from javax.xml.transform import TransformerFactory
from javax.xml.transform import Transformer
from javax.xml.transform.stream import StreamSource
from javax.xml.transform.stream import StreamResult

#from com.jythonui.server import Util
from com.jythonui.server.holder import Holder

AUTHOR="author"
PRODUCER="producer"
CREATOR="creator"
TITLE="title"

def createPDFH(htmlcontent,map={},paragraph=None):
    fis = StringReader(htmlcontent)
    return createPDF(fis,map,paragraph)


def createPDF(fis,map={},paragraph=None) :
    b = ByteArrayOutputStream()
         
    # create a new document
    document = Document();
    # get Instance of the PDFWriter
    pdfWriter = PdfWriter.getInstance(document, b)
    
    # document header attributes
    if map :
      if map.has_key(AUTHOR) : document.addAuthor(map[AUTHOR])
      if map.has_key(PRODUCER) : document.addProducer()
      if map.has_key(CREATOR) : document.addCreator(map[CREATOR])
      if map.has_key(TITLE) : document.addTitle(map[TITLE])
      
    document.addCreationDate()
          
    document.setPageSize(PageSize.LETTER);
       
    # open document
    document.open();
    if paragraph :
        for pa in paragraph :
           document.add(Paragraph(pa))

                 
    # get the XMLWorkerHelper Instance
    worker = XMLWorkerHelper.getInstance();
    # convert to PDF
    worker.parseXHtml(pdfWriter, document, fis);
          
    # close the document
    document.close();
    # close the writer
    pdfWriter.close();
    return b.toByteArray()


def xsltHtml(xslt,xmlcontent) :
    tFactory = TransformerFactory.newInstance();
#    s = Util.getResourceAsDirectory(xslt)
    s = Holder.getIJython().getResourceDirectory() + "/" + xslt
    transformer = tFactory.newTransformer(StreamSource(s))
    out = ByteArrayOutputStream()
    reader = StringReader(xmlcontent)            
    transformer.transform(StreamSource(reader),StreamResult(out))
    return out
#    fis = ByteArrayInputStream(out.toByteArray())
#    return fis

def createPDFXSLT(xslt,xmlcontent, map = {}):
    out = xsltHtml(xslt,xmlcontent)
    fis = ByteArrayInputStream(out.toByteArray())
    return createPDF(fis,map)
    
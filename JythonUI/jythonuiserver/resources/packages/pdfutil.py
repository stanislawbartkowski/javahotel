from com.itextpdf.text import Document
from java.io import ByteArrayOutputStream

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

AUTHOR="author"
PRODUCER="producer"
CREATOR="creator"
TITLE="title"

def createPDF(htmlcontent,map={}) :
    fis = StringReader(htmlcontent)
    b = ByteArrayOutputStream()
         
    # create a new document
    document = Document();
    # get Instance of the PDFWriter
    pdfWriter = PdfWriter.getInstance(document, b)
    
    # document header attributes
    if map.has_key(AUTHOR) : document.addAuthor(map[AUTHOR])
    if map.has_key(PRODUCER) : document.addProducer(map[PRODUCER])
    if map.has_key(CREATOR) : document.addCreator(map[CREATOR])
    if map.has_key(TITLE) : document.addTitle(map[TITLE])
    document.addCreationDate()
          
    document.setPageSize(PageSize.LETTER);
       
    # open document
    document.open();
                 
    # get the XMLWorkerHelper Instance
    worker = XMLWorkerHelper.getInstance();
    # convert to PDF
    worker.parseXHtml(pdfWriter, document, fis);
          
    # close the document
    document.close();
    # close the writer
    pdfWriter.close();
    return b.toByteArray()

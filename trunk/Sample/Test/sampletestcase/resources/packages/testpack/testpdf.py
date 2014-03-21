import cutil
import pdfutil

def dialogaction(action,var):
    cutil.printVar("runpdf",action,var)
    
    if action == "runpdf" :
      s = "<H1> Hello </H1>"
      b = pdfutil.createPDF(s)
      assert b != None

    if action == "runpdf1" :
      s = "<H1> Hello </H1>"
      map={}
      map[pdfutil.CREATOR] = "I am"
      map[pdfutil.PRODUCER] = "Producer"
      map[pdfutil.AUTHOR] = "Author"
      map[pdfutil.TITLE] = "Title"
      b = pdfutil.createPDF(s)
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
    
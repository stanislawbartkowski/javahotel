import cutil,pdfutil,xmlutil

def dialogaction(action,var):
    
    cutil.printVar("test42",action,var)
    
    if action == "test1" :
       xml = xmlutil.fileToS("xslt/data1/data.xml")              
       html = pdfutil.xsltHtmlS("xslt/data1/data.xslt",xml)
       assert html != None

       print html
       i = html.find("English")
       print i       
       assert i != -1
       var["OK"] = True     

    if action == "test2" :
       xml = xmlutil.fileToS("xslt/data1/data.xml")              
       html = pdfutil.xsltHtmlS("xslt/data1/data.xslt",xml)
       assert html != None

       print html
       i = html.find("Polish")
       print i
       assert i != -1       
       var["OK"] = True     

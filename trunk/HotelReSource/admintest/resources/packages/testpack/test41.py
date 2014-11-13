import cutil,pdfutil,xmlutil

def dialogaction(action,var):
    
    cutil.printVar("test41",action,var)
    
    if action == "test1" :
       xml = xmlutil.fileToS("xslt/indata.xml")              
       html = pdfutil.xsltHtmlS("xslt/generateindata.xslt",xml)
       assert html != None

       print html       
       var["OK"] = True     

    if action == "test2" :
       xml = xmlutil.fileToS("xslt/data/data.xml")              
       html = pdfutil.xsltHtmlS("xslt/data/data.xslt",xml)
       assert html != None

       print html,type(html)
       i = html.find('My name is');
       print i       
       assert -1 != i
       var["OK"] = True     

    if action == "test3" :
       xml = xmlutil.fileToS("xslt/data/data.xml")              
       html = pdfutil.xsltHtmlS("xslt/data/data.xslt",xml)
       assert html != None

       print html,type(html)
       i = html.find('Hans Kloss');
       print i       
       assert -1 != i
       var["OK"] = True
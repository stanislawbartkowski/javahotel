import xmlutil

def confirmationmail(var,rese) :
    ma = {}
    ma["resename"] = rese
    ma["mailtype"] = 0
    xml = xmlutil.mapToXML(ma)
    print xml
    var["JUPDIALOG_START"] = xml
    var["JUP_DIALOG"]="hotel/info/sendconfirmationmail.xml" 

def sendpdfmail(var,rese,cust,blobid) :
    ma = {}
    ma["resename"] = rese
    ma["mailtype"] = 1
    ma["custname"] = cust
    alist = [{"blobid" : blobid} ]
    xml = xmlutil.toXML(ma,alist)
    var["JUPDIALOG_START"] = xml
    var["JUP_DIALOG"]="hotel/info/sendconfirmationmail.xml" 
      
def showmailnote(var,name) :    
    var["JUPDIALOG_START"] = name
    var["JUP_DIALOG"]="hotel/info/showhotelmail.xml" 
    
def pdfdownload(var,blobkey) :
    var["JUPDIALOG_START"] = blobkey
    var["JUP_DIALOG"]="mail/attachdownload.xml" 
    
def showlistofmail(var,resename) :
    var["JUPDIALOG_START"] = resename
    var["JUP_DIALOG"]="hotel/info/showlistofmails.xml" 
    
   
  

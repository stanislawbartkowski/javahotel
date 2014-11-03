import xmlutil
from hotelpack.reservation.rrutil import rparam
import util

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
        
def modifreservation(var,resename,resroom,resday) :
    var["JUPDIALOG_START"] = rparam.roomdaytoXML(resename,resroom,resday)
    var["JUP_DIALOG"]="hotel/reservation/modifrese.xml" 
    
def newreservation(var,resroom,resday,resnodays,nop,roomservice=None,roompricelist=None) :
     var["JUPDIALOG_START"] = rparam.resequeryXML(resroom,resday,resnodays,nop,roomservice,roompricelist)
     var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"

def modifreservationroom(var,resroom,resday) :
  return newreservation(var,resroom,resday,None,None)

def showstay(var,resename,resroom,resday) :
     var["JUPDIALOG_START"] = rparam.resequeryXML(resroom,resday,None,None,None,None)
     var["JUP_DIALOG"] = "hotel/reservation/showstay.xml"
     
def reservationdialogaction(var,resid,room,day) :
    rform = util.RESFORM(var).findElem(resid)
    assert rform != None
    sta = util.resStatus(rform)          
    if sta == 1 : showstay(var,resid,room,day)
    elif sta == 0 : var["JOK_MESSAGE"] = "@statuscanceled"
    else: modifreservation(var,resid,room,day)
  
    
   
  

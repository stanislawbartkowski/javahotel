import xmlutil

from hotelpack.reservation.rrutil import rparam

import util

def confirmationmail(var,rese) :
    ma = {}
    ma["resename"] = rese
    ma["mailtype"] = 0
    xml = xmlutil.mapToXML(ma)
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
    
    
def showlistofmail(var,resename) :
    var["JUPDIALOG_START"] = resename
    var["JUP_DIALOG"]="hotel/info/showlistofmails.xml" 
        
# -------------------------
# reservation
# -------------------------
        
def modifreservation(var,resename,resroom,resday) :
    var["JUPDIALOG_START"] = rparam.roomdaytoXML(resename,resroom,resday)
    var["JUP_DIALOG"]="hotel/reservation/modifrese.xml" 
    
def newreservation(var,resroom,resday,resnodays,nop,roomservice=None,roompricelist=None) :
     var["JUPDIALOG_START"] = rparam.resequeryXML(resroom,resday,resnodays,nop,roomservice,roompricelist)
     var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"

def modifreservationroom(var,resroom,resday) :
  return newreservation(var,resroom,resday,None,None)

def reservationdialogaction(var,resid,room,day) :
    rform = util.RESFORM(var).findElem(resid)
    assert rform != None
    sta = util.resStatus(rform)          
    if sta == 1 : showstay(var,resid,room,day)
    elif sta == 0 : var["JOK_MESSAGE"] = "@statuscanceled"
    else: modifreservation(var,resid,room,day)

# ---------------------------
# stay
# ---------------------------

def staydetails(var,resroom,resday) :
     var["JUPDIALOG_START"] = rparam.resequeryXML(resroom,resday,None,None,None,None)
     var["JUP_DIALOG"] = "hotel/reservation/showstay.xml"
  
def showstay(var,resename,resroom,resday) :
     var["JUPDIALOG_START"] = rparam.roomdaytoXML(resename,resroom,resday)
     var["JUP_DIALOG"] = "hotel/reservation/modifstay.xml"
  
def staycalculation(var,resename) :
     var["JUPDIALOG_START"] = resename
     var["JUP_DIALOG"] = "hotel/reservation/staycalculation.xml"
     
    
# -----------------------------
# finance document
# -----------------------------

def displayDocument(var,xml,afteraction="acceptdocument",okonly = False) :
     var["JUPDIALOG_START"] = xml
     oks = "0"
     if okonly : oks = "1"
     var["JUPDIALOG_STARTPAR"] = oks
     var["JUP_DIALOG"] = "hotel/reservation/showdocument.xml"
     var['JAFTERDIALOG_ACTION'] = afteraction
  
  

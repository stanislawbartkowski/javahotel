import cutil
from util import hmail
from util import diallaunch

MLIST="emaillist"

def dialogaction(action,var) :
  cutil.printVar("list of mail",action,var)
  
  if action == "before" :
     rename = var["JUPDIALOG_START"]
     H = hmail.HotelMail(var)
     li = H.getListForReservation(rename)
     seq = hmail.createMailSeq(var,li)
     cutil.setJMapList(var,MLIST,seq)  
   
  if action == "shownote" : 
    diallaunch.showmailnote(var,var["mailname"])
   

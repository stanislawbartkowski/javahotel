import cutil
from util import hmail
import cmail

ATALIST="listattach"

def dialogaction(action,var) :
  cutil.printVar("show note",action,var)
  
  if action == "before" :
    mnote = var["JUPDIALOG_START"]
    H = hmail.HotelMail(var)
    hma = H.findElem(mnote)
    note = H.getCMail(mnote)
    var["n_subject"] = note.getDescription()
    var["n_to"] = note.getRecipient()
    var["n_from"] = note.getFrom()
    var["n_content"] = note.getContent()
    var["n_resename"] = hma.getReseName()
    
    l = note.getaList()
    seq = []
    for a in l : seq.append({ "filename" : a.getFileName(), "realm" : a.getRealm(), "key" : a.getBlobKey() })
    cutil.setJMapList(var,ATALIST,seq)  
    
    cutil.setCopy(var,["n_subject","n_to","n_from","n_content","n_resename"])

    
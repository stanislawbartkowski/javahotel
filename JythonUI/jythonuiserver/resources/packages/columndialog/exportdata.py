import cutil,toformat,cblob,xmlutil,miscutil,cdial

LIST="list"
DATA="data"
FILENAME=LIST

M = cutil.MESS()

#D=cutil.SYSDEFAULTDATA(0)

def sendMailDialogOneAttach(var,froma,toa,subject,content,realm,key,filename,format) :
    ma = { "from" : froma, "toa" : toa, "subject" : subject, "content" : content,"realm" : realm, "key" : key, "format" : format,"filename" : filename}
    miscutil.mapToStartDialog(var,ma,"mail/sendnote.xml")

def _toRes(var) :
    format = var["exporttype"]
    dialname = var["JPAR_DIALOGNAME"]
    listname = var["JPAR_LISTNAME"]
    jsondata = var[DATA]
    jsonform = var[LIST]
    return toformat.toFormat(format,jsondata,jsonform,dialname,listname)

def dialogaction(action,var) :

  cutil.printVar("exportdata",action,var)
    
  if action == "before" :
#    var["exporttype"] = D.getData()
    cutil.setCopy(var,[LIST,DATA,"exporttype"])
    
  if action == "download" or action == "mail" :
    res = _toRes(var)
    format = var["exporttype"]
#    D.putData(format)
    dialname = var["JPAR_DIALOGNAME"]
    listname = var["JPAR_LISTNAME"]
    dFormat= miscutil.getDialogFormat(dialname)
    displayname=dFormat.getDisplayName()
    filename = FILENAME + "." + format
    if displayname == None or displayname=="" : displayname=M("downloaddefaultmessage")
    if action == "download" : cdial.downloadObj(var,displayname,filename,res)
    else :
      B = cblob.B
      (realm,key) = B.addNewTempBlob("T-MAIL",res)
      sendMailDialogOneAttach(var,"","",displayname,"",realm,key,filename,format) 
    
# ------------------------------------------    

FORMS=[toformat.XML, toformat.CSV, toformat.JSON, toformat.PDF, toformat.HTML]
    
def createexportlist(action,var) :
      
  seq = []
  for c in FORMS :
        rec = {"id" : c, "name" : c }
        seq.append(rec)

  cutil.setJMapList(var,action,seq)        


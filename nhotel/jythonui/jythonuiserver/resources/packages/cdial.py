import cblob,xmlutil,miscutil


def downloadObj(var,info,filename,o) :

  if type(o) == list : (realm,key) = o  
  else : 
      B = cblob.B
      (realm,key) = B.addNewTempBlob("T-DOWNLOAD",o)
  ma = {}
  ma["filename"] = filename
  ma["filetype"] = format
  ma["realm"] = realm
  ma["key"] = key  
  ma["info"] = info
  miscutil.mapToStartDialog(var,ma,"mail/download.xml")
  
# important:
#      if action == afteraction and var["JUPDIALOG_BUTTON"] == "attach" :

def uploadFile(var,afteraction="afterupload") :
   var["JUP_DIALOG"] = "mail/upload.xml"
   var['JAFTERDIALOG_ACTION'] = afteraction


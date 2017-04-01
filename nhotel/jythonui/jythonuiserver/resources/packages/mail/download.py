import cutil,xmlutil,miscutil


def downaction(action,var) :
  
  cutil.printVar("download object",action,var)
  
  if action == "before" :
    xml = var["JUPDIALOG_START"]
    ma = xmlutil.toMap(xml)[0]
    print ma
    var["info"] = ma["info"]
    realm = ma["realm"]
    key=ma["key"]
    filename=ma["filename"]
    var["download"] = miscutil.createBlobDownload(realm,key,filename)
    cutil.setCopy(var,["info","download"])
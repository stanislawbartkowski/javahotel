import cutil,cdial,miscutil

ALIST="attachlist"

def showattach(var,displayname=None) :
    realm = var["realm"]
    key = var["key"]
    filename = var["filename"]
    cdial.downloadObj(var,displayname,filename,[realm,key])

def mailattachaction(action,var) :
  
    if action == "addnewattach" and var["JUPDIALOG_BUTTON"] == "attach" :
      li = var["JLIST_MAP"][ALIST]
      (realm,key,filename) = cutil.splitsubmitres(var["JUPDIALOG_RES"])
      li.append({"filename" : filename,"realm" : realm, "key" : key})
      cutil.setJMapList(var,ALIST,li)
      return True
  
    if action == "removeattach"  :
       var["JYESNO_MESSAGE"] = "@removeattachmentquestion"
       var["JAFTERDIALOG_ACTION"] = "removeattachafter"
       return True
     
    if action == "removeattachafter" and var["JYESANSWER"] :
      li = var["JLIST_MAP"][ALIST]
      realm = var["realm"]
      key = var["key"]
      seq = []
      for l in li :
        if realm == l["realm"] and key == l["key"] : continue
        seq.append(l)
      cutil.setJMapList(var,ALIST,seq)
      return True

    if action=="addattach" :
      cdial.uploadFile(var,"addnewattach")
      return True
    
    if action == "showattach" :      
      showattach(var,var["subject"])
      return True
    
    return False

def sendnode(action,var) :
    
  if action == "before" :
    map = miscutil.startDialogToMap(var)
    for key in map.keys(): 
      if not var.has_key(key) or var[key] == None : var[key] = map[key]
      if var[key] == None : var[key] = ""
    cutil.setCopy(var,["from","to","subject","content"])
    seq=[]
    seq.append({"filename" : map["filename"],"realm" : map["realm"], "key" : map["key"]})
    cutil.setJMapList(var,ALIST,seq)    
    return
                
  mailattachaction(action,var)    

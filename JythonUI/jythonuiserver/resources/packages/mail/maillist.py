import cutil
import cmail

LI = "list"
ALIST="attachlist"
ATALIST="listattach"

def dialogattach(action,var) :
    cutil.printVar("dialog attach",action,var)
    
    if action == "before" :
      M = cmail.CMAIL(var)
      note = M.findElem(var["name"])
      l = note.getaList()
      seq = []
      for a in l : seq.append({ "filename" : a.getFileName(), "realm" : a.getRealm(), "key" : a.getBlobKey() })
      cutil.setJMapList(var,ATALIST,seq)  

def dialogaction(action,var) :
    cutil.printVar("send mail list",action,var)
    
    if action == "before" or action == "crud_readlist" :
      M = cmail.CMAIL(var)
      li = M.getList()
      seq = []
      for l in li :
        fromm = l.getFrom()
        recipient = l.getRecipient()
        subject = l.getDescription()
        content = l.getContent()
        res = l.getSendResult()
        noa = None
        if cutil.emptyS(res) : res = None
        if len(l.getaList()) > 0 : noa = len(l.getaList())
        seq.append({ "subject" : subject,"to" : recipient, "from" : fromm, "content": content, "res" : res, "name" : l.getName(), "noattach" : noa })
      cutil.setJMapList(var,LI,seq)  
      
    if action == "attachlist" :
        M = cmail.CMAIL(var)
        note = M.findElem(var["name"])
        l = note.getaList()
        if len(l) == 0 :
    	  var["JOK_MESSAGE"] = "@noattachmentsmessage"
          return
        var["JUP_DIALOG"] = "?mailattachments.xml"
      
      
def elemaction(action,var) :
    cutil.printVar("elem mail list",action,var)
    
    if action == "addnewattach" and var["JUPDIALOG_BUTTON"] == "attach" :
      li = var["JLIST_MAP"][ALIST]
      (realm,key,filename) = cutil.splitsubmitres(var["JUPDIALOG_RES"])
      li.append({"filename" : filename,"realm" : realm, "key" : key})
      cutil.setJMapList(var,ALIST,li)
      
    if action == "removeattach"  :
       var["JYESNO_MESSAGE"] = "@removeattachmentquestion"
       var["JAFTERDIALOG_ACTION"] = "removeattachafter"
       return      
     
    if action == "removeattachafter" and var["JYESANSWER"] :
      li = var["JLIST_MAP"][ALIST]
      realm = var["realm"]
      key = var["key"]
      seq = []
      for l in li :
        if realm == l["realm"] and key == l["key"] : continue
        seq.append(l)
      cutil.setJMapList(var,ALIST,seq)
    
    if action=="addattach" :
      var["JUP_DIALOG"] = "?mailattach.xml"
      var['JAFTERDIALOG_ACTION'] = "addnewattach"
      
    if action == "attach" :
     var["J_SUBMIT"] = True
     
    if action == "aftersubmit" :
      var["JCLOSE_DIALOG"] = var["JSUBMITRES"]
                
    if action=="before" :
      seq = []
      if var["JCRUD_DIALOG"] != "crud_add" :
        M = cmail.CMAIL(var)
        cutil.enableButton(var,"addattach",False)
        cutil.enableButton(var,"removeattach",False)
        name = var["name"]
        note = M.findElem(name)
        alist = note.getaList()
        for a in alist : seq.append({ "filename" : a.getFileName(), "realm" : a.getRealm(), "key" : a.getBlobKey() })
      else :
          S = cmail.MAILFROM(var)
          var["from"] = S.getFrom()
          cutil.setCopy(var,"from")        
      cutil.setJMapList(var,ALIST,seq)      
    
    if action == "crud_add" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "@sendmailquestion"
       return      

    if action == "crud_add" and var["JCRUD_AFTERCONF"] :
       li = var["JLIST_MAP"][ALIST]
       attachL = None
       for l in li : attachL = cmail.createAttachList(attachL,l["realm"],l["key"],l["filename"])	 
       
       M = cmail.CMAIL(var)
       res = M.sendMail(var["subject"],var["content"],var["to"],var["from"],attachL)
       print res
       S = cmail.MAILFROM(var)
       S.saveFrom(var["from"])
       var["JCLOSE_DIALOG"] = True

    if action == "crud_remove" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "@removemailquestion"
       return      
     
    if action == "crud_remove" and var["JCRUD_AFTERCONF"] :
       M = cmail.CMAIL(var)
       name = var["name"]
       M.deleteElemByName(name)
       var["JCLOSE_DIALOG"] = True
     
     
def attachdownload(action,var) :
    cutil.printVar("attach download",action,var)
    
    if action == "before" :
       key =  var["JUPDIALOG_START"]
       var["download"] = key
       cutil.setCopy(var,"download")
     
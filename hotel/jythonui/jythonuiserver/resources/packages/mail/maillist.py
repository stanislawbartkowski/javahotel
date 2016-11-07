import cutil,cmail,miscutil,cdial,cmailaction

LI = "list"
ALIST=cmailaction.ALIST
ATALIST="listattach"
  
def _sendNote(var) :
  
     li = var["JLIST_MAP"][ALIST]
     attachL = None
     for l in li : attachL = cmail.createAttachList(attachL,l["realm"],l["key"],l["filename"])	 
      
     M = cmail.CMAIL(var)
     res = M.sendMail(var["subject"],var["content"],var["to"],var["from"],attachL)
     print res
    
# ===========================================================================

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
      cutil.setJMapList(var,ALIST,seq)      
    
    if action == "crud_add" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "@sendmailquestion"
       return      

    if action == "crud_add" and var["JCRUD_AFTERCONF"] :
       _sendNote(var)
       var["JCLOSE_DIALOG"] = True
       return

    if action == "crud_remove" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "@removemailquestion"
       return      
     
    if action == "crud_remove" and var["JCRUD_AFTERCONF"] :
       M = cmail.CMAIL(var)
       name = var["name"]
       M.deleteElemByName(name)
       var["JCLOSE_DIALOG"] = True
       return
     
    if action == "showattach" :
      cmailaction.showattach(var,var["subject"])

       
    cmailaction.mailattachaction(action,var)                
          
# =============================================     

def sendnode(action,var) :        
  
  cutil.printVar("sendnode",action,var)
  
  if action == "send" :
     _sendNote(var)
     var["JCLOSE_DIALOG"] = True
     return
  
  cmailaction.sendnode(action,var)
      
# ===============================================
def dialogattach(action,var) :
    cutil.printVar("dialog attach",action,var)
    
    if action == "before" :
      M = cmail.CMAIL(var)
      note = M.findElem(var["name"])
      l = note.getaList()
      seq = []
      for a in l : seq.append({ "filename" : a.getFileName(), "realm" : a.getRealm(), "key" : a.getBlobKey() })
      cutil.setJMapList(var,ATALIST,seq)  
      
    if action == "download" :
      cmailaction.showattach(var)      

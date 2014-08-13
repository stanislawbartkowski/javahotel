import cutil
import cmail

LI = "list"

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
        if cutil.emptyS(res) : res = None
        seq.append({ "subject" : subject,"to" : recipient, "from" : fromm, "content": content, "res" : res, "name" : l.getName() })
      cutil.setJMapList(var,LI,seq)  
      
def elemaction(action,var) :
    cutil.printVar("elem mail list",action,var)
    
    if action == "crud_add" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "Do you want to send this mail ?"
       return      

    if action == "crud_add" and var["JCRUD_AFTERCONF"] :
       M = cmail.CMAIL(var)
       res = M.sendMail(var["subject"],var["content"],var["to"],var["from"])
       print res
       var["JCLOSE_DIALOG"] = True

    if action == "crud_remove" and not var["JCRUD_AFTERCONF"] :
       var["JYESNO_MESSAGE"] = "Do you want to remove this mail ?"
       return      
     
    if action == "crud_remove" and var["JCRUD_AFTERCONF"] :
       M = cmail.CMAIL(var)
       name = var["name"]
       M.deleteElemByName(name)
       var["JCLOSE_DIALOG"] = True
     
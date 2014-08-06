import cutil
import cmail

def dialogaction(action,var) :
  
  cutil.printVar("send mail",action,var)
  
  if action == "send" :
    res = cmail.sendMail(var["subject"],var["content"],var["to"],var["from"])
    print res
    if cutil.emptyS(res) : var["JOK_MESSAGE"] = "Note was sent (but does not mean that will get through)"
    else : 
      var["JERROR_MESSAGE"] = res
      var["JMESSAGE_TITTLE"] = "Not was not sent"
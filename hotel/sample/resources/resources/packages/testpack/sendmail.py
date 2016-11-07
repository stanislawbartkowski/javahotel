import cutil
import cmail

def dialogaction(action,var) :
  
  cutil.printVar("send mail",action,var)
  
  if action == "send" and var['JYESANSWER'] :       
    if var["attach"] :
       res = cmail.sendMailSingleAttach(var["subject"],var["content"],var["to"],var["from"],var["realm"],var["blobkey"],var["attach"])
    else :
       res = cmail.sendMail(var["subject"],var["content"],var["to"],var["from"])
    print res
    if cutil.emptyS(res) : var["JOK_MESSAGE"] = "Note was sent (but does not mean that will get through)"
    else : 
      var["JERROR_MESSAGE"] = res
      var["JMESSAGE_TITTLE"] = "Not was not sent"
      
def addattach(action,var) :
  cutil.printVar("send mail attach",action,var) 
  
  if action == "attach" :
     var["J_SUBMIT"] = True
     
  if action == "aftersubmit" :
    files = var["JSUBMITRES"]    
    elems = files.split(":")
    realm = elems[0]
    key = elems[1]
    filename = elems[2]
    var["attach"] = filename
    var["realm"] = realm
    var["blobkey"] = key
    cutil.setCopy(var,["attach","realm","blobkey"])
    var["JCLOSE_DIALOG"] = True

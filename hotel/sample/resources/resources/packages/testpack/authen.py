import util
import cutil

def dialogaction(action,var) :

  cutil.printVar("authen",action,var)
    
  if action == "before" :
#    print "...........",cutil.getAppId(var)
    var["current"] = cutil.getPerson(var)
    var["firstname"] = "firstname"
    var["lastname"] = "lastname"
    var["secret"] = "top secret"
    util.setcopy(var,["firstname","lastname","secret","current"])
    
  if action == "copy" :
    seq = ['firstname','lastname',"secret"]
    for n in seq :
      var['JCOPY_copy' + n] = True
      var['copy' + n] = var[n]
      

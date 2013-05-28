import util


def dialogaction(action,var) :

  print action
  for k in var.keys() : 
    print k
    print var[k]
    
  if action == "before" :
    var["firstname"] = "firstname"
    var["lastname"] = "lastname"
    var["secret"] = "top secret"
    util.setcopy(var,["firstname","lastname","secret"])
    
  if action == "copy" :
    seq = ['firstname','lastname',"secret"]
    for n in seq :
      var['JCOPY_copy' + n] = True
      var['copy' + n] = var[n]
      

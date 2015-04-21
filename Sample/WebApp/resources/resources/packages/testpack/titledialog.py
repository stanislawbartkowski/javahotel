import cutil

#def cellTitle(var,id,title) :
#  var["JSETATTR_FIELD_" + id + "_celltitle"] = title

def dialogaction(action,var) :
  cutil.printVar("title dialog",action,var)
  
  if action == "before" :
    cutil.cellTitle(var,"title2","Hello from Jython")
    
  if action == "res" :
    cutil.cellTitle(var,"title3","Now set " + var["title2"])
    

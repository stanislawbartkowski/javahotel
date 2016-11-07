from com.jython.ui.server.guice import ServiceInjector
import cutil

def dialogaction(action,var) :
  cutil.printVar("helperpa",action,var)
  
  
  if action == "copy" :
      var['helpid'] = var['fieldid']
      var['JCOPY_helpid'] = True
      var["JCLOSE_DIALOG"] = True
      
      
def dialoglist(action,var) :
  print "dialoglist",action
  
  if action == "select" :
    for k in var.keys() : 
      print k, var[k]
    if not var["list_lineset"] : return
    var['helplist'] = var["pnumber"]
    var['JCOPY_helplist'] = True
    var["JCLOSE_DIALOG"] = True
    
  
  
  if action == "before" :
    list = []
    op = ServiceInjector.constructPersonOp()
    seq = op.getAllPersons()
       
    for s in seq : 
       elem = {}
       elem["key"] = s.id
       elem["pnumber"] = s.getPersonNumb()
       elem["pname"] = s.getPersonName()
       list.append(elem)
       
    map={}   
    map["list"] = list
    var["JLIST_MAP"] = map  
    return
     
import datetime

def dialogaction(action,var) :
  print "test",action
    
  if action == "main" :
    var["JMAIN_DIALOG"] = "start.xml"  
    
  if action == "list" :
    var["JMAIN_DIALOG"] = "list.xml"  
    
  if action == "before" :
    for k in var.keys() : 
      print k
      print var[k]
  
    var['glob1'] = 'Company'
    var['JCOPY_glob1'] = True
  
    var['globbool'] = False;
    var['JCOPY_globbool'] = True
  
    var['globint'] = 12;
    var['JCOPY_globint'] = True
  
    var['globlong'] = 12456;
    var['JCOPY_globlong'] = True
  
    var['globdecimal'] = 1789.65;
    var['JCOPY_globdecimal'] = True
  
    var['globdate'] = datetime.date(2001,11,5);
    var['JCOPY_globdate'] = True
  
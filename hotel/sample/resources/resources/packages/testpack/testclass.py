import datetime

def dialogaction(action,var) :
  print "test",action
  for k in var.keys() : 
      print k
      print var[k]
  
  if action == "crudaction" :
    var['JERROR_id'] = "error"
    var['JERROR_name'] = " sdsdfsd error"
    pass
  
  if action == "before" :
    for k in var.keys() : 
      print k
      print var[k]
  
    var['glob1'] = 'aaaa'
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
  
    seq = []
    for i in range(10) :
        rec = {}
        rec['id'] = i
        rec['name'] = 'Name ' + str(i)
        seq.append(rec)
    map = {}
    map["list"] = seq
    var["JLIST_MAP"] = map  
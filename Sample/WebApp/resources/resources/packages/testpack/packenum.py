from cutil import printVar

def dialogaction(action,var) :
  printVar("packenum",action,var)
    
  if action == "before" :
     var['combof'] = "3"
     var['JCOPY_combof'] = True
     return  
     
  if action == "testcombo" :
    return   
    
  if action == "signalchange" :
    s = var["combof"]
    var['outcombof'] = s;
    var['JCOPY_outcombof'] = True
    return  
    
  seq = []
  for i in range(100) :
        rec = {}
        rec['id'] = str(i)
        rec['name'] = 'name' + str(i)
        seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  

  
def helperaction(action,var) :

  print "helperaction",action
  for k in var.keys() : 
    print k, var[k]
    
  seq = []
  for i in range(100) :
        rec = {}
        rec['id'] = str(i)
        rec['name'] = 'helper ' + str(i)
        seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  
    
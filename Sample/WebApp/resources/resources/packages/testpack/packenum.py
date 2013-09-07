from cutil import printVar
from cutil import getTypeUpList
from cutil import setJMapList

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

  printVar("helperaction",action,var)
    
  seq = []
  for i in range(100) :
        rec = {}
        rec['id'] = str(i)
        rec['name'] = 'helper ' + str(i)
        seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  
    
    
def uptextaction(action,var) :
  printVar("uptextaction",action,var)
  seq = []
  for i in getTypeUpList() :
        rec = {'id' : i, "name" : i }
        seq.append(rec)
 
  setJMapList(var,action,seq)    

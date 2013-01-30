
def dialogaction(action,var) :
  print "packenum",action
  for k in var.keys() : 
    print k, var[k]
    
  seq = []
  if action == "tenum" :
    for i in range(100) :
          rec = {}
          rec['id'] = str(i)
          rec['name'] = 'name' + str(i)
          seq.append(rec)
  
  if action == "tehelper" :
    for i in range(10) :
          rec = {}
          rec['id'] = str(i)
          rec['name'] = 'name' + str(i)
          seq.append(rec)
  
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  

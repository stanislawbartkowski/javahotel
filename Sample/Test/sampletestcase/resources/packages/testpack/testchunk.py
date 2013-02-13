
def dialogaction(action,var) :

  print "action",action
  for v in var.keys() :
    print v,var[v]

  if action == "readchunk" :
    start = var["JLIST_FROM"]
    len = var["JLIST_LENGTH"]
    list = []
    
    for s in range(len) : 
       elem = {}
       list.append(elem)
       
    map={}   
    map["list"] = list
    var["JLIST_MAP"] = map  
    

  if action=="before" :           
    map={}   
    map["list"] = 20
    var["JLIST_MAP"] = map  

  if action=="listgetsize" :
    map={}   
    map["list"] = 74
    var["JLIST_MAP"] = map  
    
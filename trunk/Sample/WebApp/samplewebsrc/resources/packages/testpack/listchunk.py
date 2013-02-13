import datetime

def dialogaction(action,var) :

  print "list",action
  for k in var.keys() : 
    print k
    print var[k]

  if action == "before" :
      map={}   
      map["list"] = 200
      var["JLIST_MAP"] = map
      
  if action == "readchunk" :
      start = var["JLIST_FROM"]
      len = var["JLIST_LENGTH"]
      sort = var["JLIST_SORTLIST"]
      asc = var["JLIST_SORTASC"]
      
      
      blist = []
      for i in range(200) :
         f = i
         if sort :
           if sort == "name" : f =  str(i) + ": name"
         blist.append((f,i))
        
      if sort :
        blist.sort()
        if not asc : blist.reverse()
        
      list = []
      for elem in blist[start:start+len] :
        print elem
        i = elem[1]
        rec = {}
        rec["id"] = i
        rec["name"] = str(i) + ": name"
        list.append(rec)
          
      map={}   
      map["list"] = list
      var["JLIST_MAP"] = map
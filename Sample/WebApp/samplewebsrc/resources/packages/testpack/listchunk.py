import datetime

_SIZE=26

def __getlist(var) :
            
      blist = []
      for i in range(_SIZE) :
         if var["JSEARCH_SET_id"] :
           ifrom = var["JSEARCH_FROM_id"]
           ito = var["JSEARCH_TO_id"]
           ieq = var["JSEARCH_EQ_id"]
           if ifrom != None :
             if ieq and ifrom != i : continue
             if i < ifrom : continue
             if ito != None:
               if i > ito : continue

         name = str(i) + ": name"            
         
         if var["JSEARCH_SET_name"] :
           ifrom = var["JSEARCH_FROM_name"]
           ito = var["JSEARCH_TO_name"]
           ieq = var["JSEARCH_EQ_name"]
           if ifrom != None :
             if ieq and ifrom != name : continue
             if name < ifrom : continue
             if ito != None:
               if name > ito : continue
             
         blist.append((i,name))
         
      return blist  

def dialogaction(action,var) :

  print "list",action
  for k in var.keys() : 
    print k
    print var[k]

  if action == "before" :
      map={}   
#      map["list"] = 200
      map["list"] = _SIZE  
      var["JLIST_MAP"] = map
      
  if action == "listgetsize" :    
      blist = __getlist(var)
      print len(blist)
      map={}   
      map["list"] = len(blist)
      var["JLIST_MAP"] = map
      
  if action == "readchunk" :
      start = var["JLIST_FROM"]
      size = var["JLIST_LENGTH"]
      sort = var["JLIST_SORTLIST"]
      asc = var["JLIST_SORTASC"]
      list = __getlist(var)
      
      blist = []
      for (i,name) in list :
        f = i 
        if sort :
          if sort == "name" : f =  name
        v = {}
        v["id"] = i
        v["name"] = name  
        blist.append((f,v))
        
      if sort :  
         blist.sort()
         if not asc : blist.reverse()
      

      list = []
      for (i,v) in blist[start:start+size] :
         list.append(v)
          
      map={}   
      map["list"] = list
      var["JLIST_MAP"] = map
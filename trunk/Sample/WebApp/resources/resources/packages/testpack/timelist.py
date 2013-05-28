import datetime

def dialogaction(action,var) :

  print "list",action
  for k in var.keys() : 
    print k
    print var[k]
      
  if action == "before" :
      print "aaa"
      list = []
      ti = datetime.datetime(2017,01,13,20,45,14)
      de = datetime.timedelta(1)
      
      for i in range(100) :
         print i
         rec = {}
         ti = ti + de
         rec["timef"] = ti
         print ti
         list.append(rec)
        
      print "len=",len(list)  
      map={}   
      map["list"] = list
      var["JLIST_MAP"] = map  
      return
    
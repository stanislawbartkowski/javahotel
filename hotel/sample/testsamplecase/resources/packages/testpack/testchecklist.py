
def dialogaction(action,var) :
  print "testchecklist",action
  for k in var.keys() : 
    print k, var[k]
    
  if action == "dosth" :
      m = var["JCHECK_MAP"]
      map = m["check"]
      seq = map["hotel1"]
      ok = 0
      for r in seq :
          print r["id"],r["val"]  
          if r["id"] == "man" and r["val"] : ok = ok + 1 
          if r["id"] == "acc" and not r["val"] : ok = ok + 1
      
      var["OKTEST"] = (ok == 2)
      return
    
  if action == "before" :
      checkmap = {}
      
      seq = []
      m = {}
      m["id"] = "hotel1"
      m["displayname"] = "Hotel One"
      seq.append(m)
      m = {}
      m["id"] = "hotel2"
      m["displayname"] = "Hotel Two"
      seq.append(m)
      checkmap["lines"] = seq
      
      seq = []
      m = {}
      m["id"] = "man"
      m["displayname"] = "Manager"
      seq.append(m)
      m = {}
      m["id"] = "acc"
      m["displayname"] = "Accountant"
      seq.append(m)
      checkmap["columns"] = seq
      
      seq = []
      m = {}
      m["id"] = "man"
      m["val"] = True
      seq.append(m)
      m = {}
      m["id"] = "acc"
      m["val"] = False
      seq.append(m)
      checkmap["hotel1"] = seq

      seq = []
      m = {}
      m["id"] = "man"
      m["val"] = False
      seq.append(m)
      m = {}
      m["id"] = "acc"
      m["val"] = True
      seq.append(m)
      checkmap["hotel2"] = seq
      
      map = {}
      map["check"] = checkmap
      
      var["JCHECK_MAP"] = map    

def numdialogaction(action,var) :
  print "testnumlist",action
  for k in var.keys() : 
    print k, var[k]
    
  if action == "before" :  
      lines = [{"id" : "price1", "displayname" : "pricelist1"}, {"id" : "price2" , "displayname" : "pricelist2"}, {"id" : "price3", "displayname" : "Price list 3"}]
      columns = [{"id" : "weekend", "displayname" : "Weekend price"}, {"id" : "working", "displayname" : "Working day price"}];
      vals = [{"id" : "weekend" ,"val" : 20.11},{"id" : "working" ,"val" : 3.77} ]
      map = { "lines" : lines, "columns" : columns,"price1" : vals}
      var["JCHECK_MAP"] = {"prices" : map }
      
  if action == "dosth" :
      m = var["JCHECK_MAP"]
      map = m["prices"]
      seq = map["price1"]
      sum = 0.0
      for r in seq :
          print r["id"],r["val"]  
          sum = sum + r["val"]
      
      print str(sum)
      var["OKTEST"] = (str(sum) == "13.24")
      return
  
  if action == "checkerror" :
      seq = [{"line" : "price1","col" : "working","errmess" : "Should be greater then 0"}]
      map = {"JERROR" : seq}
      var["JCHECK_MAP"] = {"prices" : map}
  



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

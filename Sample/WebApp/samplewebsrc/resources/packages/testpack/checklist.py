

def setperm(action,var):
    map = var["JCHECK_MAP"] 
    checkmap = map["check"]
    for h in checkmap.keys() :
        if h == "lines" : continue
        if h == "columns" : continue
        var["JCOPY_" + h] = True
        val = ""
        for v in checkmap[h] :
            print v
            if v["val"] : 
                if val != "" : val = val + " "
                val = val + v["id"]
        var[h] = val    
    

def dialogaction(action,var) :
  print "test",action
  for k in var.keys() : 
      print k
      print var[k]
      
      
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
      
      print var["JCHECK_MAP"]
      
      setperm(action,var) 

  if action == "check" :
      setperm(action,var) 
      

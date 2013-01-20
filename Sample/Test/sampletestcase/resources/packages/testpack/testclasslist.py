
def dialogaction(action,var) :
  print "test",action
  if action == "writelist" :
    map = var["JLIST_MAP"]
    seq = map["lista1"]
    for e in seq :
      print e
      id = e['id']
      name = e['name']
      print id,name
      var['OK'] = True
      if id != "cid" : var['OK'] = False
      if name != "Company name" : var['OK'] = False
      
  if action == "checkList" :
    map = var["JLIST_MAP"]
    seq = map["lista1"]
    for e in seq :
      print e
      id = e['id']
      name = e['name']
      print id,name
      var['OK'] = True
      if id != "cid" : var['OK'] = False
      if name != None : var['OK'] = False
      
    
def dialogaction1(action,var) :
    print "action1 ",action
    if action == "createList" :
      seq = []
      for i in range(100) :
        rec = {}
        rec['id'] = i
        seq.append(rec)
      map = {}
      map["lista"] = seq
      var["JLIST_MAP"] = map  
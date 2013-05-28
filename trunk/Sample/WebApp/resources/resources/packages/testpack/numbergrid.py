
def dialogaction(action,var) :
  print "numbergrid",action
  for k in var.keys() : 
    print k, var[k]
    
    
  if action == "before" :  
      lines = [{"id" : "price1", "displayname" : "Price list 1"}, {"id" : "price2" , "displayname" : "Price list 2"}, {"id" : "price3", "displayname" : "Price list 3"},{"id" : "sum", "displayname" : "Sum"}]
      columns = [{"id" : "weekend", "displayname" : "Weekend price"}, {"id" : "working", "displayname" : "Working day price"}];
      vals = [{"id" : "weekend" ,"val" : 20.11},{"id" : "working" ,"val" : 3.77} ]
      map = { "lines" : lines, "columns" : columns,"price1" : vals}
      var["JCHECK_MAP"] = {"prices" : map }
      
  if action == "check":
      map = var["JCHECK_MAP"]["prices"]
      summap = map["sum"]
      for col in summap :
          col["val"] = None    
      lines = ["price1","price2","price3"]
      err = []
      for line in lines:
          row = map[line]
          for col in row :
              id = col["id"]
              val = col["val"]
              if val == None : continue
              if val < -10 :
                err.append({"line" : line, "col" : id, "errmess" : "Number cannot be less then -10"})    
      map["JERROR"] = err
      for line in lines :
          row = map[line]
          for col in row :
            id = col["id"]
            for su in summap :
              if id == su["id"] :
                  val1 = col["val"]
                  if val1 == None : continue
                  val2 = su["val"]
                  if val2 == None : val2 = val1
                  else: val2 = val2 + val1
                  su["val"] = val2

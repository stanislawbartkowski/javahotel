import cutil

def dialogaction(action,var) :

  cutil.printVar("actionlist",action,var)

  if action == "before" :
      seq=[]
      for i in range(100) :
#          elem = {"id" : i, "name" : "name " + str(i), "action" : "Do" }
          elem = {"id" : i, "name" : "name " + str(i) }
          seq.append(elem)
          
      map={}
      map["list"] = seq             
      var["JLIST_MAP"] = map
   
  if action == "dosth" :
     var['JUP_DIALOG'] = 'actiondial.xml'
    

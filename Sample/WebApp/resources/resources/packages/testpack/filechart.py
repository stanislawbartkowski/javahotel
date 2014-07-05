import cutil

LI = "chart"
ELI = "list"

def dialogaction(action,var) :
  cutil.printVar("chart action",action,var)
  
  if action == "before" :
     seq = []
     seq.append({ "id" : 10,"name" : "Hello"})
     seq.append({ "id" : 20,"name" : "Wow"})
     seq.append({ "id" : 40,"name" : "Ough"})
     cutil.setJChartList(var,LI,seq)
     cutil.setJMapList(var,ELI,seq)
     cutil.setAddEditMode(var,ELI,["id","name"])
     
  if action == "editlistrowaction":
    var["JLIST_EDIT_ACTIONOK_" + ELI] = True
    
  if action == "refresh" :
    seq = var["JLIST_MAP"][ELI]
    cutil.setJChartList(var,LI,seq)    
  
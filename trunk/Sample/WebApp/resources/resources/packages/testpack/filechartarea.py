import cutil

LI = "chart"
LI1 = "chart1"
LI2 = "chart2"
LI3 = "chart3"
ELI = "list"

def dialogaction(action,var) :
  cutil.printVar("chart action",action,var)
  
  if action == "before" :
     seq = []
     seq.append({ "id" : 100,"id1" : 50, "id2" : 10, "name" : "Morning"})
     seq.append({ "id" : 90,"id1" : 60, "id2" : 15, "name" : "Afternoon"})
     seq.append({ "id" : 80,"id1" : 70, "id2" : 10, "name" : "Evening"})
     cutil.setJChartList(var,LI,seq)
     cutil.setJChartList(var,LI1,seq)
     cutil.setJChartList(var,LI2,seq)
     cutil.setJChartList(var,LI3,seq)
     cutil.setJMapList(var,ELI,seq)
     cutil.setAddEditMode(var,ELI,["id","name","id1","id2" ])
     
  if action == "editlistrowaction":
    var["JLIST_EDIT_ACTIONOK_" + ELI] = True
    
  if action == "refresh" :
    seq = var["JLIST_MAP"][ELI]
    cutil.setJChartList(var,LI,seq)
    cutil.setJChartList(var,LI1,seq)
    cutil.setJChartList(var,LI2,seq)            
    cutil.setJChartList(var,LI3,seq)
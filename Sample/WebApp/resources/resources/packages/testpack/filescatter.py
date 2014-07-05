import cutil

EDIT="list"
CHART="chart"

def dialogaction(action,var) :
  cutil.printVar("scatter chart",action,var)
  
  if action == "before" :
    seq = []
    seq.append({"num1" : 10,"num2" : 50})
    seq.append({"num1" : 12,"num2" : 55})
    seq.append({"num1" : 6,"num2" : 40})
    seq.append({"num1" : 3,"num2" : 66})
    seq.append({"num1" : 2,"num2" : 15})
    cutil.setJChartList(var,CHART,seq)
    cutil.setJMapList(var,EDIT,seq)
    cutil.setAddEditMode(var,EDIT,["num1","num2" ])
    
  if action == "editlistrowaction":
    var["JLIST_EDIT_ACTIONOK_" + EDIT] = True
    
  if action == "refresh" :
    seq = var["JLIST_MAP"][EDIT]
    cutil.setJChartList(var,CHART,seq)
    cutil.setJMapList(var,EDIT,seq)
    
import cutil


def dialogaction(action,var) :
  cutil.printVar("chart",action,var)
  
  if action == "before" :
      seq = []
      seq.append({ "id" : 5, "name" : "hello"})
      cutil.setJChartList(var,"chart",seq)

import cutil

LIST="list"


def dialogaction(action,var) :
    cutil.printVar("change",action,var)
    
    if action == "before" :
      no = 0
      seq = []
      for no in range(100) :
	ma = { "id":no+1, "number0": float(no), "number1" : float(no*2), "number2" : float(no*3),"number3" : float(no*4),"number4" : float(no*5) }
	seq.append(ma)
      cutil.setJMapList(var,LIST,seq)
      cutil.setHeader(var,LIST,"id","Next num")
      cutil.listColumnVisible(var,LIST,"id",True)
      
    if action == "change" :
      head = var["ncolumn"]
      cutil.setHeader(var,LIST,"number0",head)
      
    if action == "signalchange" :
      vis = var["nvis"]
      cutil.listColumnVisible(var,LIST,"number2",vis)
      
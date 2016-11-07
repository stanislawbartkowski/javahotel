import cutil

LIST="list"

def dialogaction(action,var) :
    cutil.printVar("change",action,var)
    
    if action == "before" :
      no = 0
      seq = []
      for no in range(100) :
	ma = { "id":no+1, "number": float(no), "check" : False }
	seq.append(ma)
      cutil.setJMapList(var,LIST,seq)
      cutil.setStandEditMode(var,LIST,"check")
      

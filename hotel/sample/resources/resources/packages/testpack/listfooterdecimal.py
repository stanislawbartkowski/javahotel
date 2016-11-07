import cutil

def dialogaction(action,var) :
  
  cutil.printVar("list footer",action,var)
  
  if action == "before" :
      seq = []
      sum = 0
      for i in range(1,100) :
	ma = {"id":i,"number" : 45678.99}
	seq.append(ma)
	sum = sum + ma["number"]
      cutil.setJMapList(var,"list",seq)	
      cutil.setFooter(var,"list","number",sum)
	
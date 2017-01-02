import util
import cutil


def dialogaction(action,var) :
  cutil.printVar("binder1",action,var)
  
  if action == "before" :
    for i in range(3) :
        v = "value" + str(i+1)
        cutil.setCopy(var,v)
        var[v] = False
  
  i = -1  
  if action == "heading1" : i = 1
  if action == "heading2" : i = 2
  if action == "heading3" : i = 3
  if i == -1 : return
  v = "value" + str(i)
  var[v] = not var[v]
  cutil.setBinderOpenedAttr(var,"collapse"+str(i),var[v])
  cutil.setCopy(var,v)

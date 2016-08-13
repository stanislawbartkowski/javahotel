import util
import cutil


def dialogaction(action,var) :
  cutil.printVar("binder1",action,var)

# =================
VAR = ["elevation","up"]
VAR1 = ["elevation1","up1"]

def setTap(var,ele,up,v) :
    cutil.setCopy(var,v)
    var[v[0]] = ele
    var[v[1]] = up
  
def dialogaction12(action,var) :

  cutil.printVar("binder12",action,var)
  
  if action == "before" :
#    cutil.setCopy(var,["elevation","up"],)
    setTap(var,0,True,VAR)
    setTap(var,0,True,VAR1)
    

  v = VAR  
  if action == "material" : v = VAR1
  
  if action == "material" or action == "fabMaterial" :
    ele = var[v[0]]
    up =var[v[1]]
    if ele == 5 : up = False
    elif ele == 0 : up = True
    if up : ele = ele + 1
    else : ele = ele - 1
    setTap(var,ele,up,v)
    cutil.setElevationAttr(var,action,ele)
    
      

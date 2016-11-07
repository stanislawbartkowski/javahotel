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
    
VALS1 = ["toggle1_1","toggle1_2","toggle1_3","toggle1_4"]
VALS2 = ["toggle2_1","toggle2_2","toggle2_3","toggle2_4"]
#VALS1 = ["toggle1_1","toggle1_2"]
#VALS2 = ["toggle2_1","toggle2_2"]
    
def spinneraction(action,var) :
  cutil.printVar("spinner action",action,var)
  
  if action == "before" :
    cutil.setCopy(var,["toggle1","toggle2"])
    var["toggle1"]  = True
    var["toggle2"]  = True
    return
  
  V = None
  if action == "toggleBtn1" : 
    V = VALS1
    to = "toggle1"
  if action == "toggleBtn2" : 
    V = VALS2
    to = "toggle2"
  if V == None : return
  active = not var[to]  
  for v in V :
    cutil.setSpinnerActive(var,v,active)
  cutil.setCopy(var,to)
  var[to] = active
  
  
def slideraction(action,var) :
  cutil.printVar("slider action",action,var)
  
  if action == "signalchange" :
     cutil.setLabelText(var,"ratingsLabel",var["ratings"])
    
  
    
      

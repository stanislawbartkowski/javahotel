import cutil
import math

LI = "chart"

def _drawCircle(var) :
  radius = var["radius"]
  nop = var["nop"]
  seq = []
  for i in range(nop) :
    x = (2 * math.pi * i) / nop
    posx = math.cos(x) * radius
    posy = math.sin(x) * radius
    seq.append( {"posx" : posx, "posy" : posy } )
  cutil.setJChartList(var,LI,seq)

def dialogaction(action,var) :
  cutil.printVar("circle",action,var)
  
  if action == "before" :
    var["radius"] = 200
    var["nop"] = 100
    cutil.setCopy(var,["radius","nop"])
    _drawCircle(var)
    
  if action == "refresh" :
    _drawCircle(var)
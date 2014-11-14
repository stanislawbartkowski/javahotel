import cutil

from util import util

class RESPARAM(util.HOTELSAVEPARAM) :
  
  def __init__(self,resename="") :
    li = ["resdays","respriceperson","serviceperperson","respriceperroom","resnop","resnochildren","respricechildren","resnoextrabeds","respriceextrabeds"]
    util.HOTELSAVEPARAM.__init__(self,resename,li)
    
  def copyParam(self,var) :
    cutil.setCopy(var,"xmlparam")
    var["xmlparam"] = self.createXMLParam(var)
    
  def setParam(self,var) :
    ma = self.getParam()
    if ma == None : return
    for l in self.getLi() :
      var[l] = ma[l]
      
  def diffP(self,var) :
    maold = self.getParam(var["xmlparam"])
    print maold
    diff = self.diffParam(var,maold)
    return diff
  
  def modifvar(self,var) :
    ma = self.getParam()
    if ma == None : return
    for k in ma.keys() : var[k] = ma[k]

      
    
        
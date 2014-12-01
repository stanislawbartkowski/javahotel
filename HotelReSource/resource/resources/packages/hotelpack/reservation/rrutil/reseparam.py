import cutil

from util import util

class RESPARAM(util.HOTELSAVEPARAM) :
  
  def __init__(self,resename="") :
    li = ["resdays","respriceperson","serviceperperson","respriceperroom","resnop","resnochildren","respricechildren","resnoextrabeds","respriceextrabeds"]
    util.HOTELSAVEPARAM.__init__(self,resename,li)
    
  def copyParam(self,var) :
    """ Create the copy of current values and set it in xmlparam
     Args: var : current values, source
    """
    cutil.setCopy(var,"xmlparam")
    var["xmlparam"] = self.createXMLParam(var)
    
  def setParam(self,var,setcopy=True) :
    """ Restores data proviously save
      Args: 
        var : var container, destination
        setcopy : if true it automatically sets cutil.setCopy for value retrieved
      
    """
    ma = self.getParam()
    if ma == None : return
    for l in self.getLi() :
      if setcopy: cutil.setCopy(var,l)
      var[l] = ma[l]
      
  def diffP(self,var) :
    maold = self.getParam(var["xmlparam"])
    diff = self.diffParam(var,maold)
    return diff
        
  # saveParam(self,var)      
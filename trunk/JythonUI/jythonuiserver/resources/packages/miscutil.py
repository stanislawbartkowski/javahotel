from com.jythonui.server.holder import Holder

import cutil
import xmlutil

class SAVE_REGISTRY() :
  
  def __init__(self,rentry) :
    self.rentry = rentry
    
  def __getR(self,var) :
    return cutil.StorageRegistry(Holder.getRegFactory(),"TEMPORARY-DATA-" + var["SECURITY_TOKEN"])
    
  def saveMap(self,var,l) :
    R = self.__getR(var)
    R.putEntry(self.rentry,xmlutil.toXML(l))
    
  def getMap(self,var) :
    R = self.__getR(var)
    xml = R.getEntryS(self.rentry)
    (rmap,li) = xmlutil.toMap(xml)
    return rmap

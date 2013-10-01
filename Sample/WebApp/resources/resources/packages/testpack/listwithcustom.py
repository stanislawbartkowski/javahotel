import cutil
from com.jython.ui.server.guice import ServiceInjector

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "number" : cutil.LONG, "dic" : cutil.STRING }
      cutil.RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"REGISTRY-CUSTOM-ENUM",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()

def dialogaction(action,var) :
    cutil.printVar("listwithcustom",action,var)
  
    if action == "before" or action == "crud_readlist" :
      li = F.readList(var)
      
def elemaction(action,var) :
    cutil.printVar("elem action",action,var)
    
    if action == "before" :
      if var["JCRUD_DIALOG"] == "crud_change" or var["JCRUD_DIALOG"] == "crud_remove" :
        setCopy(var,["id","number","dic"])
    
    if action == "crud_add" :
      key = F.nextKey()
      var["id"] = key
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True

    if action == "crud_change" :
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True

    if action == "crud_remove" :
     F.removeMap(var)
     var["JCLOSE_DIALOG"] = True

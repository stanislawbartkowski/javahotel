from cutil import printVar
from cutil import StorageRegistry
from cutil import RegistryFile
from com.jython.ui.server.guice import ServiceInjector
from cutil import setCopy
from xmlutil import getVar

class LISTREGISTRY(RegistryFile):

    def __init__(self) :
      map = {"id" : "long", "firstname" : "string","lastname" : "string", "credit" : "decimal"  }
      RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"LIST-ICON-REGISTRY",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()


def dialogaction(action,var) :
  printVar("listicon",action,var)
  
  if action == "before" or action == "crud_readlist" :
       F.readList(var)
       seq = var["JLIST_MAP"]["list"]
       dname = "listxml.xml"
       for m in seq :
         m["image"] = str(m["credit"])


  if action == "clearrecords" :
        F.removeAll()
        F.readList(var)
        
  if action == "clickimage" :
    no = var["imagecolumn"]
    if no == 0 :
      var["JOK_MESSAGE"] = "Less or greater then 0"
    else : var["JOK_MESSAGE"] = "Odd or even" 

  
def elemaction(action,var) :
  
    printVar("elem action",action,var)
    
    if action == "before" :
      if var["JCRUD_DIALOG"] == "crud_change" or var["JCRUD_DIALOG"] == "crud_remove" :
        setCopy(var,["id","firstname","lastname","credit"])
    
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
  
from cutil import printVar
from cutil import StorageRegistry
from cutil import RegistryFile
from com.jython.ui.server.guice import ServiceInjector
from cutil import setCopy
from xmlutil import getVar

class LISTREGISTRY(RegistryFile):

    def __init__(self) :
      map = {"id" : "long", "xml" : "string" }
      RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"LIST-XML-REGISTRY",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()


def dialogaction(action,var) :
    printVar("dialogxml",action,var)
    
    if action == "before" or action == "crud_readlist" :
       F.readList(var)
       seq = var["JLIST_MAP"]["list"]
#       dname = var["J_DIALOGNAME"]
       dname = "listxml.xml"
       for m in seq :
         xml = m["xml"]
         ma = {}
         getVar(ma,dname,xml,["name1"])
         m["firstname"] = ma["name1"]


    if action == "clearrecords" :
        F.removeAll()
        F.readList(var)

def elemaction(action,var) :
    printVar("elem action",action,var)
    
    if action == "before" :
      if var["JCRUD_DIALOG"] == "crud_change" or var["JCRUD_DIALOG"] == "crud_remove" :
        var["JXMLCONTENT"] = var["xml"]
        var["JXMLCONTENTSET"] = True
        setCopy(var,["id","name1","name2"])
    
    if action == "crud_add" :
      key = F.nextKey()
      var["id"] = key
      var["xml"] = var["JXMLCONTENT"]
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True

    if action == "crud_change" :
      var["xml"] = var["JXMLCONTENT"]
      F.addMap(var)
      var["JCLOSE_DIALOG"] = True

    if action == "crud_remove" :
     F.removeMap(var)
     var["JCLOSE_DIALOG"] = True

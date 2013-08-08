from cutil import printVar
from cutil import StorageRegistry
from cutil import RegistryFile
from com.jython.ui.server.guice import ServiceInjector
import datetime
import time
            
class LISTREGISTRY(RegistryFile):

    def __init__(self) :
      map = {"id" : "long", "date1" : "date", "nameid" : "string","comboid" : "string", "helperid" : "string" }
      RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"LIST-TEST-REGISTRY",ServiceInjector.getSequenceGen(),map, "listda","id")


def doaction(action,var):
    printVar("do action",action,var)
    
    F = LISTREGISTRY()
    
    if action == "before" :
        F.readList(var)
        var["JLIST_EDIT_listda_date1"] = ""
        var["JLIST_EDIT_listda_nameid"] = ""
        var["JLIST_EDIT_listda_comboid"] = ""
        var["JLIST_EDIT_listda_helperid"] = ""
        var["JLIST_EDIT_listda_MODE"] = "CHANGEMODE" 
        
    if action == "editlistrowaction" and var["JLIST_EDIT_ACTION_listda"] == "REMOVE" :
        F.removeMap(var)
        var["JLIST_EDIT_ACTIONOK_listda"] = True
    
    if action == "editlistrowaction"  :
        key = F.nextKey()
        var['id'] = key
        var['JCOPY_id'] = True
        var["JLIST_EDIT_ACTIONOK_listda"] = True
        
    if action == "aftereditrow" :
        F.addMap(var)
        var["JEDIT_ROW_OK_listda"] = True
    
    if action == "helper" :
       var["JUP_DIALOG"] = "selectfromlist.xml"       

def selaction(action,var):
    
    sel = None
    if action == "select1" :
        sel = "AAA"
    if action == "select2" :
        sel = "BBB"
    if action == "select3" :
        sel = "CCC"
    if action == "select4" :
        sel = "DDD"
        
    if sel :
        var['nameid'] = sel
        var['JCOPY_nameid'] = True
        var["JCLOSE_DIALOG"]  = True    
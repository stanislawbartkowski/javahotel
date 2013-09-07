from cutil import printVar
import cutil
from cutil import modifDecimalFooter
from cutil import RegistryFile
from com.jython.ui.server.guice import ServiceInjector
from cutil import setCopy

class LISTREGISTRY(RegistryFile):

    def __init__(self) :
      map = {"leftk" : "string", "rightk" : "string","amount" : "decimal", "id" : "long" }
      RegistryFile.__init__(self,ServiceInjector.getStorageRegistryFactory(),"FOOTER-DIFFERENT",ServiceInjector.getSequenceGen(),map, "list","id")

F = LISTREGISTRY()

def doaction(action,var) :
  printVar("footeredit",action,var)
  if action == "before" :
#      cutil.setJMapList(var,"list",[])
      cutil.setAddEditMode(var,"list",["leftk","amount","rightk"])
      li = F.readList(var)
      leftf = None
      rightf = None
      for l in li :
        if l["leftk"] :
          leftf = cutil.addDecimal(leftf,l["amount"])
        if l["rightk"] :
          rightf = cutil.addDecimal(rightf,l["amount"])
      cutil.setFooter(var,"list","leftk",leftf)
      cutil.setFooter(var,"list","rightk",rightf)
      
  if action == "editlistrowaction"  :
    var["JLIST_EDIT_ACTIONOK_list"] = True
    if var["JLIST_EDIT_ACTION_list"] == "REMOVE" :
      cutil.removeTextFooter(var,"list","leftk","amount")
      cutil.removeTextFooter(var,"list","rightk","amount")
    
  if action == "save" :
    li = var["JLIST_MAP"]["list"]
    k = 0
    for m in li :
      m["id"] = k
      k = k + 1
    F.saveList(var)
    
  if action == "columnchangeaction" and ((var["changefield"] == "leftk" or var["changefield"] == "rightk")) :
    cutil.modifTextFooter(var,"list",var["changefield"],"amount")

  if action == "columnchangeaction" and var["changefield"] == "amount" :
    cutil.modifTextDecimalFooter(var,"list","leftk","amount")
    cutil.modifTextDecimalFooter(var,"list","rightk","amount")


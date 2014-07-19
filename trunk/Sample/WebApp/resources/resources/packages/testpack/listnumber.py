import cutil

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "number0" : cutil.LONG, "number1" : cutil.DECIMAL, "number2" : cutil.DECIMAL, "number3" : cutil.DECIMAL, "number4" : cutil.DECIMAL}
      cutil.RegistryFile.__init__(self,None,"LIST-X-X-NUMBER-DIFFERENT-DEMO",None,map, "list","id")

F = LISTREGISTRY()

def dialogaction(action,var) :
  cutil.printVar("list number",action,var)
   
  if action == "before" or action == "crud_readlist" :
    F.readList(var)
    
  if action == "crud_add"  :
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
      

  
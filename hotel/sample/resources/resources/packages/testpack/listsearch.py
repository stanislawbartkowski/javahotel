import cutil


class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "firstname" : cutil.STRING, "lastname" : cutil.STRING, "info" : cutil.STRING }
      cutil.RegistryFile.__init__(self,None,"LIST-CUSTOM-SEARCH",None,map, "list","id")

F = LISTREGISTRY()

def dialogaction(action,var) :
  cutil.printVar("search list",action,var)


  if action == "before" :
    F.removeAll()
    for i in range(10) :
      map = { "id" : F.nextKey(), "firstname" : "first" + str(i),"lastname" : "last" + str(i),"info" : "info" + str(i)}
      F.addMap(map)
      
    F.readList(var)
    
def searchaction(action,var) :    
  cutil.printVar("search action",action,var)
  
  if action == "find" :
    li = F.readList(var,False)
    sname = var["name"]
    found = False
    for l in li :
      name = l["firstname"]
      if name.find(sname) != -1 :
        var["JSEARCH_LIST_SET_list_id"] = l["id"]
        found = True
    if not found :
      var["JOK_MESSAGE"] = "Not found"
        

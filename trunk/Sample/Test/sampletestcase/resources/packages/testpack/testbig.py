import cutil

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {"id" : cutil.LONG, "bignumber" : cutil.DECIMAL}
      cutil.RegistryFile.__init__(self,None,"LIST-X-BIGNUMBER-DEMO",None,map, "list","id")

F = LISTREGISTRY()


def dialogaction(action,var):
    cutil.printVar("test big",action,var)
    
    if action == "run" :
        f = var["bignumber"]
        print f
        var["out"] = f
        
    if action == "save" :
        F.removeAll()
        key = F.nextKey()
        var["id"] = key
        F.addMap(var)    
        
    if action == "restore" :
        seq = F.readList(var,False)
        ma = seq[0]
        f = ma["bignumber"]
        print f
        var["out"] = f   
        
    if action == "runthread" :
        for i in range(10) :
            print var["no"]," -----------------------"    
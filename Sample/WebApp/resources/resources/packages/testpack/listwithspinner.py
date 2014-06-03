import cutil

LI = "list"
ID = "id"

class LISTREGISTRY(cutil.RegistryFile):

    def __init__(self) :
      map = {ID : cutil.LONG, "pnumber" : cutil.LONG, "pname" : cutil.STRING }
      cutil.RegistryFile.__init__(self,None,"LIST-EDIT-SPINNER-REGISTRY",None,map, LI,ID)

F = LISTREGISTRY()

def dialogaction(action,var) :
  cutil.printVar("edit list spinner",action,var)
  
  if action == "before" :
     F.readList(var)
     cutil.setAddEditMode(var,LI,["pnumber","pname"])
     
  if action == "editlistrowaction"  :
      var["JLIST_EDIT_ACTIONOK_" + LI] = True
      if var["JLIST_EDIT_ACTION_"+LI] == "ADDBEFORE" or var["JLIST_EDIT_ACTION_" + LI ] == "ADD" :
         key = F.nextKey()
         var[ID] = key
         var["pnumber"] = 2
         cutil.setCopy(var,[ID,"pnumber"],LI)
                  
      if var["JLIST_EDIT_ACTION_"+LI] == "REMOVE" :
         F.removeMap(var)
         
  if action == "aftereditrow" :
      F.addMap(var)   
      var["JEDIT_ROW_OK_" + LI] = True
         
         
         


    
 
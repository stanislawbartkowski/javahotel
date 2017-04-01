import cutil

LIST="list"

def dialogaction(action,var) :
   
   cutil.printVar("Column list",action,var)
   
   if action == "before" :
     cutil.setStandEditMode(var,LIST,["visible","columnname"])
     
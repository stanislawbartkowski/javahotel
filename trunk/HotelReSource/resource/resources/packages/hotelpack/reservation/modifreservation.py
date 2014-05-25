import cutil
import xmlutil

LI="reslist"

def dialogaction(action,var) :
  cutil.printVar("modif reservation",action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      (m,li) = xmlutil.toMap(xml)
      for l in li :
        print l
      cutil.setJMapList(var, LI,li)
      cutil.setAddEditMode(var,LI,["resroomname"])
      
  if action == "clickimage" and var["changefield"] == "chooseroom" :
     pass
    

  
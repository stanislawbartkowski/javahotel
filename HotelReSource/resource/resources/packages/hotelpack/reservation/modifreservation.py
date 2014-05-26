import cutil
import xmlutil

LI="resmodiflist"

def dialogaction(action,var) :
  cutil.printVar("modif reservation",action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      (m,li) = xmlutil.toMap(xml)
      for l in li :
        print l
      cutil.setJMapList(var, LI,li)
      cutil.setAddEditMode(var,LI,["resroomname"])

  if action == "roomselected" and var["JUPDIALOG_BUTTON"] == "accept" :
    roomname = var["JUPDIALOG_RES"]
    if roomname != var["resroomname"] :
      var["resroomname"] = roomname
      cutil.setCopy(var,"resroomname",LI)
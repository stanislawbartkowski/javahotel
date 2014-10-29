import cutil

from util import rutil
from util import advarese

P="p_"

def Ad(var) :
  return advarese.createAdvaRese(var,P,True)

def dialogaction(action,var) :
  
  cutil.printVar("modif rese",action,var)
  
  if action == "before" :
    res = rutil.getReservForDay(var)
    cutil.setCopy(var,"resename")
    var["resename"] = res[0].getResId()
  
  if action == "modifrese" : var["JUP_DIALOG"] = "?reserveroom.xml"
  
  if action == "aftercheckin" : rutil.afterCheckIn(var) 
  
  if action == "disclosurechange" and var["disclosureopen"] and var["disclosureid"] == "adddvancepayment" :
    A = Ad(var)
    A.setValReseName(rutil.getReseName(var))
    
  if action == "signalchange" and var["changefield"] == "p_advance_percent" and var["changeafterfocus"] :
    A = Ad(var)
    A.calculateAdvanceAmount()
    
  if action == "saveadva" :
    A = Ad(var)
    if not A.validate() : return
    var["JYESNO_MESSAGE"] = "@saveadva"
    var["JAFTERDIALOG_ACTION"] = "saveadvaafterquestion"
    
  if action == "saveadvaafterquestion" and var["JYESANSWER"] :
    A = Ad(var)
    A.modifyAdvaData()
    rutil.refreshPanel(var)
    

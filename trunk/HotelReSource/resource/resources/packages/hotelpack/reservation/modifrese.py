import cutil
from util import rutil,util,diallaunch
from rrutil import advarese,rparam,confirm

P="p_"

def Ad(var) :
  return advarese.createAdvaRese(var,P,True)

def dialogaction(action,var) :
  
  cutil.printVar("modif rese",action,var)
  
  if action == "before" :
    (rename,resroom,resday) = rparam.XMLtoroomday(var["JUPDIALOG_START"])
    cutil.setCopy(var,"resename")
    rutil.setReseName(var,rename)
    rparam.setStartParam(var)
  
  if action == "modifrese" : 
    xml = rparam.getStartParam(var)
    (resname,resroom,resday) = rparam.XMLtoroomday(xml)
    diallaunch.modifreservationroom(var,resroom,resday)
  
  if action == "aftercheckin" : rutil.afterCheckIn(var) 
  
  if action == "disclosurechange" and var["disclosureopen"] and var["disclosureid"] == "adddvancepayment" :
    A = Ad(var)
    A.setValReseName(rutil.getReseName(var))

  if action == "disclosurechange" and var["disclosureopen"] and var["disclosureid"] == "confirmnotconfirmed" :
     confirm.createC(var,P).setResNameToVar(rutil.getReseName(var))
    
  if action == "signalchange" and var["changefield"] == "p_advance_percent" and var["changeafterfocus"] :
    A = Ad(var)
    A.calculateAdvanceAmount()
    
  if action == "saveconfirm" and var["JYESANSWER"] : 
    confirm.createC(var,P).changeReservation(rutil.getReseName(var))
    rutil.refreshPanel(var)
        
  if action == "saveadva" :
    A = Ad(var)
    if not A.validate() : return
    var["JYESNO_MESSAGE"] = "@saveadva"
    var["JAFTERDIALOG_ACTION"] = "saveadvaafterquestion"
    
  if action == "saveadvaafterquestion" and var["JYESANSWER"] :
    A = Ad(var)
    A.modifyAdvaData()
    rutil.refreshPanel(var)
    
  if action == "sendmail" :
    rese = rutil.getReseName(var)
    diallaunch.confirmationmail(var,rese)
    
  if action == "cancelres" and var["JYESANSWER"] :
     util.RESOP(var).changeStatusToCancel(rutil.getReseName(var))
     var["JCLOSE_DIALOG"] = True
     rutil.refreshPanel(var)
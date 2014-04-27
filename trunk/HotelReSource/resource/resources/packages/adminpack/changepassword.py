import sec
import cutil
from util import util

def changeaction(action,var):

  cutil.printVar("changeaction",action,var) 
  
  if action == "changepassword" :
      if not var['JYESANSWER'] : return
      newpassword = var["newpassword"]
      user = var["name"]
      sec.ObjectAdmin(util.getAppId(var)).changePasswordForPerson(user,newpassword)
      var["JCLOSE_DIALOG"] = True
      
      
  
from com.gwthotel.hotel.server.service import H
from util.util import MESS
from util.util import printvar

adminI = H.getHotelAdmin()
M = MESS()

def changeaction(action,var):

  printvar("changeaction",action,var) 
  
  if action == "changepassword" :
      if not var['JYESANSWER'] : return
      newpassword = var["newpassword"]
      user = var["name"]
      adminI.changePasswordForPerson(user,newpassword)
      var["JCLOSE_DIALOG"] = True
      
      
  
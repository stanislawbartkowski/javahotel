from com.gwthotel.hotel.reservation import ResStatus
import con,cutil
from util import util

class CONFIRM :
  
  def __init__(self,var,confid) :
    self.var = var
    self.__confid = confid
    
  def __setCopy(self) :
    cutil.setCopy(self.var,self.__confid)
    
  def setDefaToVar(self) :
    self.var[self.__confid] = util.HOTELDEFADATA().getDataHB(4)
    self.__setCopy()
    
  def setReservationToVar(self,reservation) :
    state = reservation.getStatus()
    conf = (state != ResStatus.SCHEDULED)
    self.var[self.__confid] = conf
    self.__setCopy()
    
  def setResNameToVar(self,resename) :
    reservation = util.RESFORM(self.var).findElem(resename)
    self.setReservationToVar(reservation)
  
  def changeReservation(self,resename) :
    if self.var[self.__confid] : util.RESOP(self.var).changeStatusToReserv(resename)
    else : util.RESOP(self.var).changeStatusToNotConfirmed(resename)

     
def createC(var,pre=None) :
  return CONFIRM(var,con.toP("reseconfirmed",pre))
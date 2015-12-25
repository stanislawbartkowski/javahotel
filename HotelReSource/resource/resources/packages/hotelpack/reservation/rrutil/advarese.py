import cutil,con

from util import util,rutil

class CALCULATE :
  
  def __init__(self) :
    self.sum = util.SUMBDECIMAL()
  
  def calc(self,reservation) :
    for r in reservation.getResDetail() : self.sum.add(r.getPriceTotal())
    
  def getTotal(self) :
    return self.sum.sum
 
class ADVA :
  
  def __init__(self,var,advatotal,advaperce,advapay,advadate,resepay=None,resedate=None) :
    self.var = var
    self.__advatotal = advatotal
    self.__advaperce = advaperce
    self.__advapay = advapay
    self.__advadate = advadate
    self.__resepay = resepay
    self.__resedate = resedate
    
  def validate(self) :
    ok = cutil.validatePercent(self.var,self.__advaperce)
    if self.var[self.__advatotal] and self.var[self.__advapay] :
      if self.var[self.__advatotal] < self.var[self.__advapay] :
	cutil.setErrorField(self.var,self.__advapay,"@advancepaycannotbegraterthentotal")
	ok = False
    return ok	
	    
  def setCopy(self) :
    cutil.setCopy(self.var,[self.__advatotal,self.__advaperce,self.__advapay,self.__advadate])
    if self.__resepay : cutil.setCopy(self.var,self.__resepay)
    if self.__resedate : cutil.setCopy(self.var,self.__resedate)
    
  def setVal(self,reservation,sum) :
    self.var[self.__advadate] = reservation.getTermOfAdvanceDeposit()
    self.var[self.__advaperce] = util.HOTELDEFADATA().getDataHI(40)
    self.var[self.__advapay] = reservation.getAdvanceDeposit()
    self.var[self.__advatotal] = sum
    if self.__resepay : 
      self.var[self.__resepay] = reservation.getAdvancePayment()
    if self.__resedate :
      self.var[self.__resedate] = reservation.getDateofadvancePayment()
    
  def __setValReservation(self,reservation) :
    SUM = CALCULATE()
    SUM.calc(reservation)
    self.setVal(reservation,SUM.getTotal())
    self.setCopy()
    
  def setValReseName(self,resename) :
    self.__setValReservation(util.RESFORM(self.var).findElem(resename))
    
  def calculateAdvanceAmount(self,total=None) :
    if total == None : total = self.var[self.__advatotal]
    advance_total = total
    advance_payment = None
    advance_percent = self.var[self.__advaperce]
    if cutil.validatePercent(self.var,self.__advaperce) :
      if advance_total != None and advance_percent != None :
        advance_payment = con.calculatePercent(advance_total,advance_percent)
    cutil.setCopy(self.var,[self.__advatotal,self.__advapay])
    self.var[self.__advapay] = advance_payment
    self.var[self.__advatotal] = advance_total
    
  def setAdvaData(self,reservation) :
      reservation.setAdvanceDeposit(con.toB(self.var[self.__advapay]))
      reservation.setTermOfAdvanceDeposit(con.toDate(self.var[self.__advadate]))
      if self.__resepay : reservation.setAdvancePayment(con.toB(self.var[self.__resepay]))
      if self.__resedate :
        pdate = self.var[self.__resedate]
        if pdate == None : pdate = con.today()      
        reservation.setDateofadvancePayment(con.toDate(pdate))
    
  def modifyAdvaData(self,resename = None) :
    if resename == None : resename = rutil.getReseName(self.var)
    R  = util.RESFORM(self.var)
    reservation = R.findElem(resename)
    self.setAdvaData(reservation)
    R.changeElem(reservation)    
    cutil.JOURNAL(self.var).addJournalElem(util.JOURNAL_CHANGEADVANCEPAYMENT,None,resename)
    
def createAdvaRese(var,pre=None,addP=False) :
    p1 = None
    p2 = None
    if addP :
      p1 = "advance_forpayment"
      p2 = "advance_fordate"
    return ADVA(var,con.toP("advance_total",pre),con.toP("advance_percent",pre),con.toP("advance_payment",pre),con.toP("advance_duedate",pre),con.toP(p1,pre),con.toP(p2,pre))
    
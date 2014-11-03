from util import rutil
import con
from util import util

RESTYPE = ["reservationscheduled","reservationconfirmed","reservationadvancepaid","reservationadvanceexpired"]
RESTYPE = RESTYPE + ["customernotarrived","vacantnotpaid","vacantpaid","vacantexcesspaid"]
RESTYPE = RESTYPE + ["occupiednotpaid","occupiedadvancepaid","occupiedpaid","occupiedexcesspaid","statuscanceled"]

COLORS = ["#FFFF33","#FF99CC","#FFCC99","#FFCC99"]
COLORS = COLORS + ["#FF0000","#663333","#B0B0B0","#3399CC"]
COLORS = COLORS + ["#66FF66","#66CC00","#66FF00","#66CC33"]

def getStatusS(sta) :
  return RESTYPE[sta]


class RESSTATUS :
  
  def __init__(self) :
    self.arrival = None
    self.departure = None
    self.sumcost = 0.0
    self.sumpay = 0.0
    self.advancepayment = None
    self.advancepaymentused = None
    self.advancepaymentleft = None
  

def getResStatus(var,r,room=None,today=None) :
    (sta,S) = __getResStatusR(var,r,room,today)
    return sta  

def getResStatusR(var,r):
    (state,R) =  __getResStatusR(var,r,None,con.maxdate())
    return R

def __getResStatusR(var,r,room,today) :
    """ Calculates and returns the status of the reservation
    Args: 
       var
       r ReservationForm
       room room symbol
       today 
    Returns:
       0 reservation scheduled
       1 reservation confirmed
       2 reservation advance paid 
       3 reservation advanced expired
       
       4 customer not arrived
       5 vacant not paid
       6 vacant paid
       7 vacant excess payment
       
       8 occupied, not paid
       9 occupied, advance paid
       10 occupied paid
       11 occupied excess paid
       
       12 canceled              
    """
    S = RESSTATUS()
    if today == None : today = con.today()
#    print today
    sym = r.getName()
    re = util.resStatus(r)
    if re == 0 : return (12,S)
    li = rutil.getPayments(var,sym)
    for l in li :
        if room != None and room != l.getRoomName() : continue
        (S.arrival,S.departure) = rutil.calculateDates(S.arrival,S.departure,l)
        
        
    if today < S.arrival and re != 1 :
        advance = r.getAdvanceDeposit()
        resstatusset = False
        if advance == None : resstatusset = True
        else :
          term = con.toJDate(r.getTermOfAdvanceDeposit())
          if term != None :
             if today < term : resstatusset = True
             
        if resstatusset :
          if re == 2 : return (1,S)
          if re == 3 : return (0,S)
            # internal error, not expected
            # TO DO
          return (0,S)
                       
        if r.getAdvancePayment() == None : return (3,S)
        if r.getAdvancePayment() < r.getAdvanceDeposit() : return (3,S)
        return (2,S)     
    
    if re != 1 : return (4,S)
    sta = None
    # 0 - not paid
    # 1 - paid
    # 2 - excess paid
    # 3 - advance paid
    for l in li :
        add = False
        re = con.toJDate(l.getResDate())
        if util.isRoomService(l.getServiceType()) and re < today : add = True
        if not util.isRoomService(l.getServiceType()) and re <= today : add = True
#        print sym,add,l.getPriceTotal()
        if add : S.sumcost = con.addDecimal(S.sumcost,con.BigDecimalToDecimal(l.getPriceTotal()))
        
    S.advancepayment = con.BigDecimalToDecimal(r.getAdvancePayment())
    if  S.advancepayment != None and S.advancepayment >= S.sumcost : sta = 3
    else :
        bli = util.RESOP(var).findBillsForReservation(sym)
        for b in bli :
            (suma,advanced) = rutil.countPaymentsA(var,b.getName())
            S.sumpay = con.addDecimal(S.sumpay,suma)
            S.advancepaymentused = con.addDecimal(S.advancepaymentused,advanced)
        S.sumpay = con.addDecimal(S.sumpay,S.advancepayment)
#        print sym,S.sumcost,S.sumpay
        if S.sumcost == S.sumpay and S.sumcost > 0.0 : sta = 1
        elif S.sumcost > S.sumpay : sta = 0
        elif S.sumcost == 0 and S.sumpay == 0 : sta = 0
        else : sta = 2
    
    S.advancepaymentleft = con.minusDecimal(S.advancepayment,S.advancepaymentused)
    if today <= S.departure :
      if sta == 0 : return (8,S)
      if sta == 3 : return (9,S)
      if sta == 2 : return (11,S)
      return (10,S)
  
    if sta == 0 : return (5,S)
    if sta == 3 : return (6,S)
    if sta == 2 : return (7,S)
    return (6,S)         
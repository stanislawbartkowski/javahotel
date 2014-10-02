import rutil
import con
import util
from __builtin__ import None

RESTYPE = ["reservationscheduled","reservationconfirmed","reservationadvancepaid","reservationadvanceexpired"]
RESTYPE = RESTYPE + ["customernotarrived","vacantnotpaid","vacantpaid","vacantexcesspaid"]
RESTYPE = RESTYPE + ["occupiednotpaid","occupiedadvancepaid","occupiedpaid","occupiedexcesspaid"]

COLORS = ["#FFFF33","#FF99CC","#FFCC99","#FFCC99"]
COLORS = COLORS + ["#FF0000","#663333","#B0B0B0","#3399CC"]
COLORS = COLORS + ["#66FF66","#66CC00","#66FF00","#66CC33"]


def getResStatus(var,r,room = None,today = None) :
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
    if today == None : today = con.today()
    sym = r.getName()
    re = util.resStatus(r)
    if re == 0 : return 12
    li = rutil.getPayments(var,sym)
    arrival = None
    departure = None
    for l in li :
        if room != None and room != l.getRoomName() : continue
        (arrival,departure) = rutil.calculateDates(arrival,departure,l)
        
    if today < arrival :
        advance = r.getAdvanceDeposit()
        resstatusset = False
        if advance == None : resstatusset = True
        else :
          term = con.toJDate(r.getTermOfAdvanceDeposit())
          if term != None :
             if today < term : resstatusset = True
             
        if resstatusset :
          if re == 2 : return 1
          if re == 3 : return 0
            # internal error, not expected
            # TO DO
          return 0
                       
        if r.getAdvancePayment() == None : return 3
        if r.getAdvancePayment() < r.getAdvanceDeposit() : return 3
        return 2     
    
    if re != 1 : return 4
    sta = None
    # 0 - not paid
    # 1 - paid
    # 2 - excess paid
    # 3 - advance paid
    sumcost = 0.0
    for l in li :
        add = False
        re = con.toJDate(l.getResDate())
        if util.isRoomService(l.getServiceType()) and re < today : add = True
        if not util.isRoomService(l.getServiceType()) and re <= today : add = True
        if add : sumcost = con.addDecimal(sumcost,con.BigDecimalToDecimal(l.getPriceTotal()))
        
    advancepayment = con.BigDecimalToDecimal(r.getAdvancePayment())
    if  advancepayment != None and advancepayment >= sumcost : sta = 3
    else :
        wasadvanced = False
        sumpay = 0.0
        bli = util.RESOP(var).findBillsForReservation(sym)
        for b in bli :
            (suma,isadvance) = rutil.countPaymentsA(var,b.getName())
            if isadvance : wasadvanced = True
            sumpay = con.addDecimal(sumpay,suma)
        if not wasadvanced : sumpay = con.addDecimal(sumpay,advancepayment)
        print sumcost,sumpay
        if sumcost == sumpay and sumcost > 0.0 : sta = 1
        elif sumcost > sumpay : sta = 0
        elif sumcost == 0 and sumpay == 0 : sta = 0
        else : sta = 2
    
    if today <= departure :
      if sta == 0 : return 8
      if sta == 3 : return 9
      if sta == 2 : return 11
      return 10
  
    if sta == 0 : return 5
    if sta == 3 : return 6
    if sta == 2 : return 7
    return 6           
        
     
         
             
 
from cutil import printVar
from util.util import RESFORM
from util.util import ROOMLIST
from util.util import SERVICES
from util.util import RESOP
from cutil import setJMapList
from util.util import isRoomService
from util.util import getReseName
from cutil import setStandEditMode
from cutil import addDecimal
from cutil import minusDecimal
from cutil import setFooter
from cutil import BigDecimalToDecimal
from sets import Set
from util.util import BILL
from util.util import newBill
from util.util import listNumberToCVS
from util.util import CVSToListNumber

LIST="poslist"
NOPAID="billlist"
PAYBILL="paybill"

class PAID :
  
  def __init__(self,var) :
    rese = getReseName(var)
    bli = RESOP(var).findBillsForReservation(rese)
    self.se = Set()
    for b in bli :
      for l in b.getPayList() :
        self.se.add(l)
 
  def onList(self,id) :
    return id in self.se

class BILL :
  
  def __init__(self,var,liname) :
    self.sumf = 0.0
    self.var = var
    self.liname = liname
    self.li = []
    
  def addMa(self,ma,r,idp) :
    se = r.getServiceType()
    resdate = None
    servdate= None
    if isRoomService(se) : resdate = r.getResDate()
    else : servdate = r.getResDate()
    total = r.getPriceTotal()
    self.sumf = addDecimal(self.sumf,BigDecimalToDecimal(total))
    guest = r.getGuestName()
    room = r.getRoomName()
    service = r.getService()
    ma1 = { "idp" : idp, "room" : room, "resday" : resdate, "service" : service, "servday":servdate, "servdescr" : r.getDescription(),"guest" : guest, "total" : total }
    ma.update(ma1)
    self.li.append(ma)
    
  def close(self) :
    setJMapList(self.var,self.liname,self.li)
    setFooter(self.var,self.liname,"total",self.sumf)
    
def _getPayments(var) :    
  rese = getReseName(var)
  pli = RESOP(var).getResAddPaymentList(rese)
  R = RESFORM(var)
  # java.util.List
  pli.addAll(R.findElem(rese).getResDetail())
  return pli

def _createPosList(var) :
#  rese = getReseName(var)
#  li = []
#  pli = RESOP(var).getResAddPaymentList(rese)
#  R = RESFORM(var)
  # java.util.List
#  pli.addAll(R.findElem(rese).getResDetail())
  pli = _getPayments(var)
  P = PAID(var)
  L1 = BILL(var,LIST)
  L2 = BILL(var,NOPAID)
  # list of bills
  sumf = 0.0
  for r in pli :
    idp = r.getId()
    if P.onList(idp) :
      ma = { "billed" : True }
    else :
      ma = { "billed" : False }
      L2.addMa( { "add" : True },r,idp)
      
    L1.addMa(ma,r,idp)  

  L1.close()
  L2.close()
  
  setStandEditMode(var,NOPAID,["add"])

def doaction(action,var) :
  printVar("create bill",action,var)
  
  if action == "before" :
    _createPosList(var)
    
  if action == "columnchangeaction" :
     total = var["total"]
     footerf = var["JFOOTER_billlist_total"]
     if var["add"] : footerf = addDecimal(footerf,total)
     else : footerf = minusDecimal(footerf,total)
     setFooter(var,"billlist","total",footerf)
     
  if action == "accept" :
    li = var["JLIST_MAP"]["billlist"]
    lNumb = None
    for l in li :
      if l["add"] : 
        if lNumb == None : lNumb = []
        lNumb.append(l["idp"])
    if lNumb == None :
      var["JERROR_MESSAGE"] = "Nothing is checked"
      return
    var["JUPDIALOG_START"] = listNumberToCVS(lNumb)
    var["JUP_DIALOG"] = "hotel/reservation/billpayer.xml"
    var['JAFTERDIALOG_ACTION'] = "finishbillpayer"
    
   

def payerbill(action,var) :
  printVar("payer bill",action,var)
  
  if action == "before" :
    cvs = var["JUPDIALOG_START"]
    li = CVSToListNumber(cvs)
    lSe = Set(li)
    pli = _getPayments(var)
    L = BILL(var,PAYBILL)
    for r in pli :
      idp = r.getId()
      if idp in lSe :
         L.addMa( { },r,idp)
    L.close()
        
      


    
     
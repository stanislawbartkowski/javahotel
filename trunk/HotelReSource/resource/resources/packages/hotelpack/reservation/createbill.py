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
from cutil import setFooter
from cutil import BigDecimalToDecimal

LIST="poslist"

def _createPosList(var) :
  rese = getReseName(var)
  li = []
  pli = RESOP(var).getResAddPaymentList(rese)
  R = RESFORM(var)
  # java.util.List
  pli.addAll(R.findElem(rese).getResDetail())
  sumf = 0.0
  for r in pli :
    idp = r.getId()
    se = r.getServiceType()
    resdate = None
    servdate= None
    if isRoomService(se) : resdate = r.getResDate()
    else : servdate = r.getResDate()
    total = r.getPriceTotal()
    sumf = addDecimal(sumf,BigDecimalToDecimal(total))
    guest = r.getGuestName()
    room = r.getRoomName()
    service = r.getService()
    ma = { "add" : True, "idp" : idp, "room" : room, "resday" : resdate, "service" : service, "servday":servdate, "servdescr" : r.getDescription(),"guest" : guest, "total" : total }
    li.append(ma)
  setJMapList(var,LIST,li)
  setStandEditMode(var,LIST,["add"])
  setFooter(var,LIST,"total",sumf)

def doaction(action,var) :
  printVar("create bill",action,var)
  
  if action == "before" :
    _createPosList(var)
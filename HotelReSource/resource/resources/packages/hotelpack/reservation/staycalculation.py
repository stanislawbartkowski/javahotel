import cutil,con

from rrutil import resstat

from util import util,rutil

LISTID="calcid"
M=util.MESS()
COSTS="costs"
FUTURECOSTS="futurecosts"

def calcBalance(debet,credit) :
  if debet == None : return (None,credit)
  if credit == None : return (debet,None)
  if credit == debet : return (None,None)
  if debet > credit : return (con.minusDecimal(debet,credit), None)
  return (None, con.minusDecimal(credit,debet))

def _toS(id,ROZL) :
  return M(id).format(con.toS(ROZL.forday))

def _addElem(seq,name,debet,credit) :
  seq.append({ "name" : name, "debet" : debet, "credit" : credit})

def dialogaction(action,var) :
  
  cutil.printVar("stay calculation",action,var)

  if action == "before" :
    resename = var["JUPDIALOG_START"]
    var["resename"] = resename
    var["balanceat"] = con.today()
    cutil.setCopy(var,["resename","balanceat","status"])
    R = util.RESFORM(var).findElem(resename)
    assert R != None
    (sta,ROZL) = resstat.getResStatusRese(var,R)
    var["status"] = M(resstat.getStatusS(sta))
    var["balanceat"] = ROZL.forday
    seq = []

    st = _toS("servicesatday",ROZL)
    _addElem(seq,st,ROZL.sumcost,None)
    _addElem(seq,M("paymentsum"),None,ROZL.sumpay)
    if ROZL.advancepayment != None :
      _addElem(seq,M("advanceincluded"),None,ROZL.advancepayment)
    st = _toS("balanceatf",ROZL)
    _addElem(seq,st,*calcBalance(ROZL.sumcost,ROZL.sumpay))
    if ROZL.sumcostafter != None:
      _addElem(seq,_toS("servicesafter",ROZL),ROZL.sumcostafter,None)
    
    cutil.setJMapList(var,LISTID,seq)
    
    L1 = rutil.BILLPOSADD(var,COSTS)
    for r in ROZL.costlist :     
       idp = r.getId()
       L1.addMa( { },r,idp)
    L1.close()   
    
    L2 = rutil.BILLPOSADD(var,FUTURECOSTS)
    for r in ROZL.costlistafter :     
       idp = r.getId()
       L2.addMa( { },r,idp)
    L2.close()   
      


    
    

import cutil,con,vat

from rrutil import resstat,cbill

from util import util,rutil,dutil,cust

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
  
  if action == "guestdetail" :
    cust.showCustomerDetails(var,var["guest_name"])   

  if action == "before" :
    resename = var["JUPDIALOG_START"]
    var["resename"] = resename
    cutil.setCopy(var,["resename","balanceat","status"])
    R = util.RESFORM(var).findElem(resename)
    assert R != None
    (sta,ROZL) = resstat.getResStatusRese(var,R)
    var["status"] = M(resstat.getStatusS(sta))
    var["balanceat"] = ROZL.forday
    
    seq = []
    st = _toS("servicesatday",ROZL)
    _addElem(seq,st,ROZL.sumcost,None)
    if ROZL.sumcostafter != None: after = ROZL.sumcostafter
    else : after = 0
    _addElem(seq,_toS("servicesafter",ROZL),after,None)
        
    blist = util.RESOP(var).findBillsForReservation(resename)
    V = vat.CalcVat()
    for b in blist :
      bname = b.getName()      
      CC = cbill.BILLCALC(var,b)
      CC.calc()
      s = M("billinfo").format(bname,con.toS(CC.getIssueDate()))
      _addElem(seq,s,CC.getCharge(),None)
      for l in CC.getLines() :
	V.addVatLineC(dutil.getGrossValueLine(l),dutil.getNettoValueLine(l),dutil.getTaxValueLine(l),dutil.getVatLine(l))
    vl = V.calculateVat()
    for v in vl :
      (netto,vatt,gross,level,vats) = v
      s = M("vatvalueinfo").format(vats)
      _addElem(seq,s,vatt,None)      
      
    _addElem(seq,M("paymentsum"),None,ROZL.sumpay)
    if ROZL.advancepayment != None :
      _addElem(seq,M("advanceincluded"),None,ROZL.advancepayment)
      
    st = _toS("balanceatf",ROZL)
    _addElem(seq,st,*calcBalance(ROZL.sumcost,ROZL.sumpay))
 
    cutil.setJMapList(var,LISTID,seq)
    
    L1 = cbill.BILLPOSADD(var,COSTS)
    for r in ROZL.costlist :     
       idp = r.getId()
       L1.addMa( { },r,idp)
    L1.close()   
    
    L2 = cbill.BILLPOSADD(var,FUTURECOSTS)
    for r in ROZL.costlistafter :     
       idp = r.getId()
       L2.addMa( { },r,idp)
    L2.close()
        
      


    
    

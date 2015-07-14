import cutil,con,xmlutil

from util import util,rutil,hmail,diallaunch,rpdf,cust

M = util.MESS()
CUST="i_"
CLIST="custlist"
GLIST="guestlist"
PLIST="payerlist"
MLIST="emaillist"

def _createReseLine(var,map,rname) :
    R = util.RESFORM(var)
    r = R.findElem(rname)
    assert r != None
    map["resename"] = r.getName()
    map["custinfo"] = rutil.rescustInfo(var,r)
    sta = util.resStatus(r)    
    if sta == 1 : n = M("statusstay")
    elif sta == 2: n = M("statusopen")
    else : n = M("statuscanceled")
    map["rstatus"] = n
    (dfrom,dto,room,rate,nofnights) = rutil.getReseDate(var,r)
    
    map["rfrom"] = dfrom
    map["rto"] = con.incDays(dto)
    map["room"] = room

def _createListOfMail(var) :
   H = hmail.HotelMail(var)
   li = H.getListForCustomer(var["i_name"])
   seq = hmail.createMailSeq(var,li)
   cutil.setJMapList(var,MLIST,seq)
   return seq

def _createListOfRes(var,li,lname) :
  seq = []
  for e in li :
    map = {}
    _createReseLine(var,map,e)
    seq.append(map)
  cutil.setJMapList(var,lname,seq)      

def dialinfo(action,var) :
  cutil.printVar("customer stat",action,var)
  
  if action == "before" :
      custid = var["JUPDIALOG_START"]
      custR = util.CUSTOMERLIST(var).findElem(custid)
      cust.customerToVar(var,custR,CUST)
      cust.setCustVarCopy(var,CUST)
      l = util.RESOP(var).getReseForCustomer(custid)
      l1 = util.RESOP(var).getReseForGuest(custid)
      l2 = util.RESOP(var).getReseForPayer(custid)      
      _createListOfRes(var,l,CLIST)
      _createListOfRes(var,l1,GLIST)
      _createListOfRes(var,l2,PLIST)
      l3 = _createListOfMail(var)

      m = {"nofcustomer" : len(l), "nofguest" : len(l1), "nofpayer" : len(l2), "nomails" : len(l3) }
      s = util.transformXMLFromMap("custinfo.xslt",m)
      var["i_custinfo"] = s      
#      var["i_custinfo"] = M("custinfo").format(len(l),len(l1),len(l2))
      cutil.setCopy(var,"i_custinfo")

      
  if action == "shownote" :
     diallaunch.showmailnote(var,var["mailname"])      
    
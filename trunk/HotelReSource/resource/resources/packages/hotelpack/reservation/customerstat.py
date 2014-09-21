import cutil
import con
from util import util
from util import rutil
from util import hmail
from util import diallaunch
from util import rpdf

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
    (dfrom,dto,room,rate) = rpdf.getReseDate(var,r)
    
    map["rfrom"] = dfrom
    map["rto"] = con.incDays(dto)
    map["room"] = room

def _createListOfMail(var) :
   H = hmail.HotelMail(var)
   li = H.getListForCustomer(var["i_name"])
#   seq = []
#   for l in li :
#     mm = H.getCMail(l.getName())
#     res = mm.getSendResult()
#     if cutil.emptyS(res) : res = None
#     seq.append({ "mailname" : l.getName(), "resename" : l.getReseName(),"datesend" : mm.getCreationDate(), "subject" : mm.getDescription(), "res" : res })
   seq = hmail.createMailSeq(var,li)
   cutil.setJMapList(var,MLIST,seq)  

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
      util.customerToVar(var,custR,CUST)
      util.setCustVarCopy(var,CUST)
      l = util.RESOP(var).getReseForCustomer(custid)
      l1 = util.RESOP(var).getReseForGuest(custid)
      l2 = util.RESOP(var).getReseForPayer(custid)
      var["i_custinfo"] = M("custinfo").format(len(l),len(l1),len(l2))
      cutil.setCopy(var,"i_custinfo")
      _createListOfRes(var,l,CLIST)
      _createListOfRes(var,l1,GLIST)
      _createListOfRes(var,l2,PLIST)
      _createListOfMail(var)
      
  if action == "shownote" :
     diallaunch.showmailnote(var,var["mailname"])      



    
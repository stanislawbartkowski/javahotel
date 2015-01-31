import cutil

from util import util,rutil,diallaunch,cust

from rrutil import resstat

LIST="list"

CULIST=["name","email","surname","firstname"]
PREC="cust_"

M = util.MESS()


def dialogaction(action,var) :
  
  cutil.printVar("reservation",action,var)
  
  if action == "before" :
    R = util.RESFORM(var)
    C = util.CUSTOMERLIST(var)
    seq = R.getList()
    list = []
    for s in seq :
       sta = resstat.getResStatus(var,s)
       statuS = resstat.getStatusS(sta)       
       (arrival,departure,roomname,rate,nog) = rutil.getReseDate(var,s)
       ma = {"name" : s.getName(),"resdate" : s.getCreationDate(), "checkin" : arrival, "checkout" : departure, "roomname" : roomname, "resestatus" : M(statuS) }
       cu = C.findElem(s.getCustomerName())
       cust.toCustomerVar(ma,cu,PREC,CULIST)
       list.append(ma)
       
    cutil.setJMapList(var,LIST,list)   
    
  if action == "showrese" :
    resename = var["name"]
#    (room,day) = rutil.getRoomDateFromVar(var)
    diallaunch.reservationdialogaction(var,resename,var["roomname"],var["checkin"])

    
  if action == "showmails" :
    rename = var["name"]
    diallaunch.showlistofmail(var,rename)

import cutil
from util import util
from util import rutil
#from util import rpdf
from util import diallaunch

LIST="list"

CULIST=["name","email","surname","firstname"]
PREC="cust_"

def dialogaction(action,var) :
  
  cutil.printVar("reservation",action,var)
  
  if action == "before" :
    R = util.RESFORM(var)
    C = util.CUSTOMERLIST(var)
    seq = R.getList()
    list = []
    for s in seq :
       (arrival,departure,roomname,rate) = rutil.getReseDate(var,s)
       ma = {"name" : s.getName(),"resdate" : s.getCreationDate(), "checkin" : arrival, "checkout" : departure, "roomname" : roomname }
       cu = C.findElem(s.getCustomerName())
       util.toCustomerVar(ma,cu,PREC,CULIST)
       list.append(ma)
       
    cutil.setJMapList(var,LIST,list)   
    
  if action == "showmails" :
    rename = var["name"]
    diallaunch.showlistofmail(var,rename)

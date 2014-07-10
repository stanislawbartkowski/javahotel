from java.util import Calendar

import datetime

import cutil
import con

from util import util
from util import rutil

PIE="chart"
BAR="chart1"
LINE="chart2"
 
class GETSTAT :
  
  def __init__(self,var,roomname) :
    self.var = var
    self.roomname = roomname    
    self.seq = []
    
  def readQuery(self) :
     var = self.var
     R = util.RESOP(var)
     RFORM = util.RESFORM(var)
     name = var["name"]
     year = var["year"]
     (self.dstart,self.dend) = con.getPeriod(year,1,year,12)
     query=cutil.createArrayList()
     q = rutil.createResQueryElem(self.roomname,self.dstart,self.dend)
     query.add(q)
     res = R.queryReservation(query)
     resid = None
     rese = None
     for r in res :
       presid = r.getResId()
       if presid != resid :
         rese = RFORM.findElem(presid)
         resid = presid
       self.seq.append((rese,r))
        
def _getPeriodStat(var,name) :
   G = GETSTAT(var,name)
   G.readQuery()
   nofdays = con.nofDays(G.dstart,G.dend) + 1
   staydays = 0
   resdays = 0   
   for r in G.seq :
     status = util.resStatus(r[0])
     if status == 1 : staydays = staydays + 1
     if status == 2 : resdays = resdays + 1
   return (nofdays - (staydays + resdays),staydays,resdays)

def _setPieChart(var,name) :
   (freedays,staydays,resdays) = _getPeriodStat(var,name)
   seq = []
   seq.append({"name" : rutil.M("freeroom"), "val" : freedays})
   seq.append({"name" : rutil.M("stayroom"), "val" : staydays})
   seq.append({"name" : rutil.M("reseroom"), "val" : resdays})
   cutil.setJChartList(var,PIE,seq)
   
def _getMonthStat(var,name) :
   G = GETSTAT(var,name)
   G.readQuery()
   per = {}
   y = None
   m = None
   dstart = con.incDays(G.dstart,-1)
   while (dstart != G.dend) :
     dstart = con.incDays(dstart)
     if y != None and y == dstart.year and m == dstart.month : continue
     y = dstart.year
     m = dstart.month
     dpocz = datetime.date(y,m,1)
#     print y,m,lastDay(y,m)
     dkon = datetime.date(y,m,con.lastDay(y,m))
     per[(y,m)] = (con.nofDays(dpocz,dkon)+1,0,0)
     
   for r in G.seq :
     status = util.resStatus(r[0])
     date = con.toJDateTime(r[1].getResDate())
     key = (date.year,date.month)
     (nofdays,staydays,resdays) = per[key]
     if status == 1 : staydays = staydays + 1
     if status == 2 : resdays = resdays + 1
     per[key] = (nofdays,staydays,resdays)
        
   return per
  
def _setBarStat(var,name) :
  per = _getMonthStat(var,name)
  seq = []
  li = []
  for k in per.keys() :
    li.append(k)
  li.sort()  
  for k in li :
     (nofdays,staydays,resdays) = per[k]
     month = str(k[0]) + "/" + str(k[1])
     seq.append({ "name" : month,"free" : nofdays - (staydays + resdays), "stay" : staydays, "rese" : resdays})
  cutil.setJChartList(var,BAR,seq)
  seq1 = []
  totalfreedays = 0
  totalstaydays = 0
  totalresdays = 0
  for s in seq :
    totalfreedays = totalfreedays + s["free"]
    totalstaydays = totalstaydays + s["stay"]
    totalresdays = totalresdays + s["rese"]
    seq1.append({ "name" : s["name"],"free" : totalfreedays, "stay" : totalstaydays, "rese" : totalresdays})
    
  cutil.setJChartList(var,LINE,seq1)

def dialinfo(action,var) :
  cutil.printVar("roomstat",action,var)
  
  if action == "before" :
    name = var["JUPDIALOG_START"]
    rdescr = util.getRoomInfo(var,name)
    var["rdescr"] = rdescr
    var["year"] = con.todayYear()
    var["name"] = name
    cutil.setCopy(var,["rdescr","year","name"])
    _setPieChart(var,name)
    _setBarStat(var,name)
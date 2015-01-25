import datetime

import cutil,con

from util import rutil,util,diallaunch
from rrutil import resstat

def _getList(var):
    R = util.ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "noperson" : s.getNoPersons() } )                                        
    return list        


def _resInfo(var,resid) :
   res = util.RESFORM(var).findElem(resid)
   assert res != None
   return rutil.rescustInfo(var,res)
   
def reservationaction(action,var):
    cutil.printVar("reservation",action,var)
    
    if action == "before" :
      list = _getList(var)
      var["JDATELINE_MAP"] = {"reservation" : { "linedef" : list}}
      
    if action == "datelineaction" :
      (room,day) = rutil.getRoomDateFromVar(var)
      R = util.RESOP(var)
      RO = util.ROOMLIST(var)
      RFORM = util.RESFORM(var)
      services = RO.getRoomServices(room)
      if len(services) == 0 :
           var['JERROR_MESSAGE'] = "@noserviceassigned"
           var['JMESSAGE_TITLE'] = "@incompleteconfiguration"
           return
      li = util.getServicesForRoom(var,room)
      if li == None :
           var['JERROR_MESSAGE'] = "@nopricelistforthisservice"
           var['JMESSAGE_TITLE'] = "@incompleteconfiguration"
           return
       
      res = rutil.getReservForDay(var,room,day)

      if res.isEmpty() : 
	  diallaunch.newreservation(var,room,day,1,1)
      else : 
          ares = res.get(0)
          resid = ares.getResId()
          (room,day) = rutil.getRoomDateFromVar(var)
          diallaunch.reservationdialogaction(var,resid,room,day)
      
    if action == "datelinevalues" :
       seq = var["JDATELINE_QUERYLIST"]
       vals = []
       query=cutil.createArrayList()
       R = util.RESOP(var)
       RFORM = util.RESFORM(var)
       for s in seq :
           dfrom = s["JDATELINE_FROM"]
           dto = s["JDATELINE_TO"]
           name = s["name"]
           q = rutil.createResQueryElem(name,dfrom,dto)
           query.add(q)
        
       resList = R.queryReservation(query)
       vals = []
       
       dl = datetime.timedelta(1)
       for s in seq :
           dfrom = s["JDATELINE_FROM"]
           dto = s["JDATELINE_TO"]
           name = s["name"]
           dt = dfrom
           prevres = None
           prevmap = None
           while dt <= dto :
               resid = None
               for ans in resList :
                   aname = ans.getRoomName()
                   dres = ans.getResDate()
                   if con.eqDate(dt,dres) and aname == name : 
                       resid = ans.getResId()
                       break
                   
               if resid != None :
                   if resid == prevres : 
                      prevmap["colspan"] = prevmap["colspan"] + 1
                   else :   
                     if prevmap : vals.append(prevmap) 
                     rform = RFORM.findElem(resid)
                     sta = resstat.getResStatus(var,rform)
                     form = "resroom"
                     map = {"name" : name, "datecol" : dt,"colspan" : 1, "form" : form, "0" : _resInfo(var,resid), "1" : resstat.COLORS[sta], "hint" : "@" + resstat.getStatusS(sta) }
                     prevmap = map
                     prevres = resid
               dt = dt + dl
           if prevmap : vals.append(prevmap)
            
       var["JDATELINE_MAP"] = {"reservation" : { "values" : vals}}

from util.util import printvar
from util.util import ROOMLIST
from util.util import toDate
from util.util import RESOP
from util.util import createArrayList
from util.util import RESFORM
import datetime
from util.util import createResQueryElem
from util.util import getServicesForRoom
from util.util import eqDate
from hotelpack.reservation.resutil import getReservForDay
from util.util import resStatus

def __getList(var):
    R = ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "noperson" : s.getNoPersons()} )
    return list        
       

def reservationaction(action,var):
    printvar("reservation",action,var)
    
    if action == "before" :
      list = __getList(var)
      var["JDATELINE_MAP"] = {"reservation" : { "linedef" : list}}
      
    if action == "datelineaction" :
      R = RESOP(var)
      room = var["JDATELINE_LINE"]
      RO = ROOMLIST(var)
      RFORM = RESFORM(var)
      services = RO.getRoomServices(room)
      if len(services) == 0 :
           var['JERROR_MESSAGE'] = "No services assigned to this room"
           var['JMESSAGE_TITLE'] = "Incomplete hotel configuration"
           return
      li = getServicesForRoom(var,room)
      if li == None :
           var['JERROR_MESSAGE'] = "There are services assigned to this room but no price list for this services defined"
           var['JMESSAGE_TITLE'] = "Incomplete hotel configuration"
           return
       
      res = getReservForDay(var)

      if res.isEmpty() : var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
      else : 
          ares = res.get(0)
          resid = ares.getResId()
          rform = RFORM.findElem(resid)
          sta = resStatus(rform)
          if sta == 0 : var["JUP_DIALOG"] = "hotel/reservation/showreserveroom.xml"
          else: var["JUP_DIALOG"] = "hotel/reservation/showstay.xml"
      
    if action == "datelinevalues" :
       seq = var["JDATELINE_QUERYLIST"]
       vals = []
       query=createArrayList()
       R = RESOP(var)
       RFORM = RESFORM(var)
       for s in seq :
           dfrom = s["JDATELINE_FROM"]
           dto = s["JDATELINE_TO"]
           name = s["name"]
           q = createResQueryElem(name,dfrom,dto)
           query.add(q)
        
       resList = R.queryReservation(query)
       vals = []
       
       dl = datetime.timedelta(1)
       for s in seq :
           dfrom = s["JDATELINE_FROM"]
           dto = s["JDATELINE_TO"]
           name = s["name"]
           dt = dfrom
           while dt <= dto :
               resid = None
               for ans in resList :
                   aname = ans.getRoomName()
                   dres = ans.getResDate()
                   if eqDate(dt,dres) and aname == name : 
                       resid = ans.getResId()
                       break
                   
               if resid != None :
                   rform = RFORM.findElem(resid)
                   sta = resStatus(rform)
                   if sta == 1 : form = "stay"
                   else : form = "reserved"
                   map = {"name" : name, "datecol" : dt, "form" : form, "0" : resid}
                   vals.append(map)    
               dt = dt + dl
            
       var["JDATELINE_MAP"] = {"reservation" : { "values" : vals}}

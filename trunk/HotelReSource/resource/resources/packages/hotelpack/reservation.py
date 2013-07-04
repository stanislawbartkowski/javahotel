from util.util import printvar
from util.util import ROOMLIST
from util.util import toDate
from util.util import RESOP
from util.util import createArrayList
from com.gwthotel.hotel.reservationop import ResQuery
import datetime
from util.util import createResQueryElem
from util.util import getServicesForRoom

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
      day = var["JDATELINE_DATE"]
      query=createArrayList()
      q = createResQueryElem(room,day)
      query.add(q)
      res = R.queryReservation(query)
      if not res.isEmpty() : return
      RO = ROOMLIST(var)
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
          
      var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
      
      
    if action == "datelinevalues" :
       seq = var["JDATELINE_QUERYLIST"]
       vals = []
       query=createArrayList()
       R = RESOP(var)
       for s in seq :
           dl = datetime.timedelta(1)
           dfrom = toDate(s["JDATELINE_FROM"])
           dto = toDate(s["JDATELINE_TO"])
           name = s["name"]
           q = createResQueryElem(name,dfrom,dto)
           query.add(q)
        
       resList = R.queryReservation(query)
       vals = []
       for r in resList :
           print r
       var["JDATELINE_MAP"] = {"reservation" : { "values" : vals}}

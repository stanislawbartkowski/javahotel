import datetime

import cutil
import con

from util import rutil
from util import util

def getAttrS(r,attr) :
  if r.getAttr(attr) == None : return ""
  return r.getAttr(attr)

def __getList(var):
    R = util.ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "noperson" : s.getNoPersons() } )                                        
    return list        


def __resInfo(var,resid) :
   res = util.RESFORM(var).findElem(resid)
   assert res != None
   custName = res.getCustomerName()
   cust = util.CUSTOMERLIST(var).findElem(custName)
   assert cust != None
   dName = getAttrS(cust,"surname") + " " + getAttrS(cust,"firstname")
   if util.emptyS(dName) : dName = cust.getName()
#   return dName + " , " + cutil.getDicName("countries",getAttrS(cust,"country"))
   return dName + " , " + getAttrS(cust,"country")
   
def reservationaction(action,var):
    cutil.printVar("reservation",action,var)
    
    if action == "before" :
      list = __getList(var)
      var["JDATELINE_MAP"] = {"reservation" : { "linedef" : list}}
      
    if action == "datelineaction" :
      R = util.RESOP(var)
      room = var["JDATELINE_LINE"]
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
       
      res = rutil.getReservForDay(var)

      if res.isEmpty() : var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
      else : 
          ares = res.get(0)
          resid = ares.getResId()
          rform = RFORM.findElem(resid)
          sta = util.resStatus(rform)
#          if sta == 0 : var["JUP_DIALOG"] = "hotel/reservation/showreserveroom.xml"
          if sta == 0 : var["JUP_DIALOG"] = "hotel/reservation/reserveroom.xml"
          else: var["JUP_DIALOG"] = "hotel/reservation/showstay.xml"
      
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
                     sta = util.resStatus(rform)
                     if sta == 1 : form = "stay"
                     else : form = "reserved"
                     map = {"name" : name, "datecol" : dt,"colspan" : 1, "form" : form, "0" : __resInfo(var,resid)}
                     prevmap = map
                     prevres = resid
               dt = dt + dl
           if prevmap : vals.append(prevmap)
            
       var["JDATELINE_MAP"] = {"reservation" : { "values" : vals}}

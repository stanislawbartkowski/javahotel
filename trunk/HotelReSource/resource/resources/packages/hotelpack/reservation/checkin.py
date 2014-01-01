from cutil import printVar
from cutil import setStandEditMode
from util.util import RESFORM
from util.util import ROOMLIST
from util.util import SERVICES
from util.util import RESOP
from util.util import CUSTOMERLIST
from util.util import getCustFieldId
from util.util import isResOpen
from cutil import setCopy
from util.util import mapToXML
from util.util import xmlToVar
from cutil import allEmpty
from util.util import newResGuest
from cutil import createArrayList
from util.util import newCustomer
from cutil import copyVarToProp
from util import util
from util.util import MESS
from util.util import getCustFieldIdAll
from util.util import customerToVar

M = MESS()
CUSTF = getCustFieldIdAll()
CHECKINLIST= "checkinlist"

def __toMap(map,custid,CUST) :
    cust = CUST.findElem(custid)
    map["guestselect"] = cust.getName()
    customerToVar(map,cust)    

class MAKECHECKIN(util.HOTELTRANSACTION) :
  
   def __init__(self,var) :
     util.HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :
     # double check (under semaphore) that reservation is not already changed to STAY
     resName = var["resename"]
     R = RESFORM(var)
     ROP = RESOP(var)
     CUST = CUSTOMERLIST(var)
     r = R.findElem(resName)
     if util.resStatus(r) == 1 :
       var["JERROR_MESSAGE"] = M("ALREADYCHECKEDINMESS")
       var["JMESSAGE_TITLE"] = M("ALREADYCHECKEDINTITLE")
       return
     a = createArrayList()
     for cust in var["JLIST_MAP"][CHECKINLIST] :
           if allEmpty(cust,CUSTF) : continue
           cid = cust["name"]
           if cid == None : c = newCustomer(var)
           else : c = CUST.findElem(cid)
           copyVarToProp(cust,c,CUSTF)
           if cid == None : cid = CUST.addElem(c).getName()
           else : CUST.changeElem(c)
           rGuest = newResGuest(var)
           rGuest.setGuestName(cid)
           rid = cust["roomid"]
           rGuest.setRoomName(rid)
           a.add(rGuest)
     ROP.setResGuestList(resName,a)
     ROP.changeStatusToStay(resName)
     var["JCLOSE_DIALOG"] = True   
     var["JREFRESH_DATELINE_reservation"] = ""

def checkinaction(action,var):
    printVar("checkinaction",action,var)
    R = RESFORM(var)
    ROOM = ROOMLIST(var)
    SE = SERVICES(var)
    ROP = RESOP(var)
    resName = var["resename"]
    CUST = CUSTOMERLIST(var)
       
    if action == "makecheckin" and var["JYESANSWER"] :
        TRANS = MAKECHECKIN(var)
        TRANS.doTrans()
           
    if action == "guestdetails" and var[CHECKINLIST+"_lineset"] :
        var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        var["JUPDIALOG_START"] = mapToXML(var,CUSTF)
        
    if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" : 
        xml = var["JUPDIALOG_RES"]
        xmlToVar(var,xml,CUSTF)
        setCopy(var,CUSTF,CHECKINLIST)
    
    if action == "selectguestfromlist" :
        custid =var["JUPDIALOG_RES"]
        if custid == None : return
        __toMap(var,custid,CUST)
        li = ["guestselect"] + CUSTF
        setCopy(var,li,CHECKINLIST)
    
    if action == "selectguest" :
        var["JUP_DIALOG"] = "hotel/reservation/customerselection.xml"
        var['JAFTERDIALOG_ACTION'] = "selectguestfromlist"
        
    if action == "before" :
        roomRes = {}
        reservation = R.findElem(resName)
        # list of guests
        gList = ROP.getResGuestList(resName)
        for r in reservation.getResDetail() :
            
            rname = r.getRoomName()
            room = ROOM.findElem(rname)
            servicename = r.getService()
            serv = SE.findElem(servicename)
            nop = serv.getNoPersons()
            if roomRes.has_key(rname) :
                no = roomRes[rname][0]
                if nop > no : roomRes[rname][0] = nop
            else :
                roomRes[rname] = (nop,room) 
     
            list = []
            wasGuest = False
            for roomname in roomRes.keys() :
                for i in range(roomRes[roomname][0]) :
                    map = { "roomid" : roomname, "roomdesc" : roomRes[roomname][1].getDescription()}
                    # add guest details
                    no = -1
                    found = False
                    for g in gList :
                        if g.getRoomName() == roomname :
                            no = no + 1
                            if no == i :
                                found = True
                                custid = g.getGuestName()
                                __toMap(map,custid,CUST)
                                found = True
                                wasGuest = True
                                break
                    if not found :
                        map["guestselect"] = "<select>"        
                    list.append(map)
            var["JLIST_MAP"] = { CHECKINLIST : list}
            setStandEditMode(var,CHECKINLIST,["surname","firstname","title","country"])
            resform = R.findElem(resName)
            if isResOpen(resform) and not wasGuest :
                custid = resform.getCustomerName()
                map = list[0]
                __toMap(map,custid,CUST)                
             
    
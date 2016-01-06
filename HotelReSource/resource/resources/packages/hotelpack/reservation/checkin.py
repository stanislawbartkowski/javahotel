import cutil
from util import util,cust

M = util.MESS()
CUSTF = cust.getCustFieldIdAll()
CHECKINLIST= "checkinlist"

def __toMap(map,custid,CUST) :
    custo = CUST.findElem(custid)
    map["guestselect"] = custo.getName()
    cust.customerToVar(map,custo)    

def _resStatus(var) :
     resName = var["resename"]
     r = util.RESFORM(var).findElem(resName)
     return util.resStatus(resName)  

class MAKECHECKIN(util.HOTELTRANSACTION) :
  
   def __init__(self,var) :
     util.HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :
     # double check (under semaphore) that reservation is not already changed to STAY
     resName = var["resename"]
     R = util.RESFORM(var)
     ROP = util.RESOP(var)
     CUST = util.CUSTOMERLIST(var)
     r = R.findElem(resName)
     if not var["isstay"] and util.resStatus(r) == 1 :
       var["JERROR_MESSAGE"] = M("ALREADYCHECKEDINMESS")
       var["JMESSAGE_TITLE"] = M("ALREADYCHECKEDINTITLE")
       return
     a = cutil.createArrayList()
     for custo in var["JLIST_MAP"][CHECKINLIST] :
           if cutil.allEmpty(custo,cust.getCustFieldIdWithout()) : continue
           cid = custo["name"]
           if cid == None : c = cust.newCustomer(var)
           else : c = CUST.findElem(cid)
           cust.customerDataFromVar(c,custo)
           if cid == None : cid = CUST.addElem(c).getName()
           else : CUST.changeElem(c)
           cust.saveDefaCustomer(custo)
           rGuest = util.newResGuest(var)
           rGuest.setGuestName(cid)
           rid = custo["roomid"]
           rGuest.setRoomName(rid)
           a.add(rGuest)
     ROP.setResGuestList(resName,a)
     if var["isstay"] : cutil.JOURNAL(var).addJournalElem(util.JOURNAL_CHANGELISTOFGUEST,None,resName)

     var["JCLOSE_DIALOG"] = True   

def checkinaction(action,var):
    cutil.printVar("checkinaction",action,var)
    R = util.RESFORM(var)
    ROOM = util.ROOMLIST(var)
    SE = util.SERVICES(var)
    ROP = util.RESOP(var)
    resName = var["resename"]
    CUST = util.CUSTOMERLIST(var)
       
    if action == "makecheckin" and var["JYESANSWER"] :
        TRANS = MAKECHECKIN(var)
        TRANS.doTrans()
           
    if action == "guestdetails" and var[CHECKINLIST+"_lineset"] :
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        cust.customerDetailsActive(var,None)
        
    if action == "acceptdetails" and var["JUPDIALOG_BUTTON"] == "accept" : 
        xml = var["JUPDIALOG_RES"]
        util.xmlToVar(var,xml,CUSTF)
        cutil.setCopy(var,CUSTF,CHECKINLIST)
    
    if action == "selectguestfromlist" :
        custid =var["JUPDIALOG_RES"]
        if custid == None : return
        __toMap(var,custid,CUST)
        li = ["guestselect"] + CUSTF
        cutil.setCopy(var,li,CHECKINLIST)
    
    if action == "selectguest" :
        var["JUP_DIALOG"] = "?customerselection.xml"
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
                        cust.setDefaCustomerNotCopy(map)
                        
                    list.append(map)
                    
            var["JLIST_MAP"] = { CHECKINLIST : list}
            cutil.setChangeEditMode(var,CHECKINLIST,["surname","firstname","title","country"])
            resform = R.findElem(resName)
            assert resform != None
            status = util.resStatus(resform)
            var["isstay"] = (status == 1)
            cutil.setCopy(var,"isstay")
            if status == 2 : cutil.hideButton(var,"acceptchange")
            else : cutil.hideButton(var,"accept")
            if status == 2 and not wasGuest :
                custid = resform.getCustomerName()
                map = list[0]
                __toMap(map,custid,CUST)
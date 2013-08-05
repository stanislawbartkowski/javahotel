from cutil import printVar
from util.util import RESFORM
from util.util import ROOMLIST
from util.util import SERVICES

#   for r in reservation.getResDetail() :
#         map = { "name" : r.getRoom(), "resday" : r.getResDate(), "price" : r.getPrice(), "service" : r.getService() }
#         list.append(map)
#         sum.add(r.getPrice())

def checkinaction(action,var):
    printVar("checkinaction",action,var)
    R = RESFORM(var)
    ROOM = ROOMLIST(var)
    SE = SERVICES(var)
    resName = var["resename"]
    
    if action == "before" :
        roomRes = {}
        reservation = R.findElem(resName)
        for r in reservation.getResDetail() :
            rname = r.getRoom()
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
            for roomname in roomRes.keys() :
                for i in range(roomRes[roomname][0]) :
                    map = { "roomid" : roomname, "roomdesc" : roomRes[roomname][1].getDescription()}
                    list.append(map)
            var["JLIST_MAP"] = { "checkinlist" : list}       
    # resename
    
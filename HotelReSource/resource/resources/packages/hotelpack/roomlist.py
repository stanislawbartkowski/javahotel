from util.util import printvar
from util.util import ROOMLIST
from util.util import findElemInSeq
from com.gwthotel.hotel.rooms import HotelRoom
from util.util import copyNameDescr
from util.util import SERVICES
from util.util import createSeq
from util.util import createArrayList
from util.util import MESS
from util import util
import cutil


M = MESS()

RLIST="roomlist"
SERVLIST="services"

def _setNumb(map,s) :
  map["noperson"] = s.getNoPersons()
  map["noextrabeds"] = util.getIntField(s.getNoExtraBeds())
  map["nochildren"] = util.getIntField(s.getNoChildren())

def _getServiceList(var) :
    a = createArrayList()
    seq = var["JLIST_MAP"][SERVLIST]
    for s in seq :
      if s["check"] : a.add(s["service"])
    return a  

def _createList(var):
    R = ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       map = {"name" : s.getName(), "descr" : s.getDescription()}
       _setNumb(map,s)
       list.append(map )
       
    cutil.setJMapList(var,RLIST,list)

def _duplicatedPriceList(var):    
    R = ROOMLIST(var)
    seq = R.getList()
    if findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = M("DUPLICATEDROOMNAME")
      return True
    return False

def _notValidRoomDesc(var):
    no = var["noperson"]
    if no <= 0 :
      var["JERROR_noperson"] = M("NOPERSONGREATERZERO")
      return True
    if not cutil.checkGreaterZero(var,"noextrabeds",True) : return True
    if not cutil.checkGreaterZero(var,"nochildren",True) : return True
    a = _getServiceList(var)
    if a.isEmpty() :
      var["JERROR_descr"] = M("checkatleastoneservice")
      return True      
    return False

        
def _createRoom(var):
   pr = HotelRoom()
   copyNameDescr(pr,var)
   pr.setNoPersons(var["noperson"])
   noe = var["noextrabeds"]
   util.setIntField(var,"noextrabeds", lambda val : pr.setNoExtraBeds(val))
   noc = var["nochildren"]
   util.setIntField(var,"nochildren",lambda val : pr.setNoChildren(val))
   return pr

def _createServicesList(var):
    S = SERVICES(var)
    R = ROOMLIST(var)
    
    slist = []
    if var["name"] :
      slist = R.getRoomServices(var["name"])
      
    seq = S.getRoomServices()
    list = []
    for s in seq :
      check = findElemInSeq(s.getName(),slist) != None
      map = {"check" : check ,"service" : s.getName(), "servdescr" : s.getDescription(), "perperson" : s.isPerperson()}
      _setNumb(map,s)
      list.append( map )
      
    cutil.setJMapList(var,SERVLIST,list)
                    
def roomlistaction(action,var) :
    
  printvar("roomlistaction",action, var)  
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
def elemroomaction(action,var) :
    
  printvar("elemroomaction",action, var)  
  R = ROOMLIST(var)

  if action == "columnchangeaction" :
    if not var["check"] : return
    serv = SERVICES(var).findElem(var["service"])
    assert serv != None
    _setNumb(var,serv)
    cutil.setCopy(var,["noperson","nochildren","noextrabeds"])    

  if action == "before" :
      _createServicesList(var)
      if var["JCRUD_DIALOG"] == "crud_add" or var["JCRUD_DIALOG"] == "crud_change" : cutil.setStandEditMode(var,SERVLIST,"check")
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedPriceList(var) or _notValidRoomDesc(var): return
      var["JYESNO_MESSAGE"] = M("ADDNEWROOMASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      R.addElem(_createRoom(var))
      R.setRoomServices(var["name"],_getServiceList(var))
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      if _notValidRoomDesc(var): return
      var["JYESNO_MESSAGE"] = M("CHANGEROOMASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      R.changeElem(_createRoom(var))
      R.setRoomServices(var["name"],_getServiceList(var))
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVEROOMASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      R.deleteElem(_createRoom(var))
      var["JCLOSE_DIALOG"] = True 
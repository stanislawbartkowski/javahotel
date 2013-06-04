from util.util import printvar
from util.util import ROOMLIST
from util.util import findElemInSeq
from com.gwthotel.hotel.rooms import HotelRoom
from util.util import copyNameDescr
from util.util import SERVICES
from util.util import createSeq
from util.util import createArrayList
from util.util import MESS

M = MESS()

def _createList(var):
    R = ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "noperson" : s.getNoPersons()} )
       
    var["JLIST_MAP"] = { "roomlist" : list}

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
    return False
        
def _createRoom(var):
   pr = HotelRoom()
   copyNameDescr(pr,var)
   pr.setNoPersons(var["noperson"])
   return pr

def _createServicesList(var):
    S = SERVICES(var)
    seq = createSeq(S.getList())
    map = {"lines" : seq, "columns" : [{"id" : "id","displayname" : "Service"}]}
    var["JCHECK_MAP"] = { "services" : map}
    
    R = ROOMLIST(var)
    seq = []
    if var["name"] :
      seq = R.getRoomServices(var["name"])
    for e in seq :
        sName = e.getName()
        map[sName] = [{"id" : "id", "val" : True}]
        
def _getServiceList(var) :
    S = SERVICES(var)
    a = createArrayList()
    for se in S.getList() :
        s = se.getName()
        if not var["JCHECK_MAP"]["services"].has_key(s) : continue
        va = var["JCHECK_MAP"]["services"][s]
        if not va[0]["val"] : continue
        a.add(s)
    return a    
            
def roomlistaction(action,var) :
    
  printvar("roomlistaction",action, var)  
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
def elemroomaction(action,var) :
    
  printvar("elemroomaction",action, var)  
  R = ROOMLIST(var)

  if action == "before" :
      _createServicesList(var)
    
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
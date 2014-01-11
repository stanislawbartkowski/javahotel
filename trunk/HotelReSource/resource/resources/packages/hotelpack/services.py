from com.gwthotel.hotel.server.service import H
from util.util import MESS
from util.util import printvar
from util.util import getHotelName
from util.util import SERVICES
from util.util import copyNameDescr
from util.util import findElemInSeq
from com.gwthotel.hotel.services import HotelServices
from util.util import duplicateService
from util.util import getVatName
from util import util
import cutil

M = MESS()
taxList = H.getVatTaxes()
D = util.HOTELDEFADATA()

def _getVatName(vat):
  return getVatName(vat)  

def _createList(var):
    serv = SERVICES(var)
    seq = serv.getRoomServices()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "vat" : s.getAttr("vat"), "noperson" : s.getNoPersons(), "noextrabeds" : s.getNoExtraBeds(),
                    "nochildren" : s.getNoChildren(),"perperson" : s.isPerperson(),
                    "vatname" : _getVatName(s.getAttr("vat"))} )
       
    var["JLIST_MAP"] = { "services" : list}
    
def _duplicateService(var):    
    return duplicateService(var)
    
def _notverifyService(var):
   nop = var["noperson"]
   if nop <= 0 : 
     var["JERROR_noperson"] = M("NOPERSONGREATERZERO")
     return True
   if not cutil.checkGreaterZero(var,"noextrabeds",True) : return True
   if not cutil.checkGreaterZero(var,"nochildren",True) : return True
   return False

def serviceaction(action,var) :

  printvar ("serviceaction", action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
def _createService(var):
   se = HotelServices()
   copyNameDescr(se,var)
   se.setAttr("vat",var["vat"])
   nop = var["noperson"]
   D.putDataHI(10,nop)
   se.setNoPersons(nop)
   noe = var["noextrabeds"]
   D.putDataHI(11,noe)
   se.setNoExtraBeds(noe)
   noc = var["nochildren"]
   D.putDataHI(12,noc)
   se.setNoChildren(noc)   
   se.setPerperson(var["perperson"])
   return se    

def elemserviceaction(action,var) :

  printvar ("elemserviceaction", action,var)

  serv = SERVICES(var)
  
  if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
    var["noperson"] = D.getDataHI(10)
    var["noextrabeds"] = D.getDataHI(11)
    var["nochildren"] = D.getDataHI(12)
    cutil.setCopy(var,["noperson","noextrabeds","nochildren"])
    
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicateService(var) or _notverifyService(var) : return          
      var["JYESNO_MESSAGE"] = M("ADDNEWSERVICEASK");
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.addElem(se)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      if _notverifyService(var) : return
      var["JYESNO_MESSAGE"] = M("CHANGESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.changeElem(se)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("DELETESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.deleteElem(se)
      var["JCLOSE_DIALOG"] = True      


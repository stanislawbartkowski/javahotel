from com.gwthotel.hotel.server.service import H
from util.util import MESS
from util.util import printvar
from util.util import getHotelName
from util.util import SERVICES
from util.util import copyNameDescr
from util.util import findElemInSeq
from com.gwthotel.hotel.services import HotelServices

M = MESS()
taxList = H.getVatTaxes()

def _getVatName(vat):
  list = taxList.getList()
  vat = findElemInSeq(vat,list)
  if vat == None : return ""
  return vat.getDescription()

def _createList(var):
    serv = SERVICES(var)
    seq = serv.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "vat" : s.getAttr("vat"), "noperson" : s.getNoPersons(),
                    "vatname" : _getVatName(s.getAttr("vat"))} )
       
    var["JLIST_MAP"] = { "services" : list}
    
def _duplicateService(var):    
    serv = SERVICES(var)
    seq = serv.getList()
    if findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = M("DUPLICATEDSERVICENAME")
      return True
    return False
    
def _notverifyService(var):
   nop = var["noperson"]
   if nop > 0 : return False
   var["JERROR_noperson"] = M("NOPERSONGREATERZERO")
   return True

def serviceaction(action,var) :

  printvar ("serviceaction", action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
    
def _createService(var):
   se = HotelServices()
   copyNameDescr(se,var)
   se.setAttr("vat",var["vat"])
   nop = var["noperson"]
   se.setNoPersons(nop)
   return se    

def elemserviceaction(action,var) :

  printvar ("elemserviceaction", action,var)

  serv = SERVICES(var)
    
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


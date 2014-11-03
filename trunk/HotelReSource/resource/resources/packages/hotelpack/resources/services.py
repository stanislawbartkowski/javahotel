import cutil

from util import util
from com.gwthotel.hotel.services import HotelServices
import cutil
import xmlutil

M = util.MESS()
taxList = cutil.getDict("vat")
D = util.HOTELDEFADATA()

# -------------

def _createList(var):
    serv = util.SERVICES(var)
    seq = serv.getRoomServices()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "vat" : s.getAttr("vat"), "noperson" : s.getNoPersons(), "noextrabeds" : util.getIntField(s.getNoExtraBeds()),
                    "nochildren" : util.getIntField(s.getNoChildren()),"perperson" : s.isPerperson(),
                    "vatname" : util.getVatName(s.getAttr("vat"))} )
       
    var["JLIST_MAP"] = { "services" : list}
        
def _notverifyService(var):
   nop = var["noperson"]
   if nop <= 0 : 
     var["JERROR_noperson"] = M("NOPERSONGREATERZERO")
     return True
   if not cutil.checkGreaterZero(var,"noextrabeds",True) : return True
   if not cutil.checkGreaterZero(var,"nochildren",True) : return True
   return False

def serviceaction(action,var) :

  cutil.printVar ("serviceaction", action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
        
def _createService(var):
   se = HotelServices()
   util.copyNameDescr(se,var)
   se.setAttr("vat",var["vat"])
   D.putDataH(14,var["vat"])
   nop = var["noperson"]
   D.putDataHI(10,nop)
   se.setNoPersons(nop)
   noe = var["noextrabeds"]
   D.putDataHI(11,noe)
   util.setIntField(var,"noextrabeds", lambda val : se.setNoExtraBeds(val))
   noc = var["nochildren"]
   D.putDataHI(12,noc)
   util.setIntField(var,"nochildren",lambda val : se.setNoChildren(val))
   se.setPerperson(var["perperson"])
   D.putDataHB(13,var["perperson"])
   return se    
 
 
def elemserviceaction(action,var) :

  cutil.printVar ("elemserviceaction", action,var)

  serv = util.SERVICES(var)
  
  if action == "before" :
    if var["JCRUD_DIALOG"] == "crud_add" : 
      var["noperson"] = D.getDataHI(10)
      var["noextrabeds"] = D.getDataHI(11)
      var["nochildren"] = D.getDataHI(12)
      var["vat"] = D.getDataH(14)
      var["perperson"] = D.getDataHB(13)
      cutil.setCopy(var,["noperson","noextrabeds","nochildren","vat","perperson"])
    else : cutil.hideButton(var,"showrooms",False)
    
  if action == "showrooms" :
     l = util.listOfRoomsForService(var,var["name"])
     print l
     for s in l :
       print s
     var["JUPDIALOG_START"] = xmlutil.listNumberToCVS(l,"-1")   
     var["JUP_DIALOG"] = "hotel/roomslist.xml"       
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if util.duplicateService(var) or _notverifyService(var) : return          
      var["JYESNO_MESSAGE"] = M("ADDNEWSERVICEASK")
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
      l = util.listOfRoomsForService(var,var["name"])
      l1 = util.RESOP(var).getReseForService(var["name"])
      l2 = util.listOfPriceListForService(var,var["name"])
      if len(l) > 0 or len(l1) > 0 or len(l2) > 0 :
         var["JERROR_MESSAGE"] = M("cannotremoveservice").format(len(l),len(l2),len(l1))
         return
      var["JYESNO_MESSAGE"] = M("DELETESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.deleteElem(se)
      var["JCLOSE_DIALOG"] = True      


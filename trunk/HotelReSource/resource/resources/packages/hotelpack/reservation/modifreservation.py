import cutil
import xmlutil
from util import rutil
from util import util
import con

LI="resmodiflist"

def setEditListActionOk(var,li,ok=True) :
   var["JLIST_EDIT_ACTIONOK_" + li] = ok
  

def _setPriceL(l,pmap,pr) :
  if l[pmap] != None : return
  l[pmap] = pr
  
class RELINE :
  
  def __init__(self,li,night,roomname,servicename,pricelist,perpers,nop,pop,noc,poc,noe,poe,total) :
    self.li = li
    self.night = night
    self.roomname = roomname
    self.servicename = servicename
    self.pricelist = pricelist
    self.perpers = perpers
    self.nop = nop
    self.pop = pop
    self.noc = noc
    self.poc = poc
    self.noe = noe
    self.poe = poe
    self.total = total
    
  def _testp(self,var,nop,pop) :
    if var[nop] == None: return True
    return not cutil.checkEmpty(var,pop) 
    
  def validate(self,var) :
    print "validate",var[self.night],"!"
    if cutil.checkEmpty(var,[self.night,self.roomname,self.nop,self.pop]) : return False
    if not cutil.checkGreaterZero(var,self.nop) : return False
    if not cutil.checkGreaterZero(var,self.noc) : return False
    if not cutil.checkGreaterZero(var,self.noe) : return False
    if not self._testp(var,self.noc,self.poc) : return False
    if not self._testp(var,self.noe,self.poe) : return False
    room = var[self.roomname]
    service = var[self.servicename]
    if room != None and service != None :
      slist = util.ROOMLIST(var).getRoomServices(room)
      exist = util.findElemInSeq(service,slist)
      if not exist :
        cutil.setErrorField(var,self.servicename,"@theserviceisnotthisroom")
        return False
        
    return True
    
  def setPrices(self,var) :
    pservice = var[self.servicename]
    plist = var[self.pricelist]
    if plist != None and pservice != None :
      (price,pricechild,priceextra) = rutil.getPriceList(var,plist,pservice)
      (var[self.pop],var[self.poc],var[self.poe]) = (price,pricechild,priceextra)
      cutil.setCopy(var,[self.pop,self.poc,self.poe],self.li)
      
  def calculatePrice(self,var) :
    total = rutil.calculatePrice(var[self.perpers],var[self.nop],var[self.noc],var[self.noe],var[self.pop],var[self.poc],var[self.poe])
    var["JVALBEFORE"] = var[self.total]
    var[self.total] = total
    cutil.setCopy(var,self.total,self.li)    
    cutil.modifDecimalFooter(var,self.li,self.total)
    
ELIST = ["resroomname","resday","rlist_roompricelist","rlist_roomservice","rlist_serviceperperson","rline_nop","rlist_priceperson","rlist_noc","rlist_pricechildren","rlist_noe","rlist_priceextra","rlist_pricetotal"]
    
RE=RELINE(LI,"resday","resroomname","rlist_roomservice","rlist_roompricelist","rlist_serviceperperson","rline_nop","rlist_priceperson","rlist_noc","rlist_pricechildren","rlist_noe","rlist_priceextra","rlist_pricetotal")

def dialogaction(action,var) :
  cutil.printVar("modif reservation",action,var)
  
  if action == "before" :
      xml = var["JUPDIALOG_START"]
      (m,li) = xmlutil.toMap(xml)
      prevplist = None
      prevpservice = None
      (price,pricechild,priceextra) = (None, None,None)
      sum = util.SUMBDECIMAL()
      for l in li :
        sum.add(l["rlist_pricetotal"])
        plist = l["rlist_roompricelist"]
        pservice = l["rlist_roomservice"]
        if plist != None and pservice != None :
          if plist != prevplist or pservice != prevpservice :
            (price,pricechild,priceextra) = rutil.getPriceList(var,plist,pservice)
            (prevplist,prevpservice) = (plist,pservice)
            
          _setPriceL(l,"rlist_priceperson",price)
          _setPriceL(l,"rlist_pricechildren",pricechild)
          _setPriceL(l,"rlist_priceextra",priceextra)
          
      cutil.setJMapList(var, LI,li)
      cutil.setAddEditMode(var,LI,ELIST)
      cutil.setFooter(var,LI,"rlist_pricetotal",sum.sum)
      
  if action == "aftereditrow" :
    cutil.setEditRowOk(var,LI,False)
    if not RE.validate(var) : return
    RE.calculatePrice(var)
    cutil.setEditRowOk(var,LI)
    
  if action == "editlistrowaction" :
    if var["JLIST_EDIT_ACTION_" + LI] == "ADD" or var["JLIST_EDIT_ACTION_" + LI] == "ADDBEFORE" :
      var["rlist_pricetotal"] = 0      
      setEditListActionOk(var,LI) 
      cutil.setCopy(var,ELIST,LI)
      RE.calculatePrice(var)      

  if action == "roomselected" and var["JUPDIALOG_BUTTON"] == "accept" :
    roomname = var["JUPDIALOG_RES"]
    if roomname != var["resroomname"] :
      var["resroomname"] = roomname
      cutil.setCopy(var,"resroomname",LI)
  
  # does not work select
  if action == "xxxcolumnchangeaction" :
    RE.setPrices(var)    
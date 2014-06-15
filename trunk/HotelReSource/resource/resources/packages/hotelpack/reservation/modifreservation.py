import cutil
import xmlutil
from util import rutil
from util import util
import con

LI="resmodiflist"  

def _setPriceL(l,pmap,pr) :
  if l[pmap] != None : return
  l[pmap] = pr
  
    
ELIST = ["resroomname","resday","rlist_roompricelist","rlist_roomservice","rlist_serviceperperson","rline_nop","rlist_priceperson","rlist_noc","rlist_pricechildren","rlist_noe","rlist_priceextra","rlist_pricetotal"]
    
RE=rutil.RELINE(LI,"resday","resroomname","rlist_roomservice","rlist_roompricelist","rlist_serviceperperson","rline_nop","rlist_priceperson","rlist_noc","rlist_pricechildren","rlist_noe","rlist_priceextra","rlist_pricetotal","rlist_pricetotal")

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
      cutil.setEditListActionOk(var,LI) 
      cutil.setCopy(var,ELIST,LI)
      RE.calculatePrice(var)
      
    if var["JLIST_EDIT_ACTION_" + LI] == "REMOVE" :
      var["JYESNO_MESSAGE"] = "@doyouwanttoremoveline"
      var["JAFTERDIALOG_ACTION"] = "removeyesno"
      
  if action == "removeyesno" :
    if var["JYESANSWER"] : 
       cutil.setEditListActionOk(var,LI)
       RE.calculatePriceAterRemove(var)
    else : cutil.setEditListActionOk(var,LI,False) 

  if action == "roomselected" and var["JUPDIALOG_BUTTON"] == "accept" :
    roomname = var["JUPDIALOG_RES"]
    if roomname != var["resroomname"] :
      var["resroomname"] = roomname
      cutil.setCopy(var,"resroomname",LI)
  
  # does not work select
  if action == "xxxcolumnchangeaction" :
    RE.setPrices(var)    
    
  if action == "accept" :
   list = var["JLIST_MAP"][LI]
   if len(list) == 0 :   
     rutil.setAlreadyReservedNotSelected(var)
     return

   # "resroomname","resday"
   res =  rutil.checkReseAvailibity(var,list,None,"resday","resroomname")
   if res != None : return
   xml = xmlutil.toXML({},list)
   var["JCLOSE_DIALOG"] = xml

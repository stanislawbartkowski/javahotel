import datetime

import cutil,con,xmlutil,clog

from util import rutil,util,diallaunch

from rrutil import rbefore,advarese,confirm,reseparam

M = util.MESS()

CUST=rbefore.RCUST
RESLIST=rbefore.RLIST

RE=rutil.RELINE(None,"datecol","name","roomservice","roompricelist","serviceperperson","resnop","respriceperson","resnochildren","respricechildren","resnoextrabeds","respriceextrabeds","respriceperroom",None)

RELIST=rutil.RELINE(RESLIST,*rutil.RESLIST)

D = util.HOTELDEFADATA()

def _newRese(var) :
  return rutil.getReseName(var) == None

#--------------------
# ---- validation ---
# --------------------
def _okServiceForRoom(var) :
  room = var["name"]
  service = var["roomservice"]
  if service == None : return True
  (servlist,pricelist) = util.getServicesForRoom(var,room)
  p = util.findElemInSeq(service,servlist)
  if p == None :
    # theserviceisnotthisroom
    cutil.setErrorField(var,"roomservice","@theserviceisnotthisroom")
    return False
  return True      

def _okPriceList(var) :
  room = var["name"]
  service = var["roomservice"]
  roompricelist = var["roompricelist"]
  if service == None or roompricelist == None : return True
  (servlist,pricelist) = util.getServicesForRoom(var,room)
#  clog.info("okpricelist",servlist,pricelist)
  if roompricelist in pricelist : return True
  cutil.setErrorField(var,"roompricelist","@thepricelistnotforthisservice")
  return False  

def _checkAvailibity(var) :
  list = var["JLIST_MAP"][RESLIST]  
  res =  rutil.checkReseAvailibity(var,list,"avail","resday","resroomname")
  if res == None : return True
  return False

def _checkRese(var,new=True):
  if not RE.validate(var) : return False    
  if _createListOfDays(var,new) : return True
  rutil.setAlreadyReserved(var)
  return False

def _checkCurrentRese(var) :
  list = var["JLIST_MAP"][RESLIST]
  if len(list) == 0:
    rutil.setAlreadyReservedNotSelected(var)  
    return False
  for elem in list :
    avail = elem["avail"]
    if not avail :
     rutil.setAlreadyReserved(var)
     return False
  return True          
            
# --------------------          

def _createResData(var,new):
  service = var["roomservice"]
  if cutil.emptyS(service) : return None
  pricelist = var["roompricelist"]
  if cutil.emptyS(pricelist) : return None
  roomname = var["name"]
  date = var["datecol"]
  resdays = var["resdays"]
  dl = datetime.timedelta(1)
  dt = date
  sum = util.SUMBDECIMAL()
  if new :
    list = []
  else :
    list = var["JLIST_MAP"][RESLIST]
    sum.add(var["JFOOTER_reslist_rlist_pricetotal"])
  resnop = var["resnop"]
  perperson = var["serviceperperson"]
  priceroom = var["respriceperroom"]
  priceperson = var["respriceperson"]
  pricechildren = None
  priceextra = None
  resnoc = var["resnochildren"]
  if resnoc : pricechildren = var["respricechildren"]
  resextra = var["resnoextrabeds"]
  if resextra : priceextra = var["respriceextrabeds"]
    
  price = rutil.calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra,priceroom)
  
  query = cutil.createArrayList()
  RES = util.RESOP(var)
  qelem = rutil.createResQueryElem(roomname,date,con.incDays(date,resdays))
  query.add(qelem)
  rList = RES.queryReservation(query)
  allavail = True
  
  resename = rutil.getReseName(var)

  for i in range(resdays) :
#      price = pPrice.getPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if con.eqDate(dt,rdata) : 
            if resename == None or resename != re.getResId() : 
              avail = allavail = False
      
      map = { "avail" : avail, "resroomname" : roomname, "resday" : dt, "rlist_pricetotal" : price, "rline_nop" : resnop,"rlist_priceperson" : priceperson,
              "rlist_noc" : resnoc, "rlist_pricechildren" : pricechildren, "rlist_noe" : resextra, "rlist_priceextra" : priceextra,              
              "rlist_serviceperperson" : perperson, "rlist_roomservice" : service, "rlist_roompricelist" : pricelist}
      list.append(map)
      dt = dt + dl
      sum.add(price)
    
  return [list,sum.sum,allavail]    


def _getPriceList(var) :
  pricelist = var["roompricelist"]
  serv = var["roomservice"]
  return rutil.getPriceList(var,pricelist,serv)

def _setAfterServiceName(var) :
  S = util.SERVICES(var)
  serv = S.findElem(var["roomservice"])
  if serv == None : return
  var["serviceperperson"] = serv.isPerperson()
  cutil.setCopy(var,"serviceperperson")  

def _setAfterPriceList(var) :
  (price,pricechild,priceextra) = _getPriceList(var)
  var["respriceperson"] = price
  var["respricechildren"] = pricechild
  var["respriceextrabeds"] = priceextra    
  var["respriceperroom"] = price
#  clog.info("afterpricelist",price,pricechild,priceextra)
  cutil.setCopy(var,["respriceperson","respricechildren","respriceextrabeds","respriceperroom"])

def _setAfterPerPerson(var) :  
  cutil.enableField(var,["respriceperson","respricechildren","respriceextrabeds"],var["serviceperperson"])
  cutil.enableField(var,"respriceperroom",not var["serviceperperson"])
  
def _setPriceAndService(var) :
  room = var["name"]
  (servicelistlist, pricelistlist) = util.getServicesForRoom(var,room)
  servicelist = util.createListOfNames(servicelistlist) 
  roomservice = D.getDataH(30)
  pricelist = D.getDataH(31)
  if roomservice == None or not roomservice in servicelist : roomservice = servicelist[0]
  if pricelist == None or not pricelist in pricelistlist : pricelist = pricelistlist[0]
  rutil.setServicePriceList(var,roomservice,pricelist)

def _calculatePaymentBy(var,li) :
  nodays = D.getDataHI(41)
  if nodays == None : return
  arrival = None
  departure = None
  for l in li :
    (arrival,departure) = calculateJDates(arrival,departure,l["resday"])
  paymentby = con.incDays(arrival,0-nodays)
  if paymentby <= con.today() : paymentby = None
  var["advance_duedate"] = paymentby
  cutil.setCopy(var,"advance_duedate")

def _calculateAdvanceAmount(var,total) :
   A = advarese.createAdvaRese(var)
   A.calculateAdvanceAmount(total)

def _createListOfDays(var,new): 
  rData = _createResData(var,new)
  if rData == None : return None
  cutil.setJMapList(var,RESLIST,rData[0])
  cutil.setFooter(var,RESLIST,"rlist_pricetotal",rData[1])
  _calculateAdvanceAmount(var,rData[1])
  _calculatePaymentBy(var,rData[0])
  return rData[2]  


class MAKERESE(util.HOTELTRANSACTION) :
  
   def __init__(self,var) :
     util.HOTELTRANSACTION.__init__(self,1,var)
          
   def run(self,var) :     
      # validate (under transaction !)
      if not _checkAvailibity(var) : return
      A = advarese.createAdvaRese(var)
      if not A.validate() : return
      # customer firstly
      service = var["roomservice"]
      pricelist = var["roompricelist"]
      D.putDataH(30,service)
      D.putDataH(31,pricelist)
      
      cust = util.customerFromVar(var,CUST)
      R = util.CUSTOMERLIST(var)
      name = var["cust_name"]
      if not cutil.emptyS(name) :
          cust.setName(name)
          R.changeElem(cust)
      else :
          cust.setGensymbol(True);
          name = R.addElem(cust).getName()
      util.saveDefaCustomer(var,CUST)               
      # --- customer added
      
      resename = rutil.getReseName(var) 
      reservation = util.newResForm(var)
      if resename : reservation.setName(resename)
      else : reservation.setGensymbol(True);
      reservation.setCustomerName(name)
      # advance      
      A = advarese.createAdvaRese(var)
      A.setAdvaData(reservation)
      # --
      service = var["roomservice"]
      nop = var["nop"]
      reselist = reservation.getResDetail()
      rlist = var["JLIST_MAP"][RESLIST]
      for re in rlist :
        
          (listprice,listpricechild,listpriceextra) = rutil.getPriceList(var,re["rlist_roompricelist"],re["rlist_roomservice"])

          r = util.newResAddPayment()
          r.setRoomName(re["resroomname"])
          r.setService(re["rlist_roomservice"])
          r.setResDate(con.toDate(re["resday"]))
          r.setPerperson(re["rlist_serviceperperson"])
          r.setPriceListName(re["rlist_roompricelist"])
          
          r.setNoP(re["rline_nop"])
          r.setPrice(con.toB(re["rlist_priceperson"]))
          r.setPriceList(listprice)
          
          util.setIntField(re,"rlist_noc",lambda v : r.setNoChildren(v))
          r.setPriceChildren(con.toB(re["rlist_pricechildren"]))
          r.setPriceListChildren(listpricechild)
          
          util.setIntField(re,"rlist_noe",lambda v : r.setNoExtraBeds(v))
          r.setPriceExtraBeds(con.toB(re["rlist_priceextra"]))
          r.setPriceListExtraBeds(listpriceextra)
          
          r.setPriceTotal(con.toB(re["rlist_pricetotal"]))
          
          reselist.add(r)
          
      RFORM = util.RESFORM(var)
      if resename : RFORM.changeElem(reservation)
      else : resename = RFORM.addElem(reservation).getName()
      
      reseparam.RESPARAM(resename).saveParam(var)
      
      # confirmed/not confirmed
      confirm.createC(var).changeReservation(resename)
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
      
      var["JUPDIALOG_START"] = resename
      var["JUP_DIALOG"]="?sentconfirmationquestion.xml" 
            
def reseraction(action,var):
    cutil.printVar("reseraction",action,var)
         
    if action == "aftercheckin" : rutil.afterCheckIn(var) 
    
    if action == "signalchange" and var["changeafterfocus"]:
        if var["changefield"] == "serviceperperson" :
          _setAfterPerPerson(var)
          
        if var["changefield"] == "roomservice" :
            if not _okServiceForRoom(var) : return
            _setAfterServiceName(var)
            _setAfterPriceList(var)

        if var["changefield"] == "roompricelist" : 
            if not _okServiceForRoom(var) or not _okPriceList(var) : return
            _setAfterPriceList(var)
        
        if var["changefield"] == "advance_percent" :
	    if not cutil.validatePercent(var,"advance_percent") : return 
            _calculateAdvanceAmount(var,var["advance_total"])	  
            
    
    if action=="before" :
        rbefore.setvarBefore(var)
        if not _newRese(var) :          
          cutil.hideButton(var,"checkin",False)
          util.enableCust(var,CUST,False)
          _setAfterPriceList(var)
          PA = reseparam.RESPARAM(rutil.getReseName(var))
          PA.setParam(var)
          PA.copyParam(var)

        else :  
          cutil.hideButton(var,"detailreservation",True)
          _setPriceAndService(var)
          _setAfterPriceList(var)
          _setAfterServiceName(var)
          _setAfterPerPerson(var)
          _createListOfDays(var,True)
          confirm.createC(var).setDefaToVar()
          reseparam.RESPARAM().copyParam(var)  

# --------------------
# customer
# --------------------
    if action == "acceptdetails" and (var["JUPDIALOG_BUTTON"] == "accept" or var["JUPDIALOG_BUTTON"] == "acceptask"):
        xml = var["JUPDIALOG_RES"]
        util.xmlToVar(var,xml,util.getCustFieldIdAll(),CUST)
        cutil.setCopy(var,util.getCustFieldIdAll(),None,CUST)
        if not _newRese(var) :          
          name = var[CUST+"name"]
          resename = rutil.getReseName(var)          
          util.RESFORM(var).changeCustName(resename,name)
        
    if action=="custdetails" :
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        if _newRese(var) : util.customerDetailsActive(var,CUST)
        else : util.showCustomerDetailstoActive(var,var[CUST+"name"])

# --------------------
# -- add reservation
# --------------------

    if action == "checkaval" :
        if not _okServiceForRoom(var) or not _okPriceList(var) : return
        _checkRese(var)
        reseparam.RESPARAM().copyParam(var)


# ------------------
# reservation
# ------------------

    if action == "askforreservation" :

      if not _checkCurrentRese(var) : return
      PA = reseparam.RESPARAM()
      diff = PA.diffP(var)
      if diff != None :
	xml = PA.diffAsXML(var,diff)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="?reserchange.xml"
        var["JAFTERDIALOG_ACTION"] = "continuereservation"
	return

      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if (action == "makereservation" and var["JYESANSWER"]) or (action == "continuereservation" and var["JUPDIALOG_BUTTON"] == "accept") :
      TRAN = MAKERESE(var)
      TRAN.doTrans()


# -----------------------
# additional reservation
# -----------------------
    if action == "morereservation" :
        l = var["JLIST_MAP"][RESLIST]
        xml = xmlutil.toXML({},l)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="?searchrooms.xml" 
        var["JAFTERDIALOG_ACTION"] = "morereservationaccept" 
      
    if action == "morereservationaccept" and var["JUPDIALOG_BUTTON"] == "toresrese" :
        arese =  var["resename"]
        var["JUPDIALOG_START"] = var["JUPDIALOG_RES"]        
        rbefore.setvarBefore(var)
        # restore reservation name
        var["resename"] = arese
        _setAfterPriceList(var)
        _checkRese(var,False)

# ------------------------
# modify detail reservation
# ------------------------
    if action == "detailreservationaccept" and var["JUPDIALOG_BUTTON"] == "accept" :
        xml = var["JUPDIALOG_RES"]
        (rmap,li) = xmlutil.toMapFiltrDialL(xml,var["J_DIALOGNAME"],RESLIST)
        RELIST.initsum()
        for l in li :
          RELIST.addsum(l)
          RELIST.removePricesFromMap(l)
          l["avail"] = True
        RELIST.tofooter(var)  
          
        cutil.setJMapList(var,RESLIST,li)

    if action == "detailreservation"  :
        l = var["JLIST_MAP"][RESLIST]
        xml = xmlutil.toXML({},l)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="?modifreservation.xml" 
        var["JAFTERDIALOG_ACTION"] = "detailreservationaccept" 


def mailquestion(action,var) :
   
   cutil.printVar("mail question",action,var)
   
   if action == "before" :
     var["n_resename"] = var["JUPDIALOG_START"]
     var["n_info"] = M("htmlconfirmationquestion")
     cutil.setCopy(var,["n_resename","n_info"])
     
   if action == "sendmail" :
     var["JCLOSE_DIALOG"] = True
     resename = var["n_resename"]
     diallaunch.confirmationmail(var,resename)
     
def showchange(action,var) :
   cutil.printVar("show change",action,var) 
   
   if action == "before" :
     xml = var["JUPDIALOG_START"]
     var["content"] = util.transformXML("reservchange.xslt",xml)
     cutil.setCopy(var,"content")
     
   if action == "accept" and var["JYESANSWER"] :
     var["JCLOSE_DIALOG"] = True
     
     
     
     
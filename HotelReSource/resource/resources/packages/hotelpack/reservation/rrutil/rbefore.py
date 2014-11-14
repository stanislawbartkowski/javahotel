import cutil,xmlutil,con

from util import util,rutil

import confirm,advarese,rparam,reseparam

RCUST="cust_"
RLIST="reslist"

def setvarBefore(var,cust=RCUST):
    xml = var["JUPDIALOG_START"]
    (roomname,resday,resdays,nop,roomservice,roompricelist) = rparam.XMLtoresquery(xml)
    if roomservice != None and roompricelist != None : rutil.setServicePriceList(var,roomservice,roompricelist)      
      
    R = util.ROOMLIST(var)
    room = R.findElem(roomname)
    assert room != None
    if nop == None : nop = room.getNoPersons()
    var["name"] = roomname
    var["desc"] = room.getDescription()
    var["nop"] = nop
    var["noextrabeds"] = util.getIntField(room.getNoExtraBeds())
    var["nochildren"] = util.getIntField(room.getNoChildren())
    var["resnop"] = util.getIntField(nop)
    util.setCopy(var,["resename","name","datecol","desc","resdays","noextrabeds","nochildren","resnop","nop","serviceperperson"])
    res = rutil.getReservForDay(var,roomname,resday)
    if len(res) == 0 :
      var["datecol"] = resday
      var["resdays"] = resdays
      var["resename"] = None
      util.setDefaCustomer(var,cust)
      return
  
    assert len(res) == 1
    resname = res[0].getResId()
    assert resname != None
    RFORM = util.RESFORM(var)
    reservation = RFORM.findElem(resname)
    assert reservation != None
    custname = reservation.getCustomerName()
    assert custname != None
    rutil.setReseName(var,resname)
        
    util.setCustData(var,custname,cust)
    
    perperson = True    
    list = []
    S = util.SERVICES(var)
    mindate = None
    for r in reservation.getResDetail() :
                 
         map = { "avail" : True, "resroomname" : r.getRoomName(), "resday" : r.getResDate(), "rlist_pricetotal" : con.BigDecimalToDecimal(r.getPriceTotal()), 
              "rline_nop" : r.getNoP(),"rlist_priceperson" : con.BigDecimalToDecimal(r.getPrice()),
              "rlist_noc" : util.getIntField(r.getNoChildren()), "rlist_pricechildren" : con.BigDecimalToDecimal(r.getPriceChildren()),
              "rlist_noe" : util.getIntField(r.getNoExtraBeds()), "rlist_priceextra" : con.BigDecimalToDecimal(r.getPriceExtraBeds()),
              "rlist_serviceperperson" : r.isPerperson(), 
              "rlist_roomservice" : r.getService(), "rlist_roompricelist" : r.getPriceListName()}

         if not r.isPerperson() : perperson = False
         if mindate == None : mindate = r.getResDate()
         elif mindate > r.getResDate() : mindate = r.getResDate()
         roomservice = cutil.ifnull(roomservice,r.getService())
         roompricelist = cutil.ifnull(roompricelist,r.getPriceListName())

         list.append(map)

    var["serviceperperson"] = perperson
    reseparam.RESPARAM(resname).modifvar(var)    
    rutil.setServicePriceList(var,roomservice,roompricelist)
    var["datecol"] = mindate
    var["resdays"] = len(reservation.getResDetail())
    
    # advance payment

    SUM = advarese.CALCULATE()
    SUM.calc(reservation)
    A = advarese.createAdvaRese(var)
    A.setVal(reservation,SUM.getTotal())
    A.setCopy()
    
    confirm.createC(var).setReservationToVar(reservation)
    
    cutil.setJMapList(var,RLIST,list)
    cutil.setFooter(var,RLIST,"rlist_pricetotal",SUM.getTotal())        

import cutil
import con

from util import util

from com.gwthotel.hotel.pricelist import HotelPriceList
from com.gwthotel.hotel.prices import HotelPriceElem

_PRICE="price"
_PRICECHILDREN="children"
_PRICEEXTRABDEDS="extrabeds"
M=util.MESS()

PRICEOTHERS="pricesothers"
_PRICES="prices"

_PRICELIST="pricelist"

def _createList(var):
    P = util.PRICELIST(var)
    seq = P.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "validfrom" : s.getFromDate(), "validto" :  s.getToDate()} )
       
    var["JLIST_MAP"] = { _PRICELIST : list}

def _duplicatedPriceList(var):    
    P = util.PRICELIST(var)
    seq = P.getList()
    if util.findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = M("DUPLICATEDPRICELISTNAME")
      return True
    return False
    
def _createPriceList(var):
   pr = HotelPriceList()
   util.copyNameDescr(pr,var)
   pr.setFromDate(con.toDate(var["validfrom"]))
   pr.setToDate(con.toDate(var["validto"]))
   return pr

def _createPriceElemListForType(var,hotel):
    P = util.PRICEELEM(var)
    displayname = M("pricelistprice")
    columns = [{"id" :_PRICE,  "displayname" : displayname}]
    if hotel : 
       columns.append({"id" :_PRICECHILDREN, "displayname" : M("pricelistchildren")})
       columns.append({"id" :_PRICEEXTRABDEDS, "displayname" : M("pricelistextraprice")})
    
    S = util.SERVICES(var)
    if hotel: seq = S.getRoomServices()
    else : seq = S.getOtherServices()
    if hotel :
      displayname = lambda(s) : s.getName() + M("serviceperperson") if S.findElem(s.getName()).isPerperson() else  s.getName() + M("serviceperroom")
      lines = util.createSeq(seq,False,displayname)
    else : lines = util.createSeq(seq)

    defmap = {"lines" : lines, "columns" : columns}

    prices = []
    if var["name"]:
        prices = P.getPricesForPriceList(var["name"])        
        
    for s in seq :
        id = s.getName()
        for p in prices :
            service = p.getService()
            if id == service :
              defmap[id] = [{"id" : _PRICE, "val" : p.getPrice()}]
              if hotel : 
                defmap[id].append({"id" : _PRICECHILDREN, "val" : p.getChildrenPrice()})
                defmap[id].append({"id" : _PRICEEXTRABDEDS, "val" : p.getExtrabedsPrice()})
    return defmap

def _createPriceElemList(var): 
    var["JCHECK_MAP"] = {_PRICES : _createPriceElemListForType(var,True), PRICEOTHERS : _createPriceElemListForType(var,False)}     
    
def _constructPriceElemList(var):
    if var["JCHECK_MAP"].has_key(PRICEOTHERS) :
      values = cutil.concatDict(var["JCHECK_MAP"][_PRICES],var["JCHECK_MAP"][PRICEOTHERS])
    else : values = var["JCHECK_MAP"][_PRICES]  
    seq = util.SERVICES(var).getList() 
    a = cutil.createArrayList()
    for s in seq :
        id = s.getName()
        if values.has_key(id) :
            p = HotelPriceElem()
            p.setService(id)
            notnull = False
            for v in values[id] :
                if v["val"] == None : continue
                if v["id"] == _PRICE : p.setPrice(con.toB(v["val"]))
                if v["id"] == _PRICECHILDREN : p.setChildrenPrice(con.toB(v["val"]))
                if v["id"] == _PRICEEXTRABDEDS : p.setExtrabedsPrice(con.toB(v["val"]))
                notnull = True
            if notnull : a.add(p)
    return a        

def _verPrice(err,serv,col,v):
    if v == None : return
    if v.doubleValue() >= 0.001 : return
    err.append({"line" : serv, "col" : col,"errmess" : M("PRICEGREATERTHEN0")})
    
def _notValidPriceElemList(var):
    a = _constructPriceElemList(var)
    err = []
    for p in a :
        serv = p.getService()
        _verPrice(err,serv,_PRICE,p.getPrice())
        _verPrice(err,serv,_PRICECHILDREN,p.getChildrenPrice())
        _verPrice(err,serv,_PRICEEXTRABDEDS,p.getExtrabedsPrice())
    if len(err) == 0 : return False
    var["JCHECK_MAP"] = {_PRICES : {"JERROR" : err}}
    return True
    
def _savePriceElemList(var):
    pricelist = var["name"]
    util.PRICEELEM(var).savePricesForPriceList(pricelist,_constructPriceElemList(var))        

def pricelistaction(action,var) :

  cutil.printVar ("price list action", action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
  
def  elempricelistaction(action,var) :

  cutil.printVar ("elem price action", action,var)
  P = util.PRICELIST(var)
  
  if action == "before" :
      _createPriceElemList(var)
      if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_prices_readonly"] = True
       var["JVALATTR_CHECKLIST_prices_readonly"] = ""       
       return


  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if _duplicatedPriceList(var) : return
      if _notValidPriceElemList(var) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWPRICELIST")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      pr = _createPriceList(var)
      P.addElem(pr)
      _savePriceElemList(var)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      if _notValidPriceElemList(var) : return
      var["JYESNO_MESSAGE"] = M("MODIFYPRICELIST")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      pr = _createPriceList(var)
      P.changeElem(pr)
      _savePriceElemList(var)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
#    getReseForPriceList
      l = util.RESOP(var).getReseForPriceList(var["name"])
      if len(l) > 0 :
         var["JERROR_MESSAGE"] = M("cannotremovepricelist").format(len(l))
         return
      var["JYESNO_MESSAGE"] = M("REMOVEPRICELIST")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      pr = _createPriceList(var)
      P.deleteElem(pr)
      var["JCLOSE_DIALOG"] = True      
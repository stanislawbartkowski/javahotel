from util.util import printvar
from util.util import PRICELIST
from util.util import copyNameDescr
from util.util import findElemInSeq
from util.util import toDate
from util.util import PRICEELEM
from com.gwthotel.hotel.pricelist import HotelPriceList
from util.util import SERVICES
from util.util import createArrayList
from com.gwthotel.hotel.prices import HotelPriceElem
from util.util import toB
from util.util import MESS
from util.util import createSeq
from cutil import concatDict

_PRICE="price"
_PRICECHILDREN="children"
_PRICEEXTRABDEDS="extrabeds"
M=MESS()

PRICEOTHERS="pricesothers"

def _createList(var):
    P = PRICELIST(var)
    seq = P.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "validfrom" : s.getFromDate(), "validto" :  s.getToDate()} )
       
    var["JLIST_MAP"] = { "pricelist" : list}

def _duplicatedPriceList(var):    
    P = PRICELIST(var)
    seq = P.getList()
    if findElemInSeq(var["name"],seq) != None :
      var["JERROR_name"] = M("DUPLICATEDPRICELISTNAME")
      return True
    return False
    
def _createPriceList(var):
   pr = HotelPriceList()
   copyNameDescr(pr,var)
   pr.setFromDate(toDate(var["validfrom"]))
   pr.setToDate(toDate(var["validto"]))
   return pr

def _createPriceElemListForType(var,hotel):
    P = PRICEELEM(var)
    displayname = M("pricelistprice")
    columns = [{"id" :_PRICE,  "displayname" : displayname}]
    if hotel : 
       columns.append({"id" :_PRICECHILDREN, "displayname" : M("pricelistchildren")})
       columns.append({"id" :_PRICEEXTRABDEDS, "displayname" : M("pricelistextraprice")})
    
    if hotel: seq = SERVICES(var).getRoomServices()
    else : seq = SERVICES(var).getOtherServices()
    lines = createSeq(seq)

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
    var["JCHECK_MAP"] = {"prices" : _createPriceElemListForType(var,True), PRICEOTHERS : _createPriceElemListForType(var,False)}     
    
def _constructPriceElemList(var):
    if var["JCHECK_MAP"].has_key(PRICEOTHERS) :
      values = concatDict(var["JCHECK_MAP"]["prices"],var["JCHECK_MAP"][PRICEOTHERS])
    else : values = var["JCHECK_MAP"]["prices"]  
    seq = SERVICES(var).getList() 
    a = createArrayList()
    for s in seq :
        id = s.getName()
        if values.has_key(id) :
            p = HotelPriceElem()
            p.setService(id)
            notnull = False
            for v in values[id] :
                if v["val"] == None : continue
                if v["id"] == _PRICE : p.setPrice(toB(v["val"]))
                if v["id"] == _PRICECHILDREN : p.setChildrenPrice(toB(v["val"]))
                if v["id"] == _PRICEEXTRABDEDS : p.setExtrabedsPrice(toB(v["val"]))
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
    var["JCHECK_MAP"] = {"prices" : {"JERROR" : err}}
    print var["JCHECK_MAP"]
    return True
#    map["JERROR"] = err
    
def _savePriceElemList(var):
    pricelist = var["name"]
    PRICEELEM(var).savePricesForPriceList(pricelist,_constructPriceElemList(var))
        

def pricelistaction(action,var) :

  printvar ("price list action", action,var)
  
  if action == "before" or action == "crud_readlist" :
    _createList(var)
  
def  elempricelistaction(action,var) :

  printvar ("elem price action", action,var)
  P = PRICELIST(var)
  
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
      var["JYESNO_MESSAGE"] = M("REMOVEPRICELIST")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      pr = _createPriceList(var)
      P.deleteElem(pr)
      var["JCLOSE_DIALOG"] = True      
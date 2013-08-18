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

_WEEKENDPRICE="weekend"
_WORKINGPRICE="working"
M=MESS()

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

def _createPriceElemList(var): 
    P = PRICEELEM(var)
    columns = [{"id" :_WEEKENDPRICE,  "displayname" : M("WEEKENDPRICE")} ,
               {"id" :_WORKINGPRICE, "displayname" : M("WORKINGPRICE")}]
    seq = SERVICES(var).getRoomServices()
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
              defmap[id] = [{"id" : _WEEKENDPRICE, "val" : p.getWeekendPrice()},
                            {"id" : _WORKINGPRICE, "val" : p.getWorkingPrice()}]

    var["JCHECK_MAP"] = {"prices" : defmap }     
    
def _constructPriceElemList(var):    
    values = var["JCHECK_MAP"]["prices"]
    seq = SERVICES(var).getRoomServices()
    a = createArrayList()
    for s in seq :
        id = s.getName()
        if values.has_key(id) :
            p = HotelPriceElem()
            p.setService(id)
            notnull = False
            for v in values[id] :
                if v["val"] == None : continue
                if v["id"] == _WEEKENDPRICE : p.setWeekendPrice(toB(v["val"]))
                if v["id"] == _WORKINGPRICE : p.setWorkingPrice(toB(v["val"]))
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
        print p.getService(),p.getWeekendPrice(),p.getWorkingPrice()
        serv = p.getService()
        _verPrice(err,serv,_WEEKENDPRICE,p.getWeekendPrice())
        _verPrice(err,serv,_WORKINGPRICE,p.getWorkingPrice())
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
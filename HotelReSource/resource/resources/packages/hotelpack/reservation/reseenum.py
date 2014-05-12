from util import util

def serviceenum(action,var):
  room = var["JDATELINE_LINE"]
  li = util.getServicesForRoom(var,room)
  var["JLIST_MAP"] = { "roomservice" : util.createEnumFromList(li[0])}
  
def otherserviceenum(action,var):  
  li = util.SERVICES(var).getOtherServices()
  var["JLIST_MAP"] = { "otherservice" : util.createEnumFromList(li)}
      
def pricelistenum(action,var):
  room = var["JDATELINE_LINE"]
  li = util.getServicesForRoom(var,room)
  PR = util.PRICELIST(var)
  f = lambda elem : [elem, PR.findElem(elem).getDescription()]
  var["JLIST_MAP"] = { "roompricelist" : util.createEnumFromList(li[1],f)}

def allpricelistenum(action, var):
  PR = util.PRICELIST(var)
  li = PR.getList()
  var["JLIST_MAP"] = { "allpricelist" : util.createEnumFromList(li)}
  
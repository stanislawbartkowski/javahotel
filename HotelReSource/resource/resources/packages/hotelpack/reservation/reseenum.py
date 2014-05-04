from util.util import printvar
from util.util import getServicesForRoom
from util.util import createEnumFromList
from util.util import PRICELIST
from util.util import SERVICES

def serviceenum(action,var):
#  printvar ("serviceenum", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  var["JLIST_MAP"] = { "roomservice" : createEnumFromList(li[0])}
  
def otherserviceenum(action,var):  
#  printvar ("otherserviceenum", action,var)
  li = SERVICES(var).getOtherServices()
  var["JLIST_MAP"] = { "otherservice" : createEnumFromList(li)}
      
def pricelistenum(action,var):
#  printvar ("pricelistenum,", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  PR = PRICELIST(var)
  f = lambda elem : [elem, PR.findElem(elem).getDescription()]
  var["JLIST_MAP"] = { "roompricelist" : createEnumFromList(li[1],f)}

def allpricelistenum(action, var):
#  printvar ("allpricelistenum,", action,var)
  PR = PRICELIST(var)
  li = PR.getList()
  var["JLIST_MAP"] = { "allpricelist" : createEnumFromList(li)}
  
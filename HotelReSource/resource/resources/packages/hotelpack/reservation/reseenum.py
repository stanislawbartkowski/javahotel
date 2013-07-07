from util.util import printvar
from util.util import getServicesForRoom
from util.util import createEnumFromList
from util.util import PRICELIST

def serviceenum(action,var):
  printvar ("serviceenum", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  f = lambda elem : [elem.getName(), elem.getDescription()]
  var["JLIST_MAP"] = { "roomservice" : createEnumFromList(li[0],f)}
      
def pricelistenum(action,var):
  printvar ("pricelistenum,", action,var)
  room = var["JDATELINE_LINE"]
  li = getServicesForRoom(var,room)
  PR = PRICELIST(var)
  f = lambda elem : [elem, PR.findElem(elem).getDescription()]
  var["JLIST_MAP"] = { "roompricelist" : createEnumFromList(li[1],f)}

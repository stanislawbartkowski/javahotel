from com.gwthotel.hotel.server.service import H
from util.util import MESS
from util.util import printvar

taxList = H.getVatTaxes()

def serviceenum(action,var) :

  printvar ("serviceenum", action,var)
  
  seq = []
  
  list = taxList.getList()
  
  for v in list :
      seq.append({"id" : v.getName(), "name" : v.getDescription()})
        
  var["JLIST_MAP"] = { "vattax" : seq}


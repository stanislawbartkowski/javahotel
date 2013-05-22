from com.gwthotel.hotel.server.service import H
from util.util import MESS
from util.util import printvar
from util.util import getHotelName
from util.util import SERVICES

taxList = H.getVatTaxes

def serviceaction(action,var) :

  printvar ("serviceaction", action,var)
  
  serv = SERVICES(var)
  
  if action == "before" or action == "crudlist" :
       
    seq = serv.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription()} )
       
    var["JLIST_MAP"] = { "services" : list}

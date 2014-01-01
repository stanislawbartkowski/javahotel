from com.gwthotel.hotel.server.service import H
from util.util import MESS
import cutil

taxList = H.getVatTaxes()

def serviceenum(action,var) :

  cutil.printVar ("serviceenum", action,var)
  
  seq = []
  
  list = taxList.getList()
  
  for v in list :
      seq.append({"id" : v.getName(), "name" : v.getDescription()})
        
  var["JLIST_MAP"] = { "vattax" : seq}
  
def paymentmethodenum(action,var) :  

  cutil.printVar ("paymentmethod", action,var)

  seq = [
    { "id" : "CA", "name" : "Cache" } ,
    { "id" : "CC", "name" : "Credit Card" },
    { "id" : "TR", "name" : "Transfer" },
  ]
          
  var["JLIST_MAP"] = { "paymentmethod" : seq}
 
def dictaction(action,var,what) :
    cutil.enumDictAction(action,var,what)

#from com.gwthotel.hotel.server.service import H
import cutil

taxList = cutil.getDict("vat")

def serviceenum(action,var) :

#  cutil.printVar ("serviceenum", action,var)
  
  seq = []
  
  for v in taxList :
      seq.append({"id" : v.getName(), "name" : v.getDescription()})
        
  var["JLIST_MAP"] = { "vattax" : seq}
   
def dictaction(action,var,what) :
    cutil.enumDictAction(action,var,what)

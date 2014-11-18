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
    if what == "countries" :
      seq = var["JLIST_MAP"][action]
      seq.sort(key=lambda d: d["name"])
       
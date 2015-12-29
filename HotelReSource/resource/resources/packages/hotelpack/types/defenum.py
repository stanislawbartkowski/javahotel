import cutil,sec

from util import util

taxList = cutil.getDict("vat")

def serviceenum(action,var) :

  seq = []
  
  for v in taxList :
      seq.append({"id" : v.getName(), "name" : v.getDescription()})
        
#  var["JLIST_MAP"] = { "vattax" : seq}
  cutil.setJMapList(var,"vattax",seq)
   
def dictaction(action,var,what) :
  
    if what == "doctypes" :
      seq = util.getDictOfDocType()
      cutil.setJMapList(var,"doctypes",seq)      
      return
    
    cutil.enumDictAction(action,var,what)
    if what == "countries" :
      seq = var["JLIST_MAP"][action]
      seq.sort(key=lambda d: d["name"])
      
def usersaction(action,var) :

   li = sec.ObjectAdmin(util.getAppId(var)).getListOfPersons()
   seq = []
   
   for l in li : seq.append({"id" : l.getName()})
     
   cutil.setJMapList(var,"users",seq)
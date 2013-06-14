from util.util import printvar
from util.util import ROOMLIST

def __getList(var):
    R = ROOMLIST(var)
    seq = R.getList()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "noperson" : s.getNoPersons()} )
    return list        
       

def reservationaction(action,var):
    printvar("reservation",action,var)
    
    if action == "before" :
      list = __getList(var)
      var["JDATELINE_MAP"] = {"reservation" : list}    
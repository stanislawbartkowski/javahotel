from cutil import printVar
from util.util import SERVICES
from util.util import duplicateService
from util.util import newOtherService
from util.util import copyNameDescr
from util.util import MESS
from util.util import getVatName

M = MESS()

def _createList(var):
    serv = SERVICES(var)
    seq = serv.getOtherServices()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "vat" : s.getAttr("vat"), 
                    "vatname" : getVatName(s.getAttr("vat"))} )
       
    var["JLIST_MAP"] = { "services" : list}

def _createService(var):
   se = newOtherService(var)
   copyNameDescr(se,var)
   se.setAttr("vat",var["vat"])
   return se    

def serviceaction(action,var) :
    printVar("other services",action,var)
    
    if action == "before" or action == "crud_readlist" :  
       _createList(var)
       
def elemserviceaction(action,var):
    
  printVar("elem service action",action,var)    
    
  serv = SERVICES(var)
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if duplicateService(var) : return          
      var["JYESNO_MESSAGE"] = M("ADDNEWSERVICEASK");
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.addElem(se)
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.changeElem(se)
      var["JCLOSE_DIALOG"] = True

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("DELETESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.deleteElem(se)
      var["JCLOSE_DIALOG"] = True      
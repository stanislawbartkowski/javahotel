import cutil

from util import util

M = util.MESS()
D = util.HOTELDEFADATA()

def _createList(var):
    serv = util.SERVICES(var)
    seq = serv.getOtherServices()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription(), 
                    "vat" : s.getAttr("vat"), 
                    "vatname" : util.getVatName(s.getAttr("vat"))} )
       
    var["JLIST_MAP"] = { "services" : list}

def _createService(var):
   se = util.newOtherService(var)
   util.copyNameDescr(se,var)
   se.setAttr("vat",var["vat"])
   D.putDataH(15,var["vat"])
   return se    

def serviceaction(action,var) :
    cutil.printVar("other services",action,var)
    
    if action == "before" or action == "crud_readlist" :  
       _createList(var)
       
def elemserviceaction(action,var):
    
  cutil.printVar("elem service action",action,var)    
    
  serv = util.SERVICES(var)

  if action == "before" and var["JCRUD_DIALOG"] == "crud_add" :
    var["vat"] = D.getDataH(15)
    cutil.setCopy(var,"vat")
    
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if util.duplicateService(var) : return          
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
      l1 = util.RESOP(var).getReseForService(var["name"])
      l2 = util.listOfPriceListForService(var,var["name"])
      if len(l1) > 0 or len(l2) > 0 :
         var["JERROR_MESSAGE"] = M("cannotremoveotherservice").format(len(l1),len(l2))
         return

      var["JYESNO_MESSAGE"] = M("DELETESERVICEASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      se = _createService(var)
      serv.deleteElem(se)
      var["JCLOSE_DIALOG"] = True      
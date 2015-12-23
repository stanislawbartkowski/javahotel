from com.gwthotel.hotel.server.service import H
import sec,cutil
from util import util

PERM="perm"
M = util.MESS()

class HotelAdmin(sec.ObjectAdmin) :

   def __init__(self,var,hotel) :
     sec.ObjectAdmin.__init__(self,util.getAppId(var))
     self.var = var
     self.hotel = hotel

   def readList(self):
    
     if self.hotel : seq = self.getListOfObjects()
     else : seq = self.getListOfPersons()
     list = []
    
     for s in seq :
       ma = {"name" : s.getName(), "descr" : s.getDescription()}
#       if hotel : ma["clearhotel"] = M("clearhotel")
       list.append(ma)

     if self.hotel : cutil.setJMapList(self.var,"hotels",list)
     else : cutil.setJMapList(self.var,"users",list)
    
   def preparePermissionForHotel(self):
     if self.hotel : seq = self.getListOfPersons()
     else : seq = self.getListOfObjects()
     print seq
     if len(seq) == 0 : return
     print len(seq)
 #    rolesdef = H.getHotelRoles().getList()
     rolesdef = cutil.getDict("roles")
     map = {"lines" : util.createSeq(seq,True), "columns" : util.createSeq(rolesdef,False)}
#     self.var["JCHECK_MAP"] = { "perm" : map}
     cutil.setJCheckList(self.var,PERM,map)

   def __existHotelPerson(self,pname):
     if self.hotel : seq = self.getListOfObjects()
     else : seq = self.getListOfPersons()
     return util.findElemInSeq(pname,seq) != None    
     
   def duplicatedHotelPersonName(self):
     name = self.var["name"]
     if self.__existHotelPerson(name) :
       if self.hotel : self.var["JERROR_name"] = M("DUPLICATEDHOTELNAME")
       else : self.var["JERROR_name"] = M("DUPLICATEDPERSONNAME")
       return True
     return False
     
   def createPersonObject(self) :
     name = self.var["name"]
     descr = self.var["descr"]
     return sec.createObjectOrPerson(self.hotel,name,descr)
   
   def addOrModif(self,o,roles) :
     if self.hotel : self.addOrModifObject(o,roles)
     else : self.addOrModifPerson(o,roles)     
     
   def prepareRoles(self):   
    l = cutil.createArrayList()
    if not self.var.has_key("JCHECK_MAP") : return l         
    map = self.var["JCHECK_MAP"][PERM]
    
    for row in map.keys() :
        ho = sec.createObjectOrPerson(not self.hotel,row,None)
        role = sec.createObjectRoles(ho)
        seq = map[row]
        for r in seq :
            name = r["id"]
            if r["val"] : role.getRoles().add(name)
        l.add(role)
    return l     
  
   def getValuesForPermission(self):
     if not self.var.has_key("JCHECK_MAP") : return
     name = self.var["name"]
     if name == "" : return
     if self.hotel : roles = self.getListOfRolesForObject(name)
     else : roles = self.getListOfRolesForPerson(name)
     map = self.var["JCHECK_MAP"][PERM]
     for r in roles :
        rowname = r.getObject().getName()
        seq = []
        for s in r.getRoles() :
            seq.append({"id" : s, "val" : True})
        map[rowname] = seq
        
   def removeObjectOrPerson(self,name) :
     if self.hotel : self.removeObject(name)
     else : self.removePerson(name)

     
# -------------------              


def hotelaction(action,var,hotel) :

  cutil.printVar("hotelaction", action,var)
  adminI = HotelAdmin(var,hotel)
  
  if action == "before" or action == "crud_readlist" :
      #__readList(adminI,var,True)
      adminI.readList() 
      
  if action == "clearhotel" :
      var["JYESNO_MESSAGE"] = M("REMOVEALLDATAFROMHOTEL")
      var["JMESSAGE_TITLE"] = M("REMOVECONFIRMATION")  
      var["JAFTERDIALOG_ACTION"] = "clearhotelafter"
      return
   
  if action == "clearhotelafter" and var["JYESANSWER"] :
      var["JYESNO_MESSAGE"] = M("REMOVECONFIRMATION") + M("AREYOUSURE")
      var["JMESSAGE_TITLE"] = M("REMOVECONFIRMATION")  
      var["JAFTERDIALOG_ACTION"] = "clearhotelexecute"
      return

  if action == "clearhotelexecute" and var["JYESANSWER"] :
      clearHotel(var,var["name"]) 
      
  if action == "changepassword" :
      var['JUP_DIALOG'] = 'admin/changepassword.xml'     
      
def hotelelemaction(action,var,hotel):

  cutil.printVar("hotelelemaction",action,var)  
  adminI = HotelAdmin(var,hotel)
      
  if action == "before" :
#    __preparePermissionForHotel(adminI,var,True)
    adminI.preparePermissionForHotel()
    if var["JCRUD_DIALOG"] != "crud_add" :
       adminI.getValuesForPermission()
    if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_perm_readonly"] = True  
       var["JVALATTR_CHECKLIST_perm_readonly"] = ""       
  
 
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if adminI.duplicatedHotelPersonName() : return
      var["JYESNO_MESSAGE"] = M("ADDNEWHOTELASK");
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      if adminI.duplicatedHotelPersonName() : return
      ho = adminI.createPersonObject()
      adminI.addOrModif(ho,adminI.prepareRoles())
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change" and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGEHOTELASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      ho = adminI.createPersonObject()
      adminI.addOrModif(ho,adminI.prepareRoles())
      var["JCLOSE_DIALOG"] = True      

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVEHOTELASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      name = var["name"]
      adminI.removeObjectOrPerson(name)
      var["JCLOSE_DIALOG"] = True      


def useraction(action,var) :

  printvar ("useraction", action, var)
  adminI = HotelAdmin(var)
    
  if action == "before" or action == "crud_readlist" :
#      __readList(adminI,var,False)
    adminI.readList(False)
      
  if action == "changepassword" :
      var['JUP_DIALOG'] = 'admin/changepassword.xml'
    

def userelemaction(action,var) :

  cutil.printVar ("userelemaction", action,var)
  adminI = HotelAdmin(var)
    
  if action == "before" :
    __preparePermissionForHotel(adminI,var,False)
    if var["JCRUD_DIALOG"] != "crud_add" :
       __getValuesForPermission(adminI,var,False)
    if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_perm_readonly"] = True
       var["JVALATTR_CHECKLIST_perm_readonly"] = ""       
       return

  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(adminI,var,False) : return
      var["JYESNO_MESSAGE"] = M("ADDNEPERSONASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(adminI,var,False) : return
      pe = __createHotelPerson(var,False)
      adminI.addOrModifPerson(pe,__prepareValuesForPermission(var,False))
      var["JCLOSE_DIALOG"] = True
      
  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGEPERSONASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      pe = __createHotelPerson(var,False)
      adminI.addOrModifPerson(pe,__prepareValuesForPermission(var,False))
      var["JCLOSE_DIALOG"] = True      
      
  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVEPERSONASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      name = var["name"]
      adminI.removePerson(name)
      var["JCLOSE_DIALOG"] = True      
  
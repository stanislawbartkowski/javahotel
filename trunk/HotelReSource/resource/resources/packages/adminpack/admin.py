from com.gwthotel.hotel.server.service import H
from com.gwthotel.admin import Hotel
from com.gwthotel.admin import Person
from com.gwthotel.admin import HotelRoles
from util.util import MESS
from util.util import printvar
from util.util import createArrayList
from util.util import findElemInSeq
from util.util import createSeq
from util.util import HotelAdmin
from util.util import clearHotel

M = MESS()

def __existHotelPerson(adminI,pname,hotel):
    if hotel : seq = adminI.getListOfHotels()
    else : seq = adminI.getListOfPersons()
    return findElemInSeq(pname,seq) != None    
    
def __duplicatedHotelPersonName(adminI,var,hotel): 
    name = var["name"]
    if __existHotelPerson(adminI,name,hotel) :
      if hotel : var["JERROR_name"] = M("DUPLICATEDHOTELNAME")
      else : var["JERROR_name"] = M("DUPLICATEDPERSONNAME")
      return True
    return False
        
def __createHotelPerson(var,hotel):
    name = var["name"]
    descr = var["descr"]
    if hotel : ho = Hotel()
    else : ho = Person()
    ho.setName(name)
    ho.setDescription(descr)
    return ho            

def __readList(adminI,var,hotel):
    
    if hotel : seq = adminI.getListOfHotels()
    else : seq = adminI.getListOfPersons()
    list = []
    
    for s in seq :
       ma = {"name" : s.getName(), "descr" : s.getDescription()}
       if hotel : ma["clearhotel"] = M("clearhotel")
       list.append(ma)
       
    map={}   
    if hotel : map["hotels"] = list
    else : map["users"] = list
    var["JLIST_MAP"] = map
    
    
def __preparePermissionForHotel(adminI,var, hotel):
    if hotel : seq = adminI.getListOfPersons()
    else : seq = adminI.getListOfHotels()
    if len(seq) == 0 : return
    rolesdef = H.getHotelRoles().getList()
    map = {"lines" : createSeq(seq,True), "columns" : createSeq(rolesdef,False)}
    var["JCHECK_MAP"] = { "perm" : map}
    
def __getValuesForPermission(adminI,var,hotel):
    if not var.has_key("JCHECK_MAP") : return
    name = var["name"]
    if name == "" : return
    if hotel : roles = adminI.getListOfRolesForHotel(name)
    else : roles = adminI.getListOfRolesForPerson(name)
    map = var["JCHECK_MAP"]["perm"]
    for r in roles :
        rowname = r.getObject().getName()
        seq = []
        for s in r.getRoles() :
            seq.append({"id" : s, "val" : True})
        map[rowname] = seq
      
def __prepareValuesForPermission(var,hotel):   
    l = createArrayList()
    if not var.has_key("JCHECK_MAP") : return l         
    map = var["JCHECK_MAP"]["perm"]
    
    for row in map.keys() :
        if not hotel : ho = Hotel()
        else : ho = Person()
        ho.setName(row)
        role = HotelRoles(ho)
        seq = map[row]
        for r in seq :
            name = r["id"]
            if r["val"] : role.getRoles().add(name)
        l.add(role)
    return l            

def hotelaction(action,var) :

  printvar("hotelaction", action,var)
  adminI = HotelAdmin(var)
  
  if action == "before" or action == "crud_readlist" :
      __readList(adminI,var,True)
      
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
      
def hotelelemaction(action,var):

  printvar("hotelelemaction",action,var)  
  adminI = HotelAdmin(var)
      
  if action == "before" :
    __preparePermissionForHotel(adminI,var,True)
    if var["JCRUD_DIALOG"] != "crud_add" :
       __getValuesForPermission(adminI,var,True)
    if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_perm_readonly"] = True  
       var["JVALATTR_CHECKLIST_perm_readonly"] = ""       
  
 
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(adminI,var,True) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWHOTELASK");
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(adminI,var,True) : return
      ho = __createHotelPerson(var,True)
      adminI.addOrModifHotel(ho,__prepareValuesForPermission(var,True))
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change" and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("CHANGEHOTELASK")
      var["JMESSAGE_TITLE"] = ""  
      return

  if action == "crud_change"  and var["JCRUD_AFTERCONF"] :
      ho = __createHotelPerson(var,True)
      adminI.addOrModifHotel(ho,__prepareValuesForPermission(var,True))
      var["JCLOSE_DIALOG"] = True      

  if action == "crud_remove"  and not var["JCRUD_AFTERCONF"] :
      var["JYESNO_MESSAGE"] = M("REMOVEHOTELASK")
      var["JMESSAGE_TITLE"] = ""  
      return
  
  if action == "crud_remove"  and var["JCRUD_AFTERCONF"] :
      name = var["name"]
      adminI.removeHotel(name)
      var["JCLOSE_DIALOG"] = True      


def useraction(action,var) :

  printvar ("useraction", action, var)
  adminI = HotelAdmin(var)
    
  if action == "before" or action == "crud_readlist" :
      __readList(adminI,var,False)
      
  if action == "changepassword" :
      var['JUP_DIALOG'] = 'admin/changepassword.xml'
    

def userelemaction(action,var) :

  printvar ("userelemaction", action,var)
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
  
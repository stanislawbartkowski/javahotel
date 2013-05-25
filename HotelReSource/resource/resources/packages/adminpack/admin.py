from com.gwthotel.hotel.server.service import H
from com.gwthotel.admin import Hotel
from com.gwthotel.admin import Person
from com.gwthotel.admin import HotelRoles
from util.util import MESS
from util.util import printvar
from util.util import createArrayList
from util.util import findElemInSeq

adminI = H.getHotelAdmin()
M = MESS()

def __existHotelPerson(pname,hotel):
    if hotel : seq = adminI.getListOfHotels()
    else : seq = adminI.getListOfPersons()
    return findElemInSeq(pname,seq) != None    
    
def __duplicatedHotelPersonName(var,hotel): 
    name = var["name"]
    if __existHotelPerson(name,hotel) :
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

def __readList(var,hotel):
    
    if hotel : seq = adminI.getListOfHotels()
    else : seq = adminI.getListOfPersons()
    list = []
    
    for s in seq : 
       list.append({"name" : s.getName(), "descr" : s.getDescription()} )
       
    map={}   
    if hotel : map["hotels"] = list
    else : map["users"] = list
    var["JLIST_MAP"] = map
    
def __createSeq(list,addName):    
    seq = []
    for s in list :
        m = {}
        m["id"] = s.getName()
        if addName :  m["displayname"] = s.getName() + " " + s.getDescription()
        else : m["displayname"] = s.getDescription()
        seq.append(m)
    return seq    
    
def __preparePermissionForHotel(var, hotel):
    if hotel : seq = adminI.getListOfPersons()
    else : seq = adminI.getListOfHotels()
    if len(seq) == 0 : return
    rolesdef = H.getHotelRoles().getList()
    map = {"lines" : __createSeq(seq,True), "columns" : __createSeq(rolesdef,False)}
    var["JCHECK_MAP"] = { "perm" : map}
    
def __getValuesForPermission(var,hotel):
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

  if action == "before" or action == "crud_readlist" :
      __readList(var,True)
            
      
def hotelelemaction(action,var):

  printvar("hotelelemaction",action,var)  
      
  if action == "before" :
    __preparePermissionForHotel(var,True)
    if var["JCRUD_DIALOG"] != "crud_add" :
       __getValuesForPermission(var,True)
    if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_perm_readonly"] = True  
       var["JVALATTR_CHECKLIST_perm_readonly"] = ""       
  
 
  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(var,True) : return
      var["JYESNO_MESSAGE"] = M("ADDNEWHOTELASK");
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(var,True) : return
      ho = __createHotelPerson(var,True)
      adminI.addOrModifHotel(ho,__prepareValuesForPermission(var,True))
      var["JCLOSE_DIALOG"] = True
      return

  if action == "crud_change"  and not var["JCRUD_AFTERCONF"] :
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
    
  if action == "before" or action == "crud_readlist" :
      __readList(var,False)
      
  if action == "changepassword" :
      var['JUP_DIALOG'] = 'admin/changepassword.xml'
    

def userelemaction(action,var) :

  printvar ("userelemaction", action,var)
    
  if action == "before" :
    __preparePermissionForHotel(var,False)
    if var["JCRUD_DIALOG"] != "crud_add" :
       __getValuesForPermission(var,False)
    if var["JCRUD_DIALOG"] == "crud_remove" :
       var["JSETATTR_CHECKLIST_perm_readonly"] = True
       var["JVALATTR_CHECKLIST_perm_readonly"] = ""       
       return

  if action == "crud_add"  and not var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(var,False) : return
      var["JYESNO_MESSAGE"] = M("ADDNEPERSONASK")
      var["JMESSAGE_TITLE"] = ""  
      return
      
  if action == "crud_add"  and var["JCRUD_AFTERCONF"] :
      if __duplicatedHotelPersonName(var,False) : return
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
  
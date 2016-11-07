import cutil
import sec

def __getI(var):
#    return instanceI.getInstance("AppInstanceTest","user")
    return sec.ICACHE.getTestInstance("user") 

def __createHotelPerson(var,hotel):
    name = var["name"]
    descr = var["descr"]
#    if hotel : ho = Hotel()
#    else : ho = Person()
#    ho.setName(name)
#    ho.setDescription(descr)
#    return ho
    return sec.createObjectOrPerson(hotel,name,descr)            


def dialogaction(action,var) :
  cutil.printVar("dialog1",action,var)  
    
  if action == "modif" :
      print "aaa"
      pe = __createHotelPerson(var,False)
      pe.setName("person")
      A = sec.ObjectAdmin(__getI(var))
      A.addOrModifPerson(pe,cutil.createArrayList())
      ho = __createHotelPerson(var,True)
      perm = cutil.createArrayList()
#      role = HotelRoles(pe)
      role = sec.createObjectRoles(pe)
      role.getRoles().add("man")
      perm.add(role)
      A.addOrModifObject(ho,perm)
from com.gwthotel.admintest.guice import ServiceInjector
from com.gwthotel.admin import Hotel
from com.gwthotel.admin import Person
from com.gwthotel.admin import HotelRoles
from java.util import ArrayList

adminI = ServiceInjector.constructHotelAdmin()
instanceI = ServiceInjector.getInstanceHotel()


def __getI(var):
    return instanceI.getInstance("AppInstanceTest")

def __createHotelPerson(var,hotel):
    name = var["name"]
    descr = var["descr"]
    if hotel : ho = Hotel()
    else : ho = Person()
    ho.setName(name)
    ho.setDescription(descr)
    return ho            


def dialogaction(action,var) :
  print "test",action
  for k in var.keys() : 
    print k, var[k]
    
  if action == "modif" :
      pe = Person()
      pe.setName("person");
      adminI.addOrModifPerson(__getI(var),pe,ArrayList())
      ho = __createHotelPerson(var,True)
      perm = ArrayList()
      role = HotelRoles(pe)
      role.getRoles().add("man")
      perm.add(role)
      adminI.addOrModifHotel(__getI(var),ho,perm)
      
      

      
  
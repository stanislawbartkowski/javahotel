from com.jythonui.server.holder import Holder
from com.jythonui.server import ISharedConsts
from com.jython.serversecurity import Person
from com.jython.serversecurity import OObject
from com.jython.serversecurity import OObjectRoles

class InstanceCache :
    
    def __init__(self):
        self.i = Holder.getInstanceCache()
        
    def invalidateCache(self):
        self.i.invalidateCache()    
        
    def getInstance(self,iname,username):
        return self.i.getInstance(iname,username)
    
    def getTestInstance(self,username):
        return self.getInstance(ISharedConsts.INSTANCETEST,username)
        
ICACHE=InstanceCache()                

class ObjectAdmin :
    
    def __init__(self,app) :
      self.adminI = Holder.getAdmin()
      self.app = app
      
    def getListOfPersons(self) :
      return self.adminI.getListOfPersons(self.app)

    def getListOfObjects(self) :
      return self.adminI.getListOfObjects(self.app)

    def getListOfRolesForPerson(self,person):
      return self.adminI.getListOfRolesForPerson(self.app,person)
  
    def getListOfRolesForObject(self,object):
      return self.adminI.getListOfRolesForObject(self.app,object)
  
    def addOrModifObject(self,object,roles):
      self.adminI.addOrModifObject(self.app,object,roles)
      ICACHE.invalidateCache()   

    def addOrModifPerson(self,person,roles):
      self.adminI.addOrModifPerson(self.app,person,roles)  

    def changePasswordForPerson(self,person,password):
      self.adminI.changePasswordForPerson(self.app,person,password)  

    def validatePasswordForPerson(self,person,password):
        return self.adminI.validatePasswordForPerson(self.app,person,password)        

    def getPassword(self,person):
        return self.adminI.getPassword(self.app,person)

    def clearAll(self):
        self.adminI.clearAll(self.app)

    def removePerson(self,person):
        self.adminI.removePerson(self.app,person)
        
    def removeObject(self,object):
        self.adminI.removeObject(self.app,object)
        ICACHE.invalidateCache()         

def createObjectOrPerson(object,name,desc):
    if object : m = OObject()
    else : m = Person()
    m.setName(name)
    m.setDescription(desc)
    return m  

def createObjectRoles(o):
    return OObjectRoles(o)
        
class PersonAdmin(ObjectAdmin) :

    def __init__(self,app) :
      ObjectAdmin.__init__(self,app)  
      self.adminI = Holder.getAdminPerson()
         
    def addOrModifPerson(self,person,roles):
      ro = []  
      o = OObjectRoles()
      ro.append(o)
      for r in roles :
          o.getRoles().add(r)
      ObjectAdmin.addOrModifPerson(self, person, ro)
      
    def getListOfRolesForPerson(self,person):
      rol = ObjectAdmin.getListOfRolesForPerson(self,person)
      ro = rol[0]
      roles = []
      for r in ro.getRoles() : roles.append(r)
      return roles  

      

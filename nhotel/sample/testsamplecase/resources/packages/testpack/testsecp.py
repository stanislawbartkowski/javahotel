import cutil
import sec

P = sec.PersonAdmin(sec.ICACHE.getTestInstance("person"))

def dialogaction(action,var):
    cutil.printVar("testsec",action,var)
    
    if action == "setperson" :
        pe = sec.createObjectOrPerson(False,"user","I am the best")
        P.addOrModifPerson(pe,["role1","role2"])
        
    if action == "checkperson" :
        li = P.getListOfPersons()
        assert len(li) == 1
        for l in li :
            print l.getName()
        rol = P.getListOfRolesForPerson("user")
        print rol
        assert len(rol) == 2

    if action == "setpassword" :
        P.changePasswordForPerson("user","secret")
    
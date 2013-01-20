from guice import ServiceInjector

def dialogaction(action,var) :
  print "testobject",action
  op = ServiceInjector.constructPersonOp();
  
  if action == "checkifexist" :
    p = op.findPersonByNumb("aaa");
    var["OK"] = False;
    if p != None : return
    p = op.findPersonByNumb("P0002");
    if p == None : return
    var["OK"] = True
    return
  
  pname = var["pname"]
  pnumb = var["pnumber"]
  print pname," ",pnumb
  
  if action == "add" :
    p = op.construct()
    p.setPersonNumb(pnumb)
    p.setPersonName(pname)
    op.savePerson(p)
  
  if action == "change" :
    pid = var["id"]
    print "id=",pid
    p = op.construct()
    p.setPersonNumb(pnumb)
    p.setPersonName(pname)
    p.setId(pid);
    op.changePerson(p)
    
  if action=="readall" :
    seq = op.getAllPersons()
    list = []
    
    for s in seq : 
       print s.id
       elem = {}
       elem["id"] = s.id
       elem["pnumber"] = s.getPersonNumb()
       elem["pname"] = s.getPersonName()
       list.append(elem)
       
    map={}   
    map["list"] = list
    var["JLIST_MAP"] = map  
       
       
       
  
from com.jython.ui.server.guice import ServiceInjector

def dialogaction(action,var) :

  print "list",action
  for k in var.keys() : 
    print k
    print var[k]
    
  op = ServiceInjector.constructPersonOp()
          
  if action == "before" or action == "crud_readlist" :
    seq = op.getAllPersons()
    list = []
    
    for s in seq : 
#       print s.id
       elem = {}
       elem["key"] = s.id
       elem["pnumber"] = s.getPersonNumb()
       elem["pname"] = s.getPersonName()
       list.append(elem)
       
    map={}   
    map["list"] = list
    var["JLIST_MAP"] = map  
    return
      
  pname = var["pname"]
  pnumb = var["pnumber"]
  print pname," ",pnumb
    
  if action == "crud_add"  :
    pname = var["pname"]
    pnumb = var["pnumber"]
    print pname," ",pnumb
    if action == "crud_add" :
       p = op.findPersonByNumb(pnumb)
       if p != None :
         var["JERROR_pnumber"] = "Duplicated number, person number should be unique"
         return  
       if not var["JCRUD_AFTERCONF"] :
         var["JYESNO_MESSAGE"] = "Are you ready to add new elem ?"
         var["JMESSAGE_TITLE"] = "Ask for something"
         print "var kom ",var["JYESNO_MESSAGE"]
         return
       p = op.construct()
       p.setPersonNumb(pnumb)
       p.setPersonName(pname)
       op.savePerson(p)
       var["JCLOSE_DIALOG"] = True
       return

  key = var["key"]      
  if action == "crud_remove" or action == "crud_change" :
       p = op.construct()
       p.setId(key)
       p.setPersonNumb(pnumb)
       p.setPersonName(pname)
       if action == "crud_remove" : op.removePerson(p)
       else : op.changePerson(p)
       var["JCLOSE_DIALOG"] = True
      
def inaction(action,var) :
   print "inaction",var

    

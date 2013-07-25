from com.jython.ui.server.guice import ServiceInjector

def __create_list(op, var) :
    seq = op.getAllPersons()
    list = []
    
    for s in seq : 
       elem = {}
       elem["key"] = s.id
       elem["pnumber"] = s.getPersonNumb()
       elem["pname"] = s.getPersonName()
       list.append(elem)
       
    map={}   
    map["list"] = list
    var["JLIST_MAP"] = map
    
    
def __create_listda(dop,var):    
    list = []       
    seq = dop.getList()
    for r in seq :
        elem = { "id" : r.getId(), "date1" : r.getD1(), "date2" : r.getD2()}
        list.append(elem)
    var["JLIST_MAP"]["listda"] = list

def editlistaction(action,var):
  print "=======editlistaction=============",action
  for k in var.keys() : 
    print k, "=", var[k]
    
  op = ServiceInjector.constructPersonOp()
  dOp = ServiceInjector.constructDateRecordOp()
          
  if action == "before" :
    __create_list(op,var)
    var["JLIST_EDIT_list_pname"] = ""
    var["JLIST_EDIT_list_pnumber"] = ""
#    var["JLIST_EDIT_list_MODE"] = "NORMALMODE" 
    var["JLIST_EDIT_list_MODE"] = "CHANGEMODE" 
#    var["JLIST_EDIT_list_MODE"] = "ADDCHANGEDELETEMODE" 

    __create_listda(dOp,var)
    var["JLIST_EDIT_listda_date1"] = ""
    var["JLIST_EDIT_listda_date2"] = ""
    var["JLIST_EDIT_listda_MODE"] = "CHANGEMODE" 
    return

  if var["JLIST_NAME"] == "list" :
      
    if action == "editlistrowaction"  :
        var["JYESNO_MESSAGE"] = "Are you ready to add new elem ?"
        var["JMESSAGE_TITLE"] = "Ask for something"
        var["JAFTERDIALOG_ACTION"] = "afteryesno"
     
    if action == "afteryesno" and var["JYESANSWER"] :
        var["JLIST_EDIT_ACTIONOK_list"] = True
        var["pname"] = "xxxx"
        var["JCOPY_pname"] = True
        var["pnumber"] = "1"
        var["JCOPY_pnumber"] = True
        
    if action == "columnchangeaction" and var["changefield"] == "pnumber" :
       val = var["pnumber"]
       print "val",val,"!"
       if val == None or val == "" :
         print "==============="    
#        var["JERROR_pnumber"] = "Cannot be empty"


  if var["JLIST_NAME"] == "listda" :      
    if action == "afteryesno" and var["JYESANSWER"] :
      var["JLIST_EDIT_ACTIONOK_listda"] = True
    
    if action == "editlistrowaction" and (var["JLIST_EDIT_ACTION_listda"] == "ADDBEFORE" or var["JLIST_EDIT_ACTION_listda"] == "ADD")  :
        var["JYESNO_MESSAGE"] = "Are you ready to add new empty date elem ?"
        var["JMESSAGE_TITLE"] = "Ask for something"
        var["JAFTERDIALOG_ACTION"] = "afteryesno"

    if action == "editlistrowaction" and var["JLIST_EDIT_ACTION_listda"] == "REMOVE" :
        if var["date1"] == None and var["date2"] == None :
            dOp.removeRecord(var["id"])
            var["JEDIT_ROW_OK_listda"] = True
            return
            
        var["JYESNO_MESSAGE"] = "Do you want to remove this line ?"
        var["JMESSAGE_TITLE"] = "Ask for something"
        var["JAFTERDIALOG_ACTION"] = "removeyesno"

    if action == "afteryesno" and var["JYESANSWER"] :
        var["JLIST_EDIT_ACTIONOK_listda"] = True
        iRec = dOp.construct()
        var["id"] = dOp.addRecord(iRec)
        var["JCOPY_id"] = True

    if action == "removeyesno" and var["JYESANSWER"] :
        var["JLIST_EDIT_ACTIONOK_listda"] = True
        dOp.removeRecord(var["id"])

    if action == "aftereditrow" :
        print "ok"
        var["JEDIT_ROW_OK_listda"] = True

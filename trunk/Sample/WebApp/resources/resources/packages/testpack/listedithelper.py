from cutil import printVar

def doaction(action,var):
    printVar("do action",action,var)
    
    if action == "before" :
        list = []
        var["JLIST_MAP"] = {"listda" : list}
        var["JLIST_EDIT_listda_date1"] = ""
        var["JLIST_EDIT_listda_nameid"] = ""
        var["JLIST_EDIT_listda_MODE"] = "CHANGEMODE" 
        
    if action == "editlistrowaction"  :
        var["JLIST_EDIT_ACTIONOK_listda"] = True

    if action == "helper" :
       var["JUP_DIALOG"] = "selectfromlist.xml"       

def selaction(action,var):
    
    sel = None
    if action == "select1" :
        sel = "AAA"
    if action == "select2" :
        sel = "BBB"
    if action == "select3" :
        sel = "CCC"
    if action == "select4" :
        sel = "DDD"
        
    if sel :
        var['nameid'] = sel
        var['JCOPY_nameid'] = True
        var["JCLOSE_DIALOG"]  = True    
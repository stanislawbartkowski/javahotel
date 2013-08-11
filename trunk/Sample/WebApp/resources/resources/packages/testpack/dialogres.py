from cutil import printVar
from cutil import setCopy
from com.jython.ui.server.guice import ServiceInjector
from cutil import setAddEditMode

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


def doaction(action,var):
    printVar("dialogres",action,var)
    op = ServiceInjector.constructPersonOp()

    if action == "before" :
        __create_list(op,var)
        setAddEditMode(var,"list",["pname,pnumber"])
        
    if action == "editlistrowaction"  :
        var["JLIST_EDIT_ACTIONOK_list"] = True

    
    if action == "selectvar" :
        var["JUP_DIALOG"] = "resdialog.xml"
        var['JAFTERDIALOG_ACTION'] = "selectrowvalue"
        
    if action == "selectrowvalue" :
        setCopy(var,["key","pname","pnumber"])
        var["pname"] = var["JUPDIALOG_BUTTON"]
        var["pnumber"] = var["JUPDIALOG_RES"]
        var["key"] = 999
    
    
    if action == "actionres" :
        setCopy(var,["buttonres","stringres"])
        var["buttonres"] = var["JUPDIALOG_BUTTON"]
        var["stringres"] =var["JUPDIALOG_RES"]
        
#            <field id="buttonres" displayname="Button" readonly="" />
#        <field id="stringres" displayname="String result" readonly="" />
#    String JBUTTONRES = "JUPDIALOG_BUTTON";
#    String JBUTTONDIALOGRES = "JUPDIALOG_RES";       
    
def resaction(action,var):
    printVar("resaction",action,var)
    if action == "res2" :
        var["JCLOSE_DIALOG"] = True
    if action == "res3" :
        var["JCLOSE_DIALOG"] = "result res 3"
    
    
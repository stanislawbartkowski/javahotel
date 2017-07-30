import datetime,cutil

def _addval(var,v) :
    return v + "=" + str(var[v]) + "\n"

def dialogaction(action,var) :

    cutil.printVar("main",action,var)

    if action == "click" :
        s = _addval(var,"vstring") + _addval(var,"vpassword") + _addval(var,"vnumb") + _addval(var,"vchecked") + _addval(var,"vdate1") + _addval(var,"vdate2")
        cutil.copyField(var,"vresult",s)

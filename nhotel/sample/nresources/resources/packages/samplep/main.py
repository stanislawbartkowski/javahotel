import datetime,cutil

def _addval(var,v) :
    return v + "=" + str(var[v]) + "\n"

def dialogaction(action,var) :

    cutil.printVar("main",action,var)

#  if action == "before" :
#      var["glob1"] = "Hello"
#      var["globint"] = 12;
#      var["globdou"] = 123.45
#      var["globchecked"] = True
#      cutil.setCopy(var,["glob1","globint","globdou","globchecked"])

    if action == "click" :
        print("click")
        s = _addval(var,"vstring") + _addval(var,"vpassword") + _addval(var,"vnumb") + _addval(var,"vchecked") + _addval(var,"vdate1") + _addval(var,"vdate2")
        cutil.copyField(var,"vresult",s)

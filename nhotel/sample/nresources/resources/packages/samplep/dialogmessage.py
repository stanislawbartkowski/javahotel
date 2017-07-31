import datetime,cutil,con
from com.jythonui.shared import ICommonConsts

def dialogaction(action,var) :

    cutil.printVar("dialogmessage",action,var)

    if action == "click" :
      if cutil.checkEmpty(var,"vstring") : return
      cutil.callUpDialog(var,"dialogmessageup.xml",var["vstring"])

    if action == "closepopup" :
        cutil.copyField(var,"vresult",cutil.getUpReturnMessage(var))

def dialogactionup(action,var) :

    cutil.printVar("dialogactionup",action,var)

    if action == "before" :
        cutil.copyField(var,"vinput",cutil.getUpDialogParam(var))

    if action == "ok" or action == "no":
        if action == "ok" and cutil.checkEmpty(var,"vreturn") : return
        res = "Result=" + action
        if action == "ok" : res = res + "\n" + var["vreturn"]
        cutil.setCloseDialog(var,res)

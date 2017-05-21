import cutil

from com.jythonui.shared import ICommonConsts

def callUpDialog(var,dialname,startpar = None, startpar1=None) :
    var[ICommonConsts.JUPDIALOG] = dialname
    if startpar : var[ICommonConsts.JBUTTONDIALOGSTART] = startpar
    if startpar1 : var[ICommonConsts.JBUTTONDIALOGSTART1] = startpar1


def dialogaction(action,var) :

  cutil.printVar("dialoghelper",action,var)
  
  if action == "helper" : 
      callUpDialog(var,"helperstring.xml")
  


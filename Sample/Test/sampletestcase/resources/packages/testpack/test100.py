import cutil
import cmail

def dialogaction(action,var):
     
     cutil.printVar("test100",action,var)
     
     if action == "testfrom" :
         S = cmail.MAILFROM(var)
         fr = S.getFrom()
         print fr
         assert fr == "Hello.From.Jython"
         S.saveFrom("wow")
         var["OK"] = True
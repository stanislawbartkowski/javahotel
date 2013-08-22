from cutil import printVar
from util.util import printvar
from util.util import mapToXML
from util.util import xmlToVar

def dialogaction(action,var) :
     printVar("doaction",action,var)
     
     if action == "runxml" :
         map = {}
         map["name"] = "stringname"
         map["name1"] = "stringname1"
         s = mapToXML(map,["name","name1","name2"])
         print s
         
         xmlToVar(var,s,["name","name1","name2"])
         printVar("after",action,var)
         assert var["name2"] == ""
         assert var["name1"] == "stringname1"
         assert var["name"] == "stringname"
         
         
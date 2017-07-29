# coding=UTF-8
import datetime,cutil

def dialogaction(action,var) :

    cutil.printVar("dialogtypes",action,var)

    if action == "before" :
        cutil.setCopy(var,["vstring","vpassword"])
        var["vstring"] = u"Hello, I'm string. Do you like ąę ?"
        var["vpassword"] = "secret"

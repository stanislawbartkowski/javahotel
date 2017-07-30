# coding=UTF-8
import datetime,cutil,con

def dialogaction(action,var) :

    cutil.printVar("dialogtypes",action,var)

    if action == "before" :
        cutil.setCopy(var,["vstring","vpassword","vint","vdec0","vdec1","vdec2","vdec3","vdec4","vchecked","vdate1","vtext"])
        var["vstring"] = u"Hello, I'm string. Do you like ąę ?"
        var["vpassword"] = "secret"
        var["vint"] = 123
        var["vdec0"] = 123.0
        var["vdec1"] = 123.5
        var["vdec2"] = 123.56
        var["vdec3"] = 123.567
        var["vdec4"] = 123.5678
        var["vchecked"] = True
        var["vdate1"] = con.jDate(2017,6,7)
        var["vtext"] = "First line\nSecond line\nThird line"

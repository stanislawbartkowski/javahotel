import cutil
import sparam
import xmlutil
import datetime
import pdfutil

def construct() :    
    return sparam.SAVEPARAM("XXX","H1",["name","date","number","log"])

def construct1() :    
    return sparam.SAVEPARAM("XXX","H2",["glob1","subject","globbool","globint"])

def dialogaction(action,var):
    
    cutil.printVar("test40",action,var)

    if action == "test1" :
        C = construct()
        ma = {"name" : "Hello", "date" : datetime.date(2015,10,3),"number" : 2345.66, "log" : False}
        C.saveParam(ma)
        var["OK"] = True

    if action == "test2" :
        C = construct()
        ma = C.getParam()
        print ma
        assert not ma["log"]
        assert 2345.66 == ma["number"]
        assert datetime.date(2015,10,3) == ma["date"]
        var["OK"] = True
        
    if action == "test3" :
        # diffParam
        C = construct()
        ma = {"name" : "Hello", "date" : datetime.date(2015,10,3),"number" : 2345.66, "log" : False}
        diff = C.diffParam(ma)
        assert diff == None
        ma["name"] = "Kitty"
        diff = C.diffParam(ma)
        print diff
        assert diff != None
        assert 1==len(diff)
        assert "name" == diff[0][0] 
        assert "Hello" == diff[0][1] 
        assert "Kitty" == diff[0][2] 
        var["OK"] = True
            
    if action == "test4" :
        ma = {"name" : "Hello", "date" : datetime.date(2015,10,3),"number" : 2345.66, "log" : False}
        xml = xmlutil.mapToXML(ma)
        print xml
        xma = {}
        xmlutil.xmlToVar(xma,xml,["name","date","number","log"])
        print xma
        var["OK"] = True
        
    if action == "test5" :
        dialogaction("test1",var)
        ma = {"name" : "Kitty", "date" : datetime.date(2015,10,3),"number" : 2345.66, "log" : False}
        C = construct()
        diff = C.diffParam(ma)
        print diff
        xml = C.diffAsXML(var,diff,{"name" : "First name"})
        print xml
        assert xml != None
        var["OK"] = True
        
    if action == "test6" :
        dialogaction("test1",var)
        ma = {"name" : "Kitty", "date" : datetime.date(2015,10,3),"number" : 2345.66, "log" : False}
        C = construct()
        diff = C.diffParam(ma)
        print diff
        xml = C.diffAsXML(var,diff,{"name" : "First name"})
        print xml
        res = pdfutil.xsltHtmlS("xslt/generate.xslt",xml)
        print res
        var["OK"] = True
        
    if action == "test7" :
        D =  cutil.DLIST(var)
        ma = D.createNameMap()
        print ma
        assert "Subject" == ma["subject"]
        var["OK"] = True    

    if action == "test8" :
        C = construct1()
        ma = { "glob1" : "Hello", "subject" : "Wow", "globbool" : True, "globint" : 12 }
        ma1 = { "glob1" : "Hello", "subject" : "Ehhh", "globbool" : False, "globint" : 13 }
        diff = C.diffParam(ma1,ma)
        print diff
        xml = C.diffAsXML(var,diff)
        print xml
        i = xml.find("Yes")
        print i
        assert i != -1
        i = xml.find("No")
        print i
        assert i != -1
        var["OK"] = True
           
                
        
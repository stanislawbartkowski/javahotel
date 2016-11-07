import cutil
import xmlutil

def dialogaction(action,var):
    cutil.printVar("xmlmap",action,var);
    
    if action == "test1" :
        map = {}
        map["key1"] = "Hello1"
        map["key2"] = "Hello2"
        map["key3"] = "Hello3"
        xml = xmlutil.mapToXML(map)
        print xml
        xmlutil.xmlToVar(var,xml,["key1","key2","key3"])
        print var["key1"]
        print var["key2"]
        print var["key3"]
        assert var["key1"] == "Hello1"
        assert var["key2"] == "Hello2"
        assert var["key3"] == "Hello3"
        var["OK"] = True
        
    if action == "test2" :
        map = {}
        map["pre_key1"] = "Hello1"
        map["pre_key2"] = "Hello2"
        map["pre_key3"] = "Hello3"
        xml = xmlutil.mapToXML(map,["key1","key2","key3"],"pre_")
        print xml
        xmlutil.xmlToVar(var,xml,["key1","key2","key3"],"pre_")
        print var["pre_key1"]
        print var["pre_key2"]
        print var["pre_key3"]
        assert var["pre_key1"] == "Hello1"
        assert var["pre_key2"] == "Hello2"
        assert var["pre_key3"] == "Hello3"
        var["OK"] = True
        
    if action == "test3" :
        s = var["hello"]
        print s
        map = {}
        map["key1"] = s
        xml = xmlutil.mapToXML(map)
#        print xml
        var["xml"] = xml
        xmlutil.xmlToVar(var,xml,["key1",])
#        print var["key1"]
        assert var["key1"] == s        
        var["OK"] = True
                         
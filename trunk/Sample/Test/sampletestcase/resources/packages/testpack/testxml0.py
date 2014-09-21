import cutil
import xmlutil

def dialogaction(action,var):
    
    cutil.printVar("testxml0",action,var)
    
    if action == "testxml" :
        ma = {}
        ma["mtype"] = 0
        ma["test"] = "aaaaa"
        xml = xmlutil.mapToXML(ma)
        print xml
        (ma1,alist) = xmlutil.toMap(xml)
        print ma1
        print ma1["mtype"],type(ma1["mtype"])
        assert 0 == ma1["mtype"]
        var["OK"] = True
    
import cutil,pdfutil,xmlutil
from util import util,dutil

import datetime

def dialogaction(action,var):
    
    cutil.printVar("test43",action,var)
    
    if action == "test1" :
        l = util.getDictOfDocType()
        print l
        assert len(l) > 0
        for c in l :
            print c
            assert c["id"] != None
            assert c["name"] != None
        var["OK"] = True
    
    if action == "test2" :
        s = "<root><elem> Hello </elem></root>"
        cutil.verifyXML("test.xsd",s)
        var["OK"] = True
        
    if action == "test3" :
        ma = {}
        ma["doctype"] = "I"
        ma["docid"] = "FA/1/2043"
        ma["departuredate"] = datetime.date(2014,10,10)
        ma["nofguests"] = 4
        li = []
        li.append({"roomnumber" : "10", "rate" : 123.45})
        li.append({"roomnumber" : "11"})

        xml = dutil.doctoXML(ma,li)
        print xml
        var["OK"] = True
        
    if action == "test4" :
        ma = {}
        ma["doctype"] = "I"
        ma["docid"] = "FA/1/2043"
        ma["departuredate"] = datetime.date(2014,10,10)
        xml = dutil.doctoXML(ma,[])
        print xml
        (mx,li) = dutil.doctoMap(xml)
        print mx,li
        assert ma["doctype"] == mx["doctype"]
        assert ma["docid"] == mx["docid"]
        assert ma["departuredate"] == mx["departuredate"]
        var["OK"] = True


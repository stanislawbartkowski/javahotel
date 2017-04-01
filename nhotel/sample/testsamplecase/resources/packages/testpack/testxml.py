import cutil
import xmlutil
import datetime

def dialogaction(action,var):
    cutil.printVar("xml action",action,var)
    
    if action == "before" :
        ma = {}
        ma["id"] = "I100";
        xml = xmlutil.toXML(ma)
        print xml
        var["xml"] = xml
      
    if action == "after" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        assert len(ma) == 1
        val = ma["id"]
        assert val == "I100"

    if action == "before1" :
        ma = {}
        ma["id"] = "I100";
        ma["d"] = None
        xml = xmlutil.toXML(ma)
        var["xml"] = xml
      
    if action == "after1" :
        xml = var["xml"]
        (ma,li) = xmlutil.toMap(xml)
        print ma
        assert len(ma) == 2
        assert ma["id"] == "I100"
        assert ma["d"] == None
    
    if action == "before2" :
        ma = {}
        ma["numb"] = 99
        ma["dec"] = -45.66
        xml = xmlutil.toXML(ma)
        var["xml"] = xml
      
    if action == "after2" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        print ma
        l= ma["numb"]
        assert type(l) ==long
        assert l == 99
        v = ma["dec"]
        print type(v)
        assert type(v) == float
        assert v == -45.66

    if action == "before3" :
        ma = {}
        ma["numb"] = 99
        ma["dec"] = -45.66
        ma["ok"] = True
        ma["nok"] = False
        xml = xmlutil.toXML(ma)
        var["xml"] = xml
        
    if action == "after3" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        print ma
        assert len(ma) == 4
        assert type(ma["ok"]) == bool
        assert type(ma["nok"]) == bool
        assert ma["ok"]
        assert not ma["nok"]
        
    if action == "before4" :
        ma = {}
        ma["numb"] = 99
        ma["dec"] = -45.66
        ma["ok"] = True
        ma["today"] = datetime.date(2013,10,12)
        xml = xmlutil.toXML(ma)
        var["xml"] = xml
        
    if action == "after4" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        print ma
        assert type(ma["today"]) == datetime.date
        assert ma["today"] == datetime.date(2013,10,12)
        
    if action == "before5" :
        li = [{"ok":True,"numb" : 5 }]
        xml = xmlutil.toXML({},li)
        print xml
        var["xml"] = xml
            
    if action == "after5" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        print ma
        assert len(ma) == 0
        print li
        assert len(li) == 1
        m = li[0]
        assert m["numb"] == 5
        assert m["ok"]

    if action == "before6" :
        li = []
        for i in range(0,100) :
            li.append({"id" : i, "name" : "P " + str(i)})
        xml = xmlutil.toXML({},li)
        print xml
        var["xml"] = xml
        
    if action == "after6" :
        xml = var["xml"]
        print xml
        (ma,li) = xmlutil.toMap(xml)
        print len(li)
        assert len(li) == 100
    
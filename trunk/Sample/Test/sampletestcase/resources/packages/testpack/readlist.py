from cutil import printVar
import datetime

def dialogaction(action,var):
    
    printVar("dialog action",action,var)
    if action == "test1" :
        li = var["JLIST_MAP"]["list"]
        assert len(li) == 1
        rec = li[0]
        assert rec["id_name"] == 5
        assert rec["name"] == "hello"
        da = datetime.date(2013,10,3)
        assert rec["date"] == da
        
    if action == "test2" :
        li = var["JLIST_MAP"]["list"]
        assert len(li) == 100
        i = 0
        for rec in li :
            assert rec["id_name"] == i
            i = i + 1

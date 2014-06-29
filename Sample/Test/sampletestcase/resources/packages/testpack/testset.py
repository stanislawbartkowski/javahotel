import cutil
import miscutil
import datetime
import con

R = miscutil.SAVE_REGISTRY("TEST-XXX")

def dialogaction(action,var):
    cutil.printVar("testset",action,var)
    
    if action == "set" :
        var["glob1"] = "Hello"
        var["globint"] = None
        var["id"] = None
        
    if action == "setmap" :
        map = {}
        map['d1'] = datetime.date(2014,5,6)
        map['i1'] = 555
        map['null'] = None
        map['h1'] = 'Hello'
        map['b1'] = con.toB(5)
        R.saveMap(var,map)
        
    
    if action == "getmap" :
        map = R.getMap(var)
        print map
        d = map['d1']
        print d,type(d)
        assert datetime.date == type(d)
        l = map['i1']
        print l,type(l)
        assert long == type(l)
        bb = map['b1']
        print bb,type(bb)
        assert float == type(bb)
        var["OK"] = True

import cutil
import con
from util import rutil
from util import util
import datetime
from rrutil import resstat
import datetime

def dialogaction(action,var):
    
    cutil.printVar("test11",action,var)
    
    if action == "test1" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 1)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 1

    if action == "test2" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 1)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 0
        
    if action == "test3" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 1)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 3
        
    if action == "test4" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 1)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 1
        da = datetime.date(2013, 4, 6)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 3

    if action == "test5" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 5)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 2
        
    if action == "test6" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 10)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 4

    if action == "test7" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 10)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 8
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 5
        
    if action == "test8" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 5
        
    if action == "test9" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 6
                
    if action == "test10" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 7

    if action == "test11" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 5
        
    if action == "test12" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 11)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 11
        
        da = datetime.date(2013, 4, 12)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 11

        da = datetime.date(2013, 4, 13)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 8

        da = datetime.date(2013, 4, 14)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 5
        
    if action == "test13" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        da = datetime.date(2013, 4, 14)
        re = resstat.getResStatus(var,r,None,da)
        print re
        assert re == 6
        
    var["OK"] = True    

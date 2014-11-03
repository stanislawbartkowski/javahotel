import cutil
from util import util
import datetime
from util import resstat

def dialogaction(action,var):
    
    cutil.printVar("test39",action,var)
    
    if action == "test1" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        RR = resstat.getResStatusR(var,r)
        print RR
        print RR.arrival,RR.departure
        print RR.sumcost,RR.sumpay
        assert 400.00 == RR.sumcost

    if action == "test2" :
        rese = var["rese"]
        r = util.RESFORM(var).findElem(rese)
        RR = resstat.getResStatusR(var,r)
        print RR
        print RR.arrival,RR.departure
        print RR.sumcost,RR.sumpay
        print RR.advancedpaymentused
        assert 400.00 == RR.sumcost
        assert 300.00 == RR.sumpay
        assert 90.0 == RR.advancedpaymentused

    var["OK"] = True

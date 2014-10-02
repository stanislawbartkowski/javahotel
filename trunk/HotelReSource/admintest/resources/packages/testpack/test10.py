import cutil
import con
from util import rutil
import datetime

def dialogaction(action,var):
    
    cutil.printVar("test10",action,var)
    
    if action == "checkrese" :
        sym = var["rese"]
        (arrival,departure,roomname,rate) = rutil.getReseDateS(var,sym)
        print arrival,departure,roomname,rate,type(rate)
        assert roomname == "P10"
        assert con.BigDecimalToDecimal(rate) == 100.0
        print arrival,type(arrival)
        assert datetime.date(2013,4,10) == arrival
        assert datetime.date(2013,4,12) == departure
        var["OK"] = True
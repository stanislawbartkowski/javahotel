import cutil,pdfutil,xmlutil
from util import util,dutil,rpdf,cust
from rrutil import cbill

import datetime

M = cutil.MESS()

def _createX(var,rese_name,paymentmethod=None):
    print rese_name
    cu = cust.newCustomer(var)
#    cu.setGensymbol(True);
    cu = util.CUSTOMERLIST(var).addElem(cu)
    cust_name = cu.getName()
    print cust_name
    bli = util.RESOP(var).findBillsForReservation(rese_name)
    print bli
    for l in bli : print l
    assert len(bli) == 0
        
    R = util.RESFORM(var).findElem(rese_name)

    li = []
    for l in R.getResDetail() : li.append(l.getId())
    print li
    assert len(li) > 0            
    xml = rpdf.buildXMLForStay(var,rese_name,cust_name,li)
    print xml
    S = cbill.HOTELBILLSAVE(var,rese_name,cust_name,xml,paymentmethod)
    res = S.doTransRes()
    print res
    if not res : print var["JERROR_MESSAGE"]
    assert res
    bli = util.RESOP(var).findBillsForReservation(rese_name)
    assert len(bli) == 1
    print S.getB().getName()
    print S.getTotal()
        
    xml = cbill.getXMLForBill(var,S.getB().getName())
    print xml
    assert xml != None
    return (S.getB(),xml)

def _create(var,rese_name,paymentmethod=None):
    (X,xml) = _createX(var,rese_name,paymentmethod)
    return X

def dialogaction(action,var):
    
    cutil.printVar("test45",action,var)
    
    if action == "test1" :
        rese_name = var["resename"]
        b = _create(var,rese_name)
        C = cbill.BILLCALC(var,b)
        C.calc()
        print C.getTotal(),C.getCharge(),C.getPayment()
        assert C.getTotal() == 500.0
        assert C.getCharge() == 500.0
        assert C.getPayment() == 0
        var["OK"] = True
        
    if action == "test2" :
        rese_name = var["resename"]
        b = _create(var,rese_name,"CA")
        C = cbill.BILLCALC(var,b)
        C.calc()
        print C.getTotal(),C.getCharge(),C.getPayment()
        assert C.getTotal() == 1500.0
        assert C.getCharge() == 1500.0
        assert C.getPayment() == 1500.0
        var["OK"] = True

    if action == "test3" :
        rese_name = var["resename"]
        (X,xml) = _createX(var,rese_name,"CA")
        (ma,li) = xmlutil.toMap(xml)
        assert 10 == len(li)
        for l in li : 
            print l
            assert "7%" == l["tax"]
            assert 93.46 == l["netvalue"]
            assert 6.54 == l["taxvalue"]
        var["OK"] = True
                
    if action == "test4" :
        rese_name = var["resename"]
        (X,xml) = _createX(var,rese_name,"CA")
        (ma,li) = xmlutil.toMap(xml)
        assert 10 == len(li)
        print dutil.getDocGrossValue(ma)
        assert 1000.0 == dutil.getDocGrossValue(ma)
        for l in li :
#            print l
            assert l["taxlevel"] == None
            assert l["taxvalue"] == None
        var["OK"] = True
        
    if action == "test5" :
        s = M("shownote")
        print s
        assert "Show note" == s
        s = M.C("shownote")
        print s
        assert "shownote" == s
        s = M.C("@shownote")
        print s
        assert "Show note" == s
        var["OK"] = True
        
        
        
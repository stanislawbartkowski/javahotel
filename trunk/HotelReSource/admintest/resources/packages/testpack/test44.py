import cutil,pdfutil,xmlutil
from util import util,dutil,rpdf

import datetime

def dialogaction(action,var):
    
    cutil.printVar("test44",action,var)
    
    if action == "test1" :
        sym = var["resename"]
        xml = rpdf.buildXMLReservation(var,sym)
        print xml
        txt =  pdfutil.xsltHtml("xslt/resconfirmation.xslt",xml)
        print txt
        var["OK"] = True
        
    if action == "test2" :
        sym = var["resename"]
        R = util.RESFORM(var).findElem(sym)
        assert R != None
        custname = R.getCustomerName()
        l = R.getResDetail()
        seq = [l[0].getId(),l[1].getId()]
        print seq
        xml = rpdf.buildXMLForStay(var,sym,custname,seq)
        print xml
        assert xml != None
        (ma,li) = xmlutil.toMap(xml)
        print ma,li
        k = ma["total"]
        assert 200 == k
        var["OK"] = True
        
        

    
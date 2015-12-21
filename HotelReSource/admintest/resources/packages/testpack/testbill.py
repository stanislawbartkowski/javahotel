import cutil
from util import rutil
from util import util
import datetime
import con

def dialogaction(action,var):
    cutil.printVar("testbill",action,var)
    
    if action == "testbill" :
        bname = var["billno"]
        print bname
        bb = util.BILLLIST(var).findElem(bname)
        resname = bb.getReseName()
        print resname
        RE = util.RESFORM(var).findElem(resname)
        print RE
        rlist = RE.getResDetail()
        for r in rlist:
            print r.getId()
            print type(r.getId())
            
        b = util.newBill(var)
#        b.setGensymbol(True);
        b.setPayer(bb.getPayer());
        b.setReseName(resname)
        dat = datetime.date(2010,1,2)
        b.setIssueDate(con.toDate(dat))
        for r in rlist:
           b.getPayList().add(r.getId());
        b1 = util.BILLLIST(var).addElem(b)
        assert b1 != None
        print b1.getName()    
        var["OK"] = True
#        b.setIssueDate(toDate(2010, 10, 12));
    
import cutil,con
from util import util

def dialogaction(action,var):
    
    cutil.printVar("test48",action,var)
    
    if action == "test1" :
        resename = var["resename"]
        R = util.RESFORM(var)
        ROP = util.RESOP(var)
        RR = R.findElem(resename)
        print RR
        lg = ROP.getResGuestList(resename)
        for g in lg :
            print g.getGuestName()
        gName = lg[0].getGuestName()
        print gName
        dli = RR.getResDetail()
        for l in dli :
            print g.getRoomName()
        roomName = dli[0].getRoomName()
        print roomName
        S = util.SERVICES(var)
        se= util.newOtherService(var)
        se.setName("addse")
        se.setVat("free")
        ase = S.addElem(se)
        rpa = util.newResAddPayment()
        rpa.setGuestName(gName)
        rpa.setRoomName(roomName)
        rpa.setResDate(con.javaDate(2015,12,23))
        rpa.setPriceList(con.toB(100.56))
        rpa.setPrice(con.toB(50))
        rpa.setPriceTotal(con.toB(200))
        rpa.setService(ase.getName())
        arpa = ROP.addResAddPayment(resename,rpa)
        print arpa.getName(),arpa.getId()," ", arpa.getResDate()
        assert arpa.getId() != None
        li = cutil.JOURNAL(var).getList()
        for l in li :
            print l.getId()
        J = li[0]
        print J.getName(),J.getJournalType(),J.getJournalTypeSpec(),J.getJournalElem1(),J.getJournalElem2()
        assert "ADDPAYMENT" == J.getJournalType()
        assert resename == J.getJournalElem1()
        assert arpa.getId() == int(J.getJournalElem2())
        var["OK"] = True

    if action == "test2" :
        bname = var["bname"]
        p = util.newBillPayment()
        p.setBillName(bname)
        p.setPaymentMethod("Cache")
        p.setDateOfPayment(cutil.toDate(cutil.today()))
        p.setPaymentTotal(con.toB(1000))
        addP = util.PAYMENTOP(var).addPaymentForBill(bname,p)         
        li = cutil.JOURNAL(var).getList()
        for l in li :
            print l.getId(),l.getJournalType(),l.getJournalElem1(),l.getJournalElem2(),l.getCreationDate()
        J = li[0]
        print J.getName(),J.getJournalType(),J.getJournalTypeSpec(),J.getJournalElem1(),J.getJournalElem2(),
        assert "ADDBILLPAYMENT" == J.getJournalType()
        assert addP.getId() == int(J.getJournalElem1())
        assert bname == J.getJournalElem2()
        # now remove
        util.PAYMENTOP(var).removePaymentForBill(bname,addP.getId())
        li = cutil.JOURNAL(var).getList()
        for l in li :
            print l.getId(),l.getJournalType(),l.getJournalElem1(),l.getJournalElem2(),l.getCreationDate()
        J = li[0]
        print J.getName(),J.getJournalType(),J.getJournalTypeSpec(),J.getJournalElem1(),J.getJournalElem2()
        assert "REMOVEBILLPAYMENT" == J.getJournalType()
        assert addP.getId() == int(J.getJournalElem1())
        assert bname == J.getJournalElem2()
        var["OK"] = True


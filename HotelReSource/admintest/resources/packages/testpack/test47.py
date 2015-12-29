import cutil
from util import util,journalmess

def dialogaction(action,var):
    
    cutil.printVar("test47",action,var)
    
    if action == "test1" :
        S = util.SERVICES(var)
        print S
        se = util.newHotelService(var)
        se.setName("SERV")
        se.setDescription("Usluga hotelowe")
        se.setVat("vat22")
        S.addElem(se)
        li = cutil.JOURNAL(var).getList()
        found = False        
        for l in li :
            print l.getJournalType(),l.getJournalTypeSpec(),l.getJournalElem1()
            if l.getJournalType() == "SERVICE" :
                found = True
                assert l.getJournalTypeSpec() == "ADD"
                assert l.getJournalElem1() == "SERV"
        assert found
        var["OK"] = True
        
    if action == "test2" :
        S = util.SERVICES(var)
        print S
        se = util.newHotelService(var)
        se.setName("SERV")
        se.setDescription("Usluga hotelowe")
        se.setVat("vat22")
        S.addElem(se)
        se.setDescription("Wow")
        S.changeElem(se)
        li = cutil.JOURNAL(var).getList()
        for l in li :
            print l.getJournalType(),l.getJournalTypeSpec(),l.getJournalElem1()
        l = li[0]
        assert l.getJournalTypeSpec() == "MODIF"
        assert l.getJournalElem1() == "SERV"
        assert l.getJournalType() == "SERVICE"
        var["OK"] = True
        
    if action == "test3" :
        S = util.SERVICES(var)
        print S
        se = util.newHotelService(var)
        se.setName("SERV")
        se.setDescription("Usluga hotelowe")
        se.setVat("vat22")
        se = S.addElem(se)
        S.deleteElem(se)
        li = cutil.JOURNAL(var).getList()
        for l in li :
            print l.getJournalType(),l.getJournalTypeSpec(),l.getJournalElem1()
        l = li[0]
        assert l.getJournalTypeSpec() == "REMOVE"
        assert l.getJournalElem1() == "SERV"
        assert l.getJournalType() == "SERVICE"
        var["OK"] = True
        
    if action == "test4" :
        dialogaction("test2",var)
        li = cutil.JOURNAL(var).getList()
        JM = journalmess.JournalMess(var)
        var["OK"] = False
        for l in li :
            print l.getJournalType(),l.getJournalTypeSpec(),l.getJournalElem1()
            print "mess=",JM.getJournalDescr(l)
            assert JM.getJournalDescr(l) != None
        print JM.getListOfType()
        var["OK"] = True

    if action == "test5" :
        dialogaction("test3",var)
        li = cutil.JOURNAL(var).getList()
        JM = journalmess.JournalMess(var)
        var["OK"] = False
        for l in li :
            print l.getJournalType(),l.getJournalTypeSpec(),l.getJournalElem1()
            print "mess=",JM.getJournalDescr(l)
            assert JM.getJournalDescr(l) != None
        print JM.getListOfType()
        var["OK"] = True

        
        
            
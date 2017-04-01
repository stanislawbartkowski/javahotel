import cutil

def dialogaction(action,var):
    
    cutil.printVar("test64",action,var)
    
    if action == "test1" :
        li = cutil.JOURNAL(var).getList()
        JM = cutil.JOURNALMESS()
        for l in li :
            print l.getJournalType(),JM.getEntryDescr(l)
            assert JM.getEntryDescr(l) != None
        assert 3 == len(li)
        var["OK"] = True
        
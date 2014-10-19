import cutil

def dialogaction(action,var):
    
    cutil.printVar("test37",action,var)
    
    if action == "test1" :
        li = cutil.getDict("titles")
        for l in li : print l
        assert 2 == len(li)
        var["OK"] = True

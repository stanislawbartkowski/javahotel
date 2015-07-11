import cutil

def dialogaction(action,var):
    
    cutil.printVar("test63",action,var)
    
    if action == "before" :
        if var.has_key("JCOPY_glob1") :
            assert "hello" == var["glob1"]
            var["OK"] = True
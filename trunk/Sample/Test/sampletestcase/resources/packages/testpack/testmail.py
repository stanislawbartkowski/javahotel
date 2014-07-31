import cutil
import cmail

def dialogaction(action,var):
    cutil.printVar("test mail",action,var)
    
    if action == "sendmail" :
        res = cmail.sendMail("hello","Sending from Jython","stanislawbartkowski@gmail.com","test.jython");
        print res
        assert res == None
        var["OK"] = True
    
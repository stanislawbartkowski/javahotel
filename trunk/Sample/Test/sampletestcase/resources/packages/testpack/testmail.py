import cutil
import cmail
from org.python.core.util  import StringUtil
from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()


def dialogaction(action,var):
    cutil.printVar("test mail",action,var)
    
    if action == "sendmail" :
        res = cmail.sendMail("hello","Sending from Jython","stanislawbartkowski@gmail.com","test.jython")
        print res
        assert res == None
        var["OK"] = True
        
    if action == "getmailno" :
        no = cmail.getMailNo()
        print no
        var["OK"] = True
        
    if action == "getlist" :
        li = cmail.getMailList()
        for l in li :
            print l.getHeader()
            print l.getSentDate()
        var["OK"] = True        
    
    if action == "sendmailattachment" :
        s = "I'm your attachment sent from Jython code.\n What do you think about it ?\n Do you like me ?"
        key = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
        print key
        res = cmail.sendMailSingleAttach("Attachment from Jython","Sending from Jython","stanislawbartkowski@gmail.com","test.jython",True,cutil.PDFTEMPORARY,key,"my.txt")
        print res
        assert res == None
        var["OK"] = True
        
        
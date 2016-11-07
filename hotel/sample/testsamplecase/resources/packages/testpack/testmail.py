import cutil
import cmail
from org.python.core.util  import StringUtil
from com.jythonui.server.holder import SHolder
import listbl

ADDBLOB=SHolder.getAddBlob()
B = listbl.BLOBREGISTRY("TEST_P","b123","blist")

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
        res = cmail.sendMailSingleAttach("Attachment from Jython","Sending from Jython","stanislawbartkowski@gmail.com","test.jython",cutil.PDFTEMPORARY,key,"my.txt")
        print res
        assert res == None
        var["OK"] = True
        
    if action == "sendmailattachment3" :
        s = "First attachment"
        key1 = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
        aList = cmail.createAttachList(None,cutil.PDFTEMPORARY,key1,"attach1.txt")

        s = "Second attachment"
        key2 = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
        aList = cmail.createAttachList(aList,cutil.PDFTEMPORARY,key2,"attach2.txt")

        s = "Third attachment"
        key3 = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
        aList = cmail.createAttachList(aList,cutil.PDFTEMPORARY,key3,"attach3.txt")
        
        res = cmail.sendMail("3 attachments from Jython","Sending from Jython","stanislawbartkowski@gmail.com","test.jython",aList)
        print res
        assert res == None
        var["OK"] = True
        
    if action == "createbloblist" :
        B.removeBlob(var)
        for a in range(10) :
          s = "Attachment no " + str(a)
          key = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTCREATE",StringUtil.toBytes(s))
          B.addBlob(var,"Hello",key)
        var["OK"] = True  
        
    if action == "sendbloblist" :           
        B.readBlobList(var)
        li = var["JLIST_MAP"]["blist"]
        print li
        realM = B.getRealm(var)
        aList = None
        no = 0
        for l in li :
            key = l["blob_key"]
            aList = cmail.createAttachList(aList,realM,key,"attach" + str(no) +".txt")
            no = no + 1            
            
        res = cmail.sendMail("A lot of attachments BLOB","Sending from Jython","stanislawbartkowski@gmail.com","test.jython",aList)
        print res
        assert res == None
        var["OK"] = True
        
    if action == "setxmail" :
        M = cmail.CMAIL(var)
        res = M.sendMail("hello","Sending from CMAIL class","stanislawbartkowski@gmail.com","test.jython")
        print res
        assert res != None
        resmail = res.getSendResult()
        print resmail
        assert resmail == None
        var["OK"] = True
        
    if action == "readxmail" :
        M = cmail.CMAIL(var)
        li = M.getList()
        print li
        for l in li : print l.getDescription()
        assert len(li) == 1
        var["OK"] = True
        
    if action == "removexmail" :
        M = cmail.CMAIL(var)
        li = M.getList()
        assert len(li) == 1
        li = M.getList()
        l = li[0]
        M.deleteElem(l)
        li = M.getList()
        assert len(li) == 0
        var["OK"] = True
        
            
        
                        
        
        
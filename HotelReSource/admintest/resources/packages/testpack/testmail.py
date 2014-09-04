import cmail
import cutil
from com.jythonui.server.holder import SHolder
from org.python.core.util  import StringUtil

ADDBLOB=SHolder.getAddBlob()


def dialogaction(action,var):
    cutil.printVar("testmail",action,var)
    
    if action == "sendmail" :
        C = cmail.CMAIL(var)
        res = C.sendMail("Mail from hotel","What do you think about us ?","stanislawbartkowski@gmail.com","Jython")
        print res.getSendResult()
        assert res.getSendResult() == None
        assert res.getName() != None
        var["OK"] = True
        
    if action == "checkmail":    
        C = cmail.CMAIL(var)
        li = C.getList()
        print li
        for l in li :
            print l.getDescription(),l.getContent()
        assert len(li) == 1
        var["OK"] = True
        
    if action == "sendmailattach" :
        C = cmail.CMAIL(var)
        li = C.getList()
        for l in li :
            C.removeElem(l)
        li = C.getList()
        assert len(li) == 0
        s = "Attachment"
        key = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
        aList = cmail.createAttachList(None,cutil.PDFTEMPORARY,key,"attach.txt")
        res = C.sendMail("Mail from hotel with attachment","Nothing interesting ","stanislawbartkowski@gmail.com","Jython",aList)
        print res.getSendResult()
        assert res.getSendResult() == None
        var["OK"] = True
        
    if action == "checkmailattach" :
        B = SHolder.getBlobHandler()   
        C = cmail.CMAIL(var)
        li = C.getList()
        assert len(li) == 1
        l = li[0]
        att = l.getaList()
        assert len(att) == 1
        for a in att :
            print a.getFileName(),a.getRealm(),a.getBlobKey()
            va = B.findBlob(a.getRealm(),a.getBlobKey())
            assert va != None
            ss = StringUtil.fromBytes(va)
            print ss
            assert ss ==  "Attachment"
        var["OK"] = True    
        
    if action == "sendmultiattach" :
        C = cmail.CMAIL(var)
        li = C.getList()
        for l in li :
            C.removeElem(l)
        li = C.getList()
        assert len(li) == 0
    
        aList = None    
        for i in range(20) :
          s = "Attachment content" + str(i)
          key = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
          aList = cmail.createAttachList(aList,cutil.PDFTEMPORARY,key,"attach" + str(i) + ".txt")
          
        res = C.sendMail("Mail from hotel with 20 attachment","Nothing interesting inside ","stanislawbartkowski@gmail.com","Jython",aList)
        print res.getSendResult()
        assert res.getSendResult() == None
        var["OK"] = True

    if action == "checkmultiattach" :
        B = SHolder.getBlobHandler()   
        C = cmail.CMAIL(var)
        li = C.getList()
        assert len(li) == 1
        l = li[0]
        att = l.getaList()
        assert len(att) == 20
        for a in att :
            print a.getFileName(),a.getRealm(),a.getBlobKey()
            va = B.findBlob(a.getRealm(),a.getBlobKey())
            assert va != None
            ss = StringUtil.fromBytes(va)
            print ss
            assert not cutil.emptyS(ss)
        var["OK"] = True    
    
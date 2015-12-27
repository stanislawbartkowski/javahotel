import cutil,con
#from com.jythonui.server.holder import SHolder
import listbl

BREG="BILLBLOB"
LIST="pdflist"
#ADDBLOB=SHolder.getAddBlob()

def _constructB(billname) :
  return listbl.BLOBREGISTRY(BREG,billname,LIST)

def dialogaction(action,var):
    
    cutil.printVar("test49",action,var)
    
    if action == "test1" :
#        s = "I'm your attachment sent from Jython code.\n What do you think about it ?\n Do you like me ?"
#        key = ADDBLOB.addNewBlob(cutil.PDFTEMPORARY,"TESTJ",StringUtil.toBytes(s))
#        print key
        billno = "BBB"
        comment = "Hello"
        tempkey = "T_KEY"
        B = _constructB(billno)
        B.removeBlob(var)
        B.readBlobList(var)
        li = var["JLIST_MAP"][LIST]
        assert 0 == len(li)
        B.addBlob(var,comment,tempkey)
        B.readBlobList(var)
        li = var["JLIST_MAP"][LIST]
        print len(li)
        for l in li : print l
        assert 1 == len(li)
        var["OK"] = True



from com.jythonui.server.holder import Holder
from com.jythonui.server.IMailSend import AttachElem

import cutil

_FROMCOOKIE = "mailfrom"

def sendMail(subject,content,to,froma,attachList=None,text=True) :
    iM = Holder.getMail()
    res = iM.postMail(text,[to,],subject,content,froma,attachList)
    return res
            
def getMailNo():
    iG = Holder.getGetMail()
    res = iG.getMail(-1,-1)
    assert res.getErrMess() == None
    return res.getNo()

def createAttachList(aList,realM,bKey,filename):
    if aList == None : aList = cutil.createArrayList()
    elem = AttachElem(realM,bKey,filename)
    aList.add(elem)
    return aList    

def sendMailSingleAttach(subject,content,to,froma,realM,bKey,filename,text=True) :
    aList = createAttachList(None,realM,bKey,filename)
    return sendMail(subject,content,to,froma,aList,text)
        
def getMailList(fromm = -1,to = 0):
    """ get list of mail from input box
    Args:
      fromm : the number of first mail to retrieve (counting from end)
      to : the number of last mail to retrieve (-1 : all mails)
    Returns:
      List of mails
      Element in the list:
        getHeader()
        getContent()
        getFrom()
        isText()
        isIsSeen()
        getSentDate()
        getPerson()  
    """
    iG = Holder.getGetMail()
    res = iG.getMail(fromm,to)
    assert res.getErrMess() == None
    return res.getList()

class CMAIL(cutil.CRUDLIST):
    
    def __init__(self,var) :
        cutil.CRUDLIST.__init__(self,var)
        self.i = Holder.getSaveMail()
        self.serviceS = Holder.getNoteStorage()
        
    def sendMail(self,subject,content,to,froma,attachList=None,text=True):
        res = self.i.postMail(text,[to,],subject,content,froma,attachList)
        assert res != None
        assert res.getName() != None
        return res

class MAILFROM :
    
    def __init__(self,var):
        self.var = var
        self.p = Holder.getMailFrom()
        
    def getFrom(self):
        fr = cutil.getCookie(self.var,_FROMCOOKIE)
        if fr != None : return fr
        return self.p.getFrom()
    
    def saveFrom(self,f) :
        cutil.setCookie(self.var,_FROMCOOKIE,f)

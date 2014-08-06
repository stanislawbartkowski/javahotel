from com.jythonui.server.holder import Holder
from com.jythonui.server.IMailSend import AttachElem

import cutil

def sendMail(subject,content,to,froma,text=True,attachList=None) :
    iM = Holder.getMail()
    res = iM.postMail(text,[to,],subject,content,froma,attachList)
    return res
            
def getMailNo():
    iG = Holder.getGetMail()
    res = iG.getMail(-1,-1)
    assert res.getErrMess() == None
    return res.getNo()

def sendMailSingleAttach(subject,content,to,froma,text,realM,bKey,a) :
    aList = cutil.createArrayList()
    elem = AttachElem(realM,bKey,a)
    aList.add(elem)
    return sendMail(subject,content,to,froma,text,aList)
    
    
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


    
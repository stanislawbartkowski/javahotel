from com.jythonui.server.holder import Holder

def sendMail(subject,content,to,froma,text=True) :
    iM = Holder.getMail()
    res = iM.postMail(text,[to,],subject,content,froma)
    return res
    
from com.jythonui.server.holder import SHolder
from org.python.core.util import StringUtil

class _BLOB :
    
    def __init__(self):        
        self._A = SHolder.getAddBlob()
        self._I = SHolder.getBlobHandler()
        
    def getList(self,realm):
        clist = self._I.findBlobs(realm)
        return clist
    
    def addNewBlob(self,realm,keypatt,b): 
        key = self._A.addNewBlob(realm,keypatt,b)
        return key
    
    def findBlob(self,realm,key):
        return self._I.findBlob(realm,key)
    
    def findBlobS(self,realm,key):
        b = self.findBlob(realm,key)
        if b == None : return None
        return StringUtil.fromBytes(b)
    
    def clearAll(self,realm):
        self._I.clearAll(realm)
        
    def changeBlob(self,realm,key,b):
        self._I.changeBlob(realm,key,b)
        
    def removeBlob(self,realm,key):
        self._I.changeBlob(realm,key)
        
    def getModifTime(self,realm,key):
        return self._I.getModifTime(realm,key)
        
B = _BLOB()
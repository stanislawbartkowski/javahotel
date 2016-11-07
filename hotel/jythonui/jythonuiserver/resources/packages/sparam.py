import cutil,xmlutil,pdfutil

class SAVEPARAM(cutil.StorageRegistry):
    
    def __init__(self,realm,pkey,li) :
        cutil.StorageRegistry.__init__(self,None,realm+"-SAVEPARAM")
        self.__li = li
        self.__pkey = pkey        

    def createXMLParam(self,var):
        xml = xmlutil.mapToXML(var,self.__li)
        return xml
      
    def saveParam(self,var):
        xml = self.createXMLParam(var)
        self.putEntry(self.__pkey,xml)
        
    def getParam(self,xml=None):
        if xml == None : xml = self.getEntryS(self.__pkey)
        if xml == None : return None
        return xmlutil.toMap(xml)[0]
    
    def diffParam(self,var,ma=None):
        if ma == None : ma = self.getParam()
        diff = None
        for k in self.__li :
            if ma[k] == var[k] : continue
            if diff == None : diff = []
            diff.append((k,ma[k],var[k]))
        return diff        
    
    def diffAsXML(self,var,diff,ma=None):
        if ma == None:
            ma = cutil.DLIST(var).createNameMap()  
        print ma                   
        li = []
        for d in diff :
            key = d[0]
            prevval = d[1]
            newval = d[2]
            print key,type(key)
            if ma.has_key(key) : descr = ma[key]
            else : descr = None
            map = { "key" : key, "prevval" : prevval, "newval" : newval, "descr" : descr}
            li.append(map)
        return xmlutil.toXML({},li,False,True)
        
    def getLi(self):
        return self.__li    
        
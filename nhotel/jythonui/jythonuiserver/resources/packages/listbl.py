import cutil,con,cblob

B = cblob.B

def __getO(var):
    return cutil.getObject(var).getObject()

class BLOBREGISTRY(cutil.RegistryFile):

    def __init__(self,realmblobid,billno,bloblist) :
      self._billno = billno
      self._bloblist = bloblist
      map = {"id" : cutil.INT, "blob_person" : cutil.STRING, "blob_comment" : cutil.STRING, "blob_key" : cutil.STRING  }
      cutil.RegistryFile.__init__(self,None,realmblobid + billno ,None,map, bloblist,"id")
      
    def readBlobList(self,var):
        self.readList(var)  
        l = var["JLIST_MAP"][self._bloblist]
        for k in l :
            bkey = k["blob_key"]
            d = B.getModifTime(__getO(var),bkey).getCreationDate()
            dd = con.toJDate(d)
            k["blob_date"] = dd
           
    def getRealm(self,var):
      return __getO(var)

    def addBlob(self,var,comment,tempkey):
      b = B.findBlob(cutil.PDFTEMPORARY,tempkey)
      key = B.addNewBlob(__getO(var),self._billno,b)
      id = self.nextKey()
      map = { "id" : id, "blob_comment" : comment, "blob_key" : key, "blob_person" : cutil.getPerson(var)}
      self.addMap(map)
    
    def changeBlobComment(self,var,id,comment):
      map = { "SECURITY_TOKEN" : var["SECURITY_TOKEN"] }    
      self.readBlobList(map)
      l = map["JLIST_MAP"][self._bloblist]
      mapc = None
      for elem in l :
          if id == elem["id"] : mapc = elem
      mapc["blob_comment"] = comment
      self.addMap(mapc)
    
    def removeBlob(self,var):
      self.removeAll()
    
    def removeOneBlob(self,var,id):
      var["id"] = id
      self.removeMap(var)

def constructPDFBLOB(var,pdfkey,name="receipt.pdf") :
    return __getO(var)  + ":" + pdfkey + ":" + name
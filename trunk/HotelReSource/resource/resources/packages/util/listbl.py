import cutil
import con
import util

from com.jythonui.server.holder import SHolder

ADDBLOB=SHolder.getAddBlob()
IBLOB=SHolder.getBlobHandler()

class BLOBREGISTRY(cutil.RegistryFile):

    def __init__(self,billno,bloblist) :
      self._billno = billno
      map = {"id" : cutil.LONG, "blob_person" : cutil.STRING, "blob_comment" : cutil.STRING, "blob_key" : cutil.STRING  }
      cutil.RegistryFile.__init__(self,None,"BILLBLOB" + billno ,None,map, bloblist,"id")

def readBlobList(var,billno,bloblist):
    B = BLOBREGISTRY(billno,bloblist)
    B.readList(var)
    l = var["JLIST_MAP"][bloblist]
    for k in l :
        bkey = k["blob_key"]
        d = IBLOB.getModifTime(util.getHotel(var),bkey).getCreationDate()
        print type(d)
        dd = con.toJDate(d)
        print dd
        k["blob_date"] = dd

def addBlob(var,billno,bloblist,comment,tempkey):
    b = IBLOB.findBlob(cutil.PDFTEMPORARY,tempkey)
    key = ADDBLOB.addNewBlob(util.getHotel(var),billno,b)
    B = BLOBREGISTRY(billno,bloblist)
    id = B.nextKey()
    map = { "id" : id, "blob_comment" : comment, "blob_key" : key, "blob_person" : util.getPerson(var)}
    B.addMap(map)
    
def changeBlobComment(var,billno,bloblist,id,comment):
    map = { "SECURITY_TOKEN" : var["SECURITY_TOKEN"] }    
    readBlobList(map,billno,bloblist)
    l = map["JLIST_MAP"][bloblist]
    mapc = None
    for elem in l :
        if id == elem["id"] : mapc = elem
    mapc["blob_comment"] = comment
    B = BLOBREGISTRY(billno,bloblist)
    B.addMap(mapc)
    
def removeBlob(var,billno,bloblist):
    B = BLOBREGISTRY(billno,bloblist)
    B.removeAll()
    
def removeOneBlob(var,billno,bloblist,id):
    B = BLOBREGISTRY(billno,bloblist)
    var["id"] = id
    B.removeMap(var)

def constructPDFBLOB(var,pdfkey,name="receipt.pdf") :
  return util.getHotel(var) + ":" + pdfkey + ":" + name
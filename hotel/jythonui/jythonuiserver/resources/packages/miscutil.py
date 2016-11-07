from com.jythonui.server.holder import Holder
from com.jythonui.shared import DialogFormat
from com.gwtmodel.table.common import TT

import cutil,xmlutil

class SAVE_REGISTRY() :
  
  def __init__(self,rentry) :
    self.rentry = rentry
    
  def __getR(self,var) :
    return cutil.StorageRegistry(Holder.getRegFactory(),"TEMPORARY-DATA-" + var["SECURITY_TOKEN"])
    
  def saveMap(self,var,l) :
    R = self.__getR(var)
    R.putEntry(self.rentry,xmlutil.toXML(l))
    
  def getMap(self,var) :
    R = self.__getR(var)
    xml = R.getEntryS(self.rentry)
    (rmap,li) = xmlutil.toMap(xml)
    return rmap

# =============================
def getMapFieldList(dialogName,list=None):
  """ Extract list of fields (columns) name from dialog
  
    Args:
        dialogName : dialog
        list : if None list of fields from dialog
               if not None list of columns from list 
  """    
  i = Holder.getiServer()
  dInfo =  i.findDialog(Holder.getRequest(), dialogName)
  assert dInfo != None
  dFormat = dInfo.getDialog()
  if list == None :
      flist = dFormat.getFieldList()
  else :
    lform = dFormat.findList(list)
    assert lform != None
    flist = lform.getColumns()  
          
  l = []
  for f in flist :
      name = f.getId()
      l.append(name)
  return l    

def toListMap(dialname,listname):
    """ Get list description ListForm from dialog
        Args:
            dialname : dialog name
            listname : list name defined in the dialog, important: list should exist
        Returns:
            ListForm            
    """
#    i = Holder.getiServer()
#    dInfo =  i.findDialog(Holder.getRequest(), dialname)
#    assert dInfo != None
    dlist = getDialogFormat(dialname).findList(listname)
    assert dlist != None
    return dlist

def getDialogFormat(dialname):
    i = Holder.getiServer()
    dInfo =  i.findDialog(Holder.getRequest(), dialname)
    assert dInfo != None
    return dInfo.getDialog()

def getColumnDescr(dlist,colid):
    """ Get column description from column list
        Args:
            dlist : ListForm, list description
            colid : column identifier
        Returns:
            (type,afterdot), cutil type (description) of the column and after dot number in case of numbers
    """
    
    e = DialogFormat.findE(dlist.getColumns(),colid)
    assert e != None
    tt = e.getFieldType()
    after = e.getAfterDot()
#    print colid,tt
    if tt == TT.BIGDECIMAL : return  (cutil.DECIMAL,after)
    if tt == TT.INT : return (cutil.LONG,0)
    if tt == TT.LONG : return (cutil.LONG,0)
    if tt == TT.BOOLEAN : return (cutil.BOOL,0)
    if tt == TT.DATE : return (cutil.DATE,0)
    if tt == TT.DATETIME : return (cutil.DATETIME,0)
    return (cutil.STRING,0) 


def createBlobDownload(realm,key,filename):
    return realm + ":" + key + ":" + filename

def startDialogToMap(var):
    xml = var["JUPDIALOG_START"]
    ma = xmlutil.toMap(xml)[0]
    return ma

def mapToStartDialog(var,ma,dialname):
    xml = xmlutil.mapToXML(ma)
    var["JUPDIALOG_START"] = xml
    var["JUP_DIALOG"]=dialname 

    

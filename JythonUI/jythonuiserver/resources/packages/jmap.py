from com.jythonui.server.holder import Holder

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

import sys
from datasource import DataSource
from com.jythonui.shared import ICommonConsts
import db2action
import re
import util

__DEFADATASCHEMA="defdataschema"

def execute_proc(var,exe,err_title) :
    da = DataSource()
    datasource = var["datasource"]
    ds = da.getDatasource(datasource)
    try :
      exe(ds,var) 
      var["JCLOSE_DIALOG"] = True
    except Exception,e:
      db2action.errormessage(ds,var,e,err_title)
  
  
def taskdescraction(action,var) :
  util.logaction(action,var)
        
  da = DataSource()
  
  if action == "before" :
    schema = da.getDefaultPar(__DEFADATASCHEMA)
    if schema == "" :
      datasource = var["datasource"]
      ds = da.getDatasource(datasource)
      schema = ds["user"]        
  
    var['schema'] = schema
    var['JCOPY_schema'] = True
    var['begintime'] = None
    var['JCOPY_begintime'] = True
    return
    
  if action == "signalchange" :
    schema = var['schema']
    if schema and schema != "" : da.setDefaultPar(__DEFADATASCHEMA,schema)
    return
    
  if action == "crud_add" :
    exe = lambda ds,var : db2action.addjob(ds,var) 
    execute_proc(var,exe,"Add job")    

  if action == "crud_change" :
    exe = lambda ds,var : db2action.modifjob(ds,var) 
    execute_proc(var,exe,"Change job")    
      
  if action == "crud_remove" :    
    exe = lambda ds,var : db2action.removejob(ds,var) 
    execute_proc(var,exe,"Remove job")      
       
def __torec(r) :
  name = r[0]
  begintime = r[4]
  endtime = r[5]
  maxi = r[6]
  schedule = r[7]
  schema = r[8]
  proc = r[9]
  param = r[10]
  updatetime = r[12]
  remarks = r[13]
  rec = {}
  rec["name"] = name
  rec["schema"] = schema
  rec["proc"] = proc
  rec["param"] = param
  rec["begintime"] = begintime
  rec["endtime"] = endtime
  rec["maxi"] = maxi
  rec["schedule"] = schedule
  rec["updatetime"] = updatetime
  rec["remarks"] = remarks
  return rec
          
def tasklistaction(action,var) :

  util.logaction(action,var)
  
  if action == "before" :
    util.setdefadatasource(var)
    return
         
  da = DataSource()
  
  if action == "signalchange" or action == "crud_readlist" :
    if not db2action.is_tasklist(var) :
      map = {}
      map["list"] = []
      var["JLIST_MAP"] = map
      return  
  
    result = db2action.executesql(var,"SELECT * from SYSTOOLS.ADMIN_TASK_LIST")
    if result == None : return
    seq = []
    for r in result :
          rec = __torec(r)
          seq.append(rec)
          
    map = {}
    map["list"] = seq
    var["JLIST_MAP"] = map 
    if action == "signalchange" : util.savedefadatasource (var)
                

import sys
from datasource import DataSource
from com.jythonui.shared import ICommonConsts
import db2action
import re
#import logging

__DEFADATASOURCE="defdatasource"
__DEFADATASCHEMA="defdataschema"


def logaction(action,var) :
#   logging.debug("dialogaction " + action)
#   for key in var :
#     logging.debug(key + " = " +  str(var[key]))
  pass


def dialogaction(action,var) :
    logaction(action,var) 
     
  
def connectionaction(action,var) :
   logaction(action,var)
   da = DataSource()
  
   if action == "before" or action == "crud_readlist" :
     map= {}   
     map["list"] = da.readList()
     var["JLIST_MAP"] = map  
     return
    
   datasource = var['datasource'] 
    
   if action == "crud_add" :
     if da.existDataSource(datasource) :
       var["JERROR_datasource"] = "Datasource name already in use"
       return  
       
   if action == "crud_add" or action == "crud_change" :
     map = {}
     for key in var.keys() :
       val = var[key]
       if key != 'datasource' and key != ICommonConsts.JLIST_NAME : map[key] = val  
     da.addDataSource(datasource,map)   
     var["JCLOSE_DIALOG"] = True      
     return
   
   if action == "crud_remove" :
     da.removeDataSource(datasource)
     var["JCLOSE_DIALOG"] = True     
     return
     
   if action == "connect" :
     try :
         db2action.testconnection(var)
         var["JOK_MESSAGE"] = "OK, connection successful"
     except Exception,e:
       print e
       var["JERROR_MESSAGE"] = str(e)
       var["JERROR_MESSAGE_TITLE"] = "Connection failed"
       
           
def setdefadatasource(var) :
  da = DataSource()
  datasource = da.getDefaultPar(__DEFADATASOURCE)
  if datasource == "" : return
  var['datasource'] = datasource
  var['JCOPY_datasource'] = True
  
def savedefadatasource(var) :
  da = DataSource()
  datasource = var['datasource']
  da.setDefaultPar(__DEFADATASOURCE,datasource)
  
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
  logaction(action,var)
        
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

  logaction(action,var)
  
  if action == "before" :
    setdefadatasource(var)
    return
         
  da = DataSource()
  
  if action == "signalchange" or action == "crud_readlist" :
    result = db2action.executesql(var,"SELECT * from SYSTOOLS.ADMIN_TASK_LIST")
    if result == None : return
    seq = []
    for r in result :
          rec = __torec(r)
          seq.append(rec)
          
    map = {}
    map["list"] = seq
    var["JLIST_MAP"] = map 
    if action == "signalchange" : savedefadatasource (var)
  
def connectiontypeaction(action,var) :

  seq = []
  da = DataSource()
  
  for ds in da.readList() :
      rec = {}
      rec['id'] = ds["datasource"]
      host = ds["host"]
      database = ds["database"]
      port = ds["port"]
      rec['name'] = host + ":" + str(port) + ":" + database
      seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map
    
   
def schemasaction(action,var) :
  logaction(action,var)
  result = db2action.executesql(var,"SELECT * from SYSCAT.SCHEMATA")
  if result == None : return 
  seq = []
  
  for r in result :
      rec = {}
      rec['id'] = r[0]
      seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  
      
def __get_statussize(var) :
   return db2action.getsize(var,"from SYSTOOLS.ADMIN_TASK_STATUS")
      
def __colto(f) :
   if f == "name" : return "NAME"
   if f == "taskid" : return "TASKID"
   if f == "status" : return "STATUS"
   if f == "invocation" : return "INVOCATION"
   if f == "begintime" : return "BEGIN_TIME"
   if f == "endtime" : return "END_TIME"
   if f == "agentid" : return "AGENT_ID"
   if f == "sqlcode" : return  "SQLCODE"
   if f == "sqlstate" : return "SQLSTATE"
   if f == "rc" : return "RC"
   return None
     
def __where(var,f) :
   if var["JSEARCH_SET_"+f] :
      ifrom = var["JSEARCH_FROM_" + f]
      ito = var["JSEARCH_TO_" + f]
      ieq = var["JSEARCH_EQ_" +f]
      col = __colto(f)
      if ito == None :
        if ieq : op = "=" 
        else : op = ">="
        return col + " " + op + " ?",[ifrom]
      return col + " >= ? AND " + col + " < ?",[ifrom,ito]  
      
   return None,None
   
def __whereclause(var) :
  list = ['name','taskid','status','invocation','begintime','endtime','agentid','sqlcode','sqlstate','rc']
  cla = None
  par = None
  for f in list :
    (s,p) = __where(var,f) 
    if s == None : continue
    if cla == None : cla = s
    else:  cla = cla + " AND " + s
    
    if par == None : par = p
    else : par = par + p
    
  return cla,par   
        
        
def __getfromstatus(var) :
      sql = 'from SYSTOOLS.ADMIN_TASK_STATUS' 
      (cla,par) = __whereclause(var)
      if cla == None : return (sql,None)
      sql = sql + ' WHERE ' + cla
      return sql,par        
     
def taskstatusaction(action,var) :
# select empno from emp where rownum >5 and rownum < 9
    logaction(action,var)
        
    da = DataSource()
          
    if action == "signalchange" :
      map = {}
      map["list"] = __get_statussize(var)
      var["JLIST_MAP"] = map
      
    if action == "listgetsize" :
      map = {}
      var["JLIST_MAP"] = map
      (sql,par) = __getfromstatus(var)
      map["list"] = db2action.getsize(var,sql,par)
    
    if action == "readchunk" : 
      start = var["JLIST_FROM"]
      size = var["JLIST_LENGTH"]
      sort = var["JLIST_SORTLIST"]
      asc = var["JLIST_SORTASC"]
      seq = []
      sort = __colto(sort)
      (sql,par) = __getfromstatus(var)
# important: ROWNUM starts from 1 (not 0)      
      bou = " ROWNUM > ? AND ROWNUM <= ? "
      if par == None : 
        sql = sql + " WHERE " + bou
        par = [start,start+size]
      else : 
        sql = sql + " AND " + bou
        par = par + [start,start+size]
      if sort != None : 
        sql = sql + " ORDER BY " + sort
        if not asc : sql = sql + " DESC"
      result = db2action.executesql(var,"SELECT * " + sql,par)
      if result == None : return
      for r in result :
           name = r[0]
           taskid = r[1]
           status = r[2]
           invocation = r[4]
           begin_time = r[5]
           end_time = r[6]
           agent_id = r[3]
           sql_code = r[7]
           sql_state = r[8]
           rc = r[10]
           rec = {}
           rec["name"] = name
           rec["taskid"] = taskid
           rec["status"] = status
           rec["invocation"] = invocation
           rec["begintime"] = begin_time
           rec["endtime"] = end_time
           rec["agentid"] = agent_id
           rec["sqlcode"] = sql_code
           rec["sqlstate"] = sql_state
           rec["rc"] = rc
           seq.append(rec)
      map = {}
      map["list"] = seq
      var["JLIST_MAP"] = map    
  
def showtask(action,var) :

    logaction(action,var)        
    
    da = DataSource()
    
    if action == "before" :
       name = var["name"] 
       result = db2action.executesql(var,"SELECT * from SYSTOOLS.ADMIN_TASK_LIST WHERE NAME=?",[name])
       r = result[0]
       rec = __torec(r)
       for key in rec :
         var[key] = rec[key]
         var["JCOPY_" + key] = True
       
       

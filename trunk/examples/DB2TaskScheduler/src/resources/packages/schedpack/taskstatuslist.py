import sys
from datasource import DataSource
from com.jythonui.shared import ICommonConsts
import db2action
import re
import util
  
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
    util.logaction(action,var)
    
    if action == "before" :
      util.setdefadatasource(var)
      return
    
        
    da = DataSource()
          
    if action == "signalchange" :
      if not db2action.is_tasklist(var) :
        map = {}
        map["list"] = 0
        var["JLIST_MAP"] = map
        return  
        
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

    util.logaction(action,var)        
    
    da = DataSource()
    
    if action == "before" :
       name = var["name"] 
       result = db2action.executesql(var,"SELECT * from SYSTOOLS.ADMIN_TASK_LIST WHERE NAME=?",[name])
       r = result[0]
       rec = __torec(r)
       for key in rec :
         var[key] = rec[key]
         var["JCOPY_" + key] = True
       
 
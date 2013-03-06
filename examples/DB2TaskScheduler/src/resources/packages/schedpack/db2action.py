from com.ziclix.python.sql import zxJDBC
import re
from datasource import DataSource
from com.ziclix.python.sql import DataHandler

class PyHandler(DataHandler):
    def __init__(self, handler):
        self.handler = handler
#        print 'Inside DataHandler'
    def getPyObject(self, set, col, datatype):
        return self.handler.getPyObject(set, col, datatype)
    def getJDBCObject(self, object, datatype):
#        print "handling prepared statement"
        return self.handler.getJDBCObject(object, datatype)
    def preExecute(self, stmt):
#        print "calling pre-execute to alter behavior"
        return self.handler.preExecute(stmt)


def formurl(var) :
     host = var["host"]
     port = var["port"]
     database = var["database"]
     url = "jdbc:db2://" +host +":" + str(port) +"/" + database 
     return url
     
def connect(var) :
    password = var["password"]
    database = var["database"]
    user = var["user"]
    
    jdbc_url = formurl(var)
    driver = "com.ibm.db2.jcc.DB2Driver"
    conn =  zxJDBC.connect(jdbc_url, user, password, driver)
    return conn
    
def testconnection(var) :
    conn = connect(var)  
    if conn == None : return
    conn.close()
    
    
def executeall(var,query,param=None) :

    print query,param
    
    c = None
    conn = None
    tran = False
    try :
      conn = connect(var)
      c = conn.cursor()
      if param == None: c.execute(query) 
      else : c.execute(query,param)
      seq = c.fetchall()
      tran = True
      conn.commit()
      return seq
    finally :  
      if c :
        c.close()
      if conn : 
        if not tran : conn.rollback()
        conn.close()

def executeproc(ds,proc,param) :
    print proc,param
    c = None
    conn = None
    tran = False
    try :
      conn = connect(ds)
      c = conn.cursor()
      c.datahandler = PyHandler(c.datahandler)
      c.callproc(proc,param)
      tran = True
      conn.commit()
    finally:  
      if c : c.close()
      if conn  :
        if not tran : conn.rollback()
        conn.close()
    
def addjob(ds,var) :
    name = var["name"]
    schema = var["schema"]
    proc = var["proc"]
    begintime = var["begintime"]
    endtime = var["endtime"]
    maxi = var["maxi"]
    remarks = var["remarks"]
    schedule = var["schedule"]
    
    input = var["param"]
    if input == "" : input = None
          
    param = [name,begintime,endtime,maxi,schedule,schema,proc,input,None,remarks]
#    executeproc(ds,'ADMIN_TASK_ADD_S',param)
    executeproc(ds,'ADMIN_TASK_ADD',param)
     
def removejob(ds,var) :
    name = var["name"]
    param = [name,None]
    executeproc(ds,'ADMIN_TASK_REMOVE',param)
    
def modifjob(ds,var) :
    name = var["name"]
    begintime = var["begintime"]
    endtime = var["endtime"]
    maxi = var["maxi"]
    remarks = var["remarks"]
    schedule = var["schedule"]
    param = [name,begintime,endtime,maxi,schedule,None,remarks]
    executeproc(ds,'ADMIN_TASK_UPDATE',param)       
     
def createfullerrmess(ds,e) :

   try :
     mess = str(e)
# warning: for some the code below does not work in Tomcat container
# but it works in standalone mode   
#    sqlcodes = re.findall(r'(?<=SQLSTATE=)\d*',"a")
     sqlcodes = re.findall(r'SQLSTATE=\d*',mess)
     for sqlcstate in sqlcodes :
       sqlc = re.search(r'\d+',sqlcstate).group()
       sqlerrm = "SELECT SYSPROC.SQLERRM (\'"+ sqlc +"\', \'\', \'\', NULL, 0) FROM DUAL"  
       seq = executeall(ds,sqlerrm)
       mess = mess + str(seq)
     return mess  
   except Exception,ee :
     return str(e) 
     
def errormessage(ds,var,e,title) :
   print e
   var["JMESSAGE_TITLE"] = title
   var['JERROR_MESSAGE'] = createfullerrmess(ds,e)     
     
def executesql(var,sql,param = None) :
    da = DataSource()
    datasource = var["datasource"]
    ds = da.getDatasource(datasource)
    try :
      result = executeall(ds,sql,param)
      return result
    except Exception,e :  
      errormessage(ds,var,e,sql)
      return None
     
def getsize(var,sql, par=None) :
      result = executesql(var,"SELECT COUNT(*) " +  sql,par)
      v = result[0]
      cou = v[0]
      print "size=",cou
      return cou
      
def is_tasklist(var) :
  result = executesql(var,"SELECT * FROM SYSCAT.TABLES WHERE TABSCHEMA='SYSTOOLS' AND TABNAME='ADMIN_TASK_LIST'")
  if result == None : return False
  return len(result) <> 0           
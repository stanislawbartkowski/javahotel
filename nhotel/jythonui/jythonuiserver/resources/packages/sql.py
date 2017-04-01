import java.sql
from java.sql import Date
import datetime

from com.jythonui.server.holder import Holder

import con,cutil,clog


def getDriverName() :
  con = __getConnection()
  s = con.getMetaData().getDatabaseProductName()
  con.close()
  if s == "Apache Derby" : return "derby"
  return "postgres"

def __getConnection() :
  return Holder.getJDBCConnection().getConnection()

def __getStatement() :
  con = __getConnection()
  return con.createStatement()
  
def __getPrepareStatementWithGen(sql,gencolumn,li=None) :
  con = __getConnection()
  st = con.prepareStatement(sql,[gencolumn,])
  return st
  
def __resolvePos(st,li) :
  if li == None : return
  pos = 0
  if type(li) != list :
      li = [li]  
  for l in li :
    pos = pos + 1
    if l == None : st.setObject(pos,None)
    elif type(l) is int or type(l) is long : st.setLong(pos,l)
    elif type(l) is float : st.setDouble(pos,l)
    elif type(l) is datetime.date :
      d = con.toDate(l)
      dsql = Date(d.getTime())
      st.setDate(pos,dsql)
    else : st.setString(pos,l)

def __close(st) :
  st.getConnection().close()

def executeUpdateSQLException(sql) :
  st = __getStatement()
  try :
    st.execute(sql)
  except java.sql.SQLException,e :  
#    print "Expected"
    clog.debug("Expected here",e)     
  finally:
    __close(st)
    
def insertSQLGenerated(sql,gencolumn,li=None) :
  st = __getPrepareStatementWithGen(sql,gencolumn)
  __resolvePos(st,li)
  st.executeUpdate()
  generatedKeys = st.getGeneratedKeys()
  generatedKeys.next()
  l =  generatedKeys.getLong(1)
  __close(st)
  return l

def __executeUpdateSQL(con, sql,li=None) :
  st = con.prepareStatement(sql)
  __resolvePos(st,li)
  st.executeUpdate()

def executeUpdateSQL(sql,li=None) :
  con = __getConnection()
  __executeUpdateSQL(con,sql,li)
  con.close()

def selectSQL(sql,param,li) :
  cone = __getConnection()
  st = cone.prepareStatement(sql)
  __resolvePos(st,param)
  res = st.executeQuery()
  resli = []
  if type(li) != list :
    li = [li]  
  while res.next() :
    ma = {}
    for l in li :
      val = None
      if l[2] == cutil.STRING : val = res.getString(l[1])
      elif l[2] == cutil.LONG : val = res.getLong(l[1])
      elif l[2] == cutil.DECIMAL : val = res.getDouble(l[1])
      else: 
        d = res.getDate(l[1])
        val = con.toJDate(d)
      ma[l[0]] = val
    resli.append(ma)
  cone.close()
  return resli  
        
      
class TRANS :
  
  def __init__(self) :
    pass
      
  def executeUpdateSQL(self,sql,li = None) :
    __executeUpdateSQL(self.con,sql,li)
  
  def dotrans(self) :
    self.con = __getConnection()
    self.con.setAutoCommit(False)
    try :
      self.dojob()
      self.con.commit()
    except java.sql.SQLException,e :  
      print e
      self.con.rollback()
    except Exception,e :
      print e
      self.con.rollback()
    finally:
      self.con.setAutoCommit(True)
      self.con.close()

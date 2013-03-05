import db2action
import util
from datasource import DataSource

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
  util.logaction(action,var)
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
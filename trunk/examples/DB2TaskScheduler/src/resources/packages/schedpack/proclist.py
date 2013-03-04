from datasource import DataSource
import db2action

def dialogaction(action,var) :
  print "proclist",action
  if action == "spprocs" :
    schema = var["schema"]
    query = "SELECT ROUTINENAME FROM SYSCAT.ROUTINES WHERE ROUTINETYPE = 'P' AND ROUTINESCHEMA = '" + schema + "'"
    print query    
    seq = db2action.executesql(var,query)
    if seq == None : return
    rlist = []
    for rec in seq :
      procname = rec[0]
      map = {}
      map["procname"] = procname
      rlist.append(map)
      
    map= {}   
    map[action] = rlist
    var["JLIST_MAP"] = map  
    return
      

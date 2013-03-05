import sys
from datasource import DataSource
from com.jythonui.shared import ICommonConsts
import db2action
import re
import util

       
def connectionaction(action,var) :
   util.logaction(action,var)
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
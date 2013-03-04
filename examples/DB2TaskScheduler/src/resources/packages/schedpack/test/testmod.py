import sys;
from schedpack.datasource import DataSource

def dialogaction(action,var) :

   print action
   da = DataSource()
   
   if action == "readlist" :
     map= {}   
     map["list"] = da.readList()
     var["JLIST_MAP"] = map  
     return
     
   if action == "writedatasource" or action == "changedatasource" :
     datasource = None
     map = {}
     for key in var.keys() :
       val = var[key]
       if key == 'datasource' : datasource = val
       else : map[key] = val  
     da.addDataSource(datasource,map)    
     return

   if action == "deletedatasource" :
     datasource = var['datasource']
     da.removeDataSource(datasource)
     return
     
   if action == "cleardefapar" :
     da.removeDefaultPar('par1')
     da.removeDefaultPar('par2')
     
     
   if action == "readdefaparam" :
     para1 = da.getDefaultPar('par1')
     para2 = da.getDefaultPar('par2')
     var['par1'] = para1
     var['par2'] = para2
     
     
   if action == "writedefaparam" :
     da.setDefaultPar('par1','val1')
     da.setDefaultPar('par2','val2')

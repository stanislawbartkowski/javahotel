from testpack import list
from cutil import printVar

#<import>from testpack import list</import>
#        <method>list.dialogaction({0},{1})</method>

def dialogaction(action,var) :

  printVar("dialog1",action,var)
  
  if action == "run" :
    seq = ['name1','name2']
    for n in seq :
      var['JCOPY_' + n + '_out'] = True
      var[n + '_out'] = var[n]
      return
    
  if action == "execute" :
     var["JEXECUTE_ACTION"] = "dosomething"
     var["JUPDIALOG_RES"] = "param"
     return
 
  list.dialogaction(action,var)
   
    

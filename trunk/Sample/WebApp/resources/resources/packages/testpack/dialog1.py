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
 
  list.dialogaction(action,var)
   
    

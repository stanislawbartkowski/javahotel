import datetime
import cutil

def dialogaction(action,var) :
  print "test",action

  if action == 'setInt' :
    var['globdecimal'] = 101
    
  if action == 'setFloat' :
    var['globdecimal'] = 101.84
    
def dialogactiondate(action,var) :
  print "test ", action
  da = var['globdate'] 
  print da
  dir(da)
  if action == 'setDate' :
    var['globdate'] = datetime.date(2001,11,5)

def dialogactiontime(action,var) :
  cutil.printVar("dialogactiontime",action,var)  
  da = var['globtimestamp'] 
  print da
  dir(da)
  if action == 'setDateTimeOnly' :
    var['globtimestamp'] = datetime.date(2013,01,13)
    
  if action == 'setTimeOnly' :
    var['globtimestamp'] = datetime.datetime(2017,01,13,12,13,14)

  if action == 'setTimeOnly23' :
#    var['globtimestamp'] = datetime.datetime(2017,4,13,12,13,14)
    var['globtimestamp'] = datetime.datetime(2001, 10, 2, 23, 4, 6)
    print var['globtimestamp']
    
    for i in range(12) :
      var['globtimestamp' + str(i+1)] = datetime.datetime(2001, i+1, 2, 23, 4, 6)
      print var['globtimestamp' + str(i+1)]
        
       
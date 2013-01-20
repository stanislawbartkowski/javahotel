import datetime

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
  print "test time date ", action
  da = var['globtimestamp'] 
  print da
  dir(da)
  if action == 'setDateTimeOnly' :
    var['globtimestamp'] = datetime.date(2013,01,13)
    
  if action == 'setTimeOnly' :
    var['globtimestamp'] = datetime.datetime(2017,01,13,12,13,14)
       
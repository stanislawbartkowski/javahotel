import datetime
import cutil,con

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
    var['globdate'] = con.jDate(2001,11,5)

def dialogactiontime(action,var) :
  cutil.printVar("dialogactiontime",action,var)  
  da = var['globtimestamp'] 
  print da
 # if da != None : print da.tzinfo
  dir(da)
  if action == 'setDateTimeOnly' :
    var['globtimestamp'] = con.jDate(2013,01,13)
    
  if action == 'setTimeOnly' :
    da = con.jDate(2017,01,13,12,13,14)
    print da,da.tzinfo
    var['globtimestamp'] = da

  if action == 'setTimeOnly23' :
#    var['globtimestamp'] = datetime.datetime(2017,4,13,12,13,14)
    da1 = con.jDate(2017,01,13,12,13,14)
    print da1,da1.tzinfo
    da = con.jDate(2001, 10, 2, 23, 4, 6)
    print da,da.tzinfo
    var['globtimestamp'] = da
    print "jython=",var['globtimestamp']
    
    
  if action == 'setTimeList' :      
    for i in range(12) :
      var['globtimestamp' + str(i+1)] = con.jDate(2001, i+1, 2, 23, 4, 6)
      print var['globtimestamp' + str(i+1)]
      
  if action == 'setTimeList' :
      li = []
      for i in range(24) : 
          ti = datetime.datetime(2001, 10, 2, i, 4, 6)
          li.append({ "ti" : ti})
      cutil.setJMapList(var,"lista",li)

        
       
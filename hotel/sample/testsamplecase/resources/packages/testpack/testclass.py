
def dialogaction(action,var) :
  print "test",action
  print "aaaa"
  print "bbbb"
  for k in var.keys() : 
    print k
    print var[k]
  
  var['glob1'] = 'aaaa'
  var['JCOPY_glob1'] = True
  

def dialogaction1(action,var) :
  print action
  i = var['globint']
  
  if action == 'testnone' :
    if i == None :
      var['result'] = 'IsNone'
      
  if action == 'inc' :
    var['globint'] = i+1
  
def dialogaction2(action,var) :
  print action
  i = var['globlong']
  if action == 'retlong' :
    return
    
  if action == 'setvalue' :
    var['globlong'] = 99

def dialogaction(action,var) :

  print "list",action
  for k in var.keys() : 
    print k
    print var[k]
    seq = ['name1','name2']
    for n in seq :
      var['JCOPY_' + n + '_out'] = True
      var[n + '_out'] = var[n]
    

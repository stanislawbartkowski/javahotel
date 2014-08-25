import cutil

def dialogaction(action,var) :
  cutil.printVar("lsitnowrap",action,var)
  
  if action == "before" :
    seq = []
    m = { "text" : "aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuwwvvxxyyzz", "numb" : 10 }
    seq.append(m)
    m = { "text" : "123456789012345678901234567890", "numb" : 11 }
    seq.append(m)
    cutil.setJMapList(var,"list",seq)
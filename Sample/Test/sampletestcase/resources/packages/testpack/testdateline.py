import datetime

def dialogaction(action,var) :
  print "testchecklist",action
  for k in var.keys() : 
    print k, var[k]
    
  if action == "before" :
       seq = []
       for i in range(30) :
           map = { "id" : i, "displayname" : "disp " + str(i)}
           seq.append(map)
       var["JDATELINE_MAP"] = {"dateline" : { "linedef" : seq}}
       
  if action == "datelinevalues" :
       seq = var["JDATELINE_QUERYLIST"]
       for s in seq :
           print s
       vals = []
       for i in range(1,10) :
           d = datetime.date(2013,1,2)
           map = {"id" : i, "datecol" : d, "form" : "rybka", "0" : "super", "1" : i}
           vals.append(map)
       var["JDATELINE_MAP"] = {"dateline" : { "values" : vals}}

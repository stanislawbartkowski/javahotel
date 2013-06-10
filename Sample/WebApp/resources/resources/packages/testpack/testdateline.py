

def dialogaction(action,var) :
   print action
   if action == "before" :
       seq = []
       for i in range(30) :
           map = { "id" : i, "displayname" : "disp " + str(i)}
           seq.append(map)
       var["JDATELINE_MAP"] = {"dateline" : seq}    
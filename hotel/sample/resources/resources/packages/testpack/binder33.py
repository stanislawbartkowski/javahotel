import cutil,xmlutil,con

def dialogaction(action,var) :
  cutil.printVar("binder32",action,var)

  if action == "before" :
      cutil.setCopy(var,"datePicker")
      var["datePicker"] = con.jDate(2016,3,5)
      
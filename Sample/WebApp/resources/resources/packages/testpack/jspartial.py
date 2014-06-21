import cutil

def dialogaction(action,var) :
  cutil.printVar("JavaScript partial",action,var)
  
  if action == "superaction" :
    var["JOK_MESSAGE"] = "Hello, I'm your Jython, I'm happy to be here"


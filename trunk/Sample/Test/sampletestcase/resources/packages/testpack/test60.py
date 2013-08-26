from cutil import printVar
import datetime

def dialogaction(action,var) :
  printVar("test60",action,var)
  if action == "before" :
      var["glob1"] = "Hello"
      var["globbool"] = True
      var["globint"] = 123
      var["globcust"] = "abc"
      var["globdec"] = 12.5612
      var["globdate"] = datetime.date(2001,10,2)
      var["globtime"] = datetime.datetime(2001, 10, 2, 23, 4, 6)
      seq = []
      for i in range(10) :
          seq.append({ "id" : i, "name" : "name" + str(i)})
      var["JLIST_MAP"] = { "lista" : seq }  
        
  if action == "setxml" :
      var["JXMLCONTENT"] = var["XML"]
      var["JXMLCONTENTSET"] = True
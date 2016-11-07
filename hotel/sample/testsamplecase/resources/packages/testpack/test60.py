from cutil import printVar
import datetime
from xmlutil import getVar
import con

def dialogaction(action,var) :
  printVar("test60",action,var)
  if action == "before" :
      var["glob1"] = "Hello"
      var["globbool"] = True
      var["globint"] = 123
      var["globcust"] = "abc"
      var["globdec"] = 12.5612
#      var["globdate"] = datetime.date(2001,10,2)
#      var["globtime"] = datetime.datetime(2001, 10, 2, 23, 4, 6)
      da = con.jDate(2001,10,2)
      print da,da.day      
      var["globdate"] = da
      var["globtime"] = con.jDate(2001, 10, 2, 23, 4, 6)
      seq = []
      for i in range(10) :
          seq.append({ "id" : i, "name" : "name" + str(i)})
      var["JLIST_MAP"] = { "lista" : seq }  
      print "****************"
        
  if action == "setxml" :
      var["JXMLCONTENT"] = var["XML"]
      var["JXMLCONTENTSET"] = True
      
  if action == "checkxml" :
      xml = var["XML"]
      dname = var["J_DIALOGNAME"]
      map = {}
      getVar(map,dname,xml,["glob1","globbool","globint","globdec","globdate","globtime"])
      print map
      assert "Hello" == map["glob1"]
      assert map["globbool"]
      assert 123 == map["globint"]
      assert 12.5612 == map["globdec"]
#      assert datetime.date(2001,10,2) == map["globdate"]
#      assert datetime.datetime(2001,10,2,23,4,6) == map["globtime"]
      assert con.jDate(2001,10,2) == map["globdate"]
      assert con.jDate(2001,10,2,23,4,6) == map["globtime"]
      
  if action == "testglobdate" :
      var["globdate"] = con.jDate(2001,10,2)
      
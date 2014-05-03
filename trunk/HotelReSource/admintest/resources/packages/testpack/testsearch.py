import cutil
from util import rutil
import datetime
import con

def dialogaction(action,var) :
  cutil.printVar("search",action,var)
  
  if action == "testsearch" :
      dfrom = datetime.date(2014,2,1)
      dto = datetime.date(2014,2,5)
      l = rutil.searchForRooms(var,dfrom,dto)
      for r in l :
          assert r != None
          print r
      assert len(l) == 2  
      dto = con.incDays(dfrom)
      l = rutil.searchForRooms(var,dfrom,dto)      
      print l
      assert len(l) == 3  
      dto = con.incDays(dfrom,2)
      l = rutil.searchForRooms(var,dfrom,dto)      
      print l
      assert len(l) == 2
      var["OK"] = True    
      
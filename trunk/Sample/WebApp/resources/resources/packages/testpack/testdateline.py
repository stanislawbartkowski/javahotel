from com.jython.ui.server.guice import ServiceInjector
from java.util import Date
import datetime
from java.util import Calendar
from cutil import printVar
from cutil import toDate
import cutil

class ElemOp :
    
    def __init__(self):
        self.op = ServiceInjector.constructDateOp()
        
    def clearAll(self):
        self.op.clearAll()
       
    def findElem(self,id,dt):
        return self.op.findElem(id,toDate(dt))
    
    def addormodifElem(self,id,dt,numb,info):
        self.op.addormodifElem(id,dt,numb,info)
        
    def removeElem(self,id,dt):
        self.op.removeElem(id,dt)            

COLS=["aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy", "olive", "orange", "purple", "red", "silver", "teal", "white", "yellow"]


def createcolor(action,var):
    
  seq = []
  i = 0
  for c in COLS :
        rec = {"id" : str(i), "name" : c }
        seq.append(rec)
        i = i + 1
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  


def dialogclickaction(action,var) :
   op = ElemOp()
   print action
   if action == "before" :
       id = var["JDATELINE_LINE"]
       date = var["JDATELINE_DATE"]
       print id,date
       var["id"] = id
       var["datecol"] = date
       var["JCOPY_id"] = True       
       var["JCOPY_datecol"] = True
       print id,date
       elem = op.findElem(id, date)
       info = None
       numb = 0
       if elem :
           info = elem.getInfo()
           numb = elem.getNumb()
       var["JCOPY_info"] = True
       var["info"] = info
       var["JCOPY_color"] = True
       var["color"] = str(numb)
       
   if action == "addinfo" :
       id = var["id"]
       date = var["datecol"]
       info = var["info"]
       col = var["color"]
       
       op.addormodifElem(id,toDate(date),int(col),info)
       var["JREFRESH_DATELINE_dateline"] = ""
       var["JCLOSE_DIALOG"] = True
       

def dialogaction(action,var) :
   printVar("dialog action", action, var)
   
   if action=="clear" :
    yes = var['JYESANSWER']
    if not yes : return
    op = ElemOp()
    op.clearAll()
    var["JREFRESH_DATELINE_dateline"] = ""
       
   if action == "before" :
       seq = []
       for i in range(30) :
           map = { "id" : i, "displayname" : str(i)}
           seq.append(map)
       var["JDATELINE_MAP"] = {"dateline" : { "linedef" : seq}}

   if action == "datelinevalues" :
       op = ElemOp()
       seq = var["JDATELINE_QUERYLIST"]
       vals = []
       for s in seq :
#           print s
           dl = datetime.timedelta(1)
           dfrom = s["JDATELINE_FROM"]
           dto = s["JDATELINE_TO"]
           i = s["id"]
           d = dfrom
           while d < dto :
             elem = op.findElem(i, d)
             if elem :
               colno = 0
               if elem.getNumb() <= len(COLS) : colno = elem.getNumb()
               s = elem.getInfo()
#               if s == None : s = " "
               map = {"id" : i, "datecol" : d,"colspan" : 2, "form" : "name2", "0" : COLS[colno], "1" : s}
               vals.append(map)
             d = d + dl
       print "========="
       var["JDATELINE_MAP"] = {"dateline" : { "values" : vals}}
       printVar("before final",action,var)
    
def searchaction(action,var) :
   cutil.printVar("search",action,var)  
   
   if action=="find" :
       if var["da"] : var["JDATELINE_GOTO_dateline"] = var["da"]
       if var["name"] : var["JSEARCH_LIST_SET_dateline_displayname"] = var["name"]


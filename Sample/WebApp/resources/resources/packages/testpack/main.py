import datetime
import cutil

def dialogaction(action,var) :

  cutil.printVar("main",action,var)
    
  if action == "main" :
    var["JMAIN_DIALOG"] = "start.xml"  
    
  if action == "list" :
    var["JMAIN_DIALOG"] = "list.xml"  
    
  if action == 'tomain' :
    seq = ['glob1','globbool','globint','globdecimal','globdate','globdatetime']
    for s in seq :
      print s,var[s]
    var["JERROR_glob1"] = "EYYYYYY"  
    var['JUP_DIALOG'] = 'copymain.xml'
      
        
    
  if action == "before" :
    for k in var.keys() : 
      print k
      print var[k]
  
    var['glob1'] = 'Company'
    var['JCOPY_glob1'] = True
  
    var['globbool'] = False;
    var['JCOPY_globbool'] = True
  
    var['globint'] = 12;
    var['JCOPY_globint'] = True
  
    var['globlong'] = 12456;
    var['JCOPY_globlong'] = True
  
    var['globdecimal'] = 1789.65;
    var['JCOPY_globdecimal'] = True
  
    var['globdate'] = datetime.date(2001,11,5);
    var['JCOPY_globdate'] = True
    
    ti = datetime.datetime(2017,01,13,20,45,14)
    var['globdatetime'] = ti
    var['JCOPY_globdatetime'] = True
  
  
def textaction(action,var) :
  print "text",action
  if action == "before" :
    var["timed"] = datetime.datetime(2017,01,13,20,45,14)
    var['JCOPY_timed'] = True

  if action == "butt" :
    for k in var.keys() : 
      print k,var[k]
    seq = ['timed','textf','text4']
    for n in seq :
      var['JCOPY_' + n + '_out'] = True
      var[n + '_out'] = var[n]
      
        
def dialogactionpoly(action,var) :
  cutil.printVar("main poly",action,var)
  
  if action == "before" :
     cutil.setCopy(var,"glob1") 
     var["glob1"] = "Hello, I'm here"
     
  if action == "seterror" :
     cutil.setErrorField(var,"glob1","@cannotbeempty")

  if action == "setenable" :
     cutil.enableField(var,"glob1",True);

  if action == "setdisable" :
     cutil.enableField(var,"glob1",False);
     
  if action == "sethidden" :
     cutil.hideButton(var,"seterror")

  if action == "setvisible" :
     cutil.hideButton(var,"seterror",False)
    
  
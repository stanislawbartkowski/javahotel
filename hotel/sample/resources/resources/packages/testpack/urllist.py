import cutil

def dialogaction(action,var) :
  cutil.printVar("url list",action,var)
  
  if action == "before" :
    val = cutil.urlPar(var,"superpar")
    var["urlsuperpar"] = val
    cutil.setCopy(var,"urlsuperpar")
    l = []
    for k in cutil.urlParList(var) :
      val = cutil.urlPar(var,k)
      l.append({"ukey" : k, "uvalue" : val})
    cutil.setJMapList(var,"list",l)
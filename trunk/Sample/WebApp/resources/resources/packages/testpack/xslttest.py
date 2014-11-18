import cutil,pdfutil,xmlutil


def dialogaction(action,var) :
  cutil.printVar("xslt test",action,var)
  
  if action == "before" :
    xml = "<root></root>"
    html = pdfutil.xsltHtmlS("dialogs/xslt/testdata.xslt",xml)
    s = ""
    for i in range(10) :
#      print i,ord(html[i]),html[i]
      s = s + " " + str(i) + "=" + str(ord(html[i]))
    s1 = str(type(html))
    cutil.setCopy(var,["info","info1","info2"])
    var["info"] = html
    var["info1"] = s
    var["info2"] = s1

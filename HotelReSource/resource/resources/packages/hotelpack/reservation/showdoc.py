import cutil,con,pdfutil,xmlutil

from rrutil import reseparam

LI=["doctype","docid","resid","issuedate","name1","name2","country","saledate","addinfo","nofguests","arrivaldate","departuredate","roomnumber",\
     "roomtype","dailyrate","amount","description","total"]

LIST="details"
T="total"

def dialogaction(action,var) :
  
  cutil.printVar("show doc",action,var)
  
  if action == "before" :
    xml = var["JUPDIALOG_START"]
    okonly = var["JUPDIALOG_STARTPAR"] == "1"
    if okonly:
      cutil.hideButton(var,["accept","resign"])
      cutil.hideButton(var,"ok",False)
      
    reseparam.setXMLParam(var,xml)
    (va,li) = xmlutil.toMap(xml)
    for l in LI :
      var[l] = va[l]
    cutil.setCopy(var,LI)
    cutil.setJMapList(var,LIST,li)
    cutil.setFooter(var,LIST,T,va[T])
    
  if action=="accept" and var["JYESANSWER"] :
     var["JCLOSE_DIALOG"] = reseparam.getXMLParam(var)

import cutil
import jmap 
import xmlutil
import datetime


#            <column id="avail" type="bool" displayname="A" hidden="" />
#            <column id="resday" type="date" displayname="@night" editable="" />
#            <column id="chooseroom" imagecolumn="1" displayname=""
#                imagelist="default_helpicon.gif" />
#            <column id="resroomname" displayname="@roomname" editable=""
#                type="custom:allroomslist" helper="search" />


def dialogaction(action,var) :
  cutil.printVar("jaction",action,var)
  
  if action == "testmap" :
    l = jmap.getMapFieldList("test85.xml")
    print l
    assert "glob1" in l
    assert not ("xxx" in l)
    
  if action == "testlistmap" :
    l = jmap.getMapFieldList("test85.xml","reslist")
    print l
    assert "avail" in l
    assert len(l) == 4
    assert not ("xxx" in l)
    
  if action == "testxmllist" :
      li = []
      for i in range(1,20) :
          ma = {}
          ma["avail"] = True
          ma["resday"] = datetime.date(2014,2,1)
          ma["chooseroom"] = 'X'
          ma["resroomname"] = "Hello"
          ma["add"] = 345
          li.append(ma) 
          
      xml = xmlutil.toXML({},li)
      assert xml != None
      print xml
      (ma,li) = xmlutil.toMap(xml)
      print li
      for l in li :
          assert l.has_key("add")
      (ma,li1)  = xmlutil.toMapFiltrDialL(xml,"test85.xml","reslist")
      print li1
      assert len(li) == len(li1)
      for l in li1 :
          assert not l.has_key("add")
      
      

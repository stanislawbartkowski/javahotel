import cutil,con
import time
import datetime

import time as _time
from datetime import timedelta
from datetime import tzinfo

def dialogaction(action,var):
    
    cutil.printVar("test46",action,var)
    
    if action == "test1" :
        li = cutil.JOURNAL(var).getList()
        for j in li : 
            print j.getCreationPerson(),j.getJournalType(),j.getOObjectId()
            da = j.getCreationDate()
            print da,type(da)
            assert "user" == j.getCreationPerson()
        assert 1 == len(li)
        var["OK"] = True
            
    if action == "test2" :
        li = cutil.JOURNAL(var).getList()
        logout = False
        for j in li : 
            print j.getCreationPerson(),j.getJournalType()
            assert "user" == j.getCreationPerson()
            if j.getJournalType() == "LOGOUT" : logout = True
        assert 3 == len(li)
        assert logout
        var["OK"] = True
        
    if action == "test3" :
        jou = cutil.JOURNAL(var).getList()
        print len(jou)
        li = []
        for j in jou :
            ma = { "name" : j.getName(),"date" : j.getCreationDate(), "hotelname" : j.getOObjectId(),"journaltype" : j.getJournalType(),"user" : j.getCreationPerson() }
            li.append(ma)
        cutil.setJMapList(var,"list",li)
        assert 3 == len(li)
        var["OK"] = True

    if action == "test4" :
        jou = cutil.JOURNAL(var).getList()
        print len(jou)
        li = []
        for j in jou :
            da = j.getCreationDate();
            print da,type(da)
            jda = con.toJDateTime(da)
#            jda = jd.replace(tzinfo=l)
 #           print "jda=",jda,type(jda)
            tda = time.time()
            print tda,type(tda)
            jjda = datetime.datetime.fromtimestamp(time.time())
            print jjda,type(jjda)
            ma = { "name" : j.getName(),"date" : da, "hotelname" : j.getOObjectId(),"journaltype" : j.getJournalType(),"user" : j.getCreationPerson(), "jdate" : jda }
            li.append(ma)
        cutil.setJMapList(var,"list1",li)
#        assert 3 == len(li)
        var["OK"] = True

import cutil
from util import rutil
from util import util
from util import rpdf
import datetime
import con
import pdfutil
from util import hmail

def dialogaction(action,var):
    cutil.printVar("testresemail",action,var)
    
    if action == "createxml" :
        R = util.ROOMLIST(var)
        C = util.CUSTOMERLIST(var)
        RES = util.RESFORM(var)
        
        E =  util.newHotelRoom()
        E.setName("R01")
        R.addElem(E)
        CC = util.newCustomer(var)
        CC.setDescription("I'm the customer")
        CC = C.addElem(CC)
        print CC.getName()
        dt = datetime.date(2014,1,2)
        dl = datetime.timedelta(1)
#        dto = datetime.date(2010,1,5)
#        q = rutil.createResQueryElem("R01",dfrom,dto)
#        print q
        re = util.newResForm(var)
        re.setCustomerName(CC.getName())
        for i in range(10) :
           r = util.newResAddPayment()
           r.setNoP(2)
           r.setPrice(con.toB(100.0))
           r.setPriceTotal(con.toB(100.0));
           r.setPriceList(con.toB(200.0));
           r.setRoomName("R01");
           r.setResDate(con.toDate(dt));            
           dt = dt + dl
           re.getResDetail().add(r)
           
        re = RES.addElem(re) 
        print re.getName()
        s = rpdf.buildResXML(var,re.getName())
        print s
        tt = pdfutil.xsltHtml("mailxslt/reseconfirmation.xslt",s)
        print tt.toString()
        H = hmail.HotelMail(var)
#        H.    def sendMail(self,mtype,resname,custname,subject,content,to,froma,attachList=None,text=True):
        hh = H.sendMail(0,re.getName(),CC.getName(),"Reservation",tt.toString(),"stanislawbartkowski@gmail.com","hotel")
        assert hh != None        
        var["OK"] = True
           
    if action == "listxml" :
        H = hmail.HotelMail(var)
        li = H.getList()
        assert len(li) == 1
        for l in li :
            print l
            print l.getName()
            print l.getReseName()
        var["OK"] = True
            
       
         
        

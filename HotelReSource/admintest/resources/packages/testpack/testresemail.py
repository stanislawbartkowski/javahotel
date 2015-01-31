import cutil,con
from util import rutil,util,rpdf,hmail,cust
import datetime
import pdfutil
from util import hmail
import cmail

def dialogaction(action,var):
    cutil.printVar("testresemail",action,var)
    
    if action == "createxml" :
        R = util.ROOMLIST(var)
        C = util.CUSTOMERLIST(var)
        RES = util.RESFORM(var)
        
        E =  util.newHotelRoom()
        E.setName("R01")
        R.addElem(E)
        CC = cust.newCustomer(var)
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
           r.setVat("7%")
           r.setResDate(con.toDate(dt));            
           dt = dt + dl
           re.getResDetail().add(r)
           
        re = RES.addElem(re) 
        print re.getName()
        s = rpdf.buildXMLReservation(var,re.getName())
        print s
        tt = pdfutil.xsltHtmlS("mailxslt/reseconfirmation.xslt",s)
        print tt
        H = hmail.HotelMail(var)
#        H.    def sendMail(self,mtype,resname,custname,subject,content,to,froma,attachList=None,text=True):
        hh = H.sendMail(0,re.getName(),CC.getName(),"Reservation",tt,"stanislawbartkowski@gmail.com","hotel")
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
        
    if action == "testfailed" :
        H = hmail.HotelMail(var)
        li = H.getList()
        h = li[0]
        # should fail because to space in 'from' address
        hh = H.sendMail(0,h.getReseName(),h.getCustomerName(),"Reservation","Hi Hi - good joke","stanislawbartkowski@gmail.com","Java hotel")
        assert hh != None
        mailName = hh.getName()
        print mailName
        M = H.getCMail(mailName)
        assert M != None
        res = M.getSendResult()
        print res
        assert res != None
        var["OK"] = True 
        
            
            
       
         
        

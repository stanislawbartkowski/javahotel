import cutil
import util
import cmail
from com.gwthotel.hotel.mailing import HotelMailElem

class HotelMail(util.HOTELMAILLIST):
    
    def __init__(self,var):
        util.HOTELMAILLIST.__init__(self,var)
        self.M = cmail.CMAIL(var)

    # mtype 0 : reservation confirmation         
    def sendMail(self,mtype,resname,custname,subject,content,to,froma,attachList=None,text=True):
        if mtype == 0 : mtype = HotelMailElem.MailType.RESCONFIRMATION
        if mtype == 1 : mtype = HotelMailElem.MailType.RECEIPTSENT
        res = self.M.sendMail(subject,content,to,froma,attachList,text)
        assert res != None
        name = res.getName()
        assert name != None
        H = util.newHotelMailElem()
        H.setmType(mtype)
        H.setCustomerName(custname)
        H.setName(name)
        H.setReseName(resname)
        hres = self.addElem(H)
        return hres

    def getCMail(self,name) :
        CC = self.M.findElem(name)
        assert CC != None
        return CC

    def getListForCustomer(self,custname) :
        li = self.getList()
        out = []
        for l in li : 
	  if l.getCustomerName() == custname : out.append(l)
	return out  
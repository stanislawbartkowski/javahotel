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
        res = self.M.sendMail(subject,content,to,froma,attachList,text)
        print res
        assert res != None
        name = res.getName()
        print name
        assert name != None
        H = util.newHotelMailElem()
        H.setmType(mtype)
        H.setCustomerName(custname)
        H.setName(name)
        H.setReseName(resname)
        hres = self.addElem(H)
        return hres


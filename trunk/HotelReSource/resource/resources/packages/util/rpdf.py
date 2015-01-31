import datetime

import con,cutil,xmlutil,vat

import util,rutil,dutil

def _getCountryName(var,id):
    if id == None : return None
    name =  cutil.getDicName("countries",id)
    if name == None : return id
    return name
   
def _XaddMapElem(destm,tag,m,key):
    if type(tag) != list : destm[tak] = m.getAttr(key)
    else : 
        for i in range(0,len(tag)) : destm[tag[i]] = m.getAttr(key[i])       

def _XbuildElemXML(var,ma,r,CV=None):
    da = con.toJDate(r.getServDate())
    roomname = r.getRoomName()
    desc = util.ROOMLIST(var).findElem(roomname).getDescription()
    rate = r.getPrice()
    amount = r.getNoP()
    total = r.getPriceTotal()
    ma["date"] =da
    ma["roomnumber"] = roomname
    ma["description"] = desc
    ma["dailyrate"] = rate           
    ma["amount"] = amount
    ma["total"] = total
    ma["id"] = r.getId()
    if CV != None :
        vats = rutil.getVatName(var,r)
        (netto,vatv,vatlevel) = CV.calculateVatValue(con.BigDecimalToDecimal(total),vats)
        ma["netvalue"] = netto
        ma["taxvalue"] = vatv
        ma["taxlevel"] = vatlevel
        ma["tax"] = vats    
        ma["grossvalue"] = con.addDecimal(netto,vatv)    

def _XbuildHeaderXML(var,ma,nog,r,custname):
    (arrival,departure,roomname,rate,numberofnights) = rutil.getReseDate(var,r)
    room = util.ROOMLIST(var).findElem(roomname)
    p = util.CUSTOMERLIST(var).findElem(custname);
    _XaddMapElem(ma,["name1","name2","address","city"],p,["firstname","surname","street","city"])       
    ma["country"] = _getCountryName(var,p.getAttr("country"))
    ma["roomnumber"] = roomname
    ma["dailyrate"] = rate
    ma["roomtype"] = room.getDescription()
    ma["arrivaldate"] = arrival
    ma["departuredate"] = departure
    ma["nofguests"] = nog
    ma["resid"] = r.getName()
    ma["issuedate"] = con.toJDate(r.getCreationDate())
    ma["saledate"] = con.today()
    ma["addinfo"] = ""
    ma["amount"] = numberofnights
    ma["description"] = ""
    
class _BI(rutil.BILLSCAN):
    
    def __init__(self,bli,var):
        rutil.BILLSCAN.__init__(self,bli)
        self.var = var
        self.seq = []
        self.CV = vat.CalcVat()
        self.grossvalue = 0
        
    def walk(self,idp,pa):
        mx = {}
        _XbuildElemXML(self.var,mx,pa,self.CV)
        self.seq.append(mx)
        self.grossvalue = con.addDecimal(self.grossvalue,mx["grossvalue"])
    
def _buildXMLForServices(var,name,resename,payername,pli) :
    ma = {}
    r = util.RESFORM(var).findElem(resename)
    nog = len(util.RESOP(var).getResGuestList(resename))
    li = rutil.getPayments(var,resename)
    _XbuildHeaderXML(var,ma,nog,r,payername)
    sum = rutil.countTotalForServices(var,pli,li)
    ma["doctype"] = "I"
    if name == None : ma["docid"] = resename
    else : ma["docid"] = name    
    
    ma["total"] = sum

    B = _BI(pli,var)
    B.scan(li)
    ma["grossvalue"] = B.grossvalue
    
    return (ma,B.seq)
     
def _buildXML(var,name) :
    ma = {}
    b = util.BILLLIST(var).findElem(name)
    resename = b.getReseName()
    return _buildXMLForServices(var,name,resename,b.getPayer(),b.getPayList())

def buildXMLBill(var,name):
    """ Create XML for bill 
    Args:
        var
        name : bill name 
    Returns:
        xml string
    """
#    return _buildXML(var,name).asString()
    (ma,list) = _buildXML(var,name)
    return dutil.doctoXML(ma,list)
        
def buildXMLReservation(var,resename):
    """ Create XML for reservation confirmation
    Args:
        var
        resename : reservation name
    Returns:
        xml string    
    """
    r = util.RESFORM(var).findElem(resename)
    li = r.getResDetail()
    nog = 0
    sum = 0.0
    seq = []
    for l in li :
        if l.getNoP() > nog : nog = l.getNoP()
        total = l.getPriceTotal()
        sum = con.addDecimal(sum,con.BigDecimalToDecimal(total))
        mx = {}
        _XbuildElemXML(var,mx,l)
        seq.append(mx)
        
    ma = {}
    _XbuildHeaderXML(var,ma,nog,r,r.getCustomerName())
    ma["total"] = sum
    ma["grossvalue"] = sum
    ma["docid"] = resename
    ma["doctype"] = "XX"
    
    return dutil.doctoXML(ma,seq)
    
def buildXMLForStay(var,resename,payername,li) :
    """ Create XML for buildXMLForStay
    Args:
        var
        resename : reservation name
        payername : payer name 
        li : list of services (pos) covered by this bill
    Returns:
        xml : string
    """
    (ma,list) = _buildXMLForServices(var,None,resename,payername,li)
    return dutil.doctoXML(ma,list)

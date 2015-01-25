from sets import Set

import cutil,con,cblob

from util import util,dutil,rutil

import resstat

B = cblob.B

class PAID :
    """ Class used for checking if the reservation service list is already billed
    Constructor: create a list of services already billed
    onList method checks if the particular service is already billed
    
    Attributes:
        self._se : set of services already billed (set of identifier)
    """
  
    def __init__(self,var,rese) :
        """Constructor : create a list of services already billed
        Args:
            var :
            rese : reservation name          
        """
        bli = util.RESOP(var).findBillsForReservation(rese)
        self._se = Set()
        for b in bli :
            for l in b.getPayList() :
                self._se.add(con.toL(l))
 
    def onList(self,id) :
        """ Check if service (identifier) is already billed
        Args:
            id: int, service identifier
        Returns:
            True: already billed, False : not billed          
        """
        return id in self._se


def _addPayment(var,billName,total,paymentmethod):
    r = util.RESFORM(var).findElem(rutil.getReseName(var))       
    RR = resstat.getResStatusR(var,r)
    li = []
    if RR.advancepaymentleft > 0 :
        payment = min(total,RR.advancepaymentleft) 
        li.append((payment,True))
        total = con.minusDecimal(total,payment)
    if total > 0 : li.append((total,False))
    
    for l in li :    
        p = util.newBillPayment()
        p.setBillName(billName)
        p.setPaymentMethod(paymentmethod)
        p.setDateOfPayment(cutil.toDate(cutil.today()))
        p.setPaymentTotal(con.toB(l[0]))         
        if (l[1]) : p.setAdvancepayment(True)
        util.PAYMENTOP(var).addPaymentForBill(billName,p)
    
class HOTELBILLSAVE(util.HOTELTRANSACTION) :
    """ Saves bill and (eventually) payment under the transaction
    """
  
    def __init__(self,var,rese_name,cust_name,xml,paymentmethod) :
        """ Constructor
        Args:
            var
            rese_name : reservation name
            cust_name : customer (payer) name
            xml : xml describing the bull
            paymentmethod: if not none then payment now, otherwise bill without payment
                  CA=Cache CC=Credit Card TR=Transfer (payment dictionary)
        """
        util.HOTELTRANSACTION.__init__(self,0,var)
        self.res = True
        self._rese_name = rese_name
        self._cust_name = cust_name
        self._xml = xml
        self._paymentmethod = paymentmethod
    
    def run(self,var) :
        (ma,li) = dutil.doctoMap(self._xml)
        self._total = ma["total"]
        P = PAID(var,self._rese_name)
        b = util.newBill(var)
        b.setGensymbol(True);
        b.setPayer(self._cust_name)
        b.setReseName(self._rese_name)
        b.setIssueDate(cutil.toDate(cutil.today()))
        for m in li :
            idp = m["id"]
            if idp == 0 : continue
            if P.onList(idp) :
                var["JERROR_MESSAGE"] = "@billalreadypaid"
                self.res = False
                return
            b.getPayList().add(idp)
         
        if b.getPayList().size() == 0 :
            var["JERROR_MESSAGE"] = "@nothingischecked"
            self.res = False
            return
      
        self._b = util.BILLLIST(var).addElem(b)
        b_name = self._b.getName()
        key = B.addNewBlob(rutil.getFinnDocRealm(var,b_name),"BILLXML",self._xml)
        if self._paymentmethod != None :
            _addPayment(var,b_name,self._total,self._paymentmethod)
    
    def getB(self):
        """ Getter, get the bill just created
        """
        return self._b
    
    def getTotal(self):
        """ Getter, get the total debet (charges) of the bill
        """
        return self._total 

def getXMLForBill(var,billname) :
    """ Get XML doc describing the bill
    Args:
        var
        billname : bill name
    Returns:
        xml or None if not found (strange)
    """
    realm = rutil.getFinnDocRealm(var,billname)
    clist = B.getList(realm)
    if clist.isEmpty() : return None
    pdfk = clist.get(0)
    xml = B.findBlobS(realm,pdfk)
    return xml

class _COUNTT(rutil.BILLSCAN) :       
  
  def __init__(self,b) :
    rutil.BILLSCAN.__init__(self,b.getPayList())
    self.total = 0.0
    
  def walk(self,idp,pa) :
    to = cutil.BigDecimalToDecimal(pa.getPriceTotal())
    self.total = cutil.addDecimal(self.total,to)


class BILLCALC:
    """ Calculates amounts related to the bill    
    """
    
    def __init__(self,var,b) :
        """ Constructor
        Args:
            var
            b : bill, CustomerBill class
        """
        self._var = var
        self._b = b
        
    def _countTotal(self,pli) :  
        t = _COUNTT(self._b)
        t.scan(pli)
        return t.total
    
    def calc(self):
        """ Calculates the amounts
        Returns:
            result available as a set of getter
        
        """
        var = self._var
        b = self._b
        pli = rutil.getPayments(var,b.getReseName())
        self._total = self._countTotal(pli)  
        xml = getXMLForBill(var,b.getName())
        if xml == None : self._sell = self.total
        else :
            (ma,li) = dutil.doctoMap(xml) 
            self._sell = ma["total"]
        self._payment = rutil.countPayments(var,b.getName())
        
    def getTotal(self):
        """ Getter, sum of services prices 
        """
        return self._total
    
    def getCharge(self):
        """ Getter, final charge for customer, debet
        """
        return self._sell
    
    def getPayment(self):
        """ Getter, sum of payments related to bill (can be None), credit
        """
        return self._payment
        

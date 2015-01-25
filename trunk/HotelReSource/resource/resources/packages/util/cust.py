from com.gwthotel.hotel import HUtils

import cutil,con

import util

CUSTACTION="custaction"
CUSTMODIFACTIVE="modifactive"
CUSTSHOWTOACTIVE="showtoaction"
CUSTSHOWONLY="showonly"

def _toV(s,prefix) :
    return con.toP(s,prefix)

def newCustomer(var) :
    c = util.ConstructObject(var)
    return c.getO(0)  

def _toS(ch) :
   return ch
 
def _toCh(s) :
   return s[0]

def customerDataFromVar(c,var,prefix=None) :
    util.copyNameDescr(c,var,prefix)
    util.toP(c,var,getCustFieldId(),prefix)
    c.setSex(_toCh(var[_toV("title",prefix)]))
    c.setDoctype(_toCh(var[_toV("doctype",prefix)]))

def customerFromVar(var,prefix=None) :
    c = newCustomer(var)
    customerDataFromVar(c,var,prefix)
    return c
  
def customerToVar(v,c,prefix=None) :
    util.toVarNameDesc(v,c,prefix)
    util.toVar(v,c,getCustFieldId(),prefix)
    v[_toV("title",prefix)] = _toS(c.getSex())
    v[_toV("doctype",prefix)] = _toS(c.getDoctype())

def getCustFieldId():
  sL = util.HUtils.getCustomerFields()
  seq = []
  for s in sL : seq.append(s)
  return seq

def getCustFieldIdAll() :
  l = getCustFieldId() + ["title","doctype","name","descr"]
  return l

def getCustFieldIdWithout() :
  l = getCustFieldId() + ["name","descr"]
  l.remove("country")
  return l  
    
def createCustomerList(var):
    C = util.CUSTOMERLIST(var)
    CLIST = getCustFieldId()
    seq = []
    for c in C.getList() :
        v = {}
        customerToVar(v,c)
        seq.append(v)
    return seq
  
def toCustomerVar(var,c,prefix,clist = ["name","surname","firstname"]) :
    util.toVar(var,c,clist,prefix)

def setCustVarCopy(var,prefix) :
    cutil.setCopy(var,getCustFieldIdAll(),None,prefix)

def setCustData(var,custname,prefix=None) :
    CU = util.CUSTOMERLIST(var)
    customer = CU.findElem(custname)
    assert customer != None
    customerToVar(var,customer,prefix)
    setCustVarCopy(var,prefix)

def setDefaCustomerNotCopy(var,prefix=None) :
   D = util.HOTELDEFADATA()
   title = D.getDataH(0)
   country = D.getDataH(1)
   doctype = D.getDataH(2)
   var[_toV("title",prefix)] = title
   var[_toV("country",prefix)] = country
   var[_toV("doctype",prefix)] = doctype

def setDefaCustomer(var,prefix=None) :
   setDefaCustomerNotCopy(var,prefix)
   cutil.setCopy(var,["title","country","doctype"],None,prefix)
   
def saveDefaCustomer(var,prefix=None) :
   D = util.HOTELDEFADATA()
   title = var[_toV("title",prefix)]
   country = var[_toV("country",prefix)]
   doctype = var[_toV("doctype",prefix)]
   D.putDataH(0,title)
   D.putDataH(1,country)
   D.putDataH(2,doctype)

def enableCust(var,pre,ena=True) :
    for cust in getCustFieldIdAll() :
      cutil.enableField(var,pre+cust,ena)
      
def saveCustomerData(var,custname,prefix) :
    CU = util.CUSTOMERLIST(var)
    customer = CU.findElem(custname)
    assert customer != None
    customerDataFromVar(customer,var,prefix)
    CU.changeElem(customer)
  
      
def customerDetailsActive(var,prefix) :
    """ Customer data with active fields, accepts but does not change data automatically
        Returns changed data 
        Return action: acceptdetails
    
    Args:
      var : map with customer data
      prefix: prefix for customer data    
    """
    var[_toV(CUSTACTION,prefix)] = CUSTMODIFACTIVE
    var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
    var["JUPDIALOG_START"] = util.mapToXML(var,getCustFieldIdAll() + [CUSTACTION,],prefix)

def _gotoCustomer(var,custid,action) :
    map = {}
    map[CUSTACTION] = action
    map["name"] = custid
    var["JUPDIALOG_START"] = util.mapToXML(map)
    var["JUP_DIALOG"]="hotel/reservation/customerdetails.xml" 
  

def showCustomerDetailstoActive(var,custid) :
    """ Show customer data with field inactive, but possible to change to modify
        Returns changed data and change data automatically
        Return button action: accept or acceptask
    Args:
        custid : customer id to show
    """
    _gotoCustomer(var,custid,CUSTSHOWTOACTIVE)
    
   
def showCustomerDetails(var,custid):
    """ Show customer details without modification
    
    Args:
      var : map
      custid : string, customer identifier to show
    """
    _gotoCustomer(var,custid,CUSTSHOWONLY)

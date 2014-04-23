from java.util import Calendar
from com.gwtmodel.table.common.dateutil import DateFormatUtil
from java.math import BigDecimal
import datetime
from com.jythonui.server import MUtil

def eqUL(l1,l2) :
    return toL(l1) == toL(l2)

def toL(l) :
  if type(l) == long : return l  
  return l.longValue()

def toDate(value):
    if value == None : return None
    ca = Calendar.getInstance()
    ca.clear()
    ca.set(value.year,value.month - 1,value.day)
    return ca.getTime()

def eqDate(d1,d2):
    dd1 = toDate(d1)
    return dd1.equals(d2)

def toJDate(value):
    if value == None : return None
    y = DateFormatUtil.getY(value)
    m = DateFormatUtil.getM(value)
    d = DateFormatUtil.getD(value)
    return datetime.date(y,m,d)
    
def toJDateTime(value):
    if value == None : return None
    y = DateFormatUtil.getY(value)
    m = DateFormatUtil.getM(value)
    d = DateFormatUtil.getD(value)
    hh = value.getHours()
    mm = value.getMinutes()
    ss = value.getSeconds()
    return datetime.datetime(y,m,d,hh,mm,ss)

def toB(value):
    if value == None : return None
    b = BigDecimal(value)
    return MUtil.roundB(b)

def BigDecimalToDecimal(b):
    if b : return b.doubleValue()
    return None

def today():
    return datetime.date.today()

def mulIntDecimal(int,dec,afterdot=2):
    if int and dec :
       if type(dec) == BigDecimal : dec = BigDecimalToDecimal(dec) 
       return round(int * dec, afterdot)
    return None 

def addDecimal(sum1,sum2,afterdot=2):
   if sum1 == None : return sum2
   if sum2 == None : return sum1
   return round(sum1 + sum2,afterdot)

def minusDecimal(sum1,sum2,afterdot=2):
   if sum1 == None : 
       if sum2 == None : return None
       return round(0 - sum2,afterdot)
   if sum2 == None : return sum1
   return round(sum1 - sum2,afterdot)

def toS(val): 
    s  = val
    f = None
    if type(val) == BigDecimal : f = BigDecimalToDecimal(val)
    elif type(val) == datetime.date : s = str(val)
    elif type(val) == int : s = str(val)    
    elif type(val) == float : f = val
    if f != None :
        s = '%.2f' % f
    return s

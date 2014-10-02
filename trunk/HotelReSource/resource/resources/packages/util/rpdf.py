import java
import util
import con
import datetime
import rutil
import cutil

from com.jamesmurty.utils import XMLBuilder

def _addElem(builder,tag,val):
    if val == None : return
    builder.e(tag).t(con.toS(val)).up()
    
def _addMapElem(builder,tag,m,key):
    if type(tag) != list : _addElem(builder,tag,m.getAttr(key))
    else :
      for i in range(0,len(tag)) : _addElem(builder,tag[i],m.getAttr(key[i]))       

def _addSubTotal(builder,name,val):
    builder = builder.e("line")
    _addElem(builder,"amount",name)
    _addElem(builder,"total",val)
    builder = builder.up()
    return builder

def _getCountryName(var,id):
    if id == None : return None
    name =  cutil.getDicName("countries",id)
    if name == None : return id
    return name

def _buildHeaderXML(var,rootname,nog,r,custname):
       (arrival,departure,roomname,rate) = rutil.getReseDate(var,r)
       room = util.ROOMLIST(var).findElem(roomname)
       p = util.CUSTOMERLIST(var).findElem(custname);
       builder = XMLBuilder.create(rootname)       
       _addMapElem(builder,["name1","name2","address","city"],p,["firstname","surname","street","city"])       
       _addElem(builder,"country",_getCountryName(var,p.getAttr("country")))
       _addElem(builder,"roomnumber",roomname)
       _addElem(builder,"dailyrate",rate)
       _addElem(builder,"roomtype",room.getDescription())
       _addElem(builder,"arrivaldate",arrival)
       _addElem(builder,"departuredate",departure)
       _addElem(builder,"nofguests",nog)
       _addElem(builder,"resid",r.getName())
       _addElem(builder,"resdate",r.getCreationDate())
       return builder
   
def _buildElemXML(var,builder,r):
     da = con.toJDate(r.getServDate())
     roomname = r.getRoomName()
     desc = util.ROOMLIST(var).findElem(roomname).getDescription()
     rate = r.getPrice()
     amount = r.getNoP()
     total = r.getPriceTotal()
     builder = builder.e("line")
     _addElem(builder,"date",da)
     _addElem(builder,"roomnumber",roomname)
     _addElem(builder,"description",desc)
     _addElem(builder,"rate",rate)           
     _addElem(builder,"amount",amount)
     _addElem(builder,"total",total)
     builder = builder.up()
                 

def _buildXML(var,name) :
       b = util.BILLLIST(var).findElem(name)
       resename = b.getReseName()
       r = util.RESFORM(var).findElem(resename)
       nog = len(util.RESOP(var).getResGuestList(resename))
       li = rutil.getPayments(var,resename)
        
       builder = _buildHeaderXML(var,"invoice",nog,r,b.getPayer())
              
       builder = builder.e("lines");
       pli = b.getPayList()
#       sum = 0
       for l in pli :
           r = None
           for pa in li :
               if con.eqUL(l,pa.getId()) : 
                   r = pa
                   break
           assert r != None
           _buildElemXML(var,builder,r) 

       sum = rutil.countTotal(var,b,li)
       _addSubTotal(builder,"Debit",sum)
       
       sump = rutil.countPayments(var,name)      
       _addSubTotal(builder,"Credit",sump)

       _addSubTotal(builder,"Balance",con.minusDecimal(sum,sump))
       
       builder = builder.up()
       return builder

def buildXMLS(var,name):
    return _buildXML(var,name).asString()

def buildResXML(var,resename):
    r = util.RESFORM(var).findElem(resename)
    li = r.getResDetail()
    nog = 0
    sum = 0.0
    for l in li :
        if l.getNoP() > nog : nog = l.getNoP()
        total = l.getPriceTotal()
        sum = con.addDecimal(sum,con.BigDecimalToDecimal(total))
        
    builder = _buildHeaderXML(var,"reservation",nog,r,r.getCustomerName())
    _addElem(builder,"total",sum)
    
    builder = builder.e("lines");
    for l in li :
      _buildElemXML(var,builder,l)            
      sum = con.addDecimal(sum,con.BigDecimalToDecimal(total))
    
    return builder.asString()

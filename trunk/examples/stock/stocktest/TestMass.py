import pyodbc
import random
import decimal
import logging

_SQLSELECTITEMS="SELECT * FROM ITEM"
_SQLCLEARSTORES=" { CALL CLEAR_ALL_STORES() }"
_SQLCLEARTEMP="{ CALL CLEAR_TEMP_SESSION(?) } "
_SQLINSERTCODE="INSERT INTO TEMP_ITEM_CODES_LIST VALUES(?,?,?,?,?)"

_UPDATESUMMARY='UPDATE TESTSUMMARY SET AMOUNT= (CASE WHEN AMOUNT IS NULL THEN 0 ELSE AMOUNT END) + ? WHERE ITEM = ?'

_SQLDELIVERY="\
 BEGIN\
  DECLARE OK INT;\
  CALL GET_ITEM_IDS_FOR_DELIVERY(?,OK);\
END"

_MODIFYFORRELEASE="UPDATE TEMP_ITEM_IDS_LIST SET AMOUNTRESERVED = AMOUNT WHERE SESSIONID=?"

_SQLIRUNFORRELEASE="\
 BEGIN\
  DECLARE OK INT;\
  CALL GET_ITEMS_FOR_RESERVATION(?,OK);\
  DELETE FROM RES; \
  INSERT INTO RES VALUES(OK, NULL); \
END"

_SQLRUNRESFOROK="SELECT OK FROM RES"
_SQLRUNRESFOROPID="SELECT OPID FROM RES"

_SQLGETFAILURE='SELECT * FROM TEMP_ITEM_IDS_LIST WHERE OPERATIONLINE IS NULL AND SESSIONID=? AND AMOUNTRESERVED > 0'
_SQLREMOVEAFTERFAILURE='DELETE FROM TEMP_ITEM_IDS_LIST WHERE OPERATIONLINE IS NULL AND SESSIONID=?'

_SQLOPERATIONOP="\
 BEGIN\
  DECLARE OPID BIGINT;\
  CALL OPERATION_OP(?,OPID,'Augustus','Saturn');\
  DELETE FROM RES; \
  INSERT INTO RES VALUES(NULL, OPID); \
END"

_SQLGETIDS='SELECT * FROM TEMP_ITEM_IDS_LIST WHERE SESSIONID=?'

_SQLGETOP="SELECT * FROM OPERATIONLINE WHERE OPERATION=?"

class TestStock :
    
# construct/delete

     def __init__(self, conn=None):
         if conn == None :
    #        self.conn = pyodbc.connect("DSN=ASTOCK;UID=db2inst1;PWD=db2inst1")
             self.conn = pyodbc.connect("DSN=STOCK;UID=db2inst2;PWD=db2inst2")
             self._connectclose = True
         else :
             self.conn = conn
             self._connectclose = False
         self.collect = {}
    
     def stop(self):
         if self._connectclose : self.conn.close()

# --------- common
        
     def sql(self, s,  param = None):
         if param == None :
             logging.debug(s)
             self.cur = self.conn.execute(s)
         else :
             logging.debug(s + " " + str(param))
             self.cur = self.conn.execute(s, param)
        
     def commit(self):
         self.conn.commit()   
                 
     def cleartemp(self):
         self.sqlwithsession(_SQLCLEARTEMP)
         
     def sqlwithsession(self, s):
         self.sql(s, (_SESSION))
         
     def genamount(self):
         decim = random.randint(0, 99)
         inte = random.randint(1, 100)
         dec = decimal.Decimal(inte) + decimal.Decimal(decim)/100
         return dec
         
     def genvalue(self):
         decim = random.randint(200, 999)
         inte = random.randint(1, 100)
         dec = decimal.Decimal(inte) + decimal.Decimal(decim)/100
         return dec
         
     def getdecimal(self, row, no):
         if row[no] == None : return None
         amo = row[no] / decimal.Decimal(10000)
         return amo


     def createcodeline(self, ids):
         self.cleartemp()
         no = 0
         for i in ids :
             no = no + 1
             code = i[0]
             amo = i[1]
             val = i[2]
             self.sql(_SQLINSERTCODE, (_SESSION, no, code, amo, val))
             
     def runoperationop(self):
         self.sqlwithsession(_SQLOPERATIONOP)
         self.sql(_SQLRUNRESFOROPID)
         row = self.cur.fetchone()
         opid = row[0]
         if opid == None : return False
         self.commit()
         self.sql(_SQLGETOP, (opid))
         row = self.cur.fetchone()
         while row != None :
             id = row[1]
             amo = self.getdecimal(row, 2)
             if amo != None :
                 if self.collect.has_key(id) : 
                     self.collect[id] = self.collect[id] + amo
                 else :
                     self.collect[id] = amo
             row = self.cur.fetchone()
         return True     

     def updatesummary(self):
         logging.info("Update summary")
         for i in self.collect :
             amo = self.collect[i]
             self.sql(_UPDATESUMMARY, (amo, i))
         self.commit()

# -------- initialization
     def clearstores(self):
         self.sql(_SQLCLEARSTORES)

     def createItems(self):
         self.sql(_SQLSELECTITEMS);
         self.items = []
         row = self.cur.fetchone()
         no = 0
         while row != None  :
             no  = no + 1
             # if no > 3 : break
             
             i = (row[0], row[1])
             self.items.append(i)
             row = self.cur.fetchone()

# ----- delivery

     def createdelivery(self):
         ids = []
         for i in self.items :
             code = i[1]
             amo = self.genamount()
             val = self.genvalue()
             ids.append((code, amo, val))
         return ids 
         
     def deliver(self):
         self.sqlwithsession(_SQLDELIVERY)
         self.runoperationop()
         
     def rundelivery(self):
         logging.info("Deliver")
         ids = self.createdelivery()
         self.createcodeline(ids)
         self.deliver()


# -------------- release
     def createrelease(self):
         ids = []
         for i in self.items :
             code = i[1]
             amo = 0-self.genamount()
             ids.append((code, amo, None))
         return ids 
        
     def runitemforrelease(self):
         self.sqlwithsession(_SQLIRUNFORRELEASE)
         self.sql(_SQLRUNRESFOROK)
         row = self.cur.fetchone()
         ok = row[0]
         return ok == 1
            
     def itemidsnotempty(self):
         self.sqlwithsession(_SQLGETIDS)
         row = self.cur.fetchone()
         return row != None
        
     def preparenextrelease(self):
         self.sqlwithsession(_SQLGETFAILURE)
         ids = None
         row = self.cur.fetchone()
         while row != None :
             id = row[2]
             amo = 0 - self.getdecimal(row, 5)
             code = None
             for i in self.items :
               if i[0] == id : 
                     code = i[1]
                     break
             if ids == None : ids = []
             ids.append((code, amo, None))
             row = self.cur.fetchone()
             
         return ids
         
     def runandmodify(self, modifysql):
         ids = self.createrelease()
         self.createcodeline(ids)
         ok = self.runitemforrelease()
         if not ok :
             # pick up failed 
             fids = self.preparenextrelease()
             self.sqlwithsession(_SQLREMOVEAFTERFAILURE)
             if self.itemidsnotempty() :
                 self.sqlwithsession(modifysql)
                 ok = self.runoperationop()
             if fids != None :
                 self.createcodeline(fids)
                 self.sqlwithsession(modifysql)
                 ok = self.runitemforrelease()
                 if ok : 
                     self.sqlwithsession(modifysql)
                     ok = self.runoperationop()
         else :
                 self.sqlwithsession(modifysql)
                 ok = self.runoperationop()
                 
     def runrelease(self):
         logging.info("Release")
         self.runandmodify(_MODIFYFORRELEASE)

# ----------------
         
     def runTest(self, session, noTest):
         global _SESSION
         _SESSION  = session
         logging.info("Run test " + session + " number of tests " + str(noTest))
#         self.clearstores()
         self.createItems()         
         self.rundelivery()
         self.runrelease()
         for i in range(noTest) :
             logging.info("Test no " + str(i))
             ttest = random.randint(0, 1)
             if ttest == 0 :  self.rundelivery()
             elif ttest == 1:  self.runrelease()
             
         self.updatesummary()

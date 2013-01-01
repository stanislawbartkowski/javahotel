-- GET_DELIVERY_ID 
-- resolves chains of references to get delivery line id
-- Parameter : lineid
-- Returns: delivery line id
CREATE OR REPLACE FUNCTION GET_DELIVERY_ID (IN lineid BIGINT)
        RETURNS BIGINT
        NO EXTERNAL ACTION
        DETERMINISTIC
F1: BEGIN 
  DECLARE lid BIGINT;
  DECLARE pid BIGINT;
  SET lid = lineid;
  LOOP
    SELECT REFERENCELINE INTO pid FROM OPERATIONLINE WHERE ID = lid;
    IF pid IS NULL THEN
      RETURN lid;
    END IF;  
    SET lid = pid;
  END LOOP;
END F1
@

-- GET_LAST_ID (DB2 specific)
-- Returnes last created identifier
-- Warning: should be "sourced" to work properly
CREATE OR REPLACE FUNCTION GET_LAST_ID () RETURNS BIGINT SOURCE SYSIBM.IDENTITY_VAL_LOCAL()
@

-- removes all fact data data
CREATE OR REPLACE PROCEDURE CLEAR_ALL_STORES ()
P1: BEGIN ATOMIC
  DELETE FROM ITEMDASHBOARD;
  DELETE FROM OPERATIONLINE;
  DELETE FROM OPERATION;
END P1
@

-- LOGMESS
-- Description: put a log mess into logmess table
-- Parameter IN: mess - message
CREATE OR REPLACE PROCEDURE LOGMESS (IN MESS VARCHAR(200))
AUTONOMOUS
P1: BEGIN
  INSERT INTO LOGMESS VALUES(MESS);
END P1
@

-- clear tables used for passing parameters
-- parameters:
--  IN psessionid 
CREATE OR REPLACE PROCEDURE CLEAR_TEMP_SESSION (IN psessionid VARCHAR(128))
P1: BEGIN
  DELETE FROM TEMP_ITEM_IDS_LIST WHERE SESSIONID = psessionid;
  DELETE FROM TEMP_ITEM_CODES_LIST WHERE SESSIONID = psessionid;
END P1
@

-- STOCK_SIGNAL (DB2 specific)
-- Throws a signal
-- Parameters IN:
--   MESSAGE : message to be sent with a signal
CREATE OR REPLACE PROCEDURE STOCK_SIGNAL(IN MESSAGE VARCHAR(100))
P1: BEGIN
    SIGNAL SQLSTATE '75000' 
             SET MESSAGE_TEXT = MESSAGE;
END
@

-- GET_ITEMS_ID_FOR_DELIVERY
-- Description: Procedure prepares TEMP_ITEM_IDS_LIST temporary file for delivery
-- Parameters IN:
--    psessionid : session identifier
-- Parameterss OUT:
--     ok = 1 if all item code are available and TEMP_IDEM_IDS_LIST is ready
--     ok = 0 if there are some codes anavailable,  ITEMID column is empty
-- Throws signal if any error has occured (for instance empty input table)
-- Source: TEMP_ITEM_CODES_LIST
-- Output: TEMP_ITEM_IDS_LIST
CREATE OR REPLACE PROCEDURE GET_ITEM_IDS_FOR_DELIVERY (IN psessionid VARCHAR(128),OUT ok INT)
P1: BEGIN ATOMIC
  DECLARE @itemid BIGINT;
  DECLARE pitemcode CHAR(30);
  DECLARE pseqnumber INTEGER;
  DECLARE inumber INT DEFAULT -1;
  DECLARE pamount DECIMAL (15,4);
  DECLARE pvalue DECIMAL(20,2);
  SET ok = 1;
  DELETE FROM TEMP_ITEM_IDS_LIST WHERE SESSIONID = psessionid;
  FOR forloop AS curs CURSOR FOR
    SELECT SEQNUMBER, ITEMCODE,AMOUNT,VALUE FROM TEMP_ITEM_CODES_LIST AS T
              WHERE SESSIONID = psessionid ORDER BY SESSIONID,SEQNUMBER
  DO
      SET pitemcode = ITEMCODE;
      SET pseqnumber = SEQNUMBER;
      SET pamount = AMOUNT;
      SET pvalue = VALUE;
      SELECT ID INTO @itemid FROM ITEM  WHERE ITEMCODE = PITEMCODE;
      IF @itemid IS NULL THEN
        SET ok = 0;
      END IF;
      SET inumber = inumber + 1;
      INSERT INTO TEMP_ITEM_IDS_LIST VALUES(psessionid,pseqnumber,@itemid,NULL, pamount,pamount, pvalue);
  END FOR;
  IF inumber = -1 THEN
     CALL STOCK_SIGNAL('There is no items in the list');
  END IF;
  END
@

-- GET_ITEMS_FOR_RESERVATION
-- Description: check if items are available for release or reservation
-- Parameters IN: psessionid
-- Parameters OUT: ok 1: Items are available
--                 ok 0: Items are not available
-- Source: TEMP_ITEM_CODES_LIST
-- Output: TEMP_ITEM_IDS_LIST
CREATE OR REPLACE PROCEDURE GET_ITEMS_FOR_RESERVATION (IN psessionid VARCHAR(128),OUT ok INT)
P1: BEGIN ATOMIC
  DECLARE @itemid BIGINT;
  DECLARE pitemcode CHAR(30);
  DECLARE pseqnumber INTEGER;
  DECLARE inumber INT DEFAULT -1;
  DECLARE pamount DECIMAL (15,4);
  DECLARE pvalue DECIMAL(20,2);
  DECLARE dvalue DECIMAL(20,2);
  DECLARE tempvalue DECIMAL(20,2);
  DECLARE damount DECIMAL (15,4);
  DECLARE dprice DECIMAL(20,2);
  DECLARE pdeliveryline BIGINT;
  SET ok = 1;
  DELETE FROM TEMP_ITEM_IDS_LIST WHERE SESSIONID = psessionid;
 P2: 
  FOR forloop AS curs CURSOR FOR
    SELECT * FROM TEMP_ITEM_CODES_LIST AS T
              WHERE SESSIONID = psessionid ORDER BY SESSIONID,SEQNUMBER
  DO
      SET inumber = inumber + 1;
      SET pitemcode = ITEMCODE;
      SET pseqnumber = SEQNUMBER;
      IF AMOUNT >= 0 THEN
          CALL STOCK_SIGNAL(AMOUNT || ' should be negative for reservation');
      END IF;   
      SET pamount = 0 - AMOUNT;
      SET pvalue = VALUE;
      SELECT ID INTO @itemid FROM ITEM  WHERE ITEMCODE = PITEMCODE;
      IF @itemid IS NULL THEN
        SET ok = 0;
        INSERT INTO TEMP_ITEM_IDS_LIST VALUES(psessionid,inumber,NULL,NULL, 0-pamount,NULL, NULL);
        ITERATE P2;
      END IF;  
      SELECT SUM(CURRENTAVAILABLE) INTO damount FROM ITEMDASHBOARD WHERE itemid = @itemid;
      IF damount IS NULL THEN
        SET damount = 0;
      END IF;  
      IF damount < pamount THEN
         SET ok = 0;
         INSERT INTO TEMP_ITEM_IDS_LIST VALUES(psessionid,inumber,@itemid,NULL, 0-pamount,damount, NULL);
         ITERATE P2;
      END IF;
     SET inumber = inumber -1 ;
     P3:
      FOR itemloop AS iteml CURSOR FOR 
          SELECT DELIVERYLINE,CURRENTVALUE,CURRENTAMOUNT,CURRENTAVAILABLE FROM ITEMDASHBOARD WHERE ITEMID = @itemid DO
          IF CURRENTAVAILABLE <= pamount THEN
            SET damount = CURRENTAVAILABLE;
          ELSE
            SET damount = pamount;
          END IF;    
          SET pdeliveryline = DELIVERYLINE;
          SET pamount = pamount - damount;
          -- IMPORTANT: calculate the price now as weight average
          -- there could be a lot of difference method of calculating price
          SET dprice = CURRENTVALUE / CURRENTAMOUNT;
          -- IMPORTANT: price is round to 2 decimals now
          SET dvalue = dprice  * damount;
          IF (pamount < CURRENTAMOUNT AND dvalue > CURRENTVALUE) THEN
             SET tempvalue = dvalue;
             SET dprice = CURRENTVALUE / CURRENTAMOUNT - 0.01;
             SET dvalue = dprice  * damount;
             CALL LOGMESS(psessionid || ' modify value and price: value before: ' || tempvalue || ' value after: '  || dvalue );
          END IF;
          SET inumber = inumber + 1 ;
          CALL LOGMESS(psessionid || ' ' || @itemid || ' ' || pdeliveryline || ' a='  
                       || CURRENTAMOUNT || ' ' || CURRENTVALUE 
                       || ' new: ' || damount || ' ' || dvalue || ' ' || dprice);
          INSERT INTO TEMP_ITEM_IDS_LIST VALUES(psessionid,inumber,@itemid,pdeliveryline, 0-damount,NULL, 0-dvalue);
          IF pamount = 0 THEN
            LEAVE P3;
          END IF;
       END FOR;
  END FOR;
  IF inumber = -1 THEN
     CALL STOCK_SIGNAL('There is no items in the list');
  END IF;
  END
@

-- UPDATE_DASHBOARDITEM (DB2 specific)
-- Modifies dashboard and resolves "amount not available" problem
-- Parameters IN:
--   pitemid : itemid of item modified
--   deliveryid : delivery item id (pitemid + deliveryid = primary key in dashboard table)
--   amount : amount modifier
--   amountres : reservation modifier
--   value : value modifier
-- Paramers OUT;
--   OK :=  2, optimistic locking failed, try again
--   OK : = 1, ok value is modfied, look also valuemodif out parameter
--   OK : = 0, amount requested is not available, pamountavailable is set
--   valuemodif: if not NULL then value modifier after changing
--   pcurrentamount : amount available 
CREATE OR REPLACE PROCEDURE UPDATE_DASHBOARDITEM (OUT OK INT,IN pitemid BIGINT, IN deliveryid BIGINT
            , IN amount DECIMAL (15,4), IN amountres DECIMAL(15,4), IN value DECIMAL(20,2), OUT pvaluemodif DECIMAL (20,2), OUT pcurrentavail DECIMAL (15,4) )
P1: BEGIN

  DECLARE newavail DECIMAL (15,4);
  DECLARE newamount DECIMAL (15,4);
  DECLARE newvalue DECIMAL (20,2);
  DECLARE changetoken BIGINT;
  
  DECLARE pcurrentvalue DECIMAL (20,2);
  DECLARE pcurrentamount DECIMAL (15,4);
  SET ok = 1;         
  SET pvaluemodif = NULL;

  -- optimistic locking enabled
  -- only test between SELECT AND UPDATE
  SELECT ROW CHANGE TOKEN FOR ITEMDASHBOARD, CURRENTAVAILABLE, CURRENTAMOUNT, CURRENTVALUE INTO changetoken, pcurrentavail,pcurrentamount, pcurrentvalue FROM ITEMDASHBOARD 
    WHERE ITEMID = pitemid AND DELIVERYLINE = deliveryid;
    
-- new delivery    
  IF pcurrentavail IS NULL THEN 
    IF amount > 0 THEN
      INSERT INTO ITEMDASHBOARD VALUES(pitemid,deliveryid,amount,amountres,value);
      RETURN;
    END IF;
    SET pcurrentavail = 0;
    SET ok = 0;
    RETURN;
  END IF;
  
-- issue or correct
  SET newavail = pcurrentavail;
  SET newamount = pcurrentamount;
  SET newvalue = pcurrentvalue;
  
-- amount available  
  IF amountres IS NOT NULL THEN
    SET newavail = pcurrentavail + amountres;      
    IF newavail < 0 THEN
      SET ok = 0;
      RETURN;
    END IF;
  END IF;

-- value  
  IF value IS NOT NULL THEN
    SET newvalue = pcurrentvalue + value;
  END IF;    
  
-- real amount 
  IF amount IS NOT NULL THEN
    SET newamount = pcurrentamount + amount;
    IF newamount < 0 THEN
      CALL LOGMESS(pitemid || ' ' || deliveryid || ' amount less then 0 ' || newamount);
      SET ok = 0;
      RETURN;
    END IF;
    
    IF newamount = 0 AND newavail = 0 THEN
      IF newvalue <> 0 THEN
        SET pvaluemodif = 0 - newvalue;
      END IF;
      DELETE FROM ITEMDASHBOARD   
         WHERE ITEMID = pitemid AND DELIVERYLINE = deliveryid;
      RETURN;
    END IF;     
  END IF;  
-- final update  
  BEGIN
     DECLARE EXIT HANDLER FOR NOT FOUND
        BEGIN
          CALL LOGMESS(' CHANGETOKEN BROKEN');
          SET OK = 0;
          RETURN;
        END;
        
    CALL LOGMESS(pitemid || ' ' || deliveryid || ' ' || newavail || ' ' || newamount || ' ' || newvalue);     
  
    IF newvalue <= 0 THEN
-- it is expected to get newvalue < 0
-- it is possible if two connections are updating the same delivery at the same time
-- and the value is close to 0
      SET ok =0;
      CALL LOGMESS(pitemid || ' ' || deliveryid || ' value less or equal 0 ' || newvalue);
      RETURN;
    END IF;

    UPDATE ITEMDASHBOARD 
       SET CURRENTAVAILABLE = newavail,
           CURRENTAMOUNT = newamount,
           CURRENTVALUE = newvalue
         WHERE ITEMID = pitemid AND DELIVERYLINE = deliveryid and changetoken = ROW CHANGE TOKEN FOR ITEMDASHBOARD;
  END;       
END P1
@

-- INNER_OPERATION_OP : performs operation and modifies DASHBOARD table
-- Parameters IN: 
--    psessionid : session identifier
--    ploginname : login name of the user performing this operation
--    pstorename : store name where operation is performed 
--           should be not null if at least one delivery
-- Parameters OUT:
--    idop : identifiers od operation just created
--       if NULL then some items are not available and nothing happened
--       if NOT NULL : operation successful
-- Throws signal if an error has occured
-- Transactional: commit or rollback automatically
-- Source: TEMP_ITEM_IDS_LIST
--    if DELIVERYLINE IS NULL then new delivery is created
--    if DELIVERYLINE IS NOT NULL then delivery (via chain also) is searched for
-- Output:
--    AMOUNTRESERVED is filled
--    if idop IS NULL then AMOUNTRESERVED is less then AMOUNT for items not available 
CREATE OR REPLACE PROCEDURE INNER_OPERATION_OP (IN psessionid VARCHAR(128),OUT idop BIGINT,IN ploginname CHAR(15), IN pstorename CHAR(15))
P1: BEGIN
  DECLARE personid BIGINT;
  DECLARE storeid BIGINT DEFAULT NULL;
  DECLARE exist INTEGER DEFAULT 0;

  DECLARE pitemid BIGINT;
  DECLARE rseqnumber INTEGER;
  DECLARE pseqnumber INTEGER DEFAULT 0;
  DECLARE pamount DECIMAL (15,4);
  DECLARE pamountavailable DECIMAL (15,4);
  DECLARE pvalue DECIMAL(20,2);
  DECLARE poperationline BIGINT;
  DECLARE pdeliveryline BIGINT;
  DECLARE pout INTEGER;
  DECLARE opok INTEGER DEFAULT 1;
  
  DECLARE pvaluemodif DECIMAL(20,2);
  DECLARE pcurrentavail DECIMAL(15,4);

--  firsly resolve personid
  SELECT ID INTO personid FROM PERSON WHERE LOGINNAME = ploginname;
  IF personid IS NULL THEN
    CALL STOCK_SIGNAL(TRIM(ploginname) || ' - cannot find this login name.');
  END IF;
-- secondly resolve storid (if storename exists)  
  if pstorename IS NOT NULL THEN
    SELECT ID INTO storeid FROM STORE WHERE STORENAME = pstorename;
    if storeid IS NULL THEN
      CALL STOCK_SIGNAL(TRIM(pstorename) || ' - cannot find this store name.');
    END IF;
  END IF;  
    
-- acquire update lock  
  UPDATE  ITEMDASHBOARD SET ITEMID = ITEMID WHERE 
    (ITEMID,DELIVERYLINE) IN (SELECT ITEMID,GET_DELIVERY_ID(OPERATIONLINE) from TEMP_ITEM_IDS_LIST WHERE OPERATIONLINE IS NOT NULL);
  
-- new operation  
  INSERT INTO OPERATION VALUES(DEFAULT,CURRENT DATE,storeid,personid);
  SET idop = GET_LAST_ID();
  IF idop IS NULL THEN
    CALL STOCK_SIGNAL('Internal error, idop is null');
  END IF;

-- main loop creating lines for new document
  FOR forloop AS curs CURSOR FOR
    SELECT SEQNUMBER,ITEMID,AMOUNT,AMOUNTRESERVED ,VALUE, OPERATIONLINE FROM TEMP_ITEM_IDS_LIST AS T
              WHERE SESSIONID = psessionid ORDER BY SESSIONID,SEQNUMBER
  DO
    SET exist = 1;
    SET rseqnumber = seqnumber;
    SET pitemid = ITEMID;
    SET pamount = AMOUNT;
    SET pamountavailable = AMOUNTRESERVED;
    SET pvalue = VALUE;
    SET poperationline = OPERATIONLINE;
    
-- delivery    
    IF poperationline IS NULL THEN
       IF storeid IS NULL THEN
         CALL STOCK_SIGNAL('Delivery should have store name defined');
       END IF;    
       INSERT INTO OPERATIONLINE VALUES(DEFAULT,pitemid,pamount,pamountavailable,pvalue,NULL,idop,pseqnumber);
       SET pdeliveryline = GET_LAST_ID();
       CALL UPDATE_DASHBOARDITEM (pout,pitemid, pdeliveryline,pamount,pamount,pvalue,pvaluemodif,pcurrentavail);              
    ELSE
-- release or correct    
     SET pdeliveryline = GET_DELIVERY_ID(poperationline);
     SELECT ITEM INTO pitemid FROM OPERATIONLINE WHERE ID = pdeliveryline;
     CALL UPDATE_DASHBOARDITEM (pout,pitemid, pdeliveryline,pamount,pamountavailable,pvalue,pvaluemodif,pcurrentavail);
     IF pvaluemodif IS NOT NULL THEN
       INSERT INTO OPERATIONLINE VALUES(DEFAULT,pitemid,NULL,NULL,pvaluemodif,poperationline,idop,pseqnumber);
       SET pseqnumber = pseqnumber + 1;
     END IF;           
    INSERT INTO OPERATIONLINE VALUES(DEFAULT,pitemid,pamount,pamountavailable,pvalue,poperationline,idop,pseqnumber);
    END IF;
    SET pseqnumber = pseqnumber + 1;
    IF pout = 0 THEN
      SET opok = 0;
      UPDATE TEMP_ITEM_IDS_LIST SET AMOUNTRESERVED = pcurrentavail WHERE SESSIONID = psessionid AND SEQNUMBER = rseqnumber;
    END IF;  
  END FOR;

  IF exist = 0 THEN
    CALL STOCK_SIGNAL('No items for delivery.');
  END IF;
  IF opok = 0 THEN
    SET idop = NULL;
  END IF;  
END
@

-- OPERATION_OP
-- enclosing for inner_operation_op to catch and release ROLLBACK command
CREATE OR REPLACE PROCEDURE OPERATION_OP (IN psessionid VARCHAR(128),OUT idop BIGINT,IN ploginname CHAR(15), IN pstorename CHAR(15))
P1: BEGIN
  CALL INNER_OPERATION_OP(psessionid,idop,ploginname,pstorename);
  IF idop IS NULL THEN
    CALL LOGMESS(psessionid || ' ' || ' ROLLBACK');
    ROLLBACK;
    CALL LOGMESS(psessionid || ' ' || ' AFTER ROLL BACK');
  END IF;
END P1
@

-----------------------------------------------
-- REFRESH_DASHBOARD_TABLE
-- recreates dashboard table from fact tables
-- Warning: caller should release COMMIT
------------------------------------------------

CREATE OR REPLACE PROCEDURE REFRESH_DASHBOARD_TABLE ()
P1: BEGIN ATOMIC
  DECLARE pout INT;
  DECLARE pitemid BIGINT;
  DECLARE pdeliveryline BIGINT;
  DECLARE preferenceline BIGINT;
  DECLARE pamount DECIMAL (15,4);
  DECLARE pamountavailable DECIMAL (15,4);
  DECLARE pvalue DECIMAL(20,2);
  DECLARE pvaluemodif DECIMAL(20,20);
  DECLARE pcurrentavailable DECIMAL (15,4);
  
  DELETE FROM ITEMDASHBOARD;

  FOR forloop1 AS curs1 CURSOR FOR SELECT ID,ITEM,REFERENCELINE,AMOUNT,AMOUNTAVAILABLE,VALUE FROM OPERATIONLINE ORDER BY ID,SEQNUMBER 
      DO      
        SET pitemid = ITEM;
        SET pdeliveryline = ID;
        SET preferenceline = REFERENCELINE;
        SET pamount = AMOUNT;
        SET pamountavailable = AMOUNTAVAILABLE;
        SET pvalue = VALUE;
        IF preferenceline IS NOT NULL THEN
           SET pdeliveryline = GET_DELIVERY_ID(preferenceline);
        END IF;          
        CALL UPDATE_DASHBOARDITEM (pout,pitemid, pdeliveryline,pamount,pamountavailable,pvalue,pvaluemodif,pcurrentavailable);
        IF pout = 0 THEN
          CALL STOCK_SIGNAL('Error : not valid update');
          RETURN;
        END IF;
        IF pvaluemodif IS NOT NULL THEN
          CALL STOCK_SIGNAL('Error : value modification not null');
          RETURN;
        END IF;  
  END FOR;      
END P1
@
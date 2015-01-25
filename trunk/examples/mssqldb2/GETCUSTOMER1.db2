CREATE OR REPLACE PROCEDURE getCustomers1 (
  pCUSTID INTEGER DEFAULT NULL,
  pCUSTNAME VARCHAR(100) DEFAULT NULL,
  pCUSTLASTACT TIMESTAMP DEFAULT NULL
)
DYNAMIC RESULT SETS 1
BEGIN
  DECLARE pSELECT VARCHAR(32000);
  DECLARE pWHERE VARCHAR(32000);
  DECLARE dc CURSOR WITH RETURN TO CLIENT FOR STMT;  

  SET pSELECT = 'SELECT * FROM CUSTOMER';

  SET pWHERE = '';

  IF pCUSTID IS NOT NULL THEN 
    SET pWHERE = pWHERE || ' AND CUSTID = ?';
  ELSE 
    SET pWHERE = pWHERE || ' AND ? IS NULL';
  END IF;  

  IF pCUSTNAME IS NOT NULL THEN
    SET pWHERE =  pWHERE || ' AND CUSTNAME = ?';
  ELSE  
    SET pWHERE =  pWHERE || ' AND ? IS NULL';
  END IF;  

  IF pCUSTLASTACT IS NOT NULL THEN
    SET pWHERE = pWHERE ||  ' AND CUSTLASTACT >= ?';
  ELSE  
    SET pWHERE = pWHERE ||  ' AND ? IS NULL';
  END IF;  

  IF LENGTH(pWHERE) > 0 THEN
--    PRINT @WHERE
    SET pWHERE = SUBSTR(pWHERE,5,9999);
--	PRINT @WHERE
    SET pSELECT = pSELECT || ' WHERE ' || pWHERE;
  END IF;

  PREPARE STMT FROM pSELECT;
  OPEN DC USING pCUSTID,pCUSTNAME,pCUSTLASTACT;
    
--  DECLARE @PARAMDEF NVARCHAR(MAX);
--
--  SET @PARAMDEF = '
--    @pCUSTID INT,
--    @pCUSTNAME VARCHAR(100),
--    @pCUSTLASTACT DATETIME';
--
--  PRINT @SELECT;
----  PRINT @PARAMDEF;    

--  EXECUTE sp_executesql @SELECT, @PARAMDEF,
--     @pCUSTID = @CUSTID,
--	 @pCUSTNAME = @CUSTNAME,
--	 @pCUSTLASTACT = @CUSTLASTACT;


END
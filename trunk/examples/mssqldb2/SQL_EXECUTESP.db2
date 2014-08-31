CREATE OR REPLACE PROCEDURE SQL_EXECUTESP  (
  IN SQLQUERY VARCHAR(32000),
  IN NAMEDPAR PARVARARGS
)
DYNAMIC RESULT SETS 1
BEGIN
  DECLARE PARS PARVALS;
  DECLARE MQUERY VARCHAR(32000);
  DECLARE I,CARD INT;
  DECLARE PARNAMES,PARVALUES PARVALS;
  DECLARE PNAME VARCHAR(100); 
  DECLARE ERRVAL INT;
  DECLARE dc CURSOR WITH RETURN TO CLIENT FOR STMT;
    
  SET PNAME = ARRAY_FIRST(NAMEDPAR);
  SET I = 1;  
  LO:  
  LOOP
   IF PNAME IS NULL THEN LEAVE LO; END IF;
   SET PARNAMES[I] = PNAME;
   SET PARVALUES[I] = NAMEDPAR[PNAME];
   SET PNAME = ARRAY_NEXT(NAMEDPAR,PNAME);
   SET I = I + 1;
  END LOOP LO; 
  
  
  CALL SQL_PREPARESTM(MQUERY,PARS,SQLQUERY,PARNAMES,PARVALUES);
--  CALL DBMS_OUTPUT.PUT_LINE(MQUERY);
  PREPARE STMT FROM MQUERY;
  IF PARS IS NULL THEN SET CARD = 0;
  ELSE 
    SET CARD = CARDINALITY(PARS);
  END IF;   
  IF CARD = 0 THEN
    OPEN DC;
  ELSEIF CARD = 1 THEN
    OPEN DC USING PARS[1];
  ELSEIF CARD = 2 THEN
    OPEN DC USING PARS[1],PARS[2];
  ELSEIF CARD = 3 THEN
    OPEN DC USING PARS[1],PARS[2],PARS[3];
  ELSE
    ERRVAL = RAISE_ERROR('70001',CA || ' number of markers in the statement too big';
  END IF;       
END

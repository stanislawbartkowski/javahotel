DROP MODULE DBMSJ_SQL@

CALL sqlj.remove_jar('DBMSJJAR')@
CALL SQLJ.INSTALL_JAR('file:///home/db2inst1/DBMSJ.jar', DBMSJJAR)@
CALL sqlj.refresh_classes()@

CREATE OR REPLACE MODULE DBMSJ_SQL@

--ALTER MODULE DBMSJ_SQL ADD TYPE ParamStringArray AS VARCHAR2(1000) ARRAY[100]@

CREATE OR REPLACE TYPE DBMSJ_SQL.ParamStringArray AS VARCHAR2(1000) ARRAY[100]@


ALTER MODULE DBMSJ_SQL PUBLISH FUNCTION OPEN_CURSOR ()
RETURNS INTEGER
NOT DETERMINISTIC
LANGUAGE JAVA
PARAMETER STYLE JAVA
NO DBINFO
FENCED
THREADSAFE
NO SQL
EXTERNAL NAME 'DBMSJJAR:com.ibm.db2.dbmsj.DB2_dbmsj.openCursor'@

ALTER MODULE DBMSJ_SQL PUBLISH PROCEDURE PARSE (C INT,statement VARCHAR(32672))
DYNAMIC RESULT SETS 0
DETERMINISTIC
LANGUAGE JAVA
PARAMETER STYLE JAVA
NO DBINFO
FENCED
THREADSAFE
NO SQL
PROGRAM TYPE SUB
EXTERNAL NAME 'DBMSJJAR:com.ibm.db2.dbmsj.DB2_dbmsj.parseQuery'@

ALTER MODULE DBMSJ_SQL PUBLISH PROCEDURE PARSEI (C INT,statement VARCHAR(32672))
DYNAMIC RESULT SETS 0
DETERMINISTIC
LANGUAGE JAVA
PARAMETER STYLE JAVA
NO DBINFO
FENCED
THREADSAFE
NO SQL
PROGRAM TYPE SUB
EXTERNAL NAME 'DBMSJJAR:com.ibm.db2.dbmsj.DB2_dbmsj.parseQueryI'@

ALTER MODULE DBMSJ_SQL PUBLISH PROCEDURE BIND_VARIABLE (C INT,name VARCHAR2(32672), value VARCHAR2(32672))
DYNAMIC RESULT SETS 0
DETERMINISTIC
LANGUAGE JAVA
PARAMETER STYLE JAVA
NO DBINFO
FENCED
THREADSAFE
NO SQL
PROGRAM TYPE SUB
EXTERNAL NAME 'DBMSJJAR:com.ibm.db2.dbmsj.DB2_dbmsj.bindV'@

ALTER MODULE DBMSJ_SQL PUBLISH PROCEDURE PREPARE_S_JAVA (X INT,OUT query VARCHAR2(32672), OUT param DBMSJ_SQL.ParamStringArray)
DYNAMIC RESULT SETS 0
DETERMINISTIC
LANGUAGE JAVA
PARAMETER STYLE JAVA
NO DBINFO
FENCED
THREADSAFE
NO SQL
PROGRAM TYPE SUB
EXTERNAL NAME 'DBMSJJAR:com.ibm.db2.dbmsj.DB2_dbmsj.prepareP'@

ALTER MODULE DBMSJ_SQL PUBLISH PROCEDURE EXECUTE (V_CUR INTEGER)
DYNAMIC RESULT SETS 1
P1: BEGIN
  DECLARE PARS DBMSJ_SQL.ParamStringArray;
  DECLARE Q VARCHAR2(32672);
  DECLARE CA,ERRRET INTEGER;
	-- Declare cursor
  DECLARE DC CURSOR WITH RETURN TO CLIENT FOR STMT;
  CALL  PREPARE_S_JAVA(V_CUR, Q, PARS);
  PREPARE STMT FROM Q; 
  SET CA = CARDINALITY(PARS);
  
--  CALL PRINT ('nu=' || CA);
--  CALL PRINT (q);
  
  CASE CA 
    WHEN 0 THEN 
      OPEN dc;
    WHEN 1 THEN
    OPEN dc USING pars[1];
  WHEN 2 THEN
    OPEN DC USING pars[1],pars[2];
  WHEN 3 THEN
    OPEN DC USING pars[1],pars[2],pars[3];
  WHEN 4 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4];
  WHEN 5 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5];
  WHEN 6 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6];
  WHEN 7 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7];
  WHEN 8 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8];
  WHEN 9 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9];
  WHEN 10 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10];
  WHEN 11 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11];
  WHEN 12 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12];  
   WHEN 13 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13];
  WHEN 14 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14];
  WHEN 15 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15];
  WHEN 16 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16];
  WHEN 17 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17];
  WHEN 18 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18];
  WHEN 19 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19];
  WHEN 20 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20];
  WHEN 21 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21];
  WHEN 22 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22];
  WHEN 23 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23];
  WHEN 24 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24];
  WHEN 25 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25];
  WHEN 26 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25],pars[26];
  WHEN 27 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25],pars[26],pars[27];
  WHEN 28 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25],pars[26],pars[27],pars[28];
  WHEN 29 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25],pars[26],pars[27],pars[28],pars[29];
  WHEN 30 THEN
    OPEN DC USING pars[1],pars[2],pars[3],pars[4],pars[5],pars[6],pars[7],pars[8],pars[9], pars[10],
                  pars[11], pars[12], pars[13], pars[14], pars[15], pars[16],pars[17],pars[18],pars[19],pars[20],
                  pars[21],pars[22],pars[23],pars[24],pars[25],pars[26],pars[27],pars[28],pars[29],pars[30];      
    ELSE
    SET errret = RAISE_ERROR('70001',' Number of markers in the statement too big ' || ca);
  END CASE;
	
END P1
@

DROP TABLE "TESTID";

CREATE TABLE "TESTID" (	"ID" VARCHAR(20));
 
INSERT INTO TESTID VALUES('G');  
INSERT INTO TESTID VALUES('H');  
INSERT INTO TESTID VALUES('A');
INSERT INTO TESTID VALUES('B');
INSERT INTO TESTID VALUES('C');
INSERT INTO TESTID VALUES('C');
  
INSERT INTO TESTID SELECT * FROM TESTID;
  
--  SELECT XIC_MERGE_A(ID) FROM TESTID;
--  SELECT LISTAGG(ID,',') WITHIN GROUP (ORDER BY ID) FROM TESTID;
  
SELECT LISTAGGDUPL(ID) FROM TESTID;

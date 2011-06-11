create or replace
PACKAGE A_NTILETEST AS 

--ORACLE CONSTANT BOOLEAN  := TRUE;
ORACLE CONSTANT BOOLEAN  := FALSE;

PROCEDURE RUN_TEST1;
PROCEDURE RUN_TEST2;
PROCEDURE RUN_TEST3;
PROCEDURE RUN_TEST4;

END A_NTFILETEST;
@
create or replace
PACKAGE BODY A_NTILETEST AS

  PROCEDURE PREPARE_TEST AS 
  BEGIN
    EXECUTE IMMEDIATE 'TRUNCATE TABLE EMPNO';
    INSERT INTO EMPNO VALUES(1,'7369','SMITH');
	  INSERT INTO EMPNO VALUES(1,'7499','ALLEN');
	  INSERT INTO EMPNO VALUES(1,'7521','WARD');
	  INSERT INTO EMPNO VALUES(1,'7566','JONES');
	  INSERT INTO EMPNO VALUES(2,'7654','MARTIN');
	  INSERT INTO EMPNO VALUES(2,'7698','BLAKE');
	  INSERT INTO EMPNO VALUES(2,'7782','CLARK');
	  INSERT INTO EMPNO VALUES(2,'7788','SCOTT');
	  INSERT INTO EMPNO VALUES(3,'7839','KING');
	  INSERT INTO EMPNO VALUES(3,'7844','TURNER');
	  INSERT INTO EMPNO VALUES(3,'7876','ADAMS');
	  INSERT INTO EMPNO VALUES(4,'7900','JAMES');
	  INSERT INTO EMPNO VALUES(4,'7902','FORD');
	  INSERT INTO EMPNO VALUES(4,'7934','MILLER');
    COMMIT; 
  END;
  
  PROCEDURE PREPARE_TEST2 AS
  BEGIN
    EXECUTE IMMEDIATE 'TRUNCATE TABLE EMPSALARY';
    INSERT INTO EMPSALARY VALUES ('Greenberg',12000);
    INSERT INTO EMPSALARY VALUES ('Faviet', 9000);
    INSERT INTO EMPSALARY VALUES ('Chen',8200);
    INSERT INTO EMPSALARY VALUES ('Urman',7800);
    INSERT INTO EMPSALARY VALUES ('Sciarra',7700);
    INSERT INTO EMPSALARY VALUES ('Popp',6900);  
  END;  
  
  FUNCTION NTILE_FUN(A IN INTEGER, BUCKETNO IN INTEGER, NOFROWS IN INTEGER) 
  RETURN INTEGER
  AS
  MINROWS INTEGER;
  MINREST INTEGER;
  X INTEGER;
  FIRSTB INTEGER;
  LASTB INTEGER;
  ACTB INTEGER;
  BEGIN
     MINROWS := FLOOR(NOFROWS / BUCKETNO);
     MINREST := NOFROWS - (BUCKETNO * MINROWS);

--     X := FLOOR(A / MINROWS);
--     DBMS_OUTPUT.PUT_LINE(NOFROWS || ' ' || A || '  ' || MINROWS || ' ' || X );

     ACTB := 1;
     FIRSTB := 1;
     WHILE FIRSTB <= NOFROWS LOOP
       LASTB := FIRSTB + MINROWS - 1;
       IF MINREST > 0 THEN
         LASTB := LASTB + 1;
         MINREST := MINREST - 1;
       END IF;
       IF A <= LASTB THEN
         EXIT;
       END IF;
       FIRSTB := LASTB + 1;
       ACTB := ACTB + 1;
     END LOOP;      
     RETURN ACTB;
  END;
    
  FUNCTION RET_STMT RETURN VARCHAR2 AS
  BEGIN
     $IF A_NTILETEST.ORACLE $THEN
      RETURN 'select ntile(4)over(order by empno) grp,
	        empno,
	        ename
	    from empno';
     $ELSE
      return 'select NTILE_FUN(row_number( )over(order by empno),4,
             (SELECT COUNT(*) FROM empno)) grp,
	        empno,
	        ename
	   from empno order by empno';
     $END  
  END;   

 FUNCTION RET_STMT2 RETURN VARCHAR2 AS
  BEGIN
     $IF A_NTILETEST.ORACLE $THEN
      RETURN 'SELECT last_name, salary, NTILE(4) OVER (ORDER BY salary DESC) 
             AS quartile FROM empsalary';
     $ELSE
      RETURN 'SELECT last_name, salary, 
             NTILE_FUN(row_number( )over(ORDER BY salary DESC),4, 
             (SELECT COUNT(*) FROM empsalary)) 
             AS quartile FROM empsalary';
     $END
  END;   

  PROCEDURE RUN_TEST1 AS
  emp_refcur      SYS_REFCURSOR;
  GRP INTEGER;
  EMPNO VARCHAR2(20);
  ENAME VARCHAR2(100);
  BEGIN
    PREPARE_TEST;
    OPEN emp_refcur FOR RET_STMT;
       LOOP
        FETCH emp_refcur INTO GRP,EMPNO,ENAME;
        EXIT WHEN emp_refcur%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(grp || ' ' || empno || '  ' || ename);
    END LOOP;
    CLOSE emp_refcur;
    
  END RUN_TEST1;
  
  PROCEDURE RUN_TEST2 AS
   emp_refcur      SYS_REFCURSOR;
   quartile INTEGER;
   SALARY NUMBER;
   LASTNAME VARCHAR2(100);
   BEGIN
     PREPARE_TEST2; 
     OPEN emp_refcur FOR RET_STMT2;
       LOOP
        FETCH emp_refcur INTO LASTNAME,SALARY,quartile;
        EXIT WHEN emp_refcur%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(LASTNAME || ' ' || SALARY || '  ' || quartile);
    END LOOP;
    CLOSE emp_refcur;
   END RUN_TEST2; 
   
PROCEDURE PREPARE_TEST3 AS
BEGIN
  EXECUTE IMMEDIATE 'TRUNCATE TABLE ContestResults';
  INSERT INTO ContestResults VALUES (1,'Pumpkin',    716, 'Chad Johnson');
  INSERT INTO ContestResults VALUES (2,'Pumpkin',    679, 'George Kopsell');
  INSERT INTO ContestResults VALUES (3,'Pumpkin',    679, 'Dan Gardner');
  INSERT INTO ContestResults VALUES (4,'Pumpkin',    481, 'John Suydam');
  INSERT INTO ContestResults VALUES (5,'Pumpkin',    452, 'Mark Bardin');
  INSERT INTO ContestResults VALUES (6,'Pumpkin',    442, 'Bill Kallas');
  INSERT INTO ContestResults VALUES (7,'Pumpkin',    428, 'Theresa Helmer');
  INSERT INTO ContestResults VALUES (8,'Pumpkin',    426, 'Terry Helmer');
  INSERT INTO ContestResults VALUES (9,'Pumpkin',    346, 'Gary Spiel');
  INSERT INTO ContestResults VALUES (10,'Pumpkin',    331, 'Kevin Rabell');
  INSERT INTO ContestResults VALUES (11,'Pumpkin',    289, 'Jan Spiel');
  INSERT INTO ContestResults VALUES (12,'Pumpkin',    247, 'Harvey Zale');
  INSERT INTO ContestResults VALUES (13,'Pumpkin',    229, 'Harvey Zale');

  INSERT INTO ContestResults VALUES (14,'Squash',     462, 'Dan Gardner');
  INSERT INTO ContestResults VALUES (15,'Squash',     462, 'Harvey Zale') ;         -- I made up this entry
  INSERT INTO ContestResults VALUES (16,'Squash',     435, 'Terry Helmer');         -- I made up this entry
  INSERT INTO ContestResults VALUES (17,'Squash',     405, 'Gary Spiel');           -- I made up this entry

  INSERT INTO ContestResults VALUES (18,'Watermelon', 146, 'Mark Bardin');
  INSERT INTO ContestResults VALUES (19,'Watermelon', 139, 'Christine Daak');       -- I made up this entry
  INSERT INTO ContestResults VALUES (20,'Watermelon', 139, 'Carlotta Giudicelli');  -- I made up this entry
  INSERT INTO ContestResults VALUES (21,'Watermelon', 132, 'Ubaldo Piangi');        -- I made up this entry
  INSERT INTO ContestResults VALUES (22,'Watermelon', 132, 'Meg Giry');             -- I made up this entry
  INSERT INTO ContestResults VALUES (23,'Watermelon', 129, 'Joseph Buquet');        -- I made up this entry
  COMMIT;
END PREPARE_TEST3;

 FUNCTION RET_STMT3 RETURN VARCHAR2 AS
  BEGIN
     $IF A_NTILETEST.ORACLE $THEN
     RETURN '
   SELECT
        Category,
        Weight,
        Entrant,
        NTILE(2) OVER (
            PARTITION BY Category
            ORDER BY Weight DESC
        ) AS Ntile
   FROM ContestResults
    ';
     $ELSE
    RETURN '  
   SELECT
        Category,
        Weight,
        Entrant,
        NTILE_FUN(row_number( )over(
            PARTITION BY Category
            ORDER BY Weight DESC),2, 
            (SELECT COUNT(*) FROM ContestResults as C WHERE C.Category = CC.Category) 
        ) AS Ntile
   FROM ContestResults AS CC
    ';
     $END
  END;   

PROCEDURE RUN_TEST3 AS
   emp_refcur      SYS_REFCURSOR;
   Category    varchar(10);
   Weight      int;
   Entrant     varchar(20);
   NT int;
BEGIN
     PREPARE_TEST3;
     OPEN emp_refcur FOR RET_STMT3;
       LOOP
        FETCH emp_refcur INTO Category,Weight,Entrant,NT;
        EXIT WHEN emp_refcur%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(NT || ' ' || Category || ' ' || Weight || ' ' || Entrant);
    END LOOP;
    CLOSE emp_refcur;
  
END RUN_TEST3;  

 FUNCTION RET_STMT4 RETURN VARCHAR2 AS
  BEGIN
     $IF A_NTILETEST.ORACLE $THEN
     RETURN '
   SELECT
        Category,
        Weight,
        Entrant,
        NTILE(2) OVER (
            ORDER BY Weight DESC
        ) AS Ntile
    FROM ContestResults
    WHERE Category = ''Squash''
    ';
     $ELSE
     RETURN '
   SELECT
        Category,
        Weight,
        Entrant,
        NTILE_FUN(
           row_number( )over(   ORDER BY Weight DESC),
           2,
           (SELECT COUNT(*) FROM ContestResults WHERE Category = ''Squash'') 
        ) AS Ntile
    FROM ContestResults
    WHERE Category = ''Squash''
    ';
     $END
  END;   

PROCEDURE RUN_TEST4 AS
   emp_refcur      SYS_REFCURSOR;
   Category    varchar(10);
   Weight      int;
   Entrant     varchar(20);
   NT int;
BEGIN
     PREPARE_TEST3;
     OPEN emp_refcur FOR RET_STMT4;
       LOOP
        FETCH emp_refcur INTO Category,Weight,Entrant,NT;
        EXIT WHEN emp_refcur%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(NT || ' ' || Category || ' ' || Weight || ' ' || Entrant);
    END LOOP;
    CLOSE emp_refcur;
END RUN_TEST4;

END A_NTILETEST;
@
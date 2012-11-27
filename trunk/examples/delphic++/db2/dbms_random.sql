CREATE OR REPLACE PACKAGE DBMS_RANDOM AS
  PROCEDURE INITIALIZE (val  IN  BINARY_INTEGER);
-- IMPORTANT: this RANDOM implementation is not good
-- it produces more even then odd integer numbers
-- use better implementation for a business purpose  
  FUNCTION RANDOM RETURN binary_integer;
  PROCEDURE TERMINATE; 
END@

CREATE OR REPLACE PACKAGE BODY DBMS_RANDOM AS

-- Good
  TWOPOWER31 CONSTANT BINARY_INTEGER := DOUBLE(2147483646);
-- Produces even numbers all the time  
--  TWOPOWER31 CONSTANT BINARY_INTEGER := DOUBLE(2147483647);
-- Overflow (but works from command line)
--  TWOPOWER31 CONSTANT BINARY_INTEGER := DOUBLE(2147483648);

  PROCEDURE INITIALIZE (val  IN  BINARY_INTEGER) AS
   R DOUBLE;
  BEGIN
    R := RAND(val);
  END;
  
  FUNCTION RANDOM RETURN binary_integer AS
    R DOUBLE;
    RI INTEGER;
  BEGIN
     R := RAND;
     IF (R < 0.5) THEN
       R := 0 - R;
     ELSE
       R := DOUBLE(1) - R;
     END IF;  
     RI := R * TWOPOWER31;
     RETURN RI;
  END;
    
  PROCEDURE TERMINATE AS
  BEGIN
    NULL;
  END;  
END;
@
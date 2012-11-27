create or replace
PROCEDURE INITIALIZERANDOM
AS
  rn NUMBER(20);
BEGIN
  SELECT hsecs INTO rn FROM gv$timer;
  dbms_random.initialize(rn);
END INITIALIZERANDOM;
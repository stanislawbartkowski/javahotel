CREATE OR REPLACE
PROCEDURE INITIALIZERANDOM
AS
  rn BIGINT;
  seed BINARY_INTEGER;
BEGIN
  SELECT DBMS_UTILITY.GET_TIME INTO rn FROM DUAL;
  seed := MOD(RN,2147483647);
  dbms_random.initialize(seed);
END INITIALIZERANDOM;
@
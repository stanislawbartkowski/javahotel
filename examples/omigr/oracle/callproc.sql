create or replace PROCEDURE SIMPLEPROC(RES OUT INTEGER,SOU IN INTEGER)
AS
begin
  RES := SOU;
end;
/

create or replace function RETPROC(RET IN INTEGER)
return integer
AS
begin
  return ret;
end;
/
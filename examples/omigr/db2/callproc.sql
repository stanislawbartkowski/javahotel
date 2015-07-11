CREATE OR REPLACE PROCEDURE SIMPLEPROC(OUT RES INTEGER,IN SOU INTEGER)
   LANGUAGE SQL
BEGIN
  SET RES = SOU;
END
@

create or replace function RETPROC(IN RET INTEGER)
returns integer
begin
  return ret;
end
@

create table blobtable (id int, filename varchar(100), fileb blob)@
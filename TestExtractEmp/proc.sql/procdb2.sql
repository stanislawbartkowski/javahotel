CREATE OR REPLACE FUNCTION EMP_SEARCH_INT(
  p_empname VARCHAR2 DEFAULT NULL,
  p_deptname VARCHAR2 DEFAULT NULL,
  p_mgmname VARCHAR2 DEFAULT NULL)
  RETURN INTEGER
AS 
  v_cmd VARCHAR2(32672);
  v_cur INTEGER;
  v_aux NUMBER;
BEGIN
  -- first part of the statement
  v_cmd := 'SELECT ENAME,JOB,DNAME FROM EMP E,DEPT D WHERE E.DEPTNO = D.DEPTNO';
  -- parameters enhancement
  IF (p_empname IS NOT NULL) THEN
    v_cmd := v_cmd || ' AND ENAME = :p_empname';
  END IF;  
  IF (p_deptname IS NOT NULL) THEN
    v_cmd := v_cmd || ' AND DNAME = :p_deptname';
  END IF;
  IF (p_mgmname IS NOT NULL) THEN
    v_cmd := v_cmd || ' AND MGR IN (SELECT EMPNO FROM EMP WHERE ENAME= :p_mgmname)';
  END IF;
  
  v_cmd := v_cmd || ' ORDER BY ENAME';
  
  -- preparation
  v_cur := dbmsj_sql.open_cursor;
  dbmsj_sql.parse(c => v_cur, statement => v_cmd);
  
  -- parameter substitution
  IF (p_empname IS NOT NULL) THEN
    dbmsj_sql.bind_variable(c => v_cur,name => ':p_empname',value => p_empname);
  END IF;  
  IF (p_deptname IS NOT NULL) THEN
    dbmsj_sql.bind_variable(c => v_cur,name => ':p_deptname',value => p_deptname);
  END IF;
  IF (p_mgmname IS NOT NULL) THEN
    dbmsj_sql.bind_variable(c => v_cur,name => ':p_mgmname',value => p_mgmname);
  END IF;
  
  RETURN v_cur;
  
END EMP_SEARCH;
@

CREATE OR REPLACE PROCEDURE EMP_SEARCH(
  p_empname VARCHAR2(32672) DEFAULT NULL,
  p_deptname VARCHAR2(32672) DEFAULT NULL,
  p_mgmname VARCHAR2(32672) DEFAULT NULL)
DYNAMIC RESULT SETS 1
P1: BEGIN
  DECLARE V_CUR INTEGER;
  SET V_CUR = EMP_SEARCH_INT(p_empname,p_deptname,p_mgmname);
  CALL dbmsj_sql.execute(v_cur);
END P1
@

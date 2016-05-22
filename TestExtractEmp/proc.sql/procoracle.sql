CREATE OR REPLACE FUNCTION EMP_SEARCH(
  p_empname VARCHAR2 DEFAULT NULL,
  p_deptname VARCHAR2 DEFAULT NULL,
  p_mgmname VARCHAR2 DEFAULT NULL)
RETURN SYS_REFCURSOR AS 
  v_refcursor SYS_REFCURSOR;
  v_cmd VARCHAR2(32767 CHAR);
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
  v_cur := dbms_sql.open_cursor;
  dbms_sql.parse(c => v_cur, statement => v_cmd, language_flag => dbms_sql.native);
  
  -- parameter substitution
  IF (p_empname IS NOT NULL) THEN
    dbms_sql.bind_variable(c => v_cur,name => ':p_empname',value => p_empname);
  END IF;  
  IF (p_deptname IS NOT NULL) THEN
    dbms_sql.bind_variable(c => v_cur,name => ':p_deptname',value => p_deptname);
  END IF;
  IF (p_mgmname IS NOT NULL) THEN
    dbms_sql.bind_variable(c => v_cur,name => ':p_mgmname',value => p_mgmname);
  END IF;
  
  -- final ivocation
  v_aux := dbms_sql.execute(v_cur);
  v_refcursor:= dbms_sql.to_refcursor(cursor_number => v_cur);
  RETURN v_refcursor;
  
END EMP_SEARCH;
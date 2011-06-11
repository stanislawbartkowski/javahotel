CREATE TABLE EMPNO (GRP INTEGER, EMPNO VARCHAR2(20), ENAME VARCHAR2(100));
CREATE TABLE EMPSALARY (LAST_NAME VARCHAR2(100), SALARY NUMBER);
CREATE TABLE ContestResults (
    ColID       int             NOT NULL,
    Category    varchar(10)     NOT NULL,
    Weight      int             NOT NULL,
    Entrant     varchar(20)    NOT NULL,

    CONSTRAINT PK_ContestResults PRIMARY KEY (ColID)
);
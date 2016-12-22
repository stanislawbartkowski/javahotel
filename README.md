# Simple solution to ease Oracle to DB2 SQL objects migration

The solution prepares migration of SQL objects: like tables, packages, stored procedures etc. from Oracle to DB2. The solution is available as Java application callable from command line or can imported as Eclipse project to evaluate or adjust to a particular needs.

Functionality implemented.

* Statistics, list of objects. Browse through the Oracle source code and enumerate the number of particular SQL objects. The output can be imported to Excel or Libre Office and for migration progress monitoring.

* Extract objects and performs some simple transformation. Every object is stored as a single file and all objects of the same type, like UDF, packages, triggers are saved in a separate directory.

* Comparison. When migration is finished compares the list of objects from Oracle source code with the DB2 deployed objects. Allows doublecheck that nothing  has been missed during migration.

# Eclipse project

Tested with Eclipse Neon.

1. Eclipse -> Import -> Projects from Git 
2. Clone URI -> URI: https://github.com/stanislawbartkowski/javahotel
3. Deselect All and select jmigration branch only
4. Select base directory for project
5. Check "Import existing Eclipse project"
6. Finish 

# Command line

Prerequisites:

* Java 8
* If comparison feature is used also DB2 JDBC driver.

Preparation

1. Clone or download project : git clone  https://github.com/stanislawbartkowski/javahotel.git -b jmigration
2. cd javahotel
3. ant (dist/OMigration.jar file is created)
4. cd sh
5. modify source.rc file according to your environment

Execute 

1. cd sh
6. list
7. extract
8. compare 

# Command line 

### source.rc


| Environment variable     | Value           | Sample  |
| ------------- |:-------------:| -----:|
| INPUTFILE      | Input Oracle DDL file | /mnt/exported/oracle/oracle.DDL |
| OJAR      | path to OMigration.jar file      |  ../dist/OMigrationDB2.jar |
| URL | DB2 JDBC url   | jdbc:db2://db211:50000/eod |
| USER | DB2 user name | db2inst1 
| PASSWD | DB2 password | db2inst1
| DB2JAR | path to DB2 JDBC driver | /opt/ibm/db2/V11.1/java/db2jcc4.jar

Important: URL, USER, PASSWORD and DB2JAR variables are required only if compare script is used.

Example

```
export INPUTFILE=/home/sbartkowski/Dokumenty/oracle/oracle.DDL
export OJAR=../dist/OMigrationDB2.jar
# only if compare is called
export URL=jdbc:db2://db211:50000/eod:retrieveMessagesFromServerOnGetMessage=true
export USER=db2inst1
export PASSWD=db2inst1
export DB2JAR=/opt/ibm/db2/V11.1/java/db2jcc4.jar
```
### list

sh/list script

List all objects found in Oracle DDL source file

### extract

sh/extract script

Extracts all objects from Oracle DDL source file and stores them to /tmp/db2odir directory. The content of the output directory is automatically removed at the beginning. Every object is kept in a single file and all objects of particular type are saved in a separate directory.
During extraction some simple tranformation is applied.

### compare

sh/compare script

Should be executed during or after migration. Retrieves all objects from Oracle DDL source file and compares against current of DB2 database. Reports all objects missed. The script needs a single parameter, the object type scanned or ALL meaning all objects

sh/compare {object type}

{object type} :  FUNCTION,SEQUENCE,TABLE,TRIGGER,VIEW,PACKAGE,PROCEDURE,GLOBALTEMP,TYPE,BODY or ALL

Example:

sh/compare SEQUENCE 

# Tranformation

[DB2 Oracle Compatibility Mode](http://www.ibm.com/support/knowledgecenter/SSEPGG_11.1.0/com.ibm.db2.luw.apdv.porting.doc/doc/c_compat_oracle.html) allows execution of Oracle PL/SQL statements almost out of the box. Nevertheless, some adjustments are required.
Examples

### Remove trails 

```
CREATE
  TABLE SCHEM.acc_history
  (
    BUSINESSOBJECTID NVARCHAR2 (25) NOT NULL ,
    MACHINEX NVARCHAR2 (50) NOT NULL ,
  )
  TABLESPACE EOAS LOGGING ;
```
All stuff after closing parentheses is remove. Optionally DB2 table space clause is added

```
CREATE
  TABLE SCHEM.acc_history
  (
    BUSINESSOBJECTID NVARCHAR2 (25) NOT NULL ,
    MACHINEX NVARCHAR2 (50) NOT NULL ,
  )
IN USERSPACE16;
```

### Object type

```
CREATE OR REPLACE TYPE schem.customer
AS
  OBJECT
  (
    ID NUMBER (9) ,
    NAME NVARCHAR2 (255) ) FINAL ;
  /
```
DB2 does not support OBJECT type. It is replace by ROW and FINAL is removed. Also object initializer is not supported in PL/SQL code, shoule be replaced by manual initialization.

```

CREATE OR REPLACE TYPE schem.customer
AS
  ROW
  (
    ID NUMBER (9) ,
    NAME NVARCHAR2 (255) ) ;
  @
```

### Sequence MAX to big

```
CREATE SEQUENCE XXXX.CM_CONTAINERPMCHANGE_SEQ INCREMENT BY 1 MAXVALUE
  999999999999999999999999999 MINVALUE 0 NOCACHE ;
```

Is replaced by maximum value supported by DB2
```
CREATE SEQUENCE XXXX.CM_CONTAINERPMCHANGE_SEQ INCREMENT BY 1 MAXVALUE
  99999999999999999999999999 MINVALUE 0 NOCACHE ;
```

# Custom tranformation

The Java project can be extended by custom tranformation. Current tranformarmations are stored in org.migration.fix.impl package.

(wiki/Zrzut ekranu z 2016-12-22 23:56:47.png)









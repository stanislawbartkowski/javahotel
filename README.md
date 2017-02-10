# Exporting data from JDBC supported databases in different format

http://hoteljavaopensource.blogspot.com/2016/04/oracle-db2-hive-data-migration.html

# The solution layout:
* export.sh Main script
* export.rc Environment variables
* {db}.properties Property file referenced by CONNPROP variable in export.rc. Contains connection parameters.
* jars Directory containing jar exported from Eclipse project with Java code
* jdbc Directory containing all dependencies, particularly JDBC drivers appropriate for the database used.
* log Log directory

# Configuration, export.rc file
* EXPORT_DIR Directory where extracted data are downloaded. 
* LOGIR Log directory
* LOADSQL File name for load data script created by the solution
* CONNPROP Property file with JDBC connection details

# Property file 
* sourceurl
* user
* password
* drivername
* sourcedb  **db2**, **mssql**, **oracle**. It is important to specify oracle value for Oracle database. For some reason, Oracle JDBC driver does not report properly Oracle temporary table. In order to exclude temporary tables from data extraction process, a specific Oracle catalog view is referenced.
* destdb **db2** or **hive**. The default is db2. It is important to specify hive if data extracted is going to be consumed by Hive later on.

# sh/export.sh, main script description

Format: sh/export.sh {action} parameters related to {action}

*listoftables*
* Parameters: list of schemas
* Creates a list of tables extracted from source database and schemas. The list is stored in the table.list file. The list is later reused by extractfromlist action. The list can be reviewed and modified manually.
* Example: sh/export.sh listoftables DATA ACCOUNT

*extracttable*
* Parameters: list of tables.
* Extract data from tables specified. More than one table can be provided.
* Example: sh/export  extract.sh extracttable data.invoices data.lines account.customers

*extractlist*
* No parameters
* Extract data from tables found in table.list file. The table.list file can be created by listoftable action.
* Example: sh/extract.sh extractfromlist

*extractschemas*
* No parameters
* Extract list of schemas from a source database. The list is stored in schema.list file. This action is not connected with any other actions, can be used as a data discovery.
* Example: sh/export.sh extractschemas

*extracthivetables*
* No parameters
* Creates create.list script file containing Hive table CREATE commands. The table.list file contains a list of tables in source database. Look below for more details.
* Example: sh/export.sh extracthivetables

*extracthivetable*
* Parameters: list of tables
* Extract data from tables specified as the parameters. Data is going to be consumed by Hive later on. Important: in order to have data extracted properly destdb=hive parameter should be specified additionally in the property file.
* Example: sh/extract.sh extracthivetable Sales.CountryRegionCurrency Sales.CreditCard Sales.Currency

*extracthivefromlist*
* No parameters
* Extract data from all tables found in table.list file. Data are extracted in the format expected by Hive.
* Example:  sh/extract.sh extracthivefromlist

*extractnumberofrecords*
* Parameters: output file name
* Extract number of records for tables found in table.list file. CSV file is created, every line containing information related to a single table. The command can be used as a rough data migration verifier. Look below.
* Example: extract.sh extractnumberofrecords number.db2

# DB2 data loader
The solution does not have any support for database schema migration. To migrate database schema use free tool IBM Database Conversion  Workbench.
Exported data are downloaded to EXPORT_DIR directory (for instance /tmp/data). Together with data extraction a LOADSQL script is created with a sequence of DB2 LOAD command to load data into DB2 database. DB2 command line processor, db2, is used for data consumption.  An example of LOAD command prepared

>LOAD FROM /tmp/data/sales_personal_data.txt
>OF DEL
>MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR
>DUMPFILE=/tmp/data/dump/sales_personal_data.txt
>MESSAGES /tmp/data/msg/sales_personal_data.txt
>REPLACE INTO SALES.PERSONAL_DATA

Also, all LOB columns are migrated. If input table contains any LOB column, additional /tmp/data/{table name} directory is created and in this directory files containing data extracted from LOB columns are stored and LOAD command contains a clause to load LOB data from that directory.
Before starting loading process, all extracted data should be moved to the DB2 host machine, otherwise LOAD command fails.
A typical scenario for data migration from Oracle or MSSQL database to DB2
9. Prepare property file containing connection data for Oracle database. The property file should contain sourcedb=oracle parameter
9. Extract all schemas from Oracle database using ./export.sh extractschemas. Identify schema or schemas containing application data to be migrated.
9. Extract list of tables using command ./export.sh listoftables. Verify and modify the list if necessary.
9. Extract data from source database using ./export.sh exportfromlist command.
9. Move extracted data and db2load.sql script to the DB2 host machine. Important: extracted data should be moved to the same directory. 
9. Connect to DB2 instance and execute command: db2 -tf db2load.sql
9. Verify the migration using ./extract.sh extractnumberofrecords command. Look below for details.
9. If necessary, repeat the process for all tables or for a single table using ./export.sh extracttable command.

# HIVE data loader
For HIVE not only data migration is implemented but also schema migration. A mapping between JDBC data types and Hive data types can be checked out in Java source code. Columns not compatible with any Hive data types, for an instance LOB columns, are ignored. Because data are extracted in CSV format, it is very important that Hive data table schema is synchronized with data extracted.
A typical scenario for data migration from MSSQL to Hive.
9. Prepare property file with connection data for MS/SQL database. The property file should contain destdb=hive parameter.
9. Extract all schemas from MS/SQL database using ./export.sh extractschemas. Identify schema or schemas containing application data to be migrated.
9. Extract list of tables using command ./export.sh listoftables. Verify and modify the list if necessary.
9. Identify all schemas, create necessary databases/schemas manually in hive.
9. Prepare script to create Hive tables by executing sh/export.sh extracthivetables.
9. Move script to Hive host machine and run beeline -f create.list command.
9. Extract data from source database using ./export.sh exportfromlist command.
9. Move data to Hadoop node. **Important**: the data should be deployed to the node where Hive2Server is operating, not the node where beeline is connect to. Sometimes nodes are different.
9. It is also possible to copy data to HDFS file system, but remove LOCAL keyword from hive.load file
9. Load data into Hive using beeline -f hive.load command
9. If necessary, repeat the process for all tables or for a single table using ./export.sh extracttable command.

# Data migration verification
9. sh /export.sh extractnumberofrecords action can be used as a rough verification if data migration did not fail. Assume that we have an access to DB2 instance.
9. Make sure that table.list contains a list of all table to be migrated.
9. Execute ./export.sh extractnumberofrecords number.oracle for Oracle source database.
9. Prepare property file for DB2 connection. Modify export.rc configuration file.
9. Execute ./export.sh extractnumberofrecords number.db2 for DB2 source database. The command should be executed over the same table.list file
9. Execute db2 commands:
 9. db2 "create table oracle_table(tname varchar(2500),nor int)"
 9. db2 load client from $PWD/number.oracle of del MODIFIED BY COLDEL',' insert into oracle_table
 9. db2 "create table db2_table like oracle_table"
 9. db2 load client from $PWD/number.db2 of del MODIFIED BY COLDEL',' insert into db2_table
 9. db2 "select cast (d.tname as char(30)),d.nor as db2,o.nor as oracle from db2_table d,oracle_table o where d.tname = o.tname and d.nor != o.nor"

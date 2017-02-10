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




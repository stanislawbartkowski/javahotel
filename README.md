# Load Hive table incrementally as Oozie/Sqoop job
 
A simple solution to synchronize source external RDMS table with target Hive table. The solution is developed as Oozie workflow and can be launched also as Oozie coordinator task. 
 
 The solution was also tested with Kerberos security.
 
 # Prerequisites
 
 * Register JDBC driver jar in Ooozie. The solution was tested against MySQL. 
 > hdfs dfs -copyFromLocal /usr/iop/4.2.0.0/sqoop/lib/mysql-connector-java.jar /user/oozie/share/lib/lib_20170317143553/sqoop
 
 > oozie admin -sharelibupdate
 * Register hive-exec jar in hive2 library if not registered yet. Otherwise, you will receive java.lang.NoClassDefFoundError: org/apache/hadoop/hive/conf/HiveConf exception while calling hive2 Oozie task
 > hdfs dfs -cp /user/oozie/share/lib/lib_20170411170541/hive/hive-exec-1.2.1-IBM-22.jar /user/oozie/share/lib/lib_20170411170541/hive2
 
 > oozie admin -sharelibupdate
 
* Add also org.apache.oozie.action.hadoop.Hive2ActionExecutor to oozie.service.ActionService.executor.ext.classes oozie-site configuration file. Look into bin/soozie and bin/scoord script file for more details.

# Installation

* Download or clone repository and change the main directory name to **loadhive**. The directory structure after deployment:

<kbd>

loadhive

 * coordinator.properties
 
 * echoscript.sh   
 
 * lib
 
   * hive-site.xml
   
 * workflow.xml
 
 * common.properties  
 
 * coordinator.xml 
 
 * job.properties  
 
 * preparelast.sh
 
 * bin
 
   * soozie
   
   * scoord
</kbd>

* Modify common.properties configuration file. It is going to be used as a template for job.propertiers and coordinator.properties. 

Parameter | Value | Configure | Example
----------|-------|-----|----------
namenode| Check oozie | yes | hdfs://nc9128110007.kraklab.pl.ibm.com:8020
jobtracker | Check oozie | yes | nc9128110007.kraklab.pl.ibm.com:8050
PREFIX | HDFS home directory | no | ${namenode}/user 
oozie.use.system.libpath | Check oozie | no | true 
user.name | Check oozie, owner of the process | yes | sb
queueName | Check oozoe | if necessary | default
oozie.use.system.libpath| Check oozie | no | true
oozie.libpath | Check oozie | yes |/user/oozie/share/lib/lib_20170405230852
oozie.action.sharelib.for.sqoop | Check oozie | no | hive,hcatalog,sqoop,hive2 (do not change)|
prescript | Preparation script | no | preparelast.sh
prescriptPath | Full path | no |${oozie.wf.application.path}/${prescript}
echoscript | Test script | no | echoscript.sh
echoscriptPath | Full path | no | ${oozie.wf.application.path}/${echoscript}
P_TABLE | Source table | yes | customers
P_ID | Incremental column | yes | customerNumber
P_DATABASE | Hive target database | yes | salesdb
P_URL | Hive URL | yes | jdbc:hive2://nc9128110007.kraklab.pl.ibm.com:10000/
P_HIVE_KERBEROS | Hive Kerbers principal | yes | hive/nc9128110007.kraklab.pl.ibm.com@SB.COM
P_METASTORE_KERBEROS | Hive metastore Kerberos principal | yes | hive/nc9128109010.kraklab.pl.ibm.com@SB.COM
P_METASTORE | Hive metastore URI | yes | thrift://nc9128109010.kraklab.pl.ibm.com:9083
S_URL | Source database URL | yes |jdbc:mysql://re64/classicmodels
S_USER | User name | yes | test
S_PASSWORD | Password | yes | test
S_DRIVER | JDBC driver class | yes |com.mysql.jdbc.Driver
S_STAGETABLE | Hive staging table | if necessary | stagedb.customers
HIVE_CREATESCRIPT | Hive script | no | ${oozie.wf.application.path}/creates.sql
HIVE_UPDATESCRIPT | Hive script | no | ${oozie.wf.application.path}/updates.sql
K_PRINC | Kerberos credentials kinit to authenticate user.name | yes | sb@SB.COM
K_PASSWORD | Kerberos password | yes |  secret

* Copy your hive-site.xml file to lib subdirectory (very important !)
* Modify bin/soozie and bin/scoord script according to your environment.

# Execution

## How it works
The solution transfers data from source table to target hive table in Parquet format. The two-hop approach is implemented: data is loaded into a staging table in text format and then target Parquet table is updated or created if not exists.
Data is loaded in incremental mode. Firstly the maximum value of **P_ID** column in target Hive table is calculated. Then only rows where **P_ID** is greater than max are selected and loaded into the staging table. If no new data is discovered, the target table is not modified.

Only new rows are inserted. The solution is unable to recognize updated or deleted rows. It is not "Capture Data Change" solution.

## Solution description

The following Oozie tasks are used: shell, sqoop and hive2.

The workflow contains the following steps:

1. preparelast.sh, shell action. Check if target Hive table **P_DATABASE**.**P_TABLE** exists. If yes than retrieves the maximum value of **P_ID** column. Both information, maximum value and the existence of target table, are returned as KEY=VALUE pairs according to shell action specification. preparelast.sh script logs information in HDFS /tmp/loadfile file.

1. sqoop action. Selects data from RDMS **P_TABLE** table. Only rows **P_ID** > maximum are extracted. Data is loaded into Hive **S_STAGETABLE** as text file.

1. Oozie switch action regarding if Hive target table exists or not

1. If table does not exists execute CREATE **P_DATABASE**.**PTABLE** STORED AS PARQUET AS SELECT * FROM **S_STAGETABLE**

1. If table is already created execute INSERT INTO **P_DATABASE**.**PTABLE** SELECT * FROM **S_STAGETABLE**

# Launching

* bin/soozie script file. Starts data loading as a single Oozie task.
* bin/scoord script file. Enables data loading as Oozie coordinator task running in the background.

# Testing

For testing, load MySQL tutorial database. 

http://www.mysqltutorial.org/mysql-sample-database.aspx

In order to load data from **customers** table it is enough to set:

* P_TABLE=customers
* P_ID=customerNumber
* S_URL=jdbc.mysql://host-name/classicmodels
* S_STAGETABLE=stagedb.customers

After data is loaded for the first time, try incremental loading. Insert into MySQL/classcismodels/customers table
> insert into customers values(497,'Galactic','Andrew','John','23-678-09','High Street',null,'Pcim','Dolny','02-234','Gabon',1612,234.2);





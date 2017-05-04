# Load Hive table incrementally as Oozie/Sqoop job
 
 Simple solution to synchronize external RDMS table with Hive table. The solution is developed as Oozie workflow and can be lauch also as Oozie coordinator task. 
 The solution was also tested with Kerberos security
 
 # Prerequisites
 
 * Register JDBC driver jar in Ooozie. The solution was tested against MySQL. 
 > hdfs dfs -copyFromLocal /usr/iop/4.2.0.0/sqoop/lib/mysql-connector-java.jar /user/oozie/share/lib/lib_20170317143553/sqoop
 
 > oozie admin -sharelibupdate
 * Register hive-exec jar in hive2 library if not registered yet. Otherwise, you will receive java.lang.NoClassDefFoundError: org/apache/hadoop/hive/conf/HiveConf exception while calling hive2 Oozie task
 > hdfs dfs -cp /user/oozie/share/lib/lib_20170411170541/hive/hive-exec-1.2.1-IBM-22.jar /user/oozie/share/lib/lib_20170411170541/hive2
 
 > oozie admin -sharelibupdate
 
* Add also org.apache.oozie.action.hadoop.Hive2ActionExecutor to oozie.service.ActionService.executor.ext.classes oozie-site configuration file

# Installation

* Download or clone repository and change main directory name to **loadhive**. The directory structure after deployment:

*~loadhive
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

* Modify common.properties configuration file. It is going to be used as a template for job.propertiers and coordinator.properties

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

# Testing

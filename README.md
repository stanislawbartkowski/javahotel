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

* Modify common.properties configuration file


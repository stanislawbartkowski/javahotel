# Introduction #

It is simple example how to create simple user interface in GWT (Google Web Toolkit) against one table in SAMPLE database in DB2. Simple web application running on Tomcat container.


# Details #

Following components should be downloaded and installed.

## DB2 ##

Download and install DB2 Express-C http://www-01.ibm.com/software/data/db2/express/
It is free (also for commercial use) version of DB2.

Also it is possible to install trial version of DB2 http://www-01.ibm.com/software/data/db2/linux-unix-windows/download.html.
Important : the license expires in 90 day. This version cannot be used for any commercial purpose.

After installing DB2 and creating DB2 instance create SAMPLE database (launch command db2sampl).

## Tomcat ##

Download and install Tomcat container.
http://tomcat.apache.org/

Copy to {CATALINA\_BASE}\lib directory JDBC jar files for DB2: db2jcc.jar and db2jcc\_license\_cu.jar.

Create data source in {CATALINA\_HOME}/conf/server.xml file - add entry like:



&lt;Resource auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" name="jdbc/SAMPLE" password="password" type="javax.sql.DataSource" url="jdbc:db2://{host}:{port}/SAMPLE" username="user"/&gt;



Replace {user}, {password}, {host} and {port} with proper values.

Add also resource mapping from local to global resource name.

File {CATALINA\_HOME}/conf/context.xml :



&lt;ResourceLink name="jdbc/SAMPLE" global="jdbc/SAMPLE" type="javax.sql.DataSource"/&gt;



## Spring JDBC ##

I found very useful Spring JDBC. Download: http://www.springsource.org/download. The whole stack is not needed - only subset, look below.

## GWT ##
Download the latest (2.2) version of GWT http://code.google.com/intl/pl-PL/webtoolkit/download.html
GWT is installed also together with GWT plugin for Eclipse.

## Eclipse ##

Create SampleGwt project from svn repository as GWT Web Application.

https://javahotel.googlecode.com/svn/trunk/examples/SampleGwt

Make sure that you have the following jars in WEB-INF/lib directory:

```
org.springframework.beans-3.0.5.RELEASE.jar
org.springframework.core-3.0.5.RELEASE.jar
org.springframework.jdbc-3.0.5.RELEASE.jar
org.springframework.transaction-3.0.5.RELEASE.jar
commons-logging-1.1.1.jar
gwt-servlet.jar
```
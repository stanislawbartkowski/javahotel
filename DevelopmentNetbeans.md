Package description, instruction how to create development environment in NetBeans

# Introduction #

NetBeans (unlike Eclipse) does not allow the same source directory to be used in different projects. So some addtional project should be created and being shared between main projects.




# Projects description #
Below is a short package description and relation matrix between packages and different software deployment units.

| **Project name** | **Project description** | **Deployable** | **Subproject** |
|:-----------------|:------------------------|:---------------|:---------------|
| HotelCommon | Contains code shared betwwen server and client |  | X |
| HotelEJB | Server part | X |  |
| HotelEJBLogin | Classes for login |  | X |
| HotelEJBTest | Contains unit tests for HotelEJB | X |  |
| HotelLoginHelper | Service locator |  | X |
| WebHotel | Client part (GWT) | X |  |
| WebHotelHelper | Bridge for RPC |  | X |



# Details #


Package list:

| **Package name** | **Package path** | **Description** | **HotelCommon** | **HotelEJB** | **HotelEJBLogin** | **HotelEJBTest** | **HotelLoginHelper** | **WebHotel** | **WebHotelHelper** |
|:-----------------|:-----------------|:----------------|:----------------|:-------------|:------------------|:-----------------|:---------------------|:-------------|:-------------------|
| gwtclient | com.javahotel.client (view,server) | Client (gwt) sources |  |  |  |  |  |X |  |
| webhelper | com.javahotel.webhelper	| Helper for GWT RPC, bridge between RPC and server part |  |  |  |  |  |  |X |
|hotellogintypes|com.javahotel.security.login|Classes and interafces for loging in|  |  |X |  |  |  |  |
|hoteltypes|com.javahotel.types|GWT module, interfaces and classes shared by client and server|X |  |  |  |  |  |  |
|hotelinterface|com.javahotel.remoteinterfaces|Onterfaces (facade) for db (DAO) beans|X |  |  |  |  |  |  |
|hoteldbres|com.javahotel.dbres|Some common util and resources for db (DAO) beans|X |  |  |  |  |  |  |
|hotelcommon|com.javahotel.common|GWT module, contains TO (Transient Objects) and some common definitions for server and client part|X |  |  |  |  |  |  |
|hoteldbutil|com.javahotel.dbutil|Some common util for db (DAO) beans|X |  |  |  |  |  |  |
|hoteldbjpa|com.javahotel.dbjpa|Some common classes and packages for JPA handling, also facade for JPA interface|  |X |  |  |  |  |  |
|hoteltomcatlogin|com.javahotel.security.tomcat.login|Implementation of admin log-in, user and password taken from xml file|  |X |  |  |  |  |  |
|hotelglassfishlogin|com.javahotel.security.glassfish.login|Implementation of admin login using glassfish JAAS|  |X |  |  |  |  |  |
|hoteljpa|com.javahotel.db|"Business rules", main DAO (server part) implementation|  |X |  |  |  |  |  |
|resources/gwtwar|  |Some files to copy to eclipse gae war project|  |  |  |  |  |  |  |
|resources/gwtwartest|  |Some files to copy to eclipse gaetest war project|  |  |  |  |  |  |  |
|daotest|com.javahotel.test com.javahotel.statictest|Junit test for dao facade|  |  |  |X |  |  |  |
|hotelejb3jpa|com.javahotel.db|EJB3/JPA part of JPA|  |X |  |  |  |  |  |
|hotelejb3cache|com.javahotel.commoncache|Simple Java(static)  implementation of cache|X |  |  |  |  |  |  |
|hotelejblogin|com.javahotel.loginhelper|Remote implementation for accesing beans|  |  |  |  |X |  |  |
|hoteldbdef|com.javahotel.commoncache|Some common definition for db|X |  |  |  |  |  |  |



# Prerequisites #

JavaBeans and install **gwt4nb** plugin.

GWT application also uses two additional packages:

http://code.google.com/p/google-gin/

and

Google Chart Tools (aka Visualization) Library 1.1
http://code.google.com/intl/pl-PL/webtoolkit/googleapilibraries.html


# Projects #

For all project create links to source as described in table above.

## HotelCommon ##

"Java class library" project.

Type: Subproject

Libraries: Java EE 6 Api Library

Important: it is necessary to keep in jar java sources only. Sources are shared between server and client (GWT) application.
Properties - Build - Packaging - Exclude from jar file (remove **.java filter)**

## HotelEJB ##
"JavaEE EJB Module" project

Type: deployable

Subprojects: HotelCommon,  HotelEJBLogin

Important: directory to compiled class should be linked because of well known problem with classloader - does not read interfaces from WEB-INF/lib libraries.

Additional: should contain in 'classpath' security.jar (jar taken from glassfish), as it uses specific glassfish security login option.

Warning: hotelglassfishlogin source path should be included only if deploying to glassfish server is planned

## HotelEJBLogin ##

"Java class library" project

Type: subproject

Subproject: HotelCommon

## HotelEJBTest ##
"Java class library" project

Type: runnable

Subproject: HotelCommon, HotelEJBLogin, HotelLoginHelper (package column checked)

Additonal: JUnit4

Important: source file add as test package.

Also you have to add {home glassfish}/glassfish/modules/gf-client.jar jar to the library jar path.

## HotelLoginHelper ##

"Java class library" project

Type: subproject

Subproject: HotelCommon

## HotelWeb ##

"Java Web" - "WebApplication"

Add framework: "Google Web Toolkit"

Type: deployable

Subprojects: HotelCommon, HotelLoginHelper, WebHotelHelper (package column checked)

Add two jars from javahotel/resource/gwtwar/modules (package column unchecked)

Add also Junit3 (package column unchecked)

Add also as subprojects: gwt-gin and gwt-visualization. Package column unchecked, they are used only during compilation.

## WebHotelHelper ##

"Java class library" project

Type: subproject

Subprojects: HotelLoginHelper, HotelCommon

Warning: left source in destination jar (look HotelCommon project)

# Set up Glassfish application server #

## Admin and users for EJB unit tests ##

If EJB unit tests are not run this step is not necessary

Configuration-Security-Realms:

Two realms should be created: **TestHotelAdmin** and **TestHotelUsers**

**TestHotelAdmin** could be defined "FileRealm" (or any other) and contain one user **admin** belonging to group **admin**

**TestHotelUsers** should be defined as JDBCRealms and have JNDI name:  jdbc/hotelsecuritytest

Field mapping should be defined as below.

## Admin and users ##

Two realms should be created: **HotelAdmin** and **HotelUsers**

**HotelAdmin** could be defined "FileRealm" (or any other) and should contain (mandatory) user: **admindb** (with password **passworddb**) belonging to the group **admin**. This user is used at the beginning to set up database and is not allowed to do anything else.
Also at least one additional user should be added (belonging to the group **admin**). This user can login to the system as 'Administrator'

**HotelUsers** should be define as JDBCRealsm and have JNDI name: jdbc/securityhotel

Following field mapping should be created:

  * User Table: PERSON

  * User Name Column: NAME

  * Password Column: PASSWORD

  * Group Table: GROUPD

  * Group Name Column: GROUPNAME

  * Digest Algorithm: none  (will be changed later)


## Database ##

### Database for EJB unit tests ###

If EJB unit tests are not run this step is not necessary

Resources - JDBC - JDBC Resources

Two JDBC datasource should be created:

  * jdbc/hotelsecuritytest : contains users and password
  * jdbc/hoteldbtest : database

### Database ###

Resources - JDBC - JDBC Resources

Two JDBC datasource should be created:

  * jdbc/securityhotel : contains users and password
  * jdbc/datahoteldb2 : database (if DB2 is used)
  * jdbc/datadbtest : database (if derby is used)

Warning: it is not good idea to utilize existing derbypools. While creating new derby database, data pool and data resource remember to remove 'SecurityMechanism" from 'AdvancedProperty'.

## Derby database - run as client ##

Resources - JDBC - Connection Pool

Data source class name : org.apache.derby.jdbc.ClientDataSource

# Setup Tomcat #

## Prerequisites ##

Download and install OpenEJB : http://openejb.apache.org/

Make sure  that at least openjpa-1.1.0.jar is installed (directory webapps/openejb/lib)

Copy derbyclient.jar to {CATALINAHOME}/lib/directory.

## HotelEJBTomcat ##

You cannot deploy HotelEJB project directly to Tomcat app server. Tomcat is not official "J2EE" and NetBeanse does not allow to deploy "EJB" project there.
But there is a workaround:
  * Create "Java - Web - Web application" HotelEJBTomcat project.
  * Add following subprojects (package column checked) : HotelEJB, HotelCommon, HotelEJBLogin)
  * This project can be deployed to Tomcat without any problem.
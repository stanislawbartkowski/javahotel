# Prerequisities

* Eclipse, tested with the latest Eclipse Neon
* Ivy plugin : http://www.apache.org/dist/ant/ivyde/updatesite
* Google plugin : https://dl.google.com/eclipse/plugin/4.6
* Configure Google plugin to use the latest GWT 2.8
* Download and install Tomcat servlet container (tested with 8.33 version)

# Create Eclipse project
9. Import -> Git -> Projects from Git
9. Clone URI: https://github.com/stanislawbartkowski/javahotel.git , master branch only, unselect others
9. Local destination -> Browse to workspace directory/javahotel
9. Import using the New Project Wizard -> Project name: javahotel (javahotel is a placeholder for Git clone)
9. The following project should appear

![](https://github.com/stanislawbartkowski/javahotel/blob/master/wiki/Zrzut%20ekranu%20z%202016-11-22%2022:36:46.png)

# Import Eclipse projects
Import two projects from javahotel/hotel/eprojects directory, eesample and jpatestsample

![](https://github.com/stanislawbartkowski/javahotel/blob/master/wiki/Zrzut%20ekranu%20z%202016-11-23%2000:36:21.png)

After importing eesample project will signal errors. To resolve them :

9. Fix problem of missing gwt-servlet.jar in WEB-INF/lib
9. Resolve again both ivy reference dependencies libraries
9. Launch GWT Compile for eesample project

# Launch eesample project as web application 
9. Create new Tomcat server as Eclipse project.
9. Define data source (tested with Postgresql and Derby)

In Tomcat server.xml GlobalNamingResources part specify following variables

| Name    | Value
| --------|:---------
| sample/javax.persistence.jdbc.driver | Driver class
| sample/javax.persistence.jdbc.url | Database URL
| sample/javax.persistence.jdbc.user | User name
| sample/javax.persistence.jdbc.password | Password

Derby example

```xml

<GlobalNamingResources>

		<Environment description="Sample database" name="sample/javax.persistence.jdbc.driver"
			type="java.lang.String" value="org.apache.derby.jdbc.EmbeddedDriver" />
			
		<Environment description="Sample database" name="sample/javax.persistence.jdbc.url"
			type="java.lang.String" value="jdbc:derby:/tmp/database/sampleDb;create=true" />
			
		<Environment description="Sample database" name="sample/javax.persistence.jdbc.user"
			type="java.lang.String" value="test" />
			
		<Environment description="Sample database" name="sample/javax.persistence.jdbc.password"
			type="java.lang.String" value="test" />

</GlobalNamingResources>
```

9. Start server
9. Launch http://localhost:8080/eesample/ or http://localhost:8080/eesample/?start=startp.xml
9. Enjoy



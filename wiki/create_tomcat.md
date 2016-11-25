# Prerequisties
* Tomcat installation (tested with Tomcat 8.0.33)
* ant with ivy plugin (https://ant.apache.org/ivy/download.cgi)
* GWT 2.8 installation (http://www.gwtproject.org/download.html)
* Java 8

# Make target
* set GWT_HOME environment variable to point GWT installation, for instance export GWT_HOME=/opt/gwt-2.8.0
* git clone https://github.com/stanislawbartkowski/javahotel.git
* cd javahotel/hotel
* ln -s eprojects/eesample/build.xml
* ant (ignore warnings and errors during GWT compilation)
* ls target/jythonuisample.war

# Define data source in Tomcat
[link](create_eclipse.md#launch-eesample-project-as-web-application)

# Deploy to Tomcat

* cp {dir}/javahotel/hotel/target/jythonuisample.war {CATALINA_BASE}/webapps
* start Tomcat

# Launch
http://localhost:8080/jythonuisample/ or http://localhost:8080/jythonuisample/?start=startp.xml

Enjoy



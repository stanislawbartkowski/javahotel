# Prerequisities

Create Heroku account https://www.heroku.com/platform

# Create war

Follow the instruction [link](create_tomcat.md). The same war is used for Tomcat and Heroku deployment

# Prepare Heroku application

* heroku create myjythonsample (you can use any other aplication name or leave heroku to produce default application name)
* heroku addons:create heroku-postgresql:hobby-dev --app myjythonsample (create an access to free potsgresql database)
* cp {dir)/javahotel/hotel/target/jythonuisample.war . (take a copy of jythonuisample.war)
* heroku plugins:install heroku-cli-deploy (if not done yet)
* heroku war:deploy jythonuisample.war  --app myjythonsample  (deploy)
* heroku open --app myjythonsample

More details: https://devcenter.heroku.com/articles/war-deployment

# Enjoy

https://myjythonsample.herokuapp.com/

https://myjythonsample.herokuapp.com/?start=startp.xml

# Running Heroku offline

It is also possible to run Heroku by means of webrunner applications. More details: https://devcenter.heroku.com/articles/java-webapp-runner.

* Prepare external Postgresql database and set JDBC_DATABASE_URL to proper URL connection string, should contain login name and password. More details: https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java
* java -jar {dir}/webapp-runner.jar {dir}/jythonuisample.war  (start embedded Tomcat)

# Enjoy
http://localhost:8080

http://localhost:8080?start=startp.xml

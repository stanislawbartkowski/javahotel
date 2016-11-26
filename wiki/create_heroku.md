# Prerequisities

Create Heroku account https://www.heroku.com/platform

# Create war

Follow the instruction [linke](create_tomcat.md). The same war is used for Tamcat and Heroku deployment

# Prepare Heroku application

* heroku create myjythonsample (you can use any other aplication name or leave heroku to produce default application name)
* heroku addons:create heroku-postgresql:hobby-dev --app myjythonsample 

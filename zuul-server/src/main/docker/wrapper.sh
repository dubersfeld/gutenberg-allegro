#!/bin/sh

while ! `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $CONFIGSERVER_HOST server to start on port $CONFIGSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo "Config Server $CONFIGSERVER_HOST up and running at $CONFIGSERVER_PORT"

echo "********************************************************"
echo "Starting Gutenberg Zuul Server"
echo "********************************************************"


java -Djava.security.egd=file:/dev/./urandom -Dspring.cloud.config.uri=$GUTENBERG_CONFIG_URI -Dspring.profiles.active=$PROFILE -jar /app.jar


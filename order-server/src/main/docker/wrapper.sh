#!/bin/sh

while ! `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $CONFIGSERVER_HOST server to start on port $CONFIGSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo "Config Server $CONFIGSERVER_HOST up and running at $CONFIGSERVER_PORT"

while ! `nc -z $ELASTICSERVER_HOST $ELASTICSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $ELASTICSERVER_HOST server to start on port $ELASTICSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo "ElasticSearch Server $ELASTICSERVER_HOST up and running at 9200"


java -Djava.security.egd=file:/dev/./urandom \
  -Dspring.cloud.config.uri=$GUTENBERG_CONFIG_URI \
  -Delasticsearch-host=$ELASTICSERVER_HOST \
  -Dspring.profiles.active=$PROFILE -jar /app.jar


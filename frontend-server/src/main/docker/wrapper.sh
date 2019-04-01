#!/bin/sh

while ! `nc -z $BOOKSERVER_HOST $BOOKSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the book server to start on port 8081"
    echo "********************************************************"
    sleep 10; 
done

echo "Book Server $BOOKSERVER_HOST up and running at $BOOKSERVER_PORT"


while ! `nc -z $REVIEWSERVER_HOST $REVIEWSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the review server to start on port 8082"
    echo "********************************************************"
    sleep 10; 
done

echo "Review Server $REVIEWSERVER_HOST up and running at $REVIEWSERVER_PORT"


while ! `nc -z $ORDERSERVER_HOST $ORDERSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the order server to start on port 8083"
    echo "********************************************************"
    sleep 10; 
done

echo "Order Server $ORDERSERVER_HOST up and running at $ORDERSERVER_PORT"


while ! `nc -z $USERSERVER_HOST $USERSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the user server to start on port 8084"
    echo "********************************************************"
    sleep 10; 
done

echo "User Server $USERSERVER_HOST up and running at $USERSERVER_PORT"



java -Djava.security.egd=file:/dev/./urandom \
  -Dspring.cloud.config.uri=$GUTENBERG_CONFIG_URI \
  -Dspring.profiles.active=$PROFILE -jar /app.jar


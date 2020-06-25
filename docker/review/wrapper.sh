#!/bin/sh

while ! `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $CONFIGSERVER_HOST server to start on port $CONFIGSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`
echo "Config Server $CONFIGSERVER_HOST up and running at $CONFIGSERVER_PORT"


./cnb/lifecycle/launcher



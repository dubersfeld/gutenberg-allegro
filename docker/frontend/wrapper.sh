#!/bin/sh

while ! `nc -z -v $GATEWAYSERVER_HOST $GATEWAYSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $GATEWAYSERVER_HOST server to start on port $GATEWAYSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z -v $GATEWAYSERVER_HOST $GATEWAYSERVER_PORT`
echo "Config Server $GATEWAYSERVER_HOST up and running at $GATEWAYSERVER_PORT"


./cnb/lifecycle/launcher



#!/bin/sh

while ! `nc -z -v $BOOKSERVER_HOST $BOOKSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $BOOKSERVER_HOST server to start on port $BOOKSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z -v $BOOKSERVER_HOST $BOOKSERVER_PORT`
echo "Book Server $BOOKSERVER_HOST up and running at $BOOKSERVER_PORT"



while ! `nc -z -v $REVIEWSERVER_HOST $REVIEWSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $REVIEWSERVER_HOST server to start on port $REVIEWSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z -v $REVIEWSERVER_HOST $REVIEWSERVER_PORT`
echo "Review Server $REVIEWSERVER_HOST up and running at $REVIEWSERVER_PORT"



while ! `nc -z -v $ORDERSERVER_HOST $ORDERSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $ORDERSERVER_HOST server to start on port $ORDERSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z -v $ORDERSERVER_HOST $ORDERSERVER_PORT`
echo "Order Server $ORDERSERVER_HOST up and running at $ORDERSERVER_PORT"


while ! `nc -z -v $USERSERVER_HOST $USERSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for $USERSERVER_HOST server to start on port $USERSERVER_PORT"
    echo "*********************************************************************"
    sleep 10; 
done

echo `nc -z -v $USERSERVER_HOST $USERSERVER_PORT`
echo "User Server $USERSERVER_HOST up and running at $USERSERVER_PORT"



./cnb/lifecycle/launcher



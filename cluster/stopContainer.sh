#!/bin/bash

CONTAINER=$1 

echo "Stopping container $CONTAINER"

docker rm -f $CONTAINER &

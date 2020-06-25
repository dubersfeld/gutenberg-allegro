#!/bin/bash

# clean context
containers=$(docker ps -a | grep elasticsearch | awk '{print $1}') 
docker rm -f $containers 



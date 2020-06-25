#!/bin/bash

# remove volumes
volumes=$(docker volume ls | grep data | awk '{print $2}')  
docker volume rm $volumes 



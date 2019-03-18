#!/bin/sh
echo "********************************************************"
echo "Starting Gutenberg Eureka Server"
echo "********************************************************"

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar

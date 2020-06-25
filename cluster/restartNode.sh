#!/bin/bash

NAME=$1
IP=$2
VOLUME=$3

echo "NAME $NAME"
echo "IP $IP"
echo "VOLUME $VOLUME"
echo "Restarting node"

docker run -e "cluster.name=gutenberg-cluster" -e "bootstrap.memory_lock=true" \
--name $NAME \
--hostname $NAME \
--ip $IP \
-e "discovery.seed_hosts=172.19.0.2,172.19.0.3,172.19.0.4,172.19.0.5,172.19.0.6" \
--ulimit memlock=-1:-1 \
--volume "$VOLUME:/usr/share/elasticsearch/data" \
--network gutenberg docker.elastic.co/elasticsearch/elasticsearch:7.7.1 &
disown

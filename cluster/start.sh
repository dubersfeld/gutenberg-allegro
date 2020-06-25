#!/bin/bash

NAME=elasticsearch1
IP=172.19.0.2
VOLUME=gutenberg-es-data1

echo "NAME $NAME"
echo "IP $IP"
echo "VOLUME $VOLUME"
echo "Restarting node"

docker run -e "cluster.name=gutenberg-cluster" -e "bootstrap.memory_lock=true" \
--name $NAME \
--ip $IP \
-e "node.name=elasticsearch1" \
-e "discovery.seed_hosts=172.19.0.2,172.19.0.3,172.19.0.4,172.19.0.5,172.19.0.6" \
-e "cluster.initial_master_nodes=172.19.0.2,172.19.0.3,172.19.0.4" \
--ulimit memlock=-1:-1 \
--volume "$VOLUME:/usr/share/elasticsearch/data" \
--network gutenberg docker.elastic.co/elasticsearch/elasticsearch:7.7.1 &
disown


NAME=elasticsearch2
IP=172.19.0.3
VOLUME=gutenberg-es-data2

echo "NAME $NAME"
echo "IP $IP"
echo "VOLUME $VOLUME"
echo "Restarting node"

docker run -e "cluster.name=gutenberg-cluster" -e "bootstrap.memory_lock=true" \
--name $NAME \
--ip $IP \
-e "discovery.seed_hosts=172.19.0.2,172.19.0.3,172.19.0.4,172.19.0.5,172.19.0.6" \
--ulimit memlock=-1:-1 \
--volume "$VOLUME:/usr/share/elasticsearch/data" \
--network gutenberg docker.elastic.co/elasticsearch/elasticsearch:7.7.1 &
disown

NAME=elasticsearch3
IP=172.19.0.4
VOLUME=gutenberg-es-data3

echo "NAME $NAME"
echo "IP $IP"
echo "VOLUME $VOLUME"
echo "Restarting node"

docker run -e "cluster.name=gutenberg-cluster" -e "bootstrap.memory_lock=true" \
--name $NAME \
--ip $IP \
-e "discovery.seed_hosts=172.19.0.2,172.19.0.3,172.19.0.4,172.19.0.5,172.19.0.6" \
--ulimit memlock=-1:-1 \
--volume "$VOLUME:/usr/share/elasticsearch/data" \
--network gutenberg docker.elastic.co/elasticsearch/elasticsearch:7.7.1 &
disown


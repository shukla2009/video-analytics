#!/bin/bash

docker run -d \
 --net=host \
 --name=conf-kafka \
 -e KAFKA_ZOOKEEPER_CONNECT=localhost:32181 \
 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:29092 \
 confluentinc/cp-kafka:3.2.0


docker exec conf-kafka kafka-topics \
 --create \
 --topic log \
 --partitions 1 \
 --replication-factor 1 \
 --if-not-exists \
 --zookeeper localhost:32181

docker exec conf-kafka kafka-topics \
 --describe --topic log \
 --zookeeper localhost:32181

 docker run --name cassandra -p 9042:9042-d cassandra
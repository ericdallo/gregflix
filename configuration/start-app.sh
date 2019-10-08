#!/bin/bash

docker rmi -f ericdallo/gregflix
docker pull ericdallo/gregflix
docker rm -f gregflix
docker run -d --name gregflix --link datomic:datomic -p 8080:8080 -e DATOMIC_DB_PASSWORD=$DATOMIC_DB_PASSWORD -e DATOMIC_DB_HOST=$DATOMIC_DB_HOST ericdallo/gregflix

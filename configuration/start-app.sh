#!/bin/bash

docker rmi -f ericdallo/gregflix
docker pull ericdallo/gregflix
docker rm -f gregflix
docker run -d --name gregflix --link datomic:datomic -p 8080:8080 --env DATOMIC_DB_PASSWORD=$DATOMIC_DB_PASSWORD ericdallo/gregflix

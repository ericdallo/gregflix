#!/bin/bash

docker rmi -f docker.pkg.github.com/ericdallo/gregflix/gregflix
docker pull docker.pkg.github.com/ericdallo/gregflix/gregflix
docker rm -f gregflix
docker run -d --name gregflix --link datomic:datomic -p 8080:8080 -e DATOMIC_DB_PASSWORD=$DATOMIC_DB_PASSWORD -e DATOMIC_DB_HOST=$DATOMIC_DB_HOST docker.pkg.github.com/ericdallo/gregflix/gregflix

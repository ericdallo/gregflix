#!/bin/bash

docker rmi -f ericdallo/gregflix
docker pull ericdallo/gregflix
docker rm -f gregflix
docker run -d --name gregflix --link mysql:mysql -p 8080:8080  ericdallo/gregflix

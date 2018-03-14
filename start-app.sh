#!/bin/bash
docker rm -f gregflix
docker rmi -f ericdallo/gregflix
docker run -d --name gregflix --link mysql:mysql -p 80:8080  ericdallo/gregflix

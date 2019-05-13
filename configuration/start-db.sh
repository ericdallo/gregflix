#!/bin/bash

docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=$DATABASE_PASSWORD -d mysql/mysql-server:5.6
sleep 120
docker exec -it mysql mysql -u root -p$DATABASE_PASSWORD -e 'create database gregflix;'
docker exec -it mysql mysql -u root -p$DATABASE_PASSWORD -e "grant all privileges on *.* to 'root'@'%' identified by '$DATABASE_PASSWORD' with grant option;"

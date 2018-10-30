#!/bin/bash

docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123mudar -d mysql/mysql-server:5.6
sleep 60
docker exec -it mysql mysql -u root -p123mudar -e 'create database gregflix;'
docker exec -it mysql mysql -u root -p123mudar -e "grant all privileges on *.* to 'root'@'%' identified by '123mudar' with grant option;"

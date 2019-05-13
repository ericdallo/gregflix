#!/bin/bash

docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=P9xTYHvqyDkEwfEEKKsNcvU4GvyEKPMgXqwm4qkuPB38UE4V9RfRsq -d mysql/mysql-server:5.6
sleep 60
docker exec -it mysql mysql -u root -pP9xTYHvqyDkEwfEEKKsNcvU4GvyEKPMgXqwm4qkuPB38UE4V9RfRsq -e 'create database gregflix;'
docker exec -it mysql mysql -u root -pP9xTYHvqyDkEwfEEKKsNcvU4GvyEKPMgXqwm4qkuPB38UE4V9RfRsq -e "grant all privileges on *.* to 'root'@'%' identified by 'P9xTYHvqyDkEwfEEKKsNcvU4GvyEKPMgXqwm4qkuPB38UE4V9RfRsq' with grant option;"

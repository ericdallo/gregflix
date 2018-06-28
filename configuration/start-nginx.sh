#!/bin/bash

set -ve

APP=gregflix
NGINX=nginx

docker pull $NGINX
if docker ps | awk -v app="NGINX" 'NR>1{  ($(NF) == NGINX )  }'; then
    docker stop "$NGINX" && docker rm -f "$NGINX"
fi

docker run --name $NGINX -d \
    -v /opt/nginx.conf:/etc/nginx/nginx.conf \
    -v /opt/gregflix.site.crt:/etc/nginx/ssl/gregflix.site.crt \
    -v /opt/gregflix.site.key:/etc/nginx/ssl/gregflix.site.key \
    --link $APP:$APP -p 80:80 -p 443:443 $NGINX
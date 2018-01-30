FROM naartjie/alpine-lein

RUN mkdir -p /app

WORKDIR /app

COPY . /app

ENTRYPOINT /app/docker/startup.sh

EXPOSE 8080

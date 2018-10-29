FROM ericdallo/lein-sass

COPY . /app

WORKDIR /app

RUN mkdir -p /app && \
    cd /app && \
    lein uberjar

ENTRYPOINT /app/docker/startup.sh

EXPOSE 8080

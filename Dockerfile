FROM ericdallo/lein-sass

COPY . /app

WORKDIR /app

RUN mkdir -p /app && \
    cd /app && \
    lein sass once && \
    lein uberjar

ENTRYPOINT /app/docker/startup.sh

EXPOSE 8080

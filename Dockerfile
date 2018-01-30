FROM java:8-alpine
RUN mkdir -p /app /app/resources
WORKDIR /app
COPY target/gregflix-*-standalone.jar .
COPY resources/public resources/public
CMD java -jar gregflix-*-standalone.jar
EXPOSE 8080

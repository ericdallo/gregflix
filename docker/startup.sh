#!/bin/bash
set -ve

lein uberjar
exec java -jar target/gregflix-*-standalone.jar

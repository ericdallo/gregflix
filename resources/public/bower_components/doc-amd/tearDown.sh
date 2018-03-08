#!/bin/bash

ps aux | grep "[n]ode ./node_modules/http-server/bin/http-server -p 8888" | awk '{print $2}' | xargs kill -9;

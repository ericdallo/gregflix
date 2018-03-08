#!/bin/bash

ps aux | grep "[n]ode server.js" | awk '{print $2}' | xargs kill -9;
echo "server stopped"

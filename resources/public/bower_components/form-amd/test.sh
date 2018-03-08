#!/bin/bash

./node_modules/mocha-phantomjs/bin/mocha-phantomjs -R spec -p ./node_modules/.bin/phantomjs "http://localhost:8888/test/formTest.js.html";

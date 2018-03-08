#!/bin/bash
./setup.sh
./node_modules/mocha-phantomjs/bin/mocha-phantomjs -p ./node_modules/.bin/phantomjs -R spec "http://localhost:8888/test/docTest.js.html";
./tearDown.sh

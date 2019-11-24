#!/bin/bash
set -ve

docker run --name datomic -p 4334-4336:4334-4336 -e ALT_HOST=gregflix.site -e ADMIN_PASSWORD=$DATOMIC_ADMIN_PASSWORD -e DATOMIC_PASSWORD=$DATOMIC_DB_PASSWORD -d akiel/datomic-free

#!/bin/bash
echo "🟢 Initializing MongoDB with JSON..."

mongoimport --username "$MONGO_INITDB_ROOT_USERNAME" \
            --password "$MONGO_INITDB_ROOT_PASSWORD" \
            --authenticationDatabase "admin" \
            --db notesdb \
            --collection notes \
            --drop \
            --file /docker-entrypoint-initdb.d/notesdb.notes.json \
            --jsonArray

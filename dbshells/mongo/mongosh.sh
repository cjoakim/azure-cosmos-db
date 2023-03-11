#!/bin/bash

# Bash script to open a mongo CLI pointing to either localhost, CosmosDB, or Atlas.
# Note: db.runCommand({getLastRequestStatistics: 1})
#
# Chris Joakim, Microsoft, 2022/06/18

if [ "$1" == 'local' ]
then 
    echo 'connecting to '$MONGODB_LOCAL_URL
    mongosh $MONGODB_LOCAL_URL
fi

if [ "$1" == 'azure' ]
then 
    echo 'connecting to: '$AZURE_COSMOSDB_MONGODB_CONN_STRING
    mongosh $AZURE_COSMOSDB_MONGODB_CONN_STRING --ssl
fi

if [ "$1" == 'atlas' ]
then 
    echo 'connecting to: '$AZURE_ATLAS_CONN_STR
    mongosh $AZURE_ATLAS_CONN_STR --ssl
fi

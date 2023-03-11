#!/bin/bash

# Script to execute several Cosmos DB REST operations.
# Chris Joakim, Microsoft

echo 'list_offers'
node rest-client.js list_offers > tmp/list_offers.txt 

echo 'get_offer'
node rest-client.js get_offer 7FMe > tmp/get_offer.txt 

echo 'list_databases'
node rest-client.js list_databases > tmp/list_databases.txt 

echo 'get_database'
node rest-client.js get_database dev telemetry > tmp/get_database.txt

echo 'list_containers'
node rest-client.js list_containers dev > tmp/list_containers.txt

echo 'get_container'
node rest-client.js get_container dev telemetry > tmp/get_container.txt

echo 'pk_ranges'
node rest-client.js pk_ranges dev telemetry > tmp/pk_ranges.txt

echo 'done'

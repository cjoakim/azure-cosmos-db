"""
Usage:
  python main.py <func>
  python main.py env         <-- displays necessary environment variables
  python main.py test_suite  <-- executes a suite of CosmosDB Mongo API operations
  python main.py create_customer_activity_stream <sleep_secs> <doc_count>
  python main.py create_customer_activity_stream 0.5 100
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

# Chris Joakim, Microsoft

import base64
import json
import sys
import time
import os

from datetime import datetime
from docopt import docopt
from faker import Faker
from faker_vehicle import VehicleProvider

from pysrc.env import Env
from pysrc.fs import FS
from pysrc.mongo import Mongo


def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version=__version__)
    print(arguments)

def check_env():
    print('AZURE_COSMOSDB_MONGODB_HOST: {}'.format(Env.var('AZURE_COSMOSDB_MONGODB_HOST')))
    print('AZURE_COSMOSDB_MONGODB_PORT: {}'.format(Env.var('AZURE_COSMOSDB_MONGODB_PORT')))
    print('AZURE_COSMOSDB_MONGODB_USER: {}'.format(Env.var('AZURE_COSMOSDB_MONGODB_USER')))
    print('AZURE_COSMOSDB_MONGODB_PASS: {}'.format(Env.var('AZURE_COSMOSDB_MONGODB_PASS')))
    print('AZURE_COSMOSDB_MONGODB_CONN_STRING: {}'.format(Env.var('AZURE_COSMOSDB_MONGODB_CONN_STRING')))

    opts = dict()
    opts['conn_string'] = Env.var('AZURE_COSMOSDB_MONGODB_CONN_STRING')
    opts['verbose'] = True
    m = Mongo(opts)
    print('databases found: {}'.format(m.list_databases()))
    #print('last request charge: '.format(m.last_request_request_charge()))

def test_suite():
    opts = dict()
    opts['conn_string'] = Env.var('AZURE_COSMOSDB_MONGODB_CONN_STRING')
    opts['verbose'] = True
    m = Mongo(opts)
    db = m.set_db('dev')
    coll = m.set_coll('movies')
    movies = FS.read_json('data/movies.json')
    keys = sorted(movies.keys())

    print('list_collections ...')
    print(m.list_collections())

    print('list_databases ...')
    print(m.list_databases())

    print('count_docs initial ...')
    print(m.count_docs({}))

    print('delete_many doctype movie ...')
    delete_result = m.delete_many({"doctype": 'movie'})
    print(delete_result)
    print('deleted doc count: {}'.format(delete_result.deleted_count))

    print('count_docs after deletes ...')
    print(m.count_docs({}))

    for idx, key in enumerate(keys):
        if idx < 40:
            data = dict()
            data['pk'] = key
            data['title_id'] = key
            data['title'] = movies[key]
            data['doctype'] = 'movie'
            if idx < 11:
                data['top10'] = True
            else:
                data['top10'] = False
            print(json.dumps(data))
            result = m.insert_doc(data)
            print('insert_doc; id: {} -> {}'.format(str(result.inserted_id), str(data)))
            time.sleep(0.05)

    print('count_docs after inserts ...')
    print(m.count_docs({}))

    print('find_one({"title": "Footloose"}) ...')
    print(m.find_one({"title": "Footloose"}))

    print('find_one({"title": "Not There"}) ...')
    print(m.find_one({"title": "Not There"}))

    print('find_by_id ...')
    print(m.find_by_id('5ea575f08bd3a96405ea6366'))

    print('update_many ...')
    um = m.update_many({"top10": True}, {'$set': {"rating": 100, "bacon": False}}, False)
    print(um)

    print('update_one ...')
    fl2 = m.update_one({"title": 'Footloose'}, {'$set': {"rating": 100, "bacon": True}}, False) # update_one(filter, update, upsert)
    print(fl2)

    print('find_one({"title": "Footloose"}) ...')
    fl3 = m.find_one({"title": 'Footloose'})
    print(fl3)

    print('find({"top10": True})')
    cursor = m.find({"top10": True})
    for doc in cursor:
        print(doc)

    print('count_docs title Footloose ...')
    print(m.count_docs({"title": 'Footloose'}))

    print(m.delete_one({"title": 'The Money Pit'}))

    print('find_one({"title": "Silverado"}) ...')
    sv = m.find_one({"title": 'Silverado'})
    print(sv)

    print('delete_by_id Silverado ...')
    delete_result = m.delete_by_id(sv['_id'])
    print(delete_result)
    print('deleted doc count: {}'.format(delete_result.deleted_count))

    print('find_one({"title": "Silverado"}) ...')
    sv = m.find_one({"title": 'Silverado'})
    print(sv)

    print('count_docs final ...')
    print(m.count_docs({}))

def create_customer_activity_stream():
    sleep_secs = float(sys.argv[2])
    doc_count  = int(sys.argv[3])
    print('create_customer_activity_stream; sleep_secs: {}, doc_count: {}'.format(
        sleep_secs, doc_count))

    print('connecting to cosmosdb ...')
    opts = dict()
    opts['conn_string'] = Env.var('AZURE_COSMOSDB_MONGODB_CONN_STRING')
    opts['verbose'] = True
    m = Mongo(opts)
    db = m.set_db('dev')
    coll = m.set_coll('vehicle_activity')

    print(int(time.time()))
    Faker.seed(int(time.time()))
    f = Faker()
    f.add_provider(VehicleProvider)

    for n in range(0, doc_count):
        time.sleep(sleep_secs)
        print('---')
        doc = dict()
        tid = f.iban()
        doc['pk'] = tid
        doc['utc_time'] = str(datetime.utcnow())  #time.time()
        doc['transponder'] = tid
        doc['location'] = f.local_latlng()
        doc['vehicle'] = f.vehicle_object()
        doc['plate'] = f.license_plate()
        print(json.dumps(doc, sort_keys=False, indent=2))
        result = m.insert_doc(doc)
        print('insert_doc; id: {} -> {}'.format(str(result.inserted_id), str(doc)))

if __name__ == "__main__":
    if len(sys.argv) > 1:
        cli_func = sys.argv[1].lower()
        if cli_func == 'env':
            check_env()
        elif cli_func == 'test_suite':
            test_suite()
        elif cli_func == 'create_customer_activity_stream':
            create_customer_activity_stream()
        else:
            print_options('Error: invalid command-line function: {}'.format(cli_func))
    else:
        print_options('Error: no command-line args provided') 

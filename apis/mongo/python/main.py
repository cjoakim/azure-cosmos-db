"""
Usage:
  python main.py <func>
  python main.py env         <-- displays necessary environment variables
  python main.py cosmos_sql  <-- executes a suite of CosmosDB SQL API operations
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

from docopt import docopt

from pysrc.env import Env
from pysrc.fs import FS
from pysrc.mongo import Mongo


def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version=__version__)
    print(arguments)

def check_env():
    print('AZURE_COSMOSDB_SQL_URI:     {}'.format(Env.var('AZURE_COSMOSDB_SQL_URI')))
    print('AZURE_COSMOSDB_SQL_RW_KEY1: {}'.format(Env.var('AZURE_COSMOSDB_SQL_RW_KEY1')))
    print('AZURE_COSMOSDB_SQL_DB:      {}'.format(Env.var('AZURE_COSMOSDB_SQL_DB')))

def test_suite():
    opts = dict()
    opts['host'] = 'localhost'
    opts['port'] = 27017
    m = Mongo(opts)
    db = m.set_db('dev')
    coll = m.set_coll('movies')
    movies = FS.read_json('data/movies.json')
    keys = sorted(movies.keys())
    for idx, key in enumerate(keys):
        if idx < 100:
            data = dict()
            data['title_id'] = key
            data['title'] = movies[key]
            data['doctype'] = 'movie'
            if idx < 12:
                data['top10'] = True
            else:
                data['top10'] = False
            result = m.insert_doc(data)
            #print('{} -> {}'.format(str(result.inserted_id), str(data)))
            print(data)
    print(m.list_collections())
    print(m.list_databases())
    print(m.find_one({"title": 'Footloose'}))
    print(m.find_one({"title": 'Not There'}))
    print(m.find_by_id('5ea575f08bd3a96405ea6366'))

    um = m.update_many({"top10": True}, {'$set': {"rating": 100, "bacon": False}}, False)
    print(um)
    fl2 = m.update_one({"title": 'Footloose'}, {'$set': {"rating": 100, "bacon": True}}, False) # update_one(filter, update, upsert)
    print(fl2)
    fl3 = m.find_one({"title": 'Footloose'})
    print(fl3)
    cursor = m.find({"top10": True})
    for doc in cursor:
        print(doc)

    print(m.count_docs({}))
    print(m.count_docs({"title": 'Footloose'}))
    print(m.delete_by_id('5ea575f08bd3a96405ea6366'))
    print(m.count_docs({}))
    print(m.delete_one({"title": 'The Money Pit'}))
    print(m.count_docs({}))
    print(m.delete_many({"doctype": 'movie'}))
    print(m.count_docs({}))

if __name__ == "__main__":
    if len(sys.argv) > 1:
        cli_func = sys.argv[1].lower()
        if cli_func == 'env':
            check_env()
        elif cli_func == 'test_suite':
            test_suite()
        else:
            print_options('Error: invalid command-line function: {}'.format(cli_func))
    else:
        print_options('Error: no command-line args provided') 

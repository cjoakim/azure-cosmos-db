import json
import os

from pymongo import MongoClient
from bson.objectid import ObjectId

# This class is used to access a MongoDB database, including the CosmosDB Mongo API.
# Chris Joakim, Microsoft, 2023/03/12

# pip install pymongo
# https://pymongo.readthedocs.io/en/stable/
# https://pymongo.readthedocs.io/en/stable/tutorial.html
# https://docs.mongodb.com/v3.6/introduction/
# https://api.mongodb.com/python/current/api/pymongo/collection.html


class Mongo(object):

    def __init__(self, opts):
        self._opts = opts
        self._db = None
        self._coll = None
        if 'conn_string' in self._opts.keys():
            self._client = MongoClient(opts['conn_string'])
        else:
            self._client = MongoClient(opts['host'], opts['port'])

        if self.is_verbose():
            print(json.dumps(self._opts, sort_keys=False, indent=2))
 
    def is_verbose(self):
        if 'verbose' in self._opts.keys():
            return self._opts['verbose']
        return False

    def list_databases(self):
        return self._client.list_database_names()

    def list_collections(self):
        return self._db.list_collection_names()

    def set_db(self, dbname):
        self._db = self._client[dbname]
        print(self._db)
        return self._db 

    def set_coll(self, collname):
        self._coll = self._db[collname]
        return self._coll 

    def insert_doc(self, doc):
        return self._coll.insert_one(doc)

    def find_one(self, query_spec):
        return self._coll.find_one(query_spec)

    def find(self, query_spec):
        return self._coll.find(query_spec)

    def find_by_id(self, id):
        return self._coll.find_one({'_id': ObjectId(id)})

    def delete_by_id(self, id):
        return self._coll.delete_one({'_id': ObjectId(id)})

    def delete_one(self, query_spec):
        return self._coll.delete_one(query_spec)

    def delete_many(self, query_spec):
        return self._coll.delete_many(query_spec)

    def update_one(self, filter, update, upsert):
        # 'update only works with $ operators'
        return self._coll.update_one(filter, update, upsert)

    def update_many(self, filter, update, upsert):
        # 'update only works with $ operators'
        return self._coll.update_many(filter, update, upsert)

    def count_docs(self, query_spec):
        return self._coll.count_documents(query_spec)

    def last_request_stats(self):
        return self._db.command({'getLastRequestStatistics': 1})

    def last_request_request_charge(self):
        stats = self.last_request_stats()
        if stats == None:
            return -1
        else:
            return stats['RequestCharge']

    def client(self):
        return self._client

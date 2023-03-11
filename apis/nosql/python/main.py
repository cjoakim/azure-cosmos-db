"""
Usage:
  python main.py <func>
  python main.py env         <-- displays necessary environment variables
  python main.py test_suite  <-- executes a suite of CosmosDB SQL API operations
  python main.py create_json_files_for_emulator
  python main.py query_emulator
  python main.py load_sales sales sales sales1.json 99999
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
import uuid

from docopt import docopt

from pysrc.constants import Constants
from pysrc.cosmos import Cosmos
from pysrc.env import Env
from pysrc.fs import FS


def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version='1.0.0')
    print(arguments)

def check_env():
    names = list()
    names.append(Constants.AZURE_COSMOSDB_NOSQL_URI)
    names.append(Constants.AZURE_COSMOSDB_NOSQL_RW_KEY1)
    names.append(Constants.AZURE_COSMOSDB_NOSQL_DB)

    for name in names:
        value = Env.var(name)
        print('{} --> {}'.format(name, value))

def load_sales():
    # python main.py load_sales sales sales sales1.json 99999
    dbname, cname, basename, max_count = sys.argv[2], sys.argv[3], sys.argv[4], int(sys.argv[5])
    print('load_sales; dbname: {}, cname: {} basename: {}, max_count:'.format(
        dbname, cname, basename, max_count))

    repo_dir = Env.var('REPOS_ROOT_DIR')
    infile   = '{}/azure-cosmos-db-playground/datagen/sales/data/retail/{}'.format(repo_dir, basename)
    print('infile: {}'.format(infile))

    opts = dict()
    opts['url'] = Env.var(Constants.AZURE_COSMOSDB_NOSQL_URI)
    opts['key'] = Env.var(Constants.AZURE_COSMOSDB_NOSQL_RW_KEY1)
    c = Cosmos(opts)
    dbproxy = c.set_db(dbname)
    ctrproxy = c.set_container(cname)

    lines = FS.read_lines(infile)
    for line_idx, line in enumerate(lines):
        if line_idx < max_count:
            doc = json.loads(line.strip())
            doc['id'] = str(uuid.uuid4())
            doc['pk'] = str(doc['pk'])
            doc['seq'] = line_idx + 1
            jstr = json.dumps(doc, sort_keys=False, indent=2)
            print(jstr)
            if True:
                result = c.upsert_doc(doc)
                c.print_last_request_charge()

def test_suite():
    opts = dict()
    opts['url'] = Env.var(Constants.AZURE_COSMOSDB_NOSQL_URI)
    opts['key'] = Env.var(Constants.AZURE_COSMOSDB_NOSQL_RW_KEY1)
    dbname = Env.var(Constants.AZURE_COSMOSDB_NOSQL_DB)
    cname  = 'testing'  # cname is container name

    c = Cosmos(opts)    # see file pysrc/cosmos.py in this repo for my Cosmos wrapper class

    print('disable/enable metrics, print_record_diagnostics:')
    c.disable_query_metrics()
    c.enable_query_metrics()
    c.reset_record_diagnostics()
    c.print_record_diagnostics()
    c.print_last_request_charge()

    print('list_databases:')
    for db in c.list_databases():
        print('database: {}'.format(db['id']))   
    c.print_last_request_charge()

    print('set_db:')
    dbproxy = c.set_db(dbname)
    c.print_last_request_charge()

    print('list_containers:')
    for con in c.list_containers():
        print('container: {}'.format(con['id']))    
    c.print_last_request_charge()

    print('delete_container:')
    c.delete_container(cname)
    c.print_last_request_charge()

    print('create_container:')
    ctrproxy = c.create_container(cname, '/pk', 500)
    c.print_last_request_charge()

    print('create_container:')
    ctrproxy = c.create_container(cname, '/pk', 500)
    c.print_last_request_charge()

    print('set_container:')
    ctrproxy = c.set_container(cname)
    c.print_last_request_charge()
    
    print('update_container_throughput:')
    offer = c.update_container_throughput(cname, 600)
    c.print_last_request_charge()

    print('get_container_offer:')
    offer = c.get_container_offer(cname)
    c.print_last_request_charge()

    infile = 'data/postal_codes_nc.csv'
    objects = FS.read_csvfile_into_objects(infile, delim=',')
    documents = list()
    ctrproxy = c.set_container(cname)

    print('upsert_docs:')
    for idx, obj in enumerate(objects):
        del obj['id']
        if idx < 9999:
            obj['pk'] = obj['postal_cd']
            print(obj)
            result = c.upsert_doc(obj)
            documents.append(result)
            c.print_last_request_charge()

    for idx, doc in enumerate(documents):
        if idx < 3:
            result = c.delete_doc(doc, doc['pk'])
            print('delete result: {}'.format(result))
            c.print_last_request_charge()
        else:
            doc['updated'] = True
            result = c.upsert_doc(doc)
            print('update result: {}'.format(result))
            c.print_last_request_charge()

    sql = "select * from c where c.state_abbrv = 'NC'"
    print('query; sql: {}'.format(sql))
    items = c.query_container(cname, sql, True, 1000)
    c.print_last_request_charge()
    last_id, last_pk = None, None
    for item in items:
        last_id = item['id']
        last_pk = item['pk']
        print(json.dumps(item, sort_keys=False, indent=2))

    print('read_doc; id: {} pk: {}'.format(last_id, last_pk))
    doc = c.read_doc(cname, last_id, last_pk)
    print(doc)
    c.print_record_diagnostics()
    c.print_last_request_charge()

    print('record_diagnostics_headers_dict:')
    print(json.dumps(c.record_diagnostics_headers_dict(), sort_keys=True, indent=2))
    
    print('reset and print diagnostics')
    c.reset_record_diagnostics()
    c.print_record_diagnostics()

    # Delete documents that are the results of a query
    sql = "select * from c where c.pk in ('27013', '27016')"
    print('query; sql: {}'.format(sql))
    items = c.query_container(cname, sql, True, 1000)
    c.print_last_request_charge()
    last_id, last_pk = None, None
    for item in items:
        print('deleting document:')
        print(json.dumps(item, sort_keys=False, indent=2))
        c.delete_doc(item, item['pk'])

    # print('delete container: {}'.format(cname))
    # c.delete_container(cname)
    # c.print_last_request_charge()

def create_json_files_for_emulator():
    infile = 'data/postal_codes_nc.csv'
    objects = FS.read_csvfile_into_objects(infile, delim=',')
    documents = list()

    for idx, obj in enumerate(objects):
        del obj['id']
        if idx < 9999:
            pk = obj['postal_cd']
            obj['pk'] = pk
            outfile = 'tmp/zip_code_{}.json'.format(pk)
            FS.write_json(obj, outfile)

def query_emulator():
    opts = dict()
    opts['url'] = Constants.AZURE_COSMOSDB_NOSQL_EMULATOR_URI_VALUE
    opts['key'] = Constants.AZURE_COSMOSDB_NOSQL_EMULATOR_KEY_VALUE
    print(opts)
    dbname = 'dev'
    cname  = 'test'   # cname is container name
    c = Cosmos(opts)  # see file pysrc/cosmos.py in this repo for my Cosmos wrapper class

    dbproxy = c.set_db(dbname)
    ctrproxy = c.set_container(cname)

    sql = "select * from c where c.state_abbrv = 'NC' and c.pk = '28036'"
    print('query; sql: {}'.format(sql))
    items = c.query_container(cname, sql, True, 1000)
    c.print_last_request_charge()
    last_id, last_pk = None, None
    for item in items:
        print(json.dumps(item, sort_keys=False, indent=2))


if __name__ == "__main__":
    if len(sys.argv) > 1:
        cli_func = sys.argv[1].lower()
        if cli_func == 'env':
            check_env()
        elif cli_func == 'test_suite':
            test_suite()
        elif cli_func == 'create_json_files_for_emulator':
            create_json_files_for_emulator()
        elif cli_func == 'query_emulator':
            query_emulator()
        elif cli_func == 'load_sales':
            load_sales()
        else:
            print_options('Error: invalid command-line function: {}'.format(cli_func))
    else:
        print_options('Error: no command-line args provided') 

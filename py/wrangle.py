"""
Usage:
  python wrangle.py <func>
  python wrangle.py wrangle_hierarchical_zipcode_docs
  python wrangle.py load_hierarchical_zipcode_docs <db> <container> 
  python wrangle.py load_hierarchical_zipcode_docs Hierarchical locations
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

import sys
import uuid

from docopt import docopt

from pysrc.cosmosbundle import Bytes, Cosmos, Counter, Env, FS, Storage, System, Template

def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version='1.0.0')
    print(arguments)

def wrangle_hierarchical_zipcode_docs():
    infile  = '../data/common/postal/postal_codes_us_filtered.csv'
    outfile = '../data/common/postal/postal_codes_us_documents.json'
    docs = FS.read_csv_as_dicts(infile)

    for doc in docs:
        try:
            doc['id'] = str(uuid.uuid4())
            doc['doctype'] = 'zipcode'
            doc['state'] = doc['state_abbrv']
            doc['city']  = doc['city_name']
            doc['zipcode']  = doc['postal_cd']
            doc['latitude']  = float(doc['latitude'])
            doc['longitude']  = float(doc['longitude'])
            del doc['state_abbrv']
            del doc['city_name']
            del doc['postal_cd']
            print(doc)
        except:
            pass
    FS.write_json(docs, outfile)

def load_hierarchical_zipcode_docs(dbname, cname):
    print(f'load_hierarchical_zipcode_docs; dbname: {dbname} cname: {cname}')
    infile = '../data/common/postal/postal_codes_us_documents.json'
    opts = {}
    opts['url'] = Env.var('AZURE_COSMOSDB_NOSQL_URI')
    opts['key'] = Env.var('AZURE_COSMOSDB_NOSQL_RW_KEY1')
    print(opts)
    c = Cosmos(opts)
    c.disable_query_metrics()
    for db in c.list_databases():
        id = db['id']
        print(f'db: {id}')
    c.set_db(dbname)
    for container in c.list_containers():
        id = container['id']
        print(f'container: {id}')
    c.set_container(cname)

    if True:
        docs = FS.read_json(infile)
        for doc_idx, doc in enumerate(docs):
            if doc_idx < 2:
                print(f'---: {doc_idx}')
                doc['id'] = str(uuid.uuid4())
                print(doc)
                res = c.upsert_doc(doc)
                print(res)



if __name__ == "__main__":
    if len(sys.argv) < 2:
        print_options('Error: no command-line args')
    else:
        func = sys.argv[1].lower()
        if func == 'wrangle_hierarchical_zipcode_docs':
            wrangle_hierarchical_zipcode_docs()
        elif func == 'load_hierarchical_zipcode_docs':
            dbname, cname = sys.argv[2], sys.argv[3]
            load_hierarchical_zipcode_docs(dbname, cname)
        else:
            print_options('Error: invalid function: {}'.format(func))

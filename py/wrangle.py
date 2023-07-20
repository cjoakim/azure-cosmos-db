"""
Usage:
  python wrangle.py <func>
  python wrangle.py wrangle_hierarchical_zipcode_docs
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

import sys
import uuid

from docopt import docopt

from pysrc.minbundle import Bytes, Counter, Env, FS, Storage, System

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


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print_options('Error: no command-line args')
    else:
        func = sys.argv[1].lower()
        if func == 'wrangle_hierarchical_zipcode_docs':
            wrangle_hierarchical_zipcode_docs()
        else:
            print_options('Error: invalid function: {}'.format(func))

"""
Usage:
    python main.py create_baseball_base_mongoexport_files
    python main.py create_baseball_large_document_count_data 50000
    python main.py create_baseball_large_document_size_data 1000
    python main.py generate_mongoimport_scripts mongodb://127.0.0.1:27017 dev --nossl ps1
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

# Chris Joakim, Microsoft

import json
import os
import platform
import random
import sys
import uuid

import arrow 

from docopt import docopt

from faker import Faker

from pysrc.env import Env
from pysrc.fs import FS
from pysrc.system import System


def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version=__version__)
    print(arguments)

def create_baseball_documents():
    print('create_baseball_documents')
    csv_files = list_files('baseballdatabank-2022.2', '.csv')
    total_count = 0
    for csv_file_obj in csv_files:
        infile = csv_file_obj['full']
        if 'core/' in infile:
            print('---')
            print('csv_file: {}'.format(infile))
            docs = FS.read_csvfile_into_objects(infile)
            total_count = total_count + len(docs)
            print('count in file: {}'.format(len(docs)))
            print(json.dumps(docs[0], sort_keys=False, indent=2)) 

    print('total_count: {}'.format(total_count))  # total_count: 537,844

def list_files(directory, suffix):
    files = list()
    for dir_name, subdirs, base_names in os.walk(directory):
        for base_name in base_names:
            full_name = "{}/{}".format(dir_name, base_name)
            entry = dict()
            entry['base'] = base_name
            entry['dir'] = dir_name
            entry['full'] = full_name
            entry['abspath'] = os.path.abspath(full_name)
            if full_name.lower().endswith(suffix):
                files.append(entry)
    return files



#######################################################################

def create_baseball_base_mongoexport_files():
    faker = Faker()
    csv_files = read_baseball_csv_files_list()
    windows = is_windows()

    for csv_filename in csv_files:
        print('reading csv file: {}'.format(csv_filename))
        rows = FS.read_csv(csv_filename.strip())
        doctype = filename_to_doctype(csv_filename)
        outfile = "data/baseball_{}.json".format(doctype)

        with open(outfile, "a") as out:
            print('creating outfile: {}'.format(outfile))
            header_row = None 
            for row_idx, row in enumerate(rows):
                if row_idx == 0:
                    header_row = row
                    for field_idx, field_name in enumerate(header_row):
                        header_row[field_idx] = field_name.replace('.','_')
                else:
                    doc = new_document()
                    doc['idx'] = row_idx
                    doc = row_to_doc(doc, header_row, row, doctype)
                    doc['notes'] = faker.paragraphs(nb=5)
                    doc['source_data_attribution'] = 'https://www.seanlahman.com/baseball-archive/statistics/'
                    doc['_asz'] = len(json.dumps(doc))  # _asz = approximate size
                    doc['_generated_at'] = str(arrow.utcnow())
                    out.write(json.dumps(doc, separators=(',', ':')))  # whitespace removed w/separators
                    if windows:
                        out.write("\r")
                    else:
                        out.write(linesep())

def create_baseball_large_document_count_data(count):
    print('create_baseball_large_document_count_data: {}'.format(count))
    parks = load_csv(parks_filename(), 'parks')
    players = load_csv(people_filename(), 'players')
    print('{} parks loaded'.format(len(parks)))
    print('{} players loaded'.format(len(players)))
    faker = Faker()
    outfile = 'data/baseball_fantasy_teams_small.json'

    with open(outfile, "a") as out:
        for team_idx in range(count):
            if (team_idx % 10000) == 0:
                print('document index {} at {}'.format(team_idx, str(arrow.utcnow())))
            doc = new_document()
            doc['idx'] = team_idx
            doc['team'] = 'fantasy_team_{}'.format(team_idx)
            doc['team_id'] = str(uuid.uuid1())
            doc['park'] = random_object(parks)
            doc['players'] = dict()
            player_count = random.randint(30, 40)
            for player_idx in range(player_count):
                player = random_object(players)
                pid = str(uuid.uuid1())
                player['playerID'] = pid  # overlay the existing value
                player['notes'] = faker.paragraphs(nb=5)
                doc['players'][pid] = player
            doc['source_data_attribution'] = 'https://www.seanlahman.com/baseball-archive/statistics/'
            doc['_asz'] = len(json.dumps(doc))  # _asz = approximate size
            doc['_generated_at'] = str(arrow.utcnow())
            out.write(json.dumps(doc, separators=(',', ':')))  # whitespace removed w/separators
            out.write(linesep())

def create_baseball_large_document_size_data(count):
    print('create_baseball_large_document_size_data: {}'.format(count))
    parks = load_csv(parks_filename(), 'parks')
    players = load_csv(people_filename(), 'players')
    print('{} parks loaded'.format(len(parks)))
    print('{} players loaded'.format(len(players)))
    faker = Faker()
    outfile = 'data/baseball_fantasy_teams_jumbo.json'

    with open(outfile, "a") as out:
        for team_idx in range(count):
            if (team_idx % 100) == 0:
                print('document index {} at {}'.format(team_idx, arrow.utcnow()))
            doc = new_document()
            doc['idx'] = team_idx
            team_id = str(uuid.uuid1())
            doc['team'] = 'fantasy_team_{}'.format(team_idx)
            doc['team_id'] = team_id
            doc['park'] = random_object(parks)
            doc['players'] = list()

            # approx 1.1KB per player document
            # 1024 players ~ 1 MB
            # 2048 players ~ 2 MB

            player_count = random.randint(256, 4096)
            if team_idx == 0:
                player_count = 2400  # ensure at least one 2MB+ document

            #print('team_id {} will have {} players'.format(team_id, player_count)) 
            for player_idx in range(player_count):
                player = random_object(players)
                pid = str(uuid.uuid1())
                player['playerID'] = pid  # overlay the existing value
                player['notes'] = faker.paragraphs(nb=5)
                doc['players'].append(player)
            doc['source_data_attribution'] = 'https://www.seanlahman.com/baseball-archive/statistics/'
            doc['_asz'] = len(json.dumps(doc))  # _asz = approximate size
            doc['_generated_at'] = str(arrow.utcnow())
            out.write(json.dumps(doc, separators=(',', ':')))  # whitespace removed w/separators
            out.write(linesep())

def read_baseball_csv_files_list():
    return FS.read_lines('seanlahman-baseballdatabank-files-list.txt')


def filename_to_doctype(csv_filename):
    filename_tokens = csv_filename.split("/")
    context  = filename_tokens[-2]
    basename = filename_tokens[-1].split(".")[0]
    return "{}_{}".format(context, basename)

def filename_to_collection(mongoimport_file):
    filename_tokens = mongoimport_file.split("/")
    return filename_tokens[-1].split(".")[0]

def row_to_doc(doc, header_row, row, doctype):
    for attr_idx, attr_name in enumerate(header_row):
        doc[attr_name] = row[attr_idx]
    doc['doctype'] = doctype
    return doc

def new_document():
    doc = dict()
    oid = dict()  
    oid['$oid'] = str(ObjectId())
    doc['_id']  = oid
    return doc

def random_object(array):
    max = len(array)
    idx = random.randint(0, max - 1)
    return array[idx]

def parks_filename():
    return 'sourcedata/seanlahman/baseballdatabank-2022.2/baseballdatabank-2022.2/core/Parks.csv'

def people_filename():
    return 'sourcedata/seanlahman/baseballdatabank-2022.2/baseballdatabank-2022.2/core/People.csv'

def load_csv(csv_filename, doctype):
    objects = list()
    rows = FS.read_csv(csv_filename.strip())
    header_row = None 
    for row_idx, row in enumerate(rows):
        if row_idx == 0:
            header_row = row
        else:
            doc = dict()
            doc = row_to_doc(doc, header_row, row, doctype)
            objects.append(doc)
    return objects

def generate_mongoimport_scripts(uri, db, ssl_flag, script_type):
    mongoimport_files_list = FS.read_lines('mongoimport-files-list.txt')
    print('{} lines read from file mongoimport-files-list.txt')
    generated_at = str(arrow.utcnow())

    for mongoimport_file in mongoimport_files_list:
        coll = filename_to_collection(mongoimport_file)
        if script_type == 'ps1':
            script_file = 'mongoimport_scripts/mongoimport_{}.ps1'.format(coll)
            shebang = ''
            continuation_char = '`'
        else:
            script_file = 'mongoimport_scripts/mongoimport_{}.sh'.format(coll)
            shebang = '!/bin/bash'
            continuation_char = '\\'

        print('infile: {} -> collection: {} -> script: {}'.format(
            mongoimport_file.strip(), coll, script_file))

        t = Template.get_template('.', 'mongoimport.txt')
        values = dict()
        values['shebang'] = shebang
        values['continuation_char'] = continuation_char
        values['generated_at'] = generated_at
        values['uri']  = uri
        values['db']   = db
        values['coll'] = coll
        values['file'] = ('data/' + mongoimport_file).strip()
        values['nworkers'] = 1
        values['batch_size'] = 24
        values['mode'] = 'upsert'  # [insert|upsert|merge|delete]
        if use_ssl(ssl_flag):
            values['ssl'] = '--ssl'
        else:
            values['ssl'] = ''
        s = Template.render(t, values)

        FS.write(script_file, s)

def explore_storage():
    # this method is for the ad-hoc development of the Azure Storage functionality
    print('explore_storage')
    stor = Storage()

    acct_info = stor.account_info()
    print('---')
    print('acct_info: {}'.format(str(acct_info)))

    containers = stor.list_containers()
    print('---')
    for c in containers:
        print('container: {}'.format(str(c)))

    blobs = stor.list_container('synapse')
    print('---')
    for b in blobs:
        print('blob: {}'.format(str(b)))

    print('---')
    uploaded = stor.upload_blob('README.md', 'synapse', 'README.md')
    print('uploaded: {}'.format(str(uploaded)))

def use_ssl(flag):
    if flag == '--ssl':
        return True
    return False

def is_windows():
    plat = platform.system().lower()
    return 'win' in plat

def linesep():
    if is_windows():
        return "\n"  # 2022/10/03: not sure why os.linesep doesn't work on Windows
    else:
        return os.linesep


if __name__ == "__main__":

    #print(sys.argv)
    func = sys.argv[1].lower()

    if func == 'create_baseball_documents':
        create_baseball_documents()

    # The following are legacy functions   
    # elif func == 'create_baseball_base_mongoexport_files':
    #     create_baseball_base_mongoexport_files()

    # elif func == 'create_baseball_large_document_count_data':
    #     count = int(sys.argv[2])
    #     create_baseball_large_document_count_data(count)

    # elif func == 'create_baseball_large_document_size_data':
    #     count = int(sys.argv[2])
    #     create_baseball_large_document_size_data(count)

    # elif func == 'generate_mongoimport_scripts':
    #     uri = sys.argv[2]
    #     db  = sys.argv[3]
    #     ssl_flag = sys.argv[4]     # --ssl or --nossl
    #     script_type = sys.argv[5]  # ps1 or sh
    #     generate_mongoimport_scripts(uri, db, ssl_flag, script_type)

    # elif func == 'explore_storage':
    #     explore_storage()

    else:
        print_options('Error: invalid function: {}'.format(func))

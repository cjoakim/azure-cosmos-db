"""
Usage:
  python main.py <func>
  python main.py env
  python main.py system
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

import base64
import json
import sys
import time
import os

import arrow 

from docopt import docopt

from pysrc.bytes import Bytes
from pysrc.constants import Constants
from pysrc.cosmos import Cosmos
from pysrc.env import Env
from pysrc.fs import FS
from pysrc.mongo import Mongo
from pysrc.rcache import RCache
from pysrc.storage import Storage
from pysrc.system import System
from pysrc.template import Template


def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version='1.0.0')
    print(arguments)

def check_env():
    for name in Constants.AZURE_CONSTANTS:
        value = Env.var(name)
        print('{} --> {}'.format(name, value))

if __name__ == "__main__":
    func = sys.argv[1].lower()
    if func == 'env':
        check_env()
    elif func == 'system':
        System.display_info()
    else:
        print_options('Error: invalid function: {}'.format(func))

import json
import os
import platform
import time

import arrow
import psutil
import socket
import sys
import time

from pysrc.bytes import Bytes
from pysrc.env import Env

# This class is an interface to system information such as memory usage.
#
# See https://docs.python.org/3/library/os.html
# See https://pypi.org/project/psutil/
# See https://psutil.readthedocs.io/en/latest/
# See https://docs.python.org/3/library/socket.html#socket.gethostname
#
# Chris Joakim, Microsoft

class System(object):

    @classmethod
    def command_line_args(cls):
        return sys.argv

    @classmethod
    def pid(cls):
        return os.getpid()

    @classmethod
    def process_name(cls):
        p = psutil.Process()
        return p.name()

    @classmethod
    def user(cls):
        return os.getlogin()

    @classmethod
    def hostname(cls):
        try:
            return socket.gethostname()
        except:
            return Constants.UNKNOWN

    @classmethod
    def cwd(cls):
        p = psutil.Process()
        return p.cwd()

    @classmethod
    def platform_info(cls):
        return('{} : {}'.format(platform.platform(), platform.processor()))

    @classmethod
    def cpu_count(cls):
        return psutil.cpu_count(logical=False)

    @classmethod
    def memory_info(cls):
        p = psutil.Process()
        return p.memory_info()

    @classmethod
    def virtual_memory(cls):
        return psutil.virtual_memory()

    @classmethod
    def epoch(cls):
        return time.time()

    @classmethod
    def utc_time(cls):
        utc = arrow.utcnow()
        return utc.format()

    @classmethod
    def sleep(cls, seconds=1.0):
        time.sleep(seconds)

    @classmethod
    def export_dir_disk_usage(cls):
        path = Env.exports_dir()
        return psutil.disk_usage(path)

    @classmethod
    def display_info(cls):
        print('System Info:')
        print('  pid                    -> {}'.format(cls.pid()))
        print('  process name           -> {}'.format(cls.process_name()))
        print('  command line args      -> {}'.format(cls.command_line_args()))
        print('  utc time               -> {}'.format(cls.utc_time()))
        print('  epoch                  -> {}'.format(cls.epoch()))
        print('  user                   -> {}'.format(cls.user()))
        print('  hostname               -> {}'.format(cls.hostname()))
        print('  cwd                    -> {}'.format(cls.cwd()))
        print('  platform info          -> {}'.format(cls.platform_info()))
        print('  cpu count              -> {}'.format(cls.cpu_count()))
        print('  memory info            -> {}'.format(cls.memory_info()))
        print('  virtual memory         -> {}'.format(cls.virtual_memory()))
        print('  exports dir            -> {}'.format(Env.exports_dir()))
        print('  exports dir disk usage -> {}'.format(cls.export_dir_disk_usage()))

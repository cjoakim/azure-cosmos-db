import os
import sys
import time

import arrow

from pysrc.constants import Constants

# This class is used to read the host environment, such as environment variables.
# It also has methods for command-line argument processing.
# Chris Joakim, Microsoft

class Env(object):

    @classmethod
    def var(cls, name, default=None):
        if name in os.environ:
            return os.environ[name]
        else:
            return default

    @classmethod
    def boolean_arg(cls, flag):
        for arg in sys.argv:
            if arg == flag:
                return True
        return False

    @classmethod
    def capture(cls):
        return cls.boolean_arg(Constants.FLAG_ARG_CAPTURE)

    @classmethod
    def verbose(cls):
        return cls.boolean_arg(Constants.FLAG_ARG_VERBOSE)

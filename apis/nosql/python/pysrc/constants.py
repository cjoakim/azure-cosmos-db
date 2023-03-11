import json
import os
import time

import arrow

# This class defines pseudo-constant values similar to a Java interface.
# The other classes in this project should use these magic/constant values.
# Chris Joakim, Microsoft

class Constants(object):

    AZURE_SUBSCRIPTION_ID              = 'AZURE_SUBSCRIPTION_ID'

    AZURE_COSMOSDB_NOSQL_URI           = 'AZURE_COSMOSDB_NOSQL_URI'
    AZURE_COSMOSDB_NOSQL_RW_KEY1       = 'AZURE_COSMOSDB_NOSQL_RW_KEY1'
    AZURE_COSMOSDB_NOSQL_DB            = 'AZURE_COSMOSDB_NOSQL_DB'
    AZURE_COSMOSDB_NOSQL_PREF_REGIONS  = 'AZURE_COSMOSDB_NOSQL_PREF_REGIONS'

    # These two are well-defined "public" values, not "secrets"
    AZURE_COSMOSDB_NOSQL_EMULATOR_URI_VALUE = 'https://localhost:8081'
    AZURE_COSMOSDB_NOSQL_EMULATOR_KEY_VALUE = 'C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw=='

    AZURE_COSMOSDB_MONGODB_CONN_STRING = 'AZURE_COSMOSDB_MONGODB_CONN_STRING'
    AZURE_COSMOSDB_MONGODB_HOST        = 'AZURE_COSMOSDB_MONGODB_HOST'
    AZURE_COSMOSDB_MONGODB_PASS        = 'AZURE_COSMOSDB_MONGODB_PASS'
    AZURE_COSMOSDB_MONGODB_PORT        = 'AZURE_COSMOSDB_MONGODB_PORT'
    AZURE_COSMOSDB_MONGODB_USER        = 'AZURE_COSMOSDB_MONGODB_USER'

    AZURE_REDISCACHE_HOST              = 'AZURE_REDISCACHE_HOST'
    AZURE_REDISCACHE_PORT              = 'AZURE_REDISCACHE_PORT'
    AZURE_REDISCACHE_KEY               = 'AZURE_REDISCACHE_KEY'

    AZURE_STORAGE_ACCOUNT              = 'AZURE_STORAGE_ACCOUNT'
    AZURE_STORAGE_CONN_STRING          = 'AZURE_STORAGE_CONN_STRING'
    AZURE_STORAGE_KEY                  = 'AZURE_STORAGE_KEY'

    AZURE_CONSTANTS = [
        AZURE_SUBSCRIPTION_ID,
        AZURE_COSMOSDB_NOSQL_URI,
        AZURE_COSMOSDB_NOSQL_RW_KEY1,
        AZURE_COSMOSDB_NOSQL_DB,
        AZURE_COSMOSDB_NOSQL_PREF_REGIONS,
        AZURE_COSMOSDB_MONGODB_CONN_STRING,
        AZURE_COSMOSDB_MONGODB_HOST,
        AZURE_COSMOSDB_MONGODB_PASS,
        AZURE_COSMOSDB_MONGODB_PORT,
        AZURE_COSMOSDB_MONGODB_USER,
        AZURE_REDISCACHE_HOST,
        AZURE_REDISCACHE_PORT,
        AZURE_REDISCACHE_KEY,
        AZURE_STORAGE_ACCOUNT,
        AZURE_STORAGE_CONN_STRING,
        AZURE_STORAGE_KEY
    ]

    REDIS_CACHE_WINDOWS_NET            = '.redis.cache.windows.net'

    FLAG_ARG_CAPTURE                   = '--capture'
    FLAG_ARG_VERBOSE                   = '--verbose'
    FLAG_ARG_USE_AZURE_REDIS           = '--use-azure-redis'
    FLAG_ARG_USE_LOCAL_REDIS           = '--use-local-redis'
    FLAG_ARG_NO_TRANSFORM              = '--no-transform'
    FLAG_ARG_SCAN_DOCS                 = '--scan-docs'
    FLAG_ARG_POST_PROCESS_IDS          = '--post-process'

    REQUEST_CHARGE_HEADER              = 'x-ms-request-charge'
    ACTIVITY_ID_HEADER                 = 'x-ms-activity-id'

    LOCALHOST_IP                       = '127.0.0.1'
    DEFAULT_REDIS_PORT                 = 6379

    def __init__(self):
        pass

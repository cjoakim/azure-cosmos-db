import redis

from pysrc.constants import Constants
from pysrc.env import Env

# This class is used to access either a local Redis server, or Azure Cache for Redis.
# Chris Joakim, Microsoft

class RCache(object):

    def __init__(self, use_azure_redis=True):
        use_azure_redis = Env.use_azure_redis()
        if Env.verbose():
            print('RCache init() - is_azure_redis_cache: {}'.format(use_azure_redis))

        if use_azure_redis:
            # host will be a value like 'cjoakimrediscache.redis.cache.windows.net'
            host = Env.var(Constants.AZURE_REDISCACHE_HOST)
            port = Env.var(Constants.AZURE_REDISCACHE_PORT)
            key  = Env.var(Constants.AZURE_REDISCACHE_KEY)
            if Env.verbose():
                print('redis host: {}'.format(host))
                print('redis port: {}'.format(port))
                print('redis key:  {}'.format(key))
            # https://github.com/MicrosoftDocs/azure-docs/blob/main/articles/azure-cache-for-redis/cache-python-get-started.md
            self.client = redis.StrictRedis(host=host, port=port, db=0, password=key, ssl=True)
        else:
            host = Constants.LOCALHOST_IP
            port = Constants.DEFAULT_REDIS_PORT
            if Env.verbose():
                print('redis host: {}'.format(host))
                print('redis port: {}'.format(port))
            # https://docs.redis.com/latest/rs/references/client_references/client_python/
            self.client = redis.Redis(host=host, port=port)

    def set(self, key, value):
        return self.client.set(key, value)

    def get(self, key):
        return self.client.get(key)

    def client(self):
        return self.client

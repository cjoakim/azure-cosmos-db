
import os
import sys
import traceback

from gremlin_python.driver import client, serializer, protocol
from gremlin_python.driver.protocol import GremlinServerError


if __name__ == "__main__":
    try:
        acct   = os.environ['AZURE_COSMOSDB_GRAPHDB_ACCT']
        key    = os.environ['AZURE_COSMOSDB_GRAPHDB_KEY']
        dbname = os.environ['AZURE_COSMOSDB_GRAPHDB_DBNAME']
        graph  = os.environ['AZURE_COSMOSDB_GRAPHDB_GRAPH']

        dbname = 'dev'
        graph  = 'npm'
        url    = 'wss://{}.gremlin.cosmosdb.azure.com:443/'.format(acct)
        username = '/dbs/{}/colls/{}'.format(dbname, graph)

        print('acct: {}'.format(acct))
        print('key:  {}'.format(key))
        print('url:  {}'.format(url))
        print('username: {}'.format(username))

        client = client.Client(url, 'g', username=username, password=key,
            message_serializer=serializer.GraphSONSerializersV2d0())

        query = 'g.V().count()'
        callback = client.submitAsync(query)
        if callback.result() is not None:
            print(callback)
            result = callback.result()
            attrs  = result.status_attributes
            print(attrs)
            print("count: {0}".format(callback.result().all().result()))
            #print("count: {0}".format(callback.result().all().result())) 
        else:
            print("unable to execute query: {0}".format(query))

        query = "g.V(['mssql','mssql'])"
        callback = client.submitAsync(query)
        if callback.result() is not None:
            print(callback)
            result = callback.result()
            attrs  = result.status_attributes
            print(attrs)
            print(callback.result().all().result())
        else:
            print("unable to execute query: {0}".format(query))

        client.close()
    except GremlinServerError as e:
        print(e)
        print('Code: {0}, Attributes: {1}'.format(e.status_code, e.status_attributes))

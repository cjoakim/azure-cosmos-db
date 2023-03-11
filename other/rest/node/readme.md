# readme for other/rest 

## Node.js REST client

### Links 

- https://learn.microsoft.com/en-us/rest/api/cosmos-db/querying-cosmosdb-resources-using-the-rest-api
- https://learn.microsoft.com/en-us/rest/api/cosmos-db/access-control-on-cosmosdb-resources
- https://learn.microsoft.com/en-us/rest/api/cosmos-db/common-cosmosdb-rest-request-headers
- https://learn.microsoft.com/en-us/rest/api/cosmos-db/get-partition-key-ranges
- https://www.digitalocean.com/community/tutorials/how-to-create-an-http-client-with-core-http-in-node-js
- https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date/toUTCString

### Use

#### List Databases 

```
> node .\rest-client.js
invalid command line args
usage:
  node rest-client.js gen_token
  node rest-client.js list_databases
  
  
> node rest-client.js list_databases
https request options:
{
    "host": "chjoakimcosmoscore.documents.azure.com",
    "path": "/dbs",
    "method": "GET",
    "headers": {
        "Host": "chjoakimcosmoscore.documents.azure.com",
        "Accept": "application/json",
        "authorization": "type%3Dmaster%26ver%3D1.0%26sig%3Dbwplsm1Pse5eGr36OmAMY%2FiuDUN1SN5TyPi4qL2963A%3D",
        "x-ms-date": "sun, 15 jan 2023 21:14:31 gmt",
        "x-ms-version": "2015-08-06"
    }
}
200
{
  _rid: '',
  Databases: [
    {
      id: 'dev',
      _rid: 'SIwaAA==',
      _self: 'dbs/SIwaAA==/',
      _etag: '"0000eb02-0000-0100-0000-6323965d0000"',
      _colls: 'colls/',
      _users: 'users/',
      _ts: 1663276637
    }
  ],
  _count: 1
}
``` 

#### Get Database 

``` 
> node rest-client.js get_database dev
getDatabase; dbName: dev
https request options:
{
    "host": "chjoakimcosmoscore.documents.azure.com",
    "path": "/dbs/dev",
    "method": "GET",
    "headers": {
        "Host": "chjoakimcosmoscore.documents.azure.com",
        "Accept": "application/json",
        "authorization": "type%3Dmaster%26ver%3D1.0%26sig%3D6Y5aEffIgWukpSgp676dprBfp6QzrlKIjBIlGFsJGok%3D",
        "x-ms-date": "sun, 15 jan 2023 22:10:32 gmt",
        "x-ms-version": "2015-08-06"
    }
}
200
{
  id: 'dev',
  _rid: 'SIwaAA==',
  _self: 'dbs/SIwaAA==/',
  _etag: '"0000eb02-0000-0100-0000-6323965d0000"',
  _colls: 'colls/',
  _users: 'users/',
  _ts: 1663276637
}
```

#### List Collections

``` 
> node rest-client.js list_collections dev
https request options:
{
    "host": "chjoakimcosmoscore.documents.azure.com",
    "path": "/dbs/dev/colls",
    "method": "GET",
    "headers": {
        "Host": "chjoakimcosmoscore.documents.azure.com",
        "Accept": "application/json",
        "authorization": "type%3Dmaster%26ver%3D1.0%26sig%3DXX9eahmqonyTllqv3ocw9Mov4ZFmJcg8%2F9cMoT5gcTM%3D",
        "x-ms-date": "sun, 15 jan 2023 22:21:44 gmt",
        "x-ms-version": "2015-08-06"
    }
}
200
{
  _rid: 'SIwaAA==',
  DocumentCollections: [
    {
      id: 'npm_graph',
      indexingPolicy: [Object],
      partitionKey: [Object],
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      analyticalStorageTtl: -1,
      _rid: 'SIwaAIOHTAo=',
      _ts: 1669040753,
      _self: 'dbs/SIwaAA==/colls/SIwaAIOHTAo=/',
      _etag: '"0000e19a-0000-0100-0000-637b8a710000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'telemetry',
      indexingPolicy: [Object],
      partitionKey: [Object],
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      analyticalStorageTtl: 999999999,
      _rid: 'SIwaALDwxo4=',
      _ts: 1663276673,
      _self: 'dbs/SIwaAA==/colls/SIwaALDwxo4=/',
      _etag: '"0000ef02-0000-0100-0000-632396810000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'imdb_graph',
      indexingPolicy: [Object],
      partitionKey: [Object],
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      analyticalStorageTtl: -1,
      _rid: 'SIwaAP-GWJA=',
      _ts: 1669040586,
      _self: 'dbs/SIwaAA==/colls/SIwaAP-GWJA=/',
      _etag: '"0000d59a-0000-0100-0000-637b89ca0000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'imdb_seed',
      indexingPolicy: [Object],
      partitionKey: [Object],
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      analyticalStorageTtl: -1,
      _rid: 'SIwaAPyUpJo=',
      _ts: 1669040678,
      _self: 'dbs/SIwaAA==/colls/SIwaAPyUpJo=/',
      _etag: '"0000db9a-0000-0100-0000-637b8a260000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'gpoc',
      indexingPolicy: [Object],
      partitionKey: [Object],
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      analyticalStorageTtl: -1,
      _rid: 'SIwaAMbkY54=',
      _ts: 1671720505,
      _self: 'dbs/SIwaAA==/colls/SIwaAMbkY54=/',
      _etag: '"00003101-0000-0100-0000-63a46e390000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'altgraph',
      indexingPolicy: [Object],
      partitionKey: [Object],
      defaultTtl: -1,
      uniqueKeyPolicy: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      _rid: 'SIwaAP8Cg8g=',
      _ts: 1669999625,
      _self: 'dbs/SIwaAA==/colls/SIwaAP8Cg8g=/',
      _etag: '"0000e4c4-0000-0100-0000-638a2c090000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    },
    {
      id: 'node',
      indexingPolicy: [Object],
      partitionKey: [Object],
      conflictResolutionPolicy: [Object],
      geospatialConfig: [Object],
      _rid: 'SIwaAN6ARdk=',
      _ts: 1673737129,
      _self: 'dbs/SIwaAA==/colls/SIwaAN6ARdk=/',
      _etag: '"0000ef38-0000-0100-0000-63c333a80000"',
      _docs: 'docs/',
      _sprocs: 'sprocs/',
      _triggers: 'triggers/',
      _udfs: 'udfs/',
      _conflicts: 'conflicts/'
    }
  ],
  _count: 7
}
```

#### Get Container

``` 
> node rest-client.js get_container dev telemetry
getContainer; dbName: dev, cName: telemetry
https request options:
{
    "host": "chjoakimcosmoscore.documents.azure.com",
    "path": "/dbs/dev/colls/telemetry",
    "method": "GET",
    "headers": {
        "Host": "chjoakimcosmoscore.documents.azure.com",
        "Accept": "application/json",
        "authorization": "type%3Dmaster%26ver%3D1.0%26sig%3DrMeYxhqf3vTt%2FEAfqG%2BIIEMaLjkN1b%2FyZzs3wfLKAnY%3D",
        "x-ms-date": "sun, 15 jan 2023 22:05:38 gmt",
        "x-ms-version": "2015-08-06"
    }
}
200
{
  id: 'telemetry',
  indexingPolicy: {
    indexingMode: 'consistent',
    automatic: true,
    includedPaths: [ [Object] ],
    excludedPaths: [ [Object] ]
  },
  partitionKey: { paths: [ '/pk' ], kind: 'Hash' },
  uniqueKeyPolicy: { uniqueKeys: [] },
  conflictResolutionPolicy: {
    mode: 'LastWriterWins',
    conflictResolutionPath: '/_ts',
    conflictResolutionProcedure: ''
  },
  geospatialConfig: { type: 'Geography' },
  analyticalStorageTtl: 999999999,
  _rid: 'SIwaALDwxo4=',
  _ts: 1663276673,
  _self: 'dbs/SIwaAA==/colls/SIwaALDwxo4=/',
  _etag: '"0000ef02-0000-0100-0000-632396810000"',
  _docs: 'docs/',
  _sprocs: 'sprocs/',
  _triggers: 'triggers/',
  _udfs: 'udfs/',
  _conflicts: 'conflicts/'
}
```

#### Container Partition Key Ranges

``` 
> node rest-client.js pk_ranges dev telemetry
getPkRanges; dbName: dev, cName: telemetry
resourceId: dbs/dev/colls/telemetry
https request options:
{
    "host": "chjoakimcosmoscore.documents.azure.com",
    "path": "/dbs/dev/colls/telemetry/pkranges",
    "method": "GET",
    "headers": {
        "Host": "chjoakimcosmoscore.documents.azure.com",
        "Accept": "application/json",
        "authorization": "type%3Dmaster%26ver%3D1.0%26sig%3DzgVx7j1s4mnvPUc2yF7ve%2F%2ByTjlZivOceHs0Dex3MRg%3D",
        "x-ms-date": "sun, 15 jan 2023 22:04:29 gmt",
        "x-ms-version": "2015-08-06"
    }
}
200
{
  _rid: 'SIwaALDwxo4=',
  PartitionKeyRanges: [
    {
      _rid: 'SIwaALDwxo4CAAAAAAAAUA==',
      id: '0',
      _etag: '"0000f202-0000-0100-0000-632396810000"',
      minInclusive: '',
      maxExclusive: 'FF',
      ridPrefix: 0,
      _self: 'dbs/SIwaAA==/colls/SIwaALDwxo4=/pkranges/SIwaALDwxo4CAAAAAAAAUA==/',
      throughputFraction: 1,
      status: 'online',
      parents: [],
      _ts: 1663276673
    }
  ],
  _count: 1
}
```
# Azure Cosmos DB : Mongo API Java SDK Use and Patterns

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

This presentation: https://github.com/cjoakim/azure-cosmos-db/tree/main/apis/mongo/java/readme.md

## Topics Covered 

- Java with the **'org.mongodb:mongodb-driver-sync:4.4.1'** library
- Indexing
- explain() your queries to ensure index utilization
- Time-to-Live (TTL)
- CRUD Operations
- Determine RU Costs of each database operation
- Server Side Retry functionality in Cosmos DB Mongo API
- Bulk Loads with a "Spike" profile Anti-Pattern
- Bulk Deletes with a "Spike" profile Anti-Pattern
- Bulk Loads with a "Flatter" profile Best-Practice to consume less RUs
- Bulk Deletes with a "Flatter" profile Best-Practice to consume less RUs
- Summary of Best Practices

---

## Cosmos DB Configuration In These Examples

- **Cosmos DB Mongo API Provisioned Account** (not Serverless)
- Create **Sharded** container **sharded1** in database **manual**.
- The container has the partition key **pk** 
- The container has **400 manual Request Units (RUs)**
- The container is created with default indexing (no wildcards)
  - Indexes are explicitly added in this set of examples

---

## Default Index is on just the _id attribute

``` 
db.getCollection("sharded1").getIndexes()

[
    {
        "v" : 1.0,
        "key" : {
            "_id" : 1.0
        },
        "name" : "_id_",
        "ns" : "manual.sharded1"
    }
]
```

---

## The Documents in this simulated Vehicle Telemetry app look like this:

```
{
  "pk" : "GB39JDZH72027092449501",
  "utc_time" : "2023-03-18 12:08:16.009094",
  "transponder" : "GB39JDZH72027092449501",
  "location" : [ "43.91452", "-69.96533", "Brunswick", "US", "America/New_York" ],
  "vehicle" : {
    "Year" : 2018,
    "Make" : "GMC",
    "Model" : "Canyon Crew Cab",
    "Category" : "Pickup"
  },
  "plate" : "JED4470",
  "ttl" : 5
}
```

## Add Indices for the attributes that the app will query on (pk, transponder, and plate)

```
db.getCollection("sharded1").createIndex( {pk : 1} ) 
db.getCollection("sharded1").createIndex( {transponder : 1} )
db.getCollection("sharded1").createIndex( {plate : 1} )

db.getCollection("sharded1").getIndexes()

[
    {
        "v" : 1.0,
        "key" : {
            "_id" : 1.0
        },
        "name" : "_id_",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "pk" : 1.0
        },
        "name" : "pk_1",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "transponder" : 1.0
        },
        "name" : "transponder_1",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "plate" : 1.0
        },
        "name" : "plate_1",
        "ns" : "manual.sharded1"
    }
]
```

## Enable the "Server Side Retry" Feature

**This is a great feature of the Cosmos DB Mongo API!**

It automatically retries DB operations n-number of times in the event of RU-throttling errors (i.e. - 429 error code).

See https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/prevent-rate-limiting-errors 

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/gbbcjmongo-features.png" width="90%">
</p>

---

## explain() your queries 

**Best Practice: explain() your queries to ensure index utilization**.

This results in lower Cosmos DB costs.

``` 
db.getCollection("sharded1").find({"pk" : "GB41FAHL15906924745293"}).explain()

{
    "command" : "db.runCommand({explain: { 'find' : 'sharded1', 'filter' : { 'pk' : 'GB41FAHL15906924745293' } }})",
    "stages" : [
        {
            "stage" : "$query",
            "timeInclusiveMS" : 4.0453,
            "timeExclusiveMS" : 4.0453,
            "in" : 1.0,
            "out" : 1.0,
            "dependency" : {
                "getNextPageCount" : 1.0,
                "count" : 1.0,
                "time" : 0.0,
                "bytes" : NumberLong(525)
            },
            "details" : {
                "database" : "manual",
                "collection" : "sharded1",
                "query" : {
                    "pk" : "GB41FAHL15906924745293"
                },
                "indexUsage" : {
                    "pathsIndexed" : {
                        "individualIndexes" : [
                            "pk"                       <--- pk index utilized
                        ],
                        "compoundIndexes" : [

                        ]
                    },
                    "pathsNotIndexed" : {
                        "individualIndexes" : [

                        ],
                        "compoundIndexes" : [

                        ]
                    }
                },
                "shardInformation" : [
                    {
                        "activityId" : "a7398f1f-6092-45bb-ac24-55109220b78b",
                        "shardKeyRangeId" : "[,FF) move next",
                        "durationMS" : 3.1956,
                        "preemptions" : 0.0,
                        "outputDocumentCount" : 1.0,
                        "retrievedDocumentCount" : 1.0
                    }
                ],
                "queryMetrics" : {
                    "retrievedDocumentCount" : 1.0,
                    "retrievedDocumentSizeBytes" : 500.0,
                    "outputDocumentCount" : 1.0,
                    "outputDocumentSizeBytes" : 525.0,
                    "indexHitRatio" : 1.0,
                    "totalQueryExecutionTimeMS" : 0.5,
                    "queryPreparationTimes" : {
                        "queryCompilationTimeMS" : 0.09,
                        "logicalPlanBuildTimeMS" : 0.03,
                        "physicalPlanBuildTimeMS" : 0.07,
                        "queryOptimizationTimeMS" : 0.01
                    },
                    "indexLookupTimeMS" : 0.08,
                    "documentLoadTimeMS" : 0.03,
                    "vmExecutionTimeMS" : 0.15,
                    "runtimeExecutionTimes" : {
                        "queryEngineExecutionTimeMS" : 0.03,
                        "systemFunctionExecutionTimeMS" : 0.01,
                        "userDefinedFunctionExecutionTimeMS" : 0.0
                    },
                    "documentWriteTimeMS" : 0.01
                }
            }
        }
    ],
    "estimatedDelayFromRateLimitingInMilliseconds" : 0.0,
    "retriedDueToRateLimiting" : false,
    "totalRequestCharge" : 4.93,
    "continuation" : {
        "hasMore" : false
    },
    "ActivityId" : "d63efb43-ae7b-421a-a287-6c85f6a2b6d2",
    "ok" : 1.0
}
```

Query on an indexed attribute, for this example was **4.93** RU.

### Point-Read Example

``` 
db.getCollection("sharded1").find({"_id" : ObjectId("6419af49abbf183d7ec9bc81"), "pk" : "GB41FAHL15906924745293"}).explain()

{
    "command" : "db.runCommand({explain: { 'find' : 'sharded1', 'filter' : { '_id' : ObjectId('6419af49abbf183d7ec9bc81'), 'pk' : 'GB41FAHL15906924745293' } }})",
    "stages" : [
        {
            "stage" : "$pointLookup",
            "timeInclusiveMS" : 21.0141,
            "timeExclusiveMS" : 0.332,
            "in" : 1.0,
            "out" : 1.0,
            "dependency" : {
                "getNextPageCount" : 1.0,
                "count" : 1.0,
                "time" : 20.6821,
                "bytes" : NumberLong(862)
            },
            "details" : {
                "database" : "manual",
                "collection" : "sharded1",
                "items" : [
                    {
                        "id" : ObjectId("6419af49abbf183d7ec9bc81"),
                        "shardKey" : "GB41FAHL15906924745293"
                    }
                ]
            }
        }
    ],
    "estimatedDelayFromRateLimitingInMilliseconds" : 0.0,
    "retriedDueToRateLimiting" : false,
    "totalRequestCharge" : 1.0,
    "continuation" : {
        "hasMore" : false
    },
    "ActivityId" : "a7abed12-05fc-45b5-88d0-4bb346897526",
    "ok" : 1.0
}
```

Notice the **$pointLookup state** in the output, and the **totalRequestCharge of 1.0**.

### No Index Example

``` 
db.getCollection("sharded1").find({"name" : "Chris Joakim"}).explain()

{
    "command" : "db.runCommand({explain: { 'find' : 'sharded1', 'filter' : { 'name' : 'GB41FAHL15906924745293' } }})",
    "stages" : [
        {
            "stage" : "$query",
            "timeInclusiveMS" : 58.1842,
            "timeExclusiveMS" : 58.1842,
            "in" : 0.0,
            "out" : 0.0,
            "dependency" : {
                "getNextPageCount" : 1.0,
                "count" : 1.0,
                "time" : 0.0,
                "bytes" : NumberLong(36)
            },
            "details" : {
                "database" : "manual",
                "collection" : "sharded1",
                "query" : {
                    "name" : "GB41FAHL15906924745293"
                },
                "indexUsage" : {
                    "pathsIndexed" : {
                        "individualIndexes" : [

                        ],
                        "compoundIndexes" : [

                        ]
                    },
                    "pathsNotIndexed" : {
                        "individualIndexes" : [
                            "name"
                        ],
                        "compoundIndexes" : [

                        ]
                    }
                },
                "shardInformation" : [
                    {
                        "activityId" : "421a8ce2-a3e2-4a54-a2ab-3507b186c931",
                        "shardKeyRangeId" : "[,FF) move next",
                        "durationMS" : 55.5739,
                        "preemptions" : 0.0,
                        "outputDocumentCount" : 0.0,
                        "retrievedDocumentCount" : 10000.0
                    }
                ],
                "queryMetrics" : {
                    "retrievedDocumentCount" : 10000.0,
                    "retrievedDocumentSizeBytes" : 4953682.0,
                    "outputDocumentCount" : 0.0,
                    "outputDocumentSizeBytes" : 36.0,
                    "indexHitRatio" : 0.0,
                    "totalQueryExecutionTimeMS" : 53.68,
                    "queryPreparationTimes" : {
                        "queryCompilationTimeMS" : 0.07,
                        "logicalPlanBuildTimeMS" : 0.03,
                        "physicalPlanBuildTimeMS" : 0.06,
                        "queryOptimizationTimeMS" : 0.0
                    },
                    "indexLookupTimeMS" : 0.0,
                    "documentLoadTimeMS" : 45.36,
                    "vmExecutionTimeMS" : 53.32,
                    "runtimeExecutionTimes" : {
                        "queryEngineExecutionTimeMS" : 7.96,
                        "systemFunctionExecutionTimeMS" : 4.76,
                        "userDefinedFunctionExecutionTimeMS" : 0.0
                    },
                    "documentWriteTimeMS" : 0.0
                }
            }
        }
    ],
    "estimatedDelayFromRateLimitingInMilliseconds" : 0.0,
    "retriedDueToRateLimiting" : false,
    "totalRequestCharge" : 171.1,
    "continuation" : {
        "hasMore" : false
    },
    "ActivityId" : "10cc0079-d0ca-4d90-b308-7d56016b59f8",
    "ok" : 1.0
}
```

Query on an unindexed attribute, for this example was **171.1** RU.

**171.1 / 4.93 = 34.7 times more expensive in RUs** in this example.

---

## TTL Example

The Cosmos DB Mongo API creates a hidden attribute named **_ts** (epoch timestamp).

This attribute is not hidden in the **Cosmos DB NoSQL** API.

See https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live

### Create the TTL (Time-to-Live) Index

Only do this if your application needs TTL functionality!

First, create the TTL index.  Expire documents in 1-hour (3600 seconds) by default.

```
db.getCollection("sharded1").createIndex({"_ts":1}, {expireAfterSeconds: 3600})

db.getCollection("sharded1").getIndexes()

[
    {
        "v" : 1.0,
        "key" : {
            "_id" : 1.0
        },
        "name" : "_id_",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "pk" : 1.0
        },
        "name" : "pk_1",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "transponder" : 1.0
        },
        "name" : "transponder_1",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "plate" : 1.0
        },
        "name" : "plate_1",
        "ns" : "manual.sharded1"
    },
    {
        "v" : 1.0,
        "key" : {
            "_ts" : 1.0
        },
        "name" : "_ts_1",
        "ns" : "manual.sharded1",
        "expireAfterSeconds" : 3600.0
    },
]
```

### Execute the Java Program

1) Create a document with the **ttl** attribute = 5 seconds
2) Read the document; it is present
3) Thread.sleep for a few seconds
4) Read the document; it is no longer present; it was deleted by the TTL functionality.

```
PS ...\java> gradle ttl

> Task :app:ttl
processType: ttl
rawVehicleActivityData read, document count: 10000
getMongoUtil creating instance...
08:21:03.095 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
08:21:03.199 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
08:21:03.617 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
ttlExample...
----------
raw HashMap:
{
  "pk" : "GB52LPSW51909248782657",
  "utc_time" : "2023-03-18 12:08:15.851577",
  "transponder" : "GB52LPSW51909248782657",
  "location" : [ "39.96097", "-75.60804", "West Chester", "US", "America/New_York" ],
  "vehicle" : {
    "Year" : 2005,
    "Make" : "Chevrolet",
    "Model" : "Silverado 1500 Regular Cab",
    "Category" : "Pickup"
  },
  "plate" : "LXO 802",
  "ttl" : 5
}
InsertOneResult, ObjectId toHexString(): 6416fe2f34c2553c4f8e1543
----------
First find() on the new Document...
Response Document:
{
  "_id": {
    "$oid": "6416fe2f34c2553c4f8e1543"
  },
  "pk": "GB52LPSW51909248782657",
  "utc_time": "2023-03-18 12:08:15.851577",
  "transponder": "GB52LPSW51909248782657",
  "location": [
    "39.96097",
    "-75.60804",
    "West Chester",
    "US",
    "America/New_York"
  ],
  "vehicle": {
    "Year": 2005,
    "Make": "Chevrolet",
    "Model": "Silverado 1500 Regular Cab",
    "Category": "Pickup"
  },
  "plate": "LXO 802",
  "ttl": 5
}
end of cursor; docsReadCount: 1
sleeping for 20 seconds...
----------
Second find() on the new Document...
end of cursor; docsReadCount: 0
08:21:25.472 [main] WARN  MongoUtil - closing mongoClient...
08:21:25.511 [main] WARN  MongoUtil - mongoClient closed
```

---

## CRUD Operations, with RU (Request Unit) Information

See https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/updateOne/
See https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/

### Wait, how does the MongoDB SDK know about and return Cosmos DB Request Units?

See class MongoUtil in this repo, file apis/mongo/java/app/src/main/java/org/cjoakim/cosmosdb/common/mongo/MongoUtil.java

It uses the **getLastRequestStatistics command**.

``` 
    public Document getLastRequestStatistics() {

        return this.currentDatabase.runCommand(new Document("getLastRequestStatistics", 1));
    }
```

### Execute the Code 

``` 
PS ...\java> gradle crud

...
processType: crud
rawVehicleActivityData read, document count: 10000
getMongoUtil creating instance...
09:06:36.251 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
09:06:36.331 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
09:06:36.758 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
crudOperationsExamples...
----------
raw HashMap:
{
  "pk" : "GB55KSCX64698313358989",
  "utc_time" : "2023-03-18 12:08:16.091083",
  "transponder" : "GB55KSCX64698313358989",
  "location" : [ "41.0051", "-73.78458", "Scarsdale", "US", "America/New_York" ],
  "vehicle" : {
    "Year" : 2017,
    "Make" : "Ram",
    "Model" : "1500 Regular Cab",
    "Category" : "Pickup"
  },
  "plate" : "JVM 089"
}
InsertOneResult, ObjectId toHexString(): 641708dc2ec1ee7667feddb6
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 20.88,
  "RequestDurationInMilliSeconds" : 173,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "09836179-fcc7-43de-b900-96e9c41d8673",
  "ok" : 1.0
}
----------
Document after Insert:
{
  "_id": {
    "$oid": "641708dc2ec1ee7667feddb6"
  },
  "pk": "GB55KSCX64698313358989",
  "utc_time": "2023-03-18 12:08:16.091083",
  "transponder": "GB55KSCX64698313358989",
  "location": [
    "41.0051",
    "-73.78458",
    "Scarsdale",
    "US",
    "America/New_York"
  ],
  "vehicle": {
    "Year": 2017,
    "Make": "Ram",
    "Model": "1500 Regular Cab",
    "Category": "Pickup"
  },
  "plate": "JVM 089"
}
LastRequestStatistics:
{
  "CommandName" : "find",
  "RequestCharge" : 1.0,
  "RequestDurationInMilliSeconds" : 18,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "7c1393a2-0722-4989-b3be-bb38ee77d4f0",
  "ok" : 1.0
}
----------
UpdateOne - matched: 1, modified: 1
LastRequestStatistics:
{
  "CommandName" : "update",
  "RequestCharge" : 11.67,
  "RequestDurationInMilliSeconds" : 28,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "37d99d99-5b7b-410e-b3d8-601b4d2b4b7f",
  "ok" : 1.0
}
----------
Document after Update:
{
  "_id": {
    "$oid": "641708dc2ec1ee7667feddb6"
  },
  "pk": "GB55KSCX64698313358989",
  "utc_time": "2023-03-18 12:08:16.091083",
  "transponder": "GB55KSCX64698313358989",
  "location": [
    "41.0051",
    "-73.78458",
    "Scarsdale",
    "US",
    "America/New_York"
  ],
  "vehicle": {
    "Year": 2017,
    "Make": "Ram",
    "Model": "1500 Regular Cab",
    "Category": "Pickup"
  },
  "plate": "JVM 089",
  "comment": "Vehicle was speeding; 97 mph in a 65 mph zone"
}
LastRequestStatistics:
{
  "CommandName" : "find",
  "RequestCharge" : 1.0,
  "RequestDurationInMilliSeconds" : 2,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "38a41f8c-dd04-4a6d-9382-4d6d0ea7b595",
  "ok" : 1.0
}
----------
DeleteOne - deleted count: 1
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 10.1,
  "RequestDurationInMilliSeconds" : 5,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "66e22aa7-55f4-4d03-8fe1-28ed649e19f7",
  "ok" : 1.0
}
----------
Attempting to read the deleted document...
LastRequestStatistics:
{
  "CommandName" : "find",
  "RequestCharge" : 1.0,
  "RequestDurationInMilliSeconds" : 2,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 0,
  "RetriedDueToRateLimiting" : false,
  "ActivityId" : "ab98ef9b-a135-437b-9cb3-3e3fcae59519",
  "ok" : 1.0
}
09:06:38.847 [main] WARN  MongoUtil - closing mongoClient...
09:06:38.884 [main] WARN  MongoUtil - mongoClient closed
```

### Know your RU Costs for your Application and DB Operations

In this example the RU costs were:

``` 
- Create:  20.88 RU
- Read:     1.00 RU  (this was an efficient "point-read" using _id and partition key)
- Read:     1.00 RU  (also 1 RU to read a Document-not-Found)
- Update:  11.67 RU
- Delete:  10.10 RU
```

---

## Bulk Inserts (Spike Profile Anti-Pattern)

MongoDB operations like **insertMany** and **deleteMany** are apt to create **"Spikes"** in the 
**RU Consumption Profile for your application** (red line).
Strive to  **"Flatten"** (blue line) the **RU Consumption Profile** for **lower Cosmos DB costs**.

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/spike-and-flat-profiles.png" width="100%">
</p>

### The Code

Variable "documents" is an ArrayList<Document> with **10,000 vehicle activity documents**.

See file data/common/vehicle_activity/vehicle_activity_data.json in this repo.

``` 
InsertManyResult result = mongoUtil.getCurrentCollection().insertMany(documents);
```

### Execute the Program

``` 
PS ...\java> gradle insertManySpike

> Task :app:insertManySpike
processType: insert_many_spike
getMongoUtil creating instance...
09:26:16.988 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
09:26:17.067 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
09:26:17.467 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
insertManySpike...
Inserting 10000 documents
Inserted documents: 10000
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 10102.00000000019,
  "RequestDurationInMilliSeconds" : 23417,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 17679,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "9265f3d5-f5df-4d6c-b5bb-10656541468d",
  "ok" : 1.0
}
09:30:13.049 [main] WARN  MongoUtil - closing mongoClient...
09:30:13.086 [main] WARN  MongoUtil - mongoClient closed
```

Count the documents in mongo shell:
```
db.getCollection("sharded1").count({})
10000
```

### The Results

**It worked, due to the enabling the "Server Side Retry" Feature**

See the above **EstimatedDelayFromRateLimitingInMilliseconds** and **RequestDurationInMilliSeconds** values.
The EstimatedDelayFromRateLimitingInMilliseconds is due to error code 429 errors

In this example, **75% of the time was spent on retries** (17679.0 / 23417.0)

10,102 RUs / 23.417 seconds = **431.396 RU per second**

- Links:
  - https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/prevent-rate-limiting-errors
  - https://learn.microsoft.com/en-us/rest/api/cosmos-db/http-status-codes-for-cosmosdb
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/troubleshoot-request-rate-too-large?tabs=resource-specific

---

## Bulk Deletes (Spike Profile Anti-Pattern)

``` 
PS ...\java> gradle deleteManySpike

> Task :app:deleteManySpike
processType: delete_many_spike
getMongoUtil creating instance...
09:46:29.967 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
09:46:30.055 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
09:46:30.481 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
deleteManySpike...
Deleting documents ...
com.mongodb.MongoWriteException -> Write operation error on server gbbcjmongo-eastus.mongo.cosmos.azure.com:10255. Write error: WriteError{code=50, message='Request timed out.', details={}}.
com.mongodb.MongoWriteException: Write operation error on server gbbcjmongo-eastus.mongo.cosmos.azure.com:10255. Write error: WriteError{code=50, message='Request timed out.', details={}}.
        at com.mongodb.client.internal.MongoCollectionImpl.executeSingleWriteRequest(MongoCollectionImpl.java:1018)
        at com.mongodb.client.internal.MongoCollectionImpl.executeDelete(MongoCollectionImpl.java:983)
        at com.mongodb.client.internal.MongoCollectionImpl.deleteMany(MongoCollectionImpl.java:530)
        at com.mongodb.client.internal.MongoCollectionImpl.deleteMany(MongoCollectionImpl.java:525)
        at org.cjoakim.cosmosdb.mongo.App.deleteManySpike(App.java:236)
        at org.cjoakim.cosmosdb.mongo.App.main(App.java:69)
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 27299.24999999916,
  "RequestDurationInMilliSeconds" : 59771,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 21729,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "5f961384-a9ad-4911-a232-6b2bcf665b9f",
  "ok" : 1.0
}
```

Count the documents in mongo shell:
```
db.getCollection("sharded1").count({})
7319
```

### The Results

**In this case it failed, despite enabling the "Server Side Retry" Feature.**

**Timed-out at 60-seconds.**

**Server Side Retry functionality worked to some degree.**
 
2,681 of the 10,000 documents were deleted.

Be sure to add Exception-handling logic to your code to handle all Cosmos DB IO.

---

## Bulk Inserts (Flatter Profile, Best-Practice)

Load the data in smaller batches of Documents, with Thread.sleep after each batch.

### The Code 

``` 
    // Configure these parameters per your application:
    int  batchIndex  = 0;
    int  batchSize   = 100;
    long sleepMs     = 500;
    boolean continueToProcess = true;

    while (continueToProcess) {
        ArrayList<Document> documentBatch = nextBatchOfDocuments(documents, batchIndex, batchSize);
        if (documentBatch.size() > 0) {
            System.out.println("Inserting " + documentBatch.size() + " documents in batch " + batchIndex);
            InsertManyResult result = mongoUtil.getCurrentCollection().insertMany(documentBatch);
            System.out.println("Inserted documents: " + result.getInsertedIds().size());
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
            Thread.sleep(sleepMs);
        }
        else {
            continueToProcess = false;
        }
        batchIndex++;
    }
```

### The Output

``` 
PS ...\java> gradle insertManyFlatter

processType: insert_many_flatter
rawVehicleActivityData read, document count: 10000
getMongoUtil creating instance...
10:43:38.833 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
10:43:38.913 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
10:43:39.332 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
insertManyFlatter...
Inserting 100 documents in batch 0
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1012.0000000000016,
  "RequestDurationInMilliSeconds" : 1328,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 772,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "7005eea0-bae7-4d72-b71d-60ed1432bede",
  "ok" : 1.0
}
Inserting 100 documents in batch 1
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1010.0000000000016,
  "RequestDurationInMilliSeconds" : 1643,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1150,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "91c7c9df-897e-47c4-8969-a1d3cf44b475",
  "ok" : 1.0
}
Inserting 100 documents in batch 2
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1012.0000000000016,
  "RequestDurationInMilliSeconds" : 1686,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1189,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "82c8a6a4-3163-43bc-a2bb-d4ef7e33f995",
  "ok" : 1.0
}

...

Inserting 100 documents in batch 97
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1010.0000000000016,
  "RequestDurationInMilliSeconds" : 1743,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1232,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "b2dd96a2-35ed-4202-9e61-ebcea085b92c",
  "ok" : 1.0
}
Inserting 100 documents in batch 98
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1012.0000000000016,
  "RequestDurationInMilliSeconds" : 1714,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1195,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "0c23d6e5-5b1a-40fd-9bdd-01b89554ebe5",
  "ok" : 1.0
}
Inserting 100 documents in batch 99
Inserted documents: 100
LastRequestStatistics:
{
  "CommandName" : "insert",
  "RequestCharge" : 1010.0000000000016,
  "RequestDurationInMilliSeconds" : 1727,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1219,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "943b190b-59a0-4939-9372-0f3f48f04663",
  "ok" : 1.0
}
10:47:29.642 [main] WARN  MongoUtil - closing mongoClient...
10:47:29.684 [main] WARN  MongoUtil - mongoClient closed
```

### The Results

**Successful**

**No Timeouts**

**Lower RU Profile** - 1,010 RU mini-batches vs 10,102 RU "spike" (with potential timeout)

**Server Side Retry functionality worked**
 
---

## Bulk Deletes (Flatter Profile, Best-Practice)

Delete the documents in smaller batches of Documents, with Thread.sleep after each batch.

Use a known set of values (i.e. - vehicle.Year) to execute the deletes for each year.

### The Code

``` 
      try {
          System.out.println("deleteManyFlatter...");
          Object[] yearValues = collectBatchFileModelYearValues();
          long sleepMs = 100; // Configure as necessary per your application

          for (int i = 0; i < yearValues.length; i++) {
              int year = Integer.parseInt((String) yearValues[i]);
              System.out.println("----------------------------------------");
              Bson query = eq("vehicle.Year", year);
              DeleteResult result = mongoUtil.getCurrentCollection().deleteMany(query);
              System.out.println("Deleted " + result.getDeletedCount() + " documents in year: " + year);
              System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
          }
      }
      catch (Exception e) {
          System.out.println(e.getClass().getName() + " -> " + e.getMessage());
          e.printStackTrace();
      }
```

### The Output

``` 
PS ...\java> gradle deleteManyFlatter

> Task :app:deleteManyFlatter
processType: delete_many_flatter, dbName: manual, cName: sharded1
getMongoUtil creating instance...
15:08:24.879 [main] ERROR MongoUtil - connStr: mongodb://gbbcjmongo:esv5gA83N...
15:08:24.981 [main] WARN  MongoUtil - MongoClientSettings, app name: @gbbcjmongo@
15:08:25.399 [main] WARN  MongoUtil - MongoClients.create ClusterDescription{type=REPLICA_SET, connectionMode=MULTIPLE, serverDescriptions=[ServerDescription{address=gbbcjmongo.mongo.cosmos.azure.com:10255, type=UNKNOWN, state=CONNECTING}]}
using database: manual, container: sharded1
deleteManyFlatter...
----------------------------------------
Deleted 298 documents in year: 1992
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 3213.4500000000053,
  "RequestDurationInMilliSeconds" : 6102,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 3385,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "45af3ef6-8430-4989-8342-6549632f175c",
  "ok" : 1.0
}
----------------------------------------
Deleted 287 documents in year: 1993
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 3084.0200000000045,
  "RequestDurationInMilliSeconds" : 8106,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 5148,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "b1ef9824-1547-4538-b9c0-44041453d1da",
  "ok" : 1.0
}
----------------------------------------
Deleted 267 documents in year: 1994
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 2876.6800000000044,
  "RequestDurationInMilliSeconds" : 6180,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 3422,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "b1cd5d32-1263-4fd5-bd2f-46d74d332a67",
  "ok" : 1.0
}

...

----------------------------------------
Deleted 443 documents in year: 2018
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 4505.1399999999985,
  "RequestDurationInMilliSeconds" : 9716,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 2977,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "d097c01b-ced1-427a-ab20-f266be2ce714",
  "ok" : 1.0
}
----------------------------------------
Deleted 442 documents in year: 2019
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 4487.129999999999,
  "RequestDurationInMilliSeconds" : 9616,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 3320,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "30a43eb4-035d-48f3-964f-bd94609f74e8",
  "ok" : 1.0
}
----------------------------------------
Deleted 231 documents in year: 2020
LastRequestStatistics:
{
  "CommandName" : "delete",
  "RequestCharge" : 2346.219999999994,
  "RequestDurationInMilliSeconds" : 4973,
  "EstimatedDelayFromRateLimitingInMilliseconds" : 1829,
  "RetriedDueToRateLimiting" : true,
  "ActivityId" : "9d80aedf-9c01-4912-987c-457f2c77d5c1",
  "ok" : 1.0
}
15:12:21.010 [main] WARN  MongoUtil - closing mongoClient...
15:12:21.045 [main] WARN  MongoUtil - mongoClient closed
```

### The Results

**Successful**

**No Timeouts**

**Lower RU Profile** - 2K to 4K mini-batches vs large RU "spike" (with potential timeout)

**Server Side Retry functionality worked**

### grep the output for RequestCharge per for each Year

In this case the RequestCharge has a reasonable distribution.
**But, be aware of the distribution/skews in your data.**

``` 
  "RequestCharge" : 3213.4500000000053,
  "RequestCharge" : 3084.0200000000045,
  "RequestCharge" : 2876.6800000000044,
  "RequestCharge" : 2801.0800000000045,
  "RequestCharge" : 3150.020000000005,
  "RequestCharge" : 2791.280000000005,
  "RequestCharge" : 3109.620000000005,
  "RequestCharge" : 3053.8000000000043,
  "RequestCharge" : 3109.200000000004,
  "RequestCharge" : 3477.8000000000047,
  "RequestCharge" : 3492.4200000000046,
  "RequestCharge" : 3425.6600000000058,
  "RequestCharge" : 4096.890000000006,
  "RequestCharge" : 3462.8900000000035,
  "RequestCharge" : 4113.5900000000065,
  "RequestCharge" : 3651.600000000006,
  "RequestCharge" : 4201.030000000007,
  "RequestCharge" : 3759.120000000004,
  "RequestCharge" : 3530.150000000005,
  "RequestCharge" : 3392.550000000005,
  "RequestCharge" : 3993.000000000005,
  "RequestCharge" : 3743.360000000005,
  "RequestCharge" : 3918.7600000000034,
  "RequestCharge" : 3972.390000000004,
  "RequestCharge" : 4339.240000000005,
  "RequestCharge" : 5099.52999999999,
  "RequestCharge" : 4505.1399999999985,
  "RequestCharge" : 4487.129999999999,
  "RequestCharge" : 2346.219999999994,
```

---

## Summary of Best Practices

- Create Indexes for all queried attributes
- explain() your queries to ensure index utilization
- For Sharded containers, create a Partition Key that is used in most of your queries
- Use SDK functionality to know the actual cost, in Request Units, of your DB operations
- Use the "Server Side Retry" Feature
- Be aware of the 60-second operation timeout limitation
- Use "flatter" bulk deletes and inserts to lower your RU consumption profile
- Eliminate the RU consumption "spikes"
- TTL functionality can reduce your overall RU consumption
  - less data/storage in your container 
  - automatic queries with deletes using unused RUs
- Point-Reads (query on _id and partition key) are extremely efficient in Cosmos DB

---

## Mongo Shell Examples

```
db.getCollection("sharded1").find({})
db.getCollection("sharded1").createIndex({"_ts":1}, {expireAfterSeconds: 3600})
db.getCollection("sharded1").createIndex( {transponder : 1} )
db.getCollection("sharded1").getIndexes()
db.getCollection("sharded1").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})
db.getCollection("sharded1").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})

db.getCollection("sharded1").count({})

db.getCollection("sharded1").find({"pk" : "GB41FAHL15906924745293"}).explain()
db.getCollection("sharded1").find({"name" : "GB41FAHL15906924745293"}).explain()
```

---

### Books and Further Reading

- [Cosmos DB for MongoDB Developers, Manish Sharma](https://www.amazon.com/dp/1484247930)

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/cosmosdb-for-mongodb-developers.jpeg" width="25%">
</p>

Please see the **Azure Cosmos DB for MongoDB documentation** at https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/

---
---

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/spacer-500.png" width="90%">
</p>

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/questions.png" width="90%">
</p>

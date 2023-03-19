# readme for apis/mongo/java 

## Topics Covered 

- Java with the **'org.mongodb:mongodb-driver-sync:4.4.1'** library
- Indexing
- Time-to-Live (TTL)
- CRUD Operations
- Determine RU Costs of each database operation
- Bulk Loads with a "Spike" profile
- Bulk Deletes with a "Spike" profile
- Bulk Loads with a "Flatter" profile to consume less RUs
- Bulk Deletes with a "Flatter" profile to consume less RUs
- Summary of Best Practices

## Setup

- Cosmos DB Mongo API Provisioned Account
- Create container **sharded1** in database **manual**.
- The container has the partition key **pk** 
- The container has **400 manual Request Units (RUs)**
- The container has the default indexing (no wildcards)

---

## Default Index on just the _id attribute

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

---

## TTL Example

Cosmos DB created a hidden attribute named **_ts**.

See https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live

### Create the TTL Index

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
2) Read the document - it is present
3) Thread.sleep for a few seconds
4) Read the document - it is no longer present

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

## CRUD Operations, with RU Information

See https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/updateOne/
See https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/

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

### Know your RU Costs 

In this example the RU costs were:

``` 
- Create:  20.88 RU
- Read:     1.00 RU  (this was an efficient "point read" using _id and partition key)
- Read:     1.00 RU  (also 1 RU to read a Document-not-Found)
- Update:  11.67 RU
- Delete:  10.10 RU
```

---

## Bulk Inserts (Spike Profile)

### Enable the "Server Side Retry" Feature

<p align="center">
    <img src="https://github.com/cjoakim/azure-cosmos-db/blob/main/presentations/img/gbbcjmongo-features.png" width="90%">
</p>

### The Code

Variable "documents" is an ArrayList<Document> with 10,000 vehicle activity documents.
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

In this case it worked, probably due to the enabling the "Server Side Retry" Feature.

10,102 RUs / 23.417 seconds = 431.396 RU per second.

---

## Bulk Deletes (Spike Profile)

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

In this case it failed, desipte enabling the "Server Side Retry" Feature.

Timed-out at 60-seconds.

2,681 of the 10,000 documents were deleted.

---

## Bulk Inserts (Flatter Profile)

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

- No Timeouts
- Lower RU Profile - 1,010 RU mini-batches vs 10,102 RU "spike" operation

---

## Bulk Deletes (Flatter Profile)

---

## Summary of Best Practices

- Create Indexes for all queried attributes
- For Sharded containers, create a Partition Key that is used in most of your queries
- Use SDK functionality to know the actual cost, in Request Units, of your DB operations
- Use the "Server Side Retry" Feature
- Be aware of the 60-second operation timeout limitation
- Use "flatter" bulk deletes and inserts to lower your RU consumption profile

---

## Mongo Shell Examples

db.getCollection("sharded1").createIndex({"_ts":1}, {expireAfterSeconds: 3600})
db.getCollection("sharded1").createIndex( {transponder : 1} )
db.getCollection("sharded1").getIndexes()
db.getCollection("sharded1").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})
db.getCollection("sharded1").count({})

---

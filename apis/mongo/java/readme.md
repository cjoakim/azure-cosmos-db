# readme for apis/mongo/java 

## Topics Covered 

- Java with the **'org.mongodb:mongodb-driver-sync:4.4.1'** library
- Indexing
- Time-to-Live (TTL)
- CRUD Operations
- Determine RU Costs of each database operation
- Bulk Loads to consume less RU
- Bulk Deletes to consume less RU

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

## Mongo Shell Examples

db.getCollection("sharded1").createIndex({"_ts":1}, {expireAfterSeconds: 3600})
db.getCollection("sharded1").createIndex( {transponder : 1} )
db.getCollection("sharded1").getIndexes()
db.getCollection("sharded1").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})


---

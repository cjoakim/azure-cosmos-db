# Azure Cosmos DB : Mongo API Indexing

Chris Joakim, Microsoft, Cosmos DB GBB

This presentation: https://github.com/cjoakim/azure-cosmos-db-presentations/tree/main/mongo_indexing

## The Cosmos DB Mongo API

<p align="center">
    <img src="../img/cosmosdb-mongo-api-benefits.png" width="90%">
</p>

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/introduction

---

## Indexing 

- The indexing **syntax is the same** as MongoDB
- The **tooling is the same** as MongoDB - mongosh, Studio 3T, etc
- Index updates are always done in the background with unused Request Units (RUs)
- Index updates/progress can be tracked (see link below)
- **Just like MongoDB, Cosmos DB requires efficient indexes for your applications to perform well**
- The Cosmos DB NoSQL API offers indexing on all fields by default, but **not** the Mongo API

---

## Indexing Types

### Single field

```
db.coll.createIndex({name:1})
```

### Compound

```
db.coll.createIndex({name:1,age:1})
```

### Multikey / Array

Azure Cosmos DB creates multikey indexes to index content stored in arrays.

If you index a field with an array value, Azure Cosmos DB automatically indexes every element in the array.

### Geospatial

Currently, Azure Cosmos DB for MongoDB supports 2dsphere indexes

```
db.coll.createIndex({ location : "2dsphere" })
```

### Wildcard

Currently, Azure Cosmos DB for MongoDB supports 2dsphere indexes

```
db.coll.createIndex({"children.$**" : 1})
```

### Unique

**Unique indexes need to be created while the collection is empty.**

#### Unsharded containers/collections

```
db.coll.createIndex( { "student_id" : 1 }, {unique:true} )  <-- no partition key
```

#### Sharded containers/collections

For sharded collections, you must provide the shard (partition) key to create a unique index.

```
db.coll.createIndex( { "university" : 1, "student_id" : 1 }, {unique:true});   <-- university is partition key
```

### TTL

A TTL index is an index on the **_ts** field

```
db.coll.createIndex({"_ts":1}, {expireAfterSeconds: 10})
```

See https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live

### Indexing all properties

```
db.coll.createIndex( { "$**" : 1 } )
```

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/indexing

---

## Indexing Limitations

### Not Supported

- Wildcard indexes on Compound, TTL, or Unique

---

## MongoMigrationAssessment.exe (MMA) Sample Outputs

TODO

---

<p align="center">
    <img src="../img/spacer-500.png" width="90%">
</p>

<p align="center">
    <img src="../img/questions.png" width="90%">
</p>

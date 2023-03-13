# azure-cosmos-db

A collection of examples and presentations on Azure Cosmos DB with multiple APIs, languages, and applications.

Chris Joakim, Microsoft, Cosmos DB GBB

<p align="center">
    <img src="docs/img/cosmos-db-logo.png" width="90%">
</p>

---

## Directory Structure

This is the directory structure of this repo.

```
├── apis
|     ├── cassandra
|     |     ├── cli
|     |     └── java
|     |     └── python
|     ├── mongo
|     |     ├── cli
|     |     ├── java
|     |     ├── node
|     |     └── python
|     ├── nosql
|     |     ├── cs
|     |     ├── java
|     |     ├── javaspring
|     |     ├── node
|     |     └── python
|     └── pg
|         ├── cli
|         ├── java
|         └── python
├── automation
|     └── az
├── data
|     └── common
├── docs
└── other
|   ├── emulator
|   ├── jupyter
|   ├── kusto
|   ├── search
|   |     └── python           (see https://github.com/cjoakim/azure-cognitive-search-example)
|   ├── server_side
|   └── synapse                (see https://github.com/cjoakim/azure-cosmosdb-synapse-link)
|       ├── adf
|       └── spark
└── presentations
    ├── change_feed
    ├── global_distribution
    ├── mongo_indexing
    ├── mongo_sdks_patterns
    └── synapse_link
```

## Environment Variables

This codebase uses the following environment variables for configuration values:

```
AZURE_SUBSCRIPTION_ID

AZURE_COSMOSDB_NOSQL_URI
AZURE_COSMOSDB_NOSQL_RW_KEY1
AZURE_COSMOSDB_NOSQL_DB
AZURE_COSMOSDB_NOSQL_PREF_REGIONS

AZURE_COSMOSDB_MONGODB_CONN_STRING
AZURE_COSMOSDB_MONGODB_HOST
AZURE_COSMOSDB_MONGODB_PASS
AZURE_COSMOSDB_MONGODB_PORT
AZURE_COSMOSDB_MONGODB_USER

AZURE_REDISCACHE_HOST
AZURE_REDISCACHE_PORT
AZURE_REDISCACHE_KEY

AZURE_STORAGE_ACCOUNT
AZURE_STORAGE_CONN_STRING
AZURE_STORAGE_KEY
```

---

## Other Repositories and Content of Interest

### Personal Repos

- https://github.com/cjoakim/azure-cosmosdb-altgraph
- https://github.com/cjoakim/azure-cosmosdb-synapse-link
- https://github.com/cjoakim/azure-cognitive-search-example
- https://github.com/cjoakim/azure-function-http-py-docker-cosmos
- https://github.com/cjoakim/azure-cosmos-demo22
- https://github.com/cjoakim/azure-maps
- https://github.com/cjoakim/azure-jupyter
- https://github.com/cjoakim/oss

### Azure-Samples

- https://github.com/Azure-Samples/azure-cosmos-db-graph-npm-bom-sample
- https://github.com/Azure-Samples/azure-cosmos-db-mongo-migration

### Cosmos DB Live TV

- https://developer.azurecosmosdb.com/tv
- [Episode #59 - Altgraph](https://www.youtube.com/watch?v=SGih_Kj_1yk&list=PLmamF3YkHLoKMzT3gP4oqHiJbjMaiiLEh&index=13)
- [Episode #40 - Azure Cosmos DB API for MongoDB + Azure Synapse Link](https://www.youtube.com/watch?v=iItNxN2EJ9U)

### Cosmos DB Blogs

- [AltGraph – Graph workloads with Azure Cosmos DB for NoSQL](https://devblogs.microsoft.com/cosmosdb/altgraph-graph-workloads-with-azure-cosmos-db-for-nosql/)


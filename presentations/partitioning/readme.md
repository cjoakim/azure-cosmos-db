# Azure Cosmos DB : Partitioning

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

## Logical and Physical Partitioning

- Applies to individual containers/collections, not databases
- Unsharded containers/collections are supported in the MongoDB API
  - 20GB max storage
- You simply define the **partition key** attribute
  - Each document needs to have this attribute populated
  - **pk** is a good name, or use the attributes you already use (i.e. - customerId)
- **Cosmos DB Manages the Physical Partitions**
- **Terminology - Physical Partitions**
  - Up to 50GB of data
  - Up to 10,000 Request Units (RUs) of throughput
  - Cosmos DB creates these as your data grows, or you increase the RU setting
- **Terminology - Logical Partitions**
  - The set of documents with a given partition key value
  - Max 20GB
  - A Logical Partition lives within one Physical Partition
  - Cosmos DB may "shuffle" the logical partitions as necessary; with no perf impact

<p align="center">
    <img src="../img/cosmosdb-partitions.png" width="80%">
</p>

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview

# Azure Cosmos DB : Partition Key Stats Aggregation in Synapse

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

This presentation: https://github.com/cjoakim/azure-cosmos-db-presentations/tree/main/partition_key_stats_logging

## The Cosmos DB Logging

Configure the Cosmos DB account to log to Azure Monitor.

<p align="center">
    <img src="../img/cosmos-db-diagnostic-settings-1.png" width="80%">
</p>

---

<p align="center">
    <img src="../img/cosmos-db-diagnostic-settings-2.png" width="80%">
</p>

---

## Azure Monitor - automated export to Azure Storage

Configure Azure Monitor to write to Azure Storage for long-term data retention.

<p align="center">
    <img src="../img/azure-monitor-export-everything-to-storage.png" width="80%">
</p>

---

## Azure Synapse - read and aggregate the blobs with Spark

- Read the many blobs exported from Azure Monitor, at 5-minute intervals
- Aggregate the data into a Spark DataFrame
- Write the DataFrame as CSV to another Azure Storage blob

<p align="center">
    <img src="../img/spark-notebook-in-azure-synapse.png" width="90%">
</p>

img/spark-notebook-in-azure-synapse.png

[PySpark Notebook](am-cdbpartitionkeystatistics.ipynb)

---

## Next Steps - process the aggregated blob

These are just a few of many options:

- Import it into a relational database in Azure
  - Azure SQL
  - Azure Database for PostgreSQL
  - Azure Cosmos DB for PostgreSQL
- Process it with Spark

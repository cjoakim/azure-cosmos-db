# Azure Cosmos DB : Scaling and Partitioning

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

## Links

- https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview
- https://learn.microsoft.com/en-us/azure/cosmos-db/scaling-provisioned-throughput-best-practices
- https://learn.microsoft.com/en-us/azure/cosmos-db/scaling-provisioned-throughput-best-practices
- https://learn.microsoft.com/en-us/azure/cosmos-db/scaling-provisioned-throughput-best-practices#how-to-scale-up-rus-without-changing-partition-layout
- https://learn.microsoft.com/en-us/azure/cosmos-db/concepts-limits ( / 100 )
- https://learn.microsoft.com/en-us/azure/cosmos-db/autoscale-faq  ( / 10 )

## Topics

- Organic Growth
- Scale RU Growth
  - https://learn.microsoft.com/en-us/azure/cosmos-db/scaling-provisioned-throughput-best-practices#how-to-scale-up-rus-without-changing-partition-layout

---

## Provision a Partitioned Container with 4,000 Request Units (RU) Autoscale

<p align="center">
    <img src="../img/pp_1.png" width="80%">
</p>

---

## Add 40 GB of Data

<p align="center">
    <img src="../img/pp_2.png" width="80%">
</p>

---

## Increase Container Throughput to 10,000 

Still one physical partition.

<p align="center">
    <img src="../img/pp_3.png" width="80%">
</p>

---

## Add 20 GB of Data - "Organic Growth"

Cosmos DB creates a second physical partition due to data volume, RUs are split.
60 GB data total.

<p align="center">
    <img src="../img/pp_4.png" width="80%">
</p>

---

## Scale to 30,000 RUs - "Provisioned Growth"

This causes Cosmos DB to create a third physical partition.
Add 40 GB more data, now 100 GB total.

<p align="center">
    <img src="../img/pp_5.png" width="80%">
</p>

---

## Scale to 100,000 RUs - More "Provisioned Growth"

Additional physical partitions are created, with 10,000 RUs each.
Still 100 GB of data, distributed.

<p align="center">
    <img src="../img/pp_6.png" width="80%">
</p>

---

## Scale down to 10,000 RUs

The ten physical partitions still exist, but the RUs are split amongst them.
Each physical partition now supports only 2,000 RUs.
Still 100 GB of data.

<p align="center">
    <img src="../img/pp_7.png" width="80%">
</p>

---

## Copy to New Container with 30,000 RUs, three Physical Partitions


<p align="center">
    <img src="../img/pp_8.png" width="80%">
</p>

---


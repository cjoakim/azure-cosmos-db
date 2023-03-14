# Azure Cosmos DB : Global Distribution and Consistency Levels

Chris Joakim, Microsoft, Cosmos DB GBB

This presentation:
https://github.com/cjoakim/azure-cosmos-db-presentations/tree/main/global_distribution

---

### Azure Regions

<p align="center">
  <a href="https://infrastructuremap.microsoft.com/explore">
    <img src="../img/regions-explorer.png" width="50%">
  </a>
</p>

- Links
  - https://infrastructuremap.microsoft.com/explore
  - https://azure.microsoft.com/en-us/explore/global-infrastructure/geographies/#geographies

**Brazil South** region, with Availability Zones, since 2014, São Paulo State

---

## Global Distribution

### Azure Cosmos DB is a globally distributed database system that allows you to read and write data from the local replicas of your database. 

### Azure Cosmos DB transparently replicates the data to all the regions associated with your Azure Cosmos DB account.

### Azure Cosmos DB is a globally distributed database service that's designed to provide low latency, elastic scalability of throughput, data consistency, and high availability.

### In short, if your application needs fast response time anywhere in the world, if it's required to be always online, and needs unlimited and elastic scalability of throughput and storage, you should build your application on Azure Cosmos DB.

---

### Azure Physical Infrastructure

- 200+ Data Centers
- 60+ Azure regions
- 165,000 miles of fiber optic and undersea cable systems
- See https://azure.microsoft.com/en-us/explore/global-infrastructure/

<p align="center">
    <img src="../img/datacenter-outside.jpeg" width="60%">
</p>

<p align="center">
    <img src="../img/datacenter-inside.jpeg" width="60%">
</p>

---

### Cosmos DB Deployment Options

- **Single Region, with no Availability Zone**
- **Single Region, with Availability Zone** (available in some regions)
- **Multiple Regions - with one Write Region**, 1+ Read Regions
- **Multiple Regions - with multiple Write Regions**

#### Recommendation: Single Write Region with Service-Managed Failover

<p align="center">
    <img src="../img/tip-hadr-single-write-region.png" width="60%">
</p>

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/
  - https://learn.microsoft.com/en-us/azure/cosmos-db/distribute-data-globally
  - https://azure.microsoft.com/en-us/explore/global-infrastructure/geographies/#overview
  - https://learn.microsoft.com/en-us/azure/cosmos-db/high-availability

---

#### Cosmos DB Guranteed Service Level Agreement (SLA)

- Single region --> **99.99%**
- Single region with AZ --> **99.995%**
- Multiple region --> **99.999%**
- Financially Backed

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/high-availability
  - https://azurecharts.com/sla
  - https://www.microsoft.com/licensing/docs/view/Service-Level-Agreements-SLA-for-Online-Services

---

### Availability Zones

<p align="center">
    <img src="../img/availability-zones.png" width="90%">
</p>

- Links
  - https://learn.microsoft.com/en-us/azure/reliability/availability-zones-overview
  - https://learn.microsoft.com/en-us/azure/reliability/availability-zones-service-support#azure-regions-with-availability-zone-support

---

### Configure Cosmos DB Regions in Azure Portal

<p align="center">
    <img src="../img/azure-portal-replicate-data.png" width="90%">
</p>

**You can also provision/configure Cosmos DB with DevOps tools - ARM, Bicep, PowerShell, az CLI, Terraform, etc.**

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-templates  (ARM = Azure Resource Manager)
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-bicep
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-powershell
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-cli
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-terraform
  - https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/cosmosdb_account

---

### Cosmos DB High Availability 

- Manual Failover, or
- **Service Managed Failover (recommended)**
- See above Azure Portal screenshot
- You define region failover sequence: Region1 -> Region2
- https://learn.microsoft.com/en-us/azure/cosmos-db/high-availability

<p align="center">
    <img src="../img/service-managed-failover.png" width="90%">
</p>

---

### Example Architecture

<p align="center">
    <img src="../img/global-distribution-deployment-topology.png" width="65%">
</p>

---

### Regional Partitioning - Can I distribute my collection by shard/partition key?

For example, Partition Key "A" in South America and Partition Key "B" in Europe?

**No.  All data in a Cosmos DB container is distributed to all regions.**

---

### Cosmos DB Consistency Levels

**This concept only applies to multi-region Cosmos DB accounts** and how the data
is written to and read from the regions.

Azure Cosmos DB offers **five well-defined consistency levels**. From strongest to weakest, the levels are:

- **Strong**
  - Writes are applied to all regions before returning a response to the client
  - Reads are guaranteed to return the most recent committed version of an item
- **Bounded Staleness**
  - You define the aceptable lag, or staleness
    - Either: The number of versions (K) of the item
    - Or: The time interval (T) reads might lag behind the writes
- **Session (the default)**
  - Within a single client session, reads are guaranteed to honor the **read-your-writes**
- **Consistent Prefix**
  - In all the regions, the reads never see out of order writes for a transactional batch of writes
- **Eventual**
  - Eventual consistency is ideal where the application doesn't require any ordering guarantees
  - Examples include count of Retweets, Likes, or non-threaded comments

<p align="center">
    <img src="../img/five-consistency-levels.png" width="100%">
</p>

- You set the **default consistency level at the Cosmos DB Account Level**
- You can also **override** the default consistency level for a specific request

<p align="center">
    <img src="../img/consistency-relax.png" width="60%">
</p>

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/consistency-levels
  - https://en.wikipedia.org/wiki/PACELC_theorem
  - [Mark Brown - Cosmos Global Distribution Demos](https://github.com/markjbrown/cosmos-global-distribution-demos)  (Shows replication latency)


#### Cosmos DB Cost Implications of Multi-Region and Consistency Levels

<p align="center">
    <img src="../img/consistency-levels-ru-costs.png" width="95%">
</p>

**In short: (RU * 2) for Strong and Bounded Staleness**

- Links
  - https://learn.microsoft.com/en-us/azure/cosmos-db/consistency-levels#consistency-levels-and-throughput
  - https://learn.microsoft.com/en-us/azure/cosmos-db/optimize-cost-regions
  - https://learn.microsoft.com/en-us/azure/cosmos-db/understand-your-bill

---

#### What do Documents Look Like?

**Mongo API Example:**

```
{
	"_id" : ObjectId("640e03d374f91c0cf7885edd"),
	"pk" : "GB19EKLL76864945389161",
	"utc_time" : "2023-03-12 16:54:43.367822",
	"transponder" : "GB19EKLL76864945389161",
	"location" : [
		"40.58654",
		"-122.39168",
		"Redding",
		"US",
		"America/Los_Angeles"
	],
	"vehicle" : {
		"Year" : 2009,
		"Make" : "Bentley",
		"Model" : "Arnage",
		"Category" : "Sedan"
	},
	"plate" : "654 IHO"
}
```

**NoSQL API Example:**

Notice the several underscored attributes added by Cosmos DB (_rid, _etag, _ts, etc).
The _etag is for OCC (Optimistic Concurrency Control)

```
{
    "id": "ac3a01a0-a52d-480d-bd3f-128b0fb894e4",
    "pk": "movie_seed",
    "doctype": "movie_seed",
    "targetId": "tt11778084",
    "targetPk": "tt11778084",
    "adjacentVertices": [
        "nm3645961"
    ],
    "_rid": "gm8hAP8qSX0CAAAAAAAAAA==",
    "_self": "dbs/gm8hAA==/colls/gm8hAP8qSX0=/docs/gm8hAP8qSX0CAAAAAAAAAA==/",
    "_etag": "\"03000fe3-0000-0100-0000-63e186160000\"",
    "_attachments": "attachments/",
    "_ts": 1675724310
}
```

See https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/database-transactions-optimistic-concurrency

---
---

#### Cosmos DB Conflict Resolution (NoSQL API)

**This concept only applies to multi-region multi-writable Cosmos DB accounts**.

> With multi-region writes, when multiple clients write to the same item, conflicts may occur.
>
> When a conflict occurs, you can resolve the conflict by using different conflict resolution policies.
>
> **The default path for last-writer-wins is the timestamp field or the _ts property.**

<p align="center">
    <img src="../img/managing-conflicts.jpeg" width="40%">
</p>

**Custom Resolution Policy in Azure Portal (NoSQL API)**

<p align="center">
    <img src="../img/conflict-resolution-tip.png" width="60%">
</p>

<p align="center">
    <img src="../img/conflict-resolution-in-portal.png" width="60%">
</p>

**Custom Resolution Policy Example (NoSQL API)**

<p align="center">
    <img src="../img/conflict-resolution-lww.png" width="90%">
</p>

- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/how-to-manage-conflicts


---

### Multi-Homing and Connecting to a preferred region

This topic is discussed in another training session

---

## Further Reading

Please see the **Azure Cosmos DB for MongoDB documentation** at https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/

---
---

<p align="center">
    <img src="../img/spacer-500.png" width="90%">
</p>

<p align="center">
    <img src="../img/questions.png" width="90%">
</p>

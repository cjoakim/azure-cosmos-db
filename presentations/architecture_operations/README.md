# Azure Cosmos DB : Architecture and Operations

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

This presentation: https://github.com/cjoakim/azure-cosmos-db/tree/main/presentations/architecture_operations

---

## Questions for the Audience

- What are the IT Roles in the Audience?  (i.e. - DBAs, DevOps, Developers, Data Engineers, Data Scientists, Managers, etc.)
- Your experience with Azure, and Cosmos DB?
- Which Cosmos DB API(s) are of interest?
- Preferred Programming Languages and Frameworks?  (i.e. - C#, Java, Python, Node.js, Go, EF, Spring, Spring Data)
- Preferred Automation Tools?  (i.e. - ARM, Bicep, Terraform, PowerShell, az, bash)

---

## APIs, Features, Pricing, Architecture

- **Why Cosmos DB?**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/introduction#key-benefits

- **Multi-Modal Cloud-based Database Service**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/introduction

  - A family of primarily NoSQL databases
    - **APIs** - NoSQL*, Mongo, Cassandra, Gremlin, Distributed PostgreSQL
    - When you create a Cosmos DB Account it is only ONE of these APIs, not ALL
    - * = this name has evolved; previously Document DB, and SQL

  - It's a PaaS service (Platform-as-a-Service)
    - Microsoft manages the physical and network infrastructure
    - The database itself is self-managing in some ways
    - You can't "ssh into" Cosmos DB or access any underlying servers

- **Primary Price Components**
  - https://azure.microsoft.com/en-us/pricing/details/cosmos-db/autoscale-provisioned/
  - Throughput (Request Units)
  - Storage
  - Number of Regions
    - https://learn.microsoft.com/en-us/azure/cosmos-db/distribute-data-globally
    - Regional egress/networking cost

- **Request Units or RUs - the Unit of Throughput in Cosmos DB**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/request-units
  - Almost infinite scale from very small to extremely large in small 100 RU increments
  - 1.0 RU = the cost to read a 1 KB document by its' ID and Partition Key (i.e. - a point read)
  - Approx 5.0 RUs to write a 1 KB document
  - RU costs go up based on the size of the documents.  Proportional, not linear
  - What does a throughput of 400 RU mean exactly?
    - Think of RUs as a "per second budget of throughput"
    - So, 400 x 1 KB point-reads in the same second
    - Cosmos DB will return an error code of **429** if you exceed your RU throughput

- **Provisioning** 
  - **Serverless**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/serverless 
    - https://learn.microsoft.com/en-us/azure/cosmos-db/serverless-performance
    - Low-volume, intermittent traffic
    - Excellent for Developer or Test envronments
    - Pay per use
  - **Provisioned Throughput**
    - Used by most production workloads
    - Manual or Autoscale Throughput
    - Container-level or Database-Level Throughput
      - 25 containers or less for DB-level Throughput
    - Small to virtually unlimited throughput
      - Manual starts at 400 RU
      - Autoscale starts at 1000 RU, scales down to 10% of max
        - https://learn.microsoft.com/en-us/azure/cosmos-db/autoscale-faq
        - Scales up to Max immediately
        - Billed at highest rate per hour
        - Autoscale costs 50% more per RU, but pays for itself in most cases


- **Partitioning and Horizontal Scaling**

  - **Cosmos DB uses a Partition Key to Horizontally Distrubute the data in a Container**
    - Also known as "sharding"
    - The Partition Key attribute must be defined at time of Container creation
    - Every document must have the partition key attribute populated
    - Using /pk is a best-practice so that your schema can evolve
    - **The choice of the partition key is critical for Cosmos DB performance and costs**
    - **Let me help your team with your initial Cosmos DB designs**

  - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview


  - **Physical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#physical-partitions
    - Up to 10,000 RU of performance and 50 GB of data
    - These are created by:
      - Explicit scaling.  For example, 100,000 RU causes 10 physical partitions
      - Organic Growth of Data.  As data in a container approaches 50 GB, with data shuffling
      - Zero downtime or performance impact during partition key scaling

  - **Logical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#logical-partitions
    - Logical Partitions are the set of all documents with the same partition key in a container
    - Be aware of a 20 GB limit per Logical Partition

- **SDKs**
  - For the NoSQL and Table APIs use SDKs created by Microsoft
    - Excellent SDK features:
      - Retry n-times when encountering 429 errors
      - Regional "auto homing"
  - For the other APIs (Mongo, Gremlin, Cassandra, PostgreSQL) use the open-source SDKs

- **Direct Mode and Gateway Mode**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/sdk-connection-modes
  - Gateway uses HTTP protocol, Direct uses TCP
  - DotNet and Java NoSQL clients can use Direct Mode 
    - Requests are routed directly to the appropriate physical partitions
    - DotNet and Java SDK clients for Cosmos DB NoSQL API and direct mode

- **Time-to-Live, or TTL**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/time-to-live
  - Free Auto-deletion using unused RUs
  - 1-second granularity
  - Automatically prune your "operational data"

- **Synapse Link Integration**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/synapse-link
  - https://github.com/cjoakim/azure-cosmosdb-synapse-link
  - Implements the **HTAP pattern - hybrid transactional and analytical processing** 
  - Cosmos DB is an "operational database", not a "datalake"

- **ADX Integration - Preview**
  - https://learn.microsoft.com/en-us/azure/data-explorer/ingest-data-cosmos-db-connection?tabs=portal

- **Change Feed**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/change-feed
  - Persistence sequential record of individual document changes
  - Consumed with an Azure Function, or DotNet/Java SDK client program

- **Integrated Cache**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/integrated-cache
  - Optional Feature
  - Item and Query Caches using Gateway Mode

- **Consistency Levels**
    - Eventual ... Session ... String
    - https://learn.microsoft.com/en-us/azure/cosmos-db/consistency-levels

- **Other Topics and Links**
  - Documentation: https://learn.microsoft.com/en-us/azure/cosmos-db/
  - Resource Model: https://learn.microsoft.com/en-us/azure/cosmos-db/resource-model
  - Dharma Shukla, Creator, 2017: https://azure.microsoft.com/en-us/blog/a-technical-overview-of-azure-cosmos-db/ 

---

## Operations, HA/DR

- **Backups: Periodic or Continuous**

  - **Periodic**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/periodic-backup-restore-introduction
      - You configure the backup interval and retention
      - Geo-redundant Storage (GRS)
  
  - **Continuous**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/continuous-backup-restore-introduction
      - One-second granularity PITR

  - Restores to another Cosmos DB account
    - Restore into same account is in preview mode 
      - https://learn.microsoft.com/en-us/azure/cosmos-db/restore-in-account-continuous-backup-introduction

- **High-Availability with Regional Failover**
  - 99.99% availability for single region account
  - 99.995% availability for single region account with availability zones
  - 99.999% availability for multiple region account
  - 

---

## Best Practices

- **Use Azure Monitor**

- **Use Azure Monitor Alerts**

- **Periodic/Weekly review of most expensive database operations in RUs**

- **Periodic/Weekly review of largest logical partitions**
  - 20 GB Logical Partition Limit

- **Periodic/Weekly review of container sizes**
  - Organic growth of data without the corresponding RU increase

- **Scale up slowly**
  - Limit of 10,000 RUs per Physical Partition 
  - Partition Merge is in Preview
    - https://learn.microsoft.com/en-us/azure/cosmos-db/merge?tabs=azure-powershell%2Cnosql


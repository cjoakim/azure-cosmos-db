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

  - A family of primarily NoSQL databases
    - **APIs** - NoSQL*, Mongo, Cassandra, Gremlin, Distributed PostgreSQL
    - When you create a Cosmos DB Account it is only ONE of these APIs, not ALL
    - * = this name has evolved; previously Document DB, and SQL

  - It's a PaaS service (Platform-as-a-Service)
    - Microsoft manages the physical and network infrastructure
    - The database itself is self-managing in some ways
    - You can't "ssh into" Cosmos DB or access any underlying servers

- **Primary Price Components**

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
    - Cosmos DB will return an error code of 429 if you exceed your RU throughput

- **Provisioning** 

  - Serverless
    - https://learn.microsoft.com/en-us/azure/cosmos-db/serverless 
    - https://learn.microsoft.com/en-us/azure/cosmos-db/serverless-performance
    - Low-volume, intermittent traffic
    - Excellent for Developer or Test envronments
    - Pay per use

  - Provisioned Throughput
    - Manual or Autoscale Throughput
    - Container-level or Database-Level Throughput
      - 25 containers or less for DB-level Throughput
    - Small to virtually unlimited throughput
      - Manual starts at 400 RU
      - Autoscale starts at 4000 RU, scales down to 10% of max
        - Autoscale costs 50% more per RU, but pays for itself in most cases


- **Partitioning and Horizontal Scaling**

  - **Cosmos DB uses a Partition Key to Horizontally Distrubute the data in a Container**
    - Also known as "sharding"
    - The Partition Key attribute must be defined at time of Container creation
    - Every document must have the partition key attribute populated
    - Using /pk is a best-practice so that your schema can evolve
    - **The choice of the partition key is critical for Cosmos DB performance and costs**

  - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview
  - **Logical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#logical-partitions
  - **Physical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#physical-partitions

- **Consistency Levels**
    - Eventual ... Session ... String
    - https://learn.microsoft.com/en-us/azure/cosmos-db/consistency-levels

- **Other Topics and Links**
  - Resource Model: https://learn.microsoft.com/en-us/azure/cosmos-db/resource-model
  - Dharma Shukla, Creator, 2017: https://azure.microsoft.com/en-us/blog/a-technical-overview-of-azure-cosmos-db/ 

## Operations and Best Practices



---

<p align="center">
    <img src="../img/cosmosdb-mongo-api-benefits.png" width="90%">
</p>


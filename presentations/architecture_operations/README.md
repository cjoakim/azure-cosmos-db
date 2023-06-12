# Azure Cosmos DB : Architecture and Operations

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

This presentation: https://github.com/cjoakim/azure-cosmos-db/tree/main/presentations/architecture_operations

---

## Questions for the Audience

- Audience?
- Experience with Azure, and Cosmos DB?
- Which Cosmos DB API(s) are of interest?
- Preferred Programming Languages and Frameworks?  (i.e. - C#, Java, Python, Node.js, Go, EF, Spring, Spring Data)
- Preferred Automation Tools?  (i.e. - ARM, Bicep, Terraform, PowerShell, az, bash)

---

## Architecture, Features, Pricing

- **Multi-Modal Cloud-based Database Service**
  - It's a PaaS service (Platform-as-a-Service)
    - Microsoft manages the physical and network infrastructure
    - The database itself is self-managing in some ways
    - You can't "ssh into" Cosmos DB or access any underlying servers

  - Primarily NoSQL - the NoSQL*, Mongo, Cassandra, and Gremlin APIs
  - Also new Cosmos DB for PostgreSQL - distributed relational

  - * = this name has evolved; previously Document DB, and SQL

- **Primary Price Components**
  - Throughput (Request Units)
  - Storage
  - Number of Regions
  - Consistency Level

- **Request Units, RUs - the unit of throughput in Cosmos DB**
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
  - "Serverless" or "Provisioned Throughput"
    - Serverless -  



- **Partitioning and Horizontal Scaling**
  - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview
  - **Logical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#logical-partitions
  - **Physical Partitions**
    - https://learn.microsoft.com/en-us/azure/cosmos-db/partitioning-overview#physical-partitions

- **Other Topics and Links**
  - Resource Model: https://learn.microsoft.com/en-us/azure/cosmos-db/resource-model
  - Dharma Shukla, Creator, 2017: https://azure.microsoft.com/en-us/blog/a-technical-overview-of-azure-cosmos-db/ 

## Operations and Best Practices



---

<p align="center">
    <img src="../img/cosmosdb-mongo-api-benefits.png" width="90%">
</p>


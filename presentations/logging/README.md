# Azure Cosmos DB : Logging

**Chris Joakim, Microsoft, Cosmos DB Global Back Belt (GBB)**

This presentation: https://github.com/cjoakim/azure-cosmos-db/tree/main/presentations/logging

---

## Links

### SDK Diagnostics

- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/troubleshoot-java-sdk-v4?tabs=sync#capture-the-diagnostics
- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/troubleshoot-dotnet-sdk#capture-diagnostics
- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/index-metrics

### Azure Monitor

- https://learn.microsoft.com/en-us/azure/cosmos-db/monitor?tabs=azure-diagnostics
- https://learn.microsoft.com/en-us/azure/cosmos-db/monitor-resource-logs?tabs=azure-portal
- https://learn.microsoft.com/en-us/azure/azure-monitor/essentials/monitor-azure-resource
- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/diagnostic-queries?tabs=resource-specific

---

### Sample Kusto Queries

#### Top N(10) queries ordered by Request Unit (RU) consumption in a specific time frame

```
let topRequestsByRUcharge = CDBDataPlaneRequests 
| where TimeGenerated > ago(24h)
| project  RequestCharge , TimeGenerated, ActivityId;
CDBQueryRuntimeStatistics
| project QueryText, ActivityId, DatabaseName , CollectionName
| join kind=inner topRequestsByRUcharge on ActivityId
| project DatabaseName , CollectionName , QueryText , RequestCharge, TimeGenerated
| order by RequestCharge desc
| take 10
```

#### Requests throttled (statusCode = 429) in a specific time window

```
let throttledRequests = CDBDataPlaneRequests
| where StatusCode == "429"
| project  OperationName , TimeGenerated, ActivityId;
CDBQueryRuntimeStatistics
| project QueryText, ActivityId, DatabaseName , CollectionName
| join kind=inner throttledRequests on ActivityId
| project DatabaseName , CollectionName , QueryText , OperationName, TimeGenerated
```

#### RU consumption by physical partition (across all replicas in the replica set)

```
CDBPartitionKeyRUConsumption
| where TimeGenerated >= now(-1d)
//specify collection and database
//| where DatabaseName == "DBNAME" and CollectionName == "COLLECTIONNAME"
// filter by operation type
//| where operationType_s == 'Create'
| summarize sum(todouble(RequestCharge)) by toint(PartitionKeyRangeId)
| render columnchart
```


# readme for apis/nosql/powershell 

## Links

- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/manage-with-powershell
- https://learn.microsoft.com/en-us/powershell/module/?view=azps-9.3.0
- https://learn.microsoft.com/en-us/powershell/module/az.cosmosdb/?view=azps-9.3.0
- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/powershell-samples
- https://learn.microsoft.com/en-us/powershell/module/az.accounts/connect-azaccount?view=azps-9.3.0

--- 

## Cosmos DB Cmdlets

These cmdlets exist in the **Microsoft.Azure.Commands.CosmosDB** namespace.
See https://learn.microsoft.com/en-us/powershell/module/az.cosmosdb/?view=azps-9.3.0

### Automated List of Cosmos Cmdlets

See **powershell_meta_info.ps1** and these two lines:

```
Get-Command               > tmp\Meta-Get-Command.txt
cat tmp\Meta-Get-Command.txt | grep Cosmos > tmp\Meta-Get-Command-Cosmos.txt
```

#### Output List 

```
Cmdlet          Get-AzCosmosDBAccount                              1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBAccountKey                           1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBCassandraKeyspace                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBCassandraKeyspaceThroughput          1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBCassandraTable                       1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBCassandraTableThroughput             1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBGremlinDatabase                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBGremlinDatabaseThroughput            1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBGremlinGraph                         1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBGremlinGraphThroughput               1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBLocation                             1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBCollection                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBCollectionBackupInformation   1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBCollectionThroughput          1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBDatabase                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBDatabaseThroughput            1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBRestorableCollection          1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBRestorableDatabase            1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBMongoDBRestorableResource            1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBRestorableDatabaseAccount            1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlContainer                         1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlContainerBackupInformation        1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlContainerThroughput               1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlDatabase                          1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlDatabaseThroughput                1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlRestorableContainer               1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlRestorableDatabase                1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlRestorableResource                1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlRoleAssignment                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlRoleDefinition                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlStoredProcedure                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlTrigger                           1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBSqlUserDefinedFunction               1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBTable                                1.4.0      Az.CosmosDB                                                     
Cmdlet          Get-AzCosmosDBTableThroughput                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBCassandraKeyspaceThroughputMig... 1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBCassandraTableThroughputMigration 1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBGremlinDatabaseThroughputMigra... 1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBGremlinGraphThroughputMigration   1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBMongoDBCollectionThroughputMig... 1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBMongoDBDatabaseThroughputMigra... 1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBSqlContainerThroughputMigration   1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBSqlDatabaseThroughputMigration    1.4.0      Az.CosmosDB                                                     
Cmdlet          Invoke-AzCosmosDBTableThroughputMigration          1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBAccount                              1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBAccountKey                           1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBCassandraClusterKey                  1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBCassandraColumn                      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBCassandraKeyspace                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBCassandraSchema                      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBCassandraTable                       1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBDatabaseToRestore                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinCompositePath                 1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinConflictResolutionPolicy      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinDatabase                      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinGraph                         1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinIncludedPath                  1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinIncludedPathIndex             1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinIndexingPolicy                1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinSpatialSpec                   1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinUniqueKey                     1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBGremlinUniqueKeyPolicy               1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBLocationObject                       1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBMongoDBCollection                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBMongoDBDatabase                      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBMongoDBIndex                         1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBPermission                           1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlCompositePath                     1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlConflictResolutionPolicy          1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlContainer                         1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlDatabase                          1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlIncludedPath                      1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlIncludedPathIndex                 1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlIndexingPolicy                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlRoleAssignment                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlRoleDefinition                    1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlSpatialSpec                       1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlStoredProcedure                   1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlTrigger                           1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlUniqueKey                         1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlUniqueKeyPolicy                   1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBSqlUserDefinedFunction               1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBTable                                1.4.0      Az.CosmosDB                                                     
Cmdlet          New-AzCosmosDBVirtualNetworkRule                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBAccount                           1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBCassandraKeyspace                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBCassandraTable                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBGremlinDatabase                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBGremlinGraph                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBMongoDBCollection                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBMongoDBDatabase                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlContainer                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlDatabase                       1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlRoleAssignment                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlRoleDefinition                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlStoredProcedure                1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlTrigger                        1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBSqlUserDefinedFunction            1.4.0      Az.CosmosDB                                                     
Cmdlet          Remove-AzCosmosDBTable                             1.4.0      Az.CosmosDB                                                     
Cmdlet          Restore-AzCosmosDBAccount                          1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBAccount                           1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBAccountFailoverPriority           1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBAccountRegion                     1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBCassandraKeyspace                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBCassandraKeyspaceThroughput       1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBCassandraTable                    1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBCassandraTableThroughput          1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBGremlinDatabase                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBGremlinDatabaseThroughput         1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBGremlinGraph                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBGremlinGraphThroughput            1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBMongoDBCollection                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBMongoDBCollectionThroughput       1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBMongoDBDatabase                   1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBMongoDBDatabaseThroughput         1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlContainer                      1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlContainerThroughput            1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlDatabase                       1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlDatabaseThroughput             1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlRoleAssignment                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlRoleDefinition                 1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlStoredProcedure                1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlTrigger                        1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBSqlUserDefinedFunction            1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBTable                             1.4.0      Az.CosmosDB                                                     
Cmdlet          Update-AzCosmosDBTableThroughput                   1.4.0      Az.CosmosDB                     
```

## Warning Message

```
> New-AzCosmosDBAccount
WARNING: Both Az and AzureRM modules were detected on this machine. Az and AzureRM modules cannot be imported in the same session or used in
the same script or runbook. If you are running PowerShell in an environment you control you can use the 'Uninstall-AzureRm' cmdlet to remove
all AzureRm modules from your machine. If you are running in Azure Automation, take care that none of your runbooks import both Az and AzureRM
 modules. More information can be found here: https://aka.ms/azps-migration-guide
```

---

## General PowerShell

See **powershell_meta_info.ps1** and these two lines:


```
> $PSVersionTable.PSVersion

Major  Minor  Build  Revision
-----  -----  -----  --------
5      1      22621  963

---

> $psversiontable

Name                           Value
----                           -----
PSVersion                      5.1.22621.963
PSEdition                      Desktop
PSCompatibleVersions           {1.0, 2.0, 3.0, 4.0...}
BuildVersion                   10.0.22621.963
CLRVersion                     4.0.30319.42000
WSManStackVersion              3.0
PSRemotingProtocolVersion      2.3
SerializationVersion           1.1.0.1
```
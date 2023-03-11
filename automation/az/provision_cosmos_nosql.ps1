# Provision a Cosmos DB NoSQL account with containers.
# Chris Joakim, Microsoft

.\env.ps1

echo 'deleting account...'
az cosmosdb delete `
    --name           $Env:nosql_acct_name `
    --resource-group $Env:cosmos_rg `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --yes `

echo 'creating account...'
az cosmosdb create `
    --name           $Env:nosql_acct_name `
    --resource-group $Env:cosmos_rg `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --kind           $Env:nosql_acct_kind `
    --locations regionName=$Env:primary_region failoverPriority=0 isZoneRedundant=False `
    --default-consistency-level $Env:nosql_acct_consistency `
    --enable-multiple-write-locations true `
    --enable-analytical-storage true `
    --backup-interval 720 `
    --backup-retention 24

echo 'creating dev database...'
az cosmosdb sql database create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --name           dev

echo 'creating retail database...'
az cosmosdb sql database create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --name           retail

# containers: npm_graph, imdb_graph, imdb_seed, gpoc, telemetry, test

echo 'creating containers in dev database...'

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           npm_graph `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           imdb_graph `
    --max-throughput 10000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           imdb_seed `
    --max-throughput 10000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           gpoc `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           telemetry `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           test `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl `
    --ttl 86400
    # 60 * 60 * 24 * 1 = 86400 = 1 day

echo 'creating containers in retail database...'

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  retail `
    --name           sales `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

az cosmosdb sql container create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  retail `
    --name           sales_aggregates `
    --max-throughput $Env:nosql_default_ru `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:nosql_default_pk `
    --analytical-storage-ttl $Env:nosql_olap_ttl

echo 'done'

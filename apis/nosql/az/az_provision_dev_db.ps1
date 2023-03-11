# Provision the dev database with a Cosmos DB NoSQL Account,
# including several containers.
# Chris Joakim, Microsoft

.\az_env.ps1

echo 'az cosmosdb sql database create: dev ...'
az cosmosdb sql database create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --name           dev

# containers: altgraph, npm_graph, imdb_graph, imdb_seed, gpoc, telemetry

echo 'az cosmosdb sql container create: altgraph ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           altgraph `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'az cosmosdb sql container create: npm_graph ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           npm_graph `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'az cosmosdb sql container create: imdb_graph ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           imdb_graph `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'az cosmosdb sql container create: imdb_seed ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           imdb_seed `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'az cosmosdb sql container create: gpoc ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           gpoc `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'az cosmosdb sql container create: telemetry ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           telemetry `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl

echo 'done'

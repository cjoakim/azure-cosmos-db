# Provision the sales database with a Cosmos DB NoSQL Account,
# including several containers.
# Chris Joakim, Microsoft

.\az_env.ps1

echo 'az cosmosdb sql database create: sales ...'
az cosmosdb sql database create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --name           sales `
    > tmp/cosmos_sql_sales_db_create.json

# containers: sales, sales_aggregates

echo 'az cosmosdb sql container create: sales ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  sales `
    --name           sales `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl `
    > tmp/cosmos_sql_container_sales_create.json

echo 'az cosmosdb sql container create: sales_aggregates ...'
az cosmosdb sql container create `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  sales `
    --name           sales_aggregates `
    --max-throughput 4000 `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --partition-key-path     $Env:cosmos_sql_default_pk `
    --analytical-storage-ttl $Env:cosmos_sql_synapse_ttl `
    > tmp/cosmos_sql_container_sales_aggregates_create.json

echo 'done'

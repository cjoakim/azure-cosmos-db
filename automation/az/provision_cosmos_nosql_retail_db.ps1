# Provision the retail database within the Cosmos DB NoSQL account
# Chris Joakim, Microsoft

.\env.ps1

echo 'creating retail database...'
az cosmosdb sql database create `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --name           retail

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

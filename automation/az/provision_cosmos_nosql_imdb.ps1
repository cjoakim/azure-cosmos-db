# Delete and recreate the imdb containers.
# Chris Joakim, Microsoft

.\env.ps1

echo 'deleting containers...'

az cosmosdb sql container delete `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           imdb_graph `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --yes

az cosmosdb sql container delete `
    --resource-group $Env:cosmos_rg `
    --account-name   $Env:nosql_acct_name `
    --database-name  dev `
    --name           imdb_seed `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --yes

echo 'creating containers...'

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

echo 'done'

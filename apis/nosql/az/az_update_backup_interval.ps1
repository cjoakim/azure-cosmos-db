# Update the backup interval for a Cosmos DB NoSQL Account.
# Chris Joakim, Microsoft

.\az_env.ps1

echo 'az cosmos update ...'
az cosmosdb update `
    --name $Env:cosmos_sql_acct_name `
    --resource-group $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    --backup-interval 720 `
    --backup-retention 24 `
    > tmp/cosmos_nosql_update.json

echo 'az cosmos show ...'
az cosmosdb show `
    --name $Env:cosmos_sql_acct_name `
    --resource-group $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    > tmp/cosmos_nosql_show.json

echo 'done'

# Update the backup interval for a Cosmos DB NoSQL Account.
# Chris Joakim, Microsoft

.\az_env.ps1

echo 'az cosmosdb sql container update: telemetry ...'
az cosmosdb sql container update `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           telemetry `
    --ttl            172800   

# >>> 60 * 60 * 48 -> 172800 (48 hours, in seconds)

echo 'az cosmosdb sql container show: telemetry ...'
az cosmosdb sql container show `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  dev `
    --name           telemetry

echo 'done'

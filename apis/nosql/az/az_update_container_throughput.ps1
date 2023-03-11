# Update the autoscale RU setting for a container.
# Chris Joakim, Microsoft

.\az_env.ps1

# Configuration parameters for this ad-hoc script
$db='dev'
$container='altgraph'
$ru='5000'

Write-Host ("db:        {0}" -f $db)
Write-Host ("container: {0}" -f $container)
Write-Host ("ru:        {0}" -f $ru)

echo 'az cosmosdb sql container throughput update ...'
az cosmosdb sql container throughput update `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  $db `
    --name           $container `
    --max-throughput $ru

echo 'az cosmosdb sql container show ...'
az cosmosdb sql container show `
    --resource-group $Env:cosmos_sql_rg `
    --account-name   $Env:cosmos_sql_acct_name `
    --database-name  $db `
    --name           $container

echo 'done'

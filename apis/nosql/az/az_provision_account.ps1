# Provision a Cosmos DB NoSQL Account with the az CLI.
# Chris Joakim, Microsoft

.\az_env.ps1

echo 'az group create ...'
az group create `
    --location $Env:cosmos_sql_region `
    --name $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    > tmp/group_create.json

echo 'az cosmos create ...'
az cosmosdb create `
    --name $Env:cosmos_sql_acct_name `
    --resource-group $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    --kind $Env:cosmos_sql_acct_kind `
    --locations regionName=$Env:cosmos_sql_region failoverPriority=0 isZoneRedundant=False `
    --default-consistency-level $Env:cosmos_sql_acct_consistency `
    --enable-multiple-write-locations true `
    --enable-analytical-storage true `
    --backup-interval 360 `
    --backup-retention 12 `
    > tmp/cosmos_nosql_create.json

#   --backup-interval is in minutes, --backup-retention is in hours, default is 240/8
#   --continuous-tier "Continuous7Days" `   <-- obsolete 
#   --backup-policy-type Continuous `
#   ERROR: (BadRequest) Continuous backup mode cannot be enabled together with Storage Analytics feature.

echo 'az cosmos show ...'
az cosmosdb show `
    --name $Env:cosmos_sql_acct_name `
    --resource-group $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    > tmp/cosmos_nosql_show.json

echo 'done'

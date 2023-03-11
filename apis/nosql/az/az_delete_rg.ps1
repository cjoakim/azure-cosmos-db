# Script to delete the Azure Resource Group where the Cosmos DB
# account resides.
# Chris Joakim, Microsoft

.\az_env.ps1

Write-Host ("deleting resource group: {0}" -f $Env:cosmos_sql_rg)

az group delete `
    --name $Env:cosmos_sql_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    --yes `
    --no-wait `
    > tmp/group_delete.json

echo 'done'

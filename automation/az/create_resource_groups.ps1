# Create the several resource groups.
# Chris Joakim, Microsoft

.\env.ps1

Write-Host ("creating resource group: {0}" -f $Env:core_rg)
az group create `
    --location     $Env:primary_region `
    --name         $Env:core_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID

Write-Host ("creating resource group: {0}" -f $Env:cosmos_rg)
az group create `
    --location     $Env:primary_region `
    --name         $Env:cosmos_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID

Write-Host ("creating resource group: {0}" -f $Env:apps_rg)
az group create `
    --location     $Env:primary_region `
    --name         $Env:apps_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID

Write-Host ("creating resource group: {0}" -f $Env:search_rg)
az group create `
    --location     $Env:primary_region `
    --name         $Env:search_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID

Write-Host ("creating resource group: {0}" -f $Env:synapse_rg)
az group create `
    --location     $Env:primary_region `
    --name         $Env:synapse_rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID

echo 'done'

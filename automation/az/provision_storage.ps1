# Provision Azure Storage.
# Chris Joakim, Microsoft

.\env.ps1

az storage account create `
    --location       $Env:primary_region `
    --name           $Env:storage_name `
    --resource-group $Env:core_rg `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID `
    --sku            Standard_LRS

echo 'done'

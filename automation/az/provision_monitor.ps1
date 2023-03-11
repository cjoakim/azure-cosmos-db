# Provision Azure Monitor.
# Chris Joakim, Microsoft

.\env.ps1

az monitor log-analytics workspace create `
    --location       $Env:primary_region `
    --workspace-name $Env:monitor_name `
    --resource-group $Env:core_rg `
    --subscription   $Env:AZURE_SUBSCRIPTION_ID

echo 'done'

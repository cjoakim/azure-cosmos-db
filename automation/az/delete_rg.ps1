# Delete the Resource Group specified as the CLI arg.
# Chris Joakim, Microsoft

.\env.ps1

$rg=$args[0]

Write-Host ("deleting resource group: {0}" -f $rg)

az group delete `
    --name $rg `
    --subscription $Env:AZURE_SUBSCRIPTION_ID `
    --yes `
    --no-wait

echo 'done'

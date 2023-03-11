# PowerShell script to get info for a given Cosmos DB account, as both text and json.
# Chris Joakim, Microsoft

.\env.ps1

Get-AzCosmosDBAccount `
    -ResourceGroupName $Env:cosmos_sql_rg `
    -Name $Env:cosmos_sql_acct_name `
    > tmp/Get-AzCosmosDBAccount.txt

Get-AzCosmosDBAccount `
    -ResourceGroupName $Env:cosmos_sql_rg `
    -Name $Env:cosmos_sql_acct_name `
    | ConvertTo-Json `
    > tmp/Get-AzCosmosDBAccount.json

echo 'done'

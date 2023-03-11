# PowerShell script to get info for a given Cosmos DB account.
# Chris Joakim, Microsoft

.\env.ps1

echo 'redirecting help content to tmp/ files ...'

Get-Help Connect-AzAccount         > tmp/Help-Connect-AzAccount.txt
Get-Help Get-AzCosmosDBAccount     > tmp/Help-AzCosmosDBAccount.txt
Get-Help Update-AzCosmosDBAccount  > tmp/Help-Update-AzCosmosDBAccount.txt
Get-Help Get-AzCosmosDBSqlContainerBackupInformation > tmp/Help-Get-AzCosmosDBSqlContainerBackupInformation.txt

echo 'done'

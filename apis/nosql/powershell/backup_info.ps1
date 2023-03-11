# Get-AzCosmosDBSqlContainerBackupInformation
# Chris Joakim, Microsoft

.\env.ps1

echo 'Get-AzCosmosDBSqlContainerBackupInformation ...'

Get-AzCosmosDBSqlContainerBackupInformation `
    -ResourceGroupName $Env:cosmos_sql_rg `
    -AccountName $Env:cosmos_sql_acct_name `
    -Location     $Env:cosmos_sql_region `
    -DatabaseName sales `
    -Name         sales

# Error message:
# Get-AzCosmosDBSqlContainerBackupInformation : DatabaseAccount gbbchriscosmos for subscription 
# <cj-scrubbed> doesn't have Point in time restore feature enabled.

echo 'done'

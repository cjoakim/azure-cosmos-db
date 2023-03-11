# PowerShell script to get info for a given Cosmos DB account.
# Chris Joakim, Microsoft

.\env.ps1

echo 'Update-AzCosmosDBAccount ...'
Update-AzCosmosDBAccount `
    -ResourceGroupName $Env:cosmos_sql_rg `
    -Name $Env:cosmos_sql_acct_name `
    -BackupIntervalInMinutes 720 `
    -BackupRetentionIntervalInHours 24 `
    > tmp/Get-AzCosmosDBAccount.txt

# WARNING: Invalid value for AnalyticalStorageSchemaType.  Valid values are 'WellDefined' and 'FullFidelity'.

#  600 minutes -> 20 hours
#  720 minutes -> 24 hours
# 1440 minutes -> 720 hours

echo 'done'

# Get-AzCosmosDBSqlContainerPerPartitionThroughput.
# See https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/distribute-throughput-across-partitions
# See https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/distribute-throughput-across-partitions#step-1-identify-which-physical-partitions-need-more-throughput
# See https://learn.microsoft.com/en-us/azure/azure-resource-manager/management/preview-features?tabs=azure-powershell
# Chris Joakim, Microsoft

.\env.ps1

# Get-AzProviderFeature -ListAvailable | grep ThroughputRedistributionAcrossPartitions
# ThroughputRedistributionAcrossPartitions

# Register-AzProviderFeature -FeatureName "ThroughputRedistributionAcrossPartitions" -ProviderNamespace "Microsoft.DocumentDB"
#
# FeatureName                              ProviderName         RegistrationState
# -----------                              ------------         -----------------
# ThroughputRedistributionAcrossPartitions Microsoft.DocumentDB Pending

Get-AzCosmosDBSqlContainerPerPartitionThroughput `
    -ResourceGroupName $Env:cosmos_sql_rg `
    -AccountName $Env:cosmos_sql_acct_name `
    -DatabaseName dev `
    -Name         telemetry `
    -AllPartitions

echo 'done'

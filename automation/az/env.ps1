# PowerShell script to set environment variables for provisioning.
# Chris Joakim, Microsoft

Write-Host ("env.ps1 setting the following enfironment variables...")

# Regions
$Env:primary_region="eastus"
Write-Host ("  primary_region:           {0}" -f $Env:primary_region)

# Resource Groups
$Env:core_rg="gbbcjcore"
$Env:cosmos_rg="gbbcjcosmos"
$Env:apps_rg="gbbcjapps"
$Env:search_rg="gbbcjsearch"
$Env:synapse_rg="gbbcjsynapse"
Write-Host ("  core_rg:                  {0}" -f $Env:core_rg)
Write-Host ("  cosmos_rg:                {0}" -f $Env:cosmos_rg)
Write-Host ("  apps_rg:                  {0}" -f $Env:apps_rg)
Write-Host ("  search_rg:                {0}" -f $Env:search_rg)
Write-Host ("  synapse_rg:               {0}" -f $Env:synapse_rg)
Write-Host ("-")

$Env:monitor_name="gbbcjmonitor"
$Env:storage_name="gbbcjstorage"
Write-Host ("  monitor_name:             {0}" -f $Env:monitor_name)
Write-Host ("  storage_name:             {0}" -f $Env:storage_name)
Write-Host ("-")

$Env:nosql_acct_name="gbbcjcdbnosql"
$Env:nosql_acct_consistency="Session"    # {BoundedStaleness, ConsistentPrefix, Eventual, Session, Strong}
$Env:nosql_acct_kind="GlobalDocumentDB"  # {GlobalDocumentDB, MongoDB, Parse}
$Env:nosql_dev_db="dev"
$Env:nosql_sales_db="sales"
$Env:nosql_default_pk="/pk"
$Env:nosql_default_ru="4000"
$Env:nosql_oltp_ttl="7776000"            # 90-days, in seconds (60 * 60 * 24 * 30)
$Env:nosql_olap_ttl="220903200"          # 7-years, in seconds (60 * 60 * 24 * 365.25 * 7)
Write-Host ("  nosql_acct_name:          {0}" -f $Env:nosql_acct_name)
Write-Host ("  nosql_acct_consistency:   {0}" -f $Env:nosql_acct_consistency)
Write-Host ("  nosql_acct_kind:          {0}" -f $Env:nosql_acct_kind)
Write-Host ("  nosql_dev_db:             {0}" -f $Env:nosql_dev_db)
Write-Host ("  nosql_sales_db:           {0}" -f $Env:nosql_sales_db)
Write-Host ("  nosql_default_pk:         {0}" -f $Env:nosql_default_pk)
Write-Host ("  nosql_default_ru:         {0}" -f $Env:nosql_default_ru)
Write-Host ("  nosql_oltp_ttl:           {0}" -f $Env:nosql_oltp_ttl)
Write-Host ("  nosql_olap_ttl:           {0}" -f $Env:nosql_olap_ttl)
Write-Host ("-")
$Env:synapse_acct_name="gbbcjsynapse"
$Env:synapse_stor_kind="StorageV2"        # {BlobStorage, BlockBlobStorage, FileStorage, Storage, StorageV2}]
$Env:synapse_stor_sku="Standard_LRS"      # {Premium_LRS, Premium_ZRS, Standard_GRS, Standard_GZRS, , Standard_RAGRS, Standard_RAGZRS, Standard_ZRS]
$Env:synapse_fs_name="gbbcjsynapsefs"
$Env:synapse_spark_version="3.2"
$Env:synapse_spark_pool_name="sparkpool3m"
$Env:synapse_spark_pool_count="3"
$Env:synapse_spark_pool_size="Medium"    # The node size. Allowed values: Large, Medium, Small
Write-Host ("  synapse_acct_name:        {0}" -f $Env:synapse_acct_name)
Write-Host ("  synapse_stor_kind:        {0}" -f $Env:synapse_stor_kind)
Write-Host ("  synapse_stor_sku:         {0}" -f $Env:synapse_stor_sku)
Write-Host ("  synapse_fs_name:          {0}" -f $Env:synapse_fs_name)
Write-Host ("  synapse_spark_version:    {0}" -f $Env:synapse_spark_version)
Write-Host ("  synapse_spark_pool_name:  {0}" -f $Env:synapse_spark_pool_name)
Write-Host ("  synapse_spark_pool_count: {0}" -f $Env:synapse_spark_pool_count)
Write-Host ("  synapse_spark_pool_size:  {0}" -f $Env:synapse_spark_pool_size)

Write-Host ("---")

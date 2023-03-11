# PowerShell script to set environment variables for provisioning.
# Chris Joakim, Microsoft

$Env:cosmos_sql_rg="gbbchriscosmos"
$Env:cosmos_sql_region="eastus"
$Env:cosmos_sql_acct_name="gbbchriscosmos"
$Env:cosmos_sql_acct_consistency="Session"    # {BoundedStaleness, ConsistentPrefix, Eventual, Session, Strong}
$Env:cosmos_sql_acct_kind="GlobalDocumentDB"  # {GlobalDocumentDB, MongoDB, Parse}
$Env:cosmos_sql_dev_db="dev"
$Env:cosmos_sql_sales_db="sales"
$Env:cosmos_sql_default_pk="/pk"
$Env:cosmos_sql_default_ru="5000"
$Env:cosmos_sql_default_ttl="2592000"         # 30-days, in seconds (60 * 60 * 24 * 30)
$Env:cosmos_sql_synapse_ttl="220903200"       # 7-years, in seconds (60 * 60 * 24 * 365.25 * 7)

Write-Host ("az_env set the following:")
Write-Host ("  cosmos_sql_rg:               {0}" -f $Env:cosmos_sql_rg)
Write-Host ("  cosmos_sql_region:           {0}" -f $Env:cosmos_sql_region)
Write-Host ("  cosmos_sql_acct_name:        {0}" -f $Env:cosmos_sql_acct_name)
Write-Host ("  cosmos_sql_acct_consistency: {0}" -f $Env:cosmos_sql_acct_consistency)
Write-Host ("  cosmos_sql_acct_kind:        {0}" -f $Env:cosmos_sql_acct_kind)
Write-Host ("  cosmos_sql_default_pk:       {0}" -f $Env:cosmos_sql_default_pk)
Write-Host ("  cosmos_sql_default_ru:       {0}" -f $Env:cosmos_sql_default_ru)
Write-Host ("  cosmos_sql_default_ttl:      {0}" -f $Env:cosmos_sql_default_ttl)
Write-Host ("  cosmos_sql_synapse_ttl:      {0}" -f $Env:cosmos_sql_synapse_ttl)

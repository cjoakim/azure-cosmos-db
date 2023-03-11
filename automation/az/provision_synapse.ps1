# Provision an Azure Synapse account with related resources.
# Chris Joakim, Microsoft

.\env.ps1

echo 'creating synapse storage (ADL V2) ...'
az storage account create `
    --name           $Env:synapse_acct_name `
    --resource-group $Env:synapse_rg `
    --location       $Env:primary_region `
    --sku            $Env:synapse_stor_sku `
    --kind           $Env:synapse_stor_kind `
    --hns            true

Start-Sleep -s 60

echo 'creating synapse workspace ... '
az synapse workspace create `
    --name            $Env:synapse_acct_name `
    --resource-group  $Env:synapse_rg `
    --location        $Env:primary_region `
    --storage-account $Env:synapse_acct_name `
    --file-system     $Env:synapse_fs_name `
    --sql-admin-login-user     $Env:AZURE_SYNAPSE_USER `
    --sql-admin-login-password $Env:AZURE_SYNAPSE_PASS

Start-Sleep -s 60

echo 'creating synapse workspace firewall-rule ...'
az synapse workspace firewall-rule create `
    --name           allowAll `
    --workspace-name $Env:synapse_acct_name `
    --resource-group $Env:synapse_rg `
    --start-ip-address 0.0.0.0 `
    --end-ip-address   255.255.255.255

echo 'creating synapse spark pool ...'
az synapse spark pool create `
    --name           $Env:synapse_spark_pool_name `
    --workspace-name $Env:synapse_acct_name `
    --resource-group $Env:synapse_rg `
    --node-count     $Env:synapse_spark_pool_count `
    --node-size      $Env:synapse_spark_pool_size `
    --spark-version  $Env:synapse_spark_version `
    --enable-auto-pause true `
    --delay             120

echo 'done'

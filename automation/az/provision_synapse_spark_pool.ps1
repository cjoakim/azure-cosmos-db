# Provision an a Spark Pool in Azure Synapse
# Chris Joakim, Microsoft

.\env.ps1

echo 'creating synapse spark pool ...'
az synapse spark pool create `
    --name           $Env:synapse_spark_pool_name `
    --workspace-name $Env:synapse_acct_name `
    --resource-group $Env:synapse_rg `
    --node-count     $Env:synapse_spark_pool_count `
    --node-size      $Env:synapse_spark_pool_size `
    --spark-version  $Env:synapse_spark_version `
    --enable-auto-pause true `
    --delay          15

echo 'done'

# List the roles in the Cosmos DB account.
# Chris Joakim, Microsoft

az cosmosdb sql role definition list `
    --account-name $Env:cosmos_sql_acct_name `
    --resource-group $Env:cosmos_sql_rg `
    > tmp/cosmosdb_sql_role_definition_list.json

cat tmp/cosmosdb_sql_role_definition_list.json | grep roleName

    # "roleName": "Cosmos DB Built-in Data Reader",
    # "roleName": "Cosmos DB Built-in Data Contributor",

echo 'done'

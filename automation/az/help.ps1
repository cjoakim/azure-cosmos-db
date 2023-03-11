# Capture the --help content for selected az commands to tmp/ files.
# Chris Joakim, Microsoft

echo 'redirecting help content to tmp/ files ...'

az group            --help > tmp/help_group.txt
az group create     --help > tmp/help_group_create.txt
az group delete     --help > tmp/help_group_delete.txt

az cosmosdb         --help > tmp/help_cosmosdb.txt
az cosmosdb create  --help > tmp/help_cosmosdb_create.txt
az cosmosdb show    --help > tmp/help_cosmosdb_show.txt
az cosmosdb update  --help > tmp/help_cosmosdb_update.txt

az cosmosdb sql database         --help > tmp/help_cosmosdb_sql_database.txt 
az cosmosdb sql database create  --help > tmp/help_cosmosdb_sql_database_create.txt 
az cosmosdb sql database show    --help > tmp/help_cosmosdb_sql_database_show.txt 

az cosmosdb sql database throughput         --help > tmp/help_cosmosdb_sql_database_throughput.txt
az cosmosdb sql database throughput show    --help > tmp/help_cosmosdb_sql_database_throughput_show.txt
az cosmosdb sql database throughput update  --help > tmp/help_cosmosdb_sql_database_throughput_update.txt
az cosmosdb sql database throughput migrate --help > tmp/help_cosmosdb_sql_database_throughput_migrate.txt

az cosmosdb sql container        --help > tmp/help_cosmosdb_sql_container.txt
az cosmosdb sql container create --help > tmp/help_cosmosdb_sql_container_create.txt
az cosmosdb sql container show   --help > tmp/help_cosmosdb_sql_container_show.txt
az cosmosdb sql container update --help > tmp/help_cosmosdb_sql_container_update.txt

az cosmosdb sql container throughput         --help > tmp/help_cosmosdb_sql_container_throughput.txt
az cosmosdb sql container throughput show    --help > tmp/help_cosmosdb_sql_container_throughput_show.txt
az cosmosdb sql container throughput update  --help > tmp/help_cosmosdb_sql_container_throughput_update.txt
az cosmosdb sql container throughput migrate --help > tmp/help_cosmosdb_sql_container_throughput_migrate.txt

az cosmosdb sql role definition      --help > tmp/help_cosmosdb_sql_role_definition.txt
az cosmosdb sql role definition list --help > tmp/help_cosmosdb_sql_role_definition_list.txt

echo 'done'

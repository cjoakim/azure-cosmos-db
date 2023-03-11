using System;
using System.IO;
using System.Dynamic;
using System.Collections.Generic;
using System.Data.SqlTypes;
using System.Reflection.Metadata.Ecma335;
using System.Text.Json;
using System.Text.Json.Serialization;
using Microsoft.Azure.Functions.Worker;
using Microsoft.Extensions.Logging;
using Microsoft.Azure.Cosmos;
//using Microsoft.Extensions.Configuration;

namespace dotnet
{
    public class CosmosDbNosqlChangeFeed
    {
        private static CosmosClient client = null;
        private static Database  targetDatabase = null;
        private static Container targetContainer = null;
        
        private static string targetDbName = "retail";
        private static string targetContainerName = "sales_by_customer";

        private readonly ILogger _logger;

        private static CosmosClient getCosmosClient()
        {
            if (client == null)
            {
                Console.WriteLine("getCosmosClient() - client is currently null");
                string uri = Environment.GetEnvironmentVariable("AZURE_COSMOSDB_NOSQL_URI");
                string key = Environment.GetEnvironmentVariable("AZURE_COSMOSDB_NOSQL_RW_KEY1");
                Console.WriteLine("uri: " + uri);
                Console.WriteLine("key: " + key);
                CosmosClientOptions options = new CosmosClientOptions {
                    ApplicationName = "NosqlChangeFeed",
                    AllowBulkExecution = false,
                    ConnectionMode = ConnectionMode.Direct,
                    MaxRetryAttemptsOnRateLimitedRequests = 12
                };
                client = new CosmosClient(uri, key, options);
                Console.WriteLine("client: " + client);
                targetDatabase = client.GetDatabase(targetDbName);
                Console.WriteLine("targetDatabase: " + targetDatabase);
                targetContainer = targetDatabase.GetContainer(targetContainerName);
                Console.WriteLine("targetContainer: " + targetContainer);
            }
            return client;
        }
        
        public CosmosDbNosqlChangeFeed(ILoggerFactory loggerFactory)
        {
            _logger = loggerFactory.CreateLogger<CosmosDbNosqlChangeFeed>();
        }

        [Function("CosmosDbNosqlChangeFeed")]
        public void Run([CosmosDBTrigger(
            databaseName: "retail",
            collectionName: "sales",
            ConnectionStringSetting = "AZURE_COSMOSDB_NOSQL_CONN_STRING1",
            LeaseCollectionName = "leases",
            CreateLeaseCollectionIfNotExists = true)] IReadOnlyList<Sale> input) {
            
            if (input != null && input.Count > 0) {
                _logger.LogInformation("Documents modified: " + input.Count);
                getCosmosClient();
                
                var options = new JsonSerializerOptions { WriteIndented = true };
                
                for (int i = 0; i < input.Count; i++)
                {
                    Sale s = input[i];
                    s.transformForTargetContainer();
                    
                    _logger.LogInformation(JsonSerializer.Serialize(s));
                    
                    Task t = targetContainer.CreateItemAsync(s);
                    targetContainer.CreateItemAsync(s);
                    t.Wait();
                }
            }
        }
    }

    public class Sale
    {
        public string id { get; set; }
        public string ref_id { get; set; }
        public string pk { get; set; }
        public int    customer_id { get; set; }
        public string doctype { get; set; }
        public string date { get; set; }
        public int    sale_id { get; set; }
        public int    line_num { get; set; }
        public int    store_id { get; set; }

        public string upc { get; set; }
        public double price { get; set; }
        public int    qty { get; set; }
        public double cost { get; set; }
        
        public void transformForTargetContainer()
        {
            this.ref_id = id;
            this.id = Guid.NewGuid().ToString();
            this.pk = "" + this.customer_id;
            
        }
    }
    
    // The triggering documents look like this
    // {
    //     "pk": "28679",
    //     "id": "54bdc341-6c4e-4e8d-ab7b-74ba60c6d264",
    //     "sale_id": 28679,
    //     "doctype": "line_item",
    //     "date": "2023-01-18",
    //     "line_num": 1,
    //     "customer_id": 6405,
    //     "store_id": 23,
    //     "upc": "1143446970878",
    //     "price": 1480.42,
    //     "qty": 2,
    //     "cost": 2960.84,
    //     "seq": 1
    // }
}

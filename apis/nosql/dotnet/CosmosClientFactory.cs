// Chris Joakim, Microsoft

namespace CosmosNoSQL {
    
    using System;
    using System.Collections.Generic;
    using Microsoft.Azure.Cosmos;

    public class CosmosClientFactory {

        private CosmosClientFactory() {
            // do not use a constructor; use the static methods instead
        }

        public static CosmosClient RegularClient() {
            
            string uri = Config.Singleton().GetCosmosUri();
            string key = Config.Singleton().GetCosmosKey();
            Console.WriteLine($"uri: {uri}");
            Console.WriteLine($"key: {key.Substring(0, 6)}...");
            
            IReadOnlyList<string> prefRegionsList = Config.Singleton().GetCosmosPreferredRegions();

            CosmosClientOptions options = new CosmosClientOptions {
                ApplicationPreferredRegions = prefRegionsList
            };
            return new CosmosClient(uri, key, options);
        }
        
        public static CosmosClient BulkLoadingClient() {
            
            string uri = Config.Singleton().GetCosmosUri();
            string key = Config.Singleton().GetCosmosKey();
            IReadOnlyList<string> prefRegionsList = Config.Singleton().GetCosmosPreferredRegions();
            
            Console.WriteLine($"uri: {uri}");
            Console.WriteLine($"key: {key.Substring(0, 6)}...");

            CosmosClientOptions options = new CosmosClientOptions {
                ApplicationPreferredRegions = prefRegionsList,
                ApplicationName = "CosmosDbPlayground",
                AllowBulkExecution = true,
                ConnectionMode = ConnectionMode.Direct,
                MaxRetryAttemptsOnRateLimitedRequests = 12
            };
            return new CosmosClient(uri, key, options);
        }
    }
}
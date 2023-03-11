package org.cjoakim.cosmosdb.common;

/**
 * This interface defines common constant values used in this application.
 *
 * Chris Joakim, Microsoft
 */

public interface CommonConstants {

    // Environment variables
    public final String AZURE_SUBSCRIPTION_ID              = "AZURE_SUBSCRIPTION_ID";
    public final String AZURE_COSMOSDB_NOSQL_URI           = "AZURE_COSMOSDB_NOSQL_URI";
    public final String AZURE_COSMOSDB_NOSQL_RW_KEY1       = "AZURE_COSMOSDB_NOSQL_RW_KEY1";
    public final String AZURE_COSMOSDB_NOSQL_DB            = "AZURE_COSMOSDB_NOSQL_DB";
    public final String AZURE_COSMOSDB_NOSQL_PREF_REGIONS  = "AZURE_COSMOSDB_NOSQL_PREF_REGIONS";
//
//            # These two are well-defined "public" values, not "secrets"
    public final String AZURE_COSMOSDB_NOSQL_EMULATOR_URI_VALUE = "https://localhost:8081";
    public final String AZURE_COSMOSDB_NOSQL_EMULATOR_KEY_VALUE = "C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw==";
    public final String AZURE_COSMOSDB_MONGODB_CONN_STRING = "AZURE_COSMOSDB_MONGODB_CONN_STRING";
    public final String AZURE_COSMOSDB_MONGODB_HOST        = "AZURE_COSMOSDB_MONGODB_HOST";
    public final String AZURE_COSMOSDB_MONGODB_PASS        = "AZURE_COSMOSDB_MONGODB_PASS";
    public final String AZURE_COSMOSDB_MONGODB_PORT        = "AZURE_COSMOSDB_MONGODB_PORT";
    public final String AZURE_COSMOSDB_MONGODB_USER        = "AZURE_COSMOSDB_MONGODB_USER";
    public final String AZURE_REDISCACHE_HOST              = "AZURE_REDISCACHE_HOST";
    public final String AZURE_REDISCACHE_PORT              = "AZURE_REDISCACHE_PORT";
    public final String AZURE_REDISCACHE_KEY               = "AZURE_REDISCACHE_KEY";
    public final String AZURE_STORAGE_ACCOUNT              = "AZURE_STORAGE_ACCOUNT";
    public final String AZURE_STORAGE_CONN_STRING          = "AZURE_STORAGE_CONN_STRING";
    public final String AZURE_STORAGE_KEY                  = "AZURE_STORAGE_KEY";
    public final String REDIS_CACHE_WINDOWS_NET            = ".redis.cache.windows.net";

    // Command-line flag args
    public final String FLAG_ARG_PRETTY                    = "--pretty";
    public final String FLAG_ARG_SILENT                    = "--silent";
    public final String FLAG_ARG_VERBOSE                   = "--verbose";
    public final String FLAG_ARG_USE_AZURE_REDIS           = "--use-azure-redis";
    public final String FLAG_ARG_USE_LOCAL_REDIS           = "--use-local-redis";

    // Throughput

    public final String THROUGHPUT_AUTOSCALE               = "autoscale";
    public final String THROUGHPUT_MANUAL                  = "manual";

    // HTTP Headers
    public final String REQUEST_CHARGE_HEADER              = "x-ms-request-charge";
    public final String ACTIVITY_ID_HEADER                 = "x-ms-activity-id";

    // Units of time
    public final double MS_PER_MINUTE = 1000 * 60;
    public final double MS_PER_HOUR   = 1000 * 60 * 60;
    public final double MS_PER_DAY    = 1000 * 60 * 60 * 24;

    // Units of storage
    public final double KB = 1024;
    public final double MB = KB * KB;
    public final double GB = KB * KB * KB;
    public final double TB = KB * KB * KB * KB;
}

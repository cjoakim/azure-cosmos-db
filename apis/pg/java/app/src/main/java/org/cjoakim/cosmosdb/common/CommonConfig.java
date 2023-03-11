package org.cjoakim.cosmosdb.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Common configuration logic for command-line args and environment-variables.
 * This is the superclass of the AppConfig classes in the dependent projects.
 *
 * Chris Joakim, Microsoft
 */

@Slf4j
public class CommonConfig implements CommonConstants {

    // Class variables:
    protected static String[] commandLineArgs = new String[0];

    /**
     * This method should directly or indirectly be invoked by the main() methods
     * in the dependent projects; pass it the command-line arguments passed to main().
     */
    public static void setCommandLineArgs(String[] args) {

        if (args != null) {
            commandLineArgs = args;
        }
    }

    public static String[] getCommandLineArgs() {

        return commandLineArgs;
    }

    public static void display(boolean extended) {

        log.warn("AppConfig commandLineArgs.length: " + commandLineArgs.length);
        for (int i = 0; i < commandLineArgs.length; i++) {
            log.warn("  arg " + i + " -> " + commandLineArgs[i]);
        }
        if (extended) {
            try {
                log.warn("mongoConnectionString:          " + getMongoConnectionString());
                log.warn("getCosmosNosqlUri:              " + getCosmosNosqlUri());
                log.warn("getCosmosNosqlKey:              " + getCosmosNosqlKey());
                log.warn("getCosmosNosqlPrefRegions:      " + getCosmosNosqlPrefRegions());
                log.warn("getStorageAccount:              " + getStorageAccount());
                log.warn("getStorageKey:                  " + getStorageKey());
                log.warn("getStorageConnectionString:     " + getStorageConnectionString());
                log.warn("getAzureRedisHost:              " + getAzureRedisHost());
                log.warn("getAzureRedisPort:              " + getAzureRedisPort());
                log.warn("getAzureRedisKey:               " + getAzureRedisKey());
                log.warn("getOsName:                      " + getOsName());
                log.warn("getOsVersion:                   " + getOsVersion());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String flagArg(String flagArg) {

        for (int i = 0; i < commandLineArgs.length; i++) {
            if (commandLineArgs[i].equalsIgnoreCase(flagArg)) {
                return commandLineArgs[i + 1];
            }
        }
        return null;
    }

    public static boolean booleanArg(String flagArg) {

        for (int i = 0; i < commandLineArgs.length; i++) {
            if (commandLineArgs[i].equalsIgnoreCase(flagArg)) {
                return true;
            }
        }
        return false;
    }

    public static long longFlagArg(String flagArg, long defaultValue) {

        try {
            return Long.parseLong(flagArg(flagArg));
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean isVerbose() {

        return booleanArg(FLAG_ARG_VERBOSE);
    }

    public static boolean isSilent() {

        return booleanArg(FLAG_ARG_SILENT);
    }

    public static boolean isPretty() {

        return booleanArg(FLAG_ARG_PRETTY);
    }

    /**
     * Return the value of the given environment variable name.
     */
    public static String getEnvVar(String name) {

        return System.getenv(name);
    }

    /**
     * Return the value of the given environment variable name, defaulting to the
     * given defaultValue if the environment variable is not set.
     */
    public static String getEnvVar(String name, String defaultValue) {

        String s = getEnvVar(name);
        if (s == null) {
            return defaultValue;
        }
        else {
            return s;
        }
    }

    public static String getAzureSubscriptionId() {

        return System.getenv(AZURE_SUBSCRIPTION_ID);
    }

    public static String getMongoConnectionString() {

        return System.getenv(AZURE_COSMOSDB_MONGODB_CONN_STRING);
    }

    public static String getCosmosNosqlUri() {

        return System.getenv(AZURE_COSMOSDB_NOSQL_URI);
    }

    public static String getCosmosNosqlKey() {

        return System.getenv(AZURE_COSMOSDB_NOSQL_RW_KEY1);
    }

    public static ArrayList<String> getCosmosNosqlPrefRegions() {

        ArrayList<String> prefRegions = new ArrayList<String>();
        try {
            String value = System.getenv(AZURE_COSMOSDB_NOSQL_PREF_REGIONS);
            if (value != null) {
                String[] tokens = value.split(",");
                for (int i = 0; i < tokens.length; i++) {
                    prefRegions.add(tokens[i]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return prefRegions;
    }

    public static String getStorageAccount() {

        return System.getenv(AZURE_STORAGE_ACCOUNT);
    }

    public static String getStorageKey() {

        return System.getenv(AZURE_STORAGE_KEY);
    }

    public static String getStorageConnectionString() {

        return System.getenv(AZURE_STORAGE_CONN_STRING);
    }

    public static String getAzureRedisHost() {

        return System.getenv(AZURE_REDISCACHE_HOST);
    }

    public static String getAzureRedisPort() {

        return System.getenv(AZURE_REDISCACHE_PORT);
    }
    public static String getAzureRedisKey() {

        return System.getenv(AZURE_REDISCACHE_KEY);
    }

    /**
     * Return the name of the host operating system, or the value 'Linux'
     * if running as a Docker container.
     */
    public static String getOsName() {

        return "" + System.getProperty("os.name");
    }

    /**
     * Return the version of the host operating system, a value like '5.10.124-linuxkit'
     * if running as a Docker container.
     */
    public static String getOsVersion() {

        return "" + System.getProperty("os.version");
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}


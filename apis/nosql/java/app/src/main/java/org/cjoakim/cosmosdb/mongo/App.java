package org.cjoakim.cosmosdb.mongo;

import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmosdb.common.CommonConfig;
import org.cjoakim.cosmosdb.common.CommonConstants;
import org.cjoakim.cosmosdb.common.sql.CosmosNosqlUtil;

import java.util.ArrayList;

@Slf4j
public class App implements CommonConstants {

    // Class variables:
    private static CosmosNosqlUtil cosmosNosqlUtil = null;

    public String getGreeting() {
        return "org.cjoakim.cosmosdb.mongo";
    }

    public static void main(String[] args) {

        CommonConfig.setCommandLineArgs(args);
        String  dbName = null;
        String  cName  = null;
        String  pk     = null;
        int     ru     = 0;
        int     ttl    = -1;
        String  scale  = THROUGHPUT_MANUAL;

        try {
            String processType = args[0];
            log.warn("processType: " + processType);

            switch (processType) {
                case "display_env":
                    CommonConfig.display(true);
                    break;
                case "database_and_container_crud":
                    dbName = args[1];
                    cName  = args[2];
                    pk     = args[3];
                    ru     = Integer.parseInt(args[4]);
                    ttl    = Integer.parseInt(args[5]);
                    scale  = args[6];
                    databaseAndContainerCrud(dbName, cName, pk, ru, ttl, scale);
                    break;
                case "list_databases":
                    listDatabases();
                    break;
                case "create_database":
                    dbName = args[1];
                    createDatabase(dbName);
                    break;
                case "delete_database":
                    dbName = args[1];
                    deleteDatabase(dbName);
                    break;
                case "create_container":
                    dbName = args[1];
                    cName  = args[2];
                    pk     = args[3];
                    ru     = Integer.parseInt(args[4]);
                    ttl    = Integer.parseInt(args[5]);
                    scale  = args[6];
                    createContainer(dbName, cName, pk, ru, ttl, scale);
                    break;
                case "delete_container":
                    dbName = args[1];
                    cName  = args[2];
                    deleteContainer(dbName, cName);
                    break;
                default:
                    log.error("undefined processType: " + processType);
            }
        }
        catch (Exception e) {
            log.error(e.getClass().getName() + " -> " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if (cosmosNosqlUtil != null) {
                cosmosNosqlUtil.close();
            }
        }
    }

    private static void databaseAndContainerCrud(
            String dbName, String cName, String pk, int ru, int ttl, String scale) throws Exception {

        boolean autoscale = scale.toLowerCase().contains(THROUGHPUT_AUTOSCALE);

        log.warn("Initial database list");
        listDatabases();

        createDatabase(dbName);

        log.warn("Database list after create");
        listDatabases();

        log.warn("Initial container list");
        listContainers(dbName);

        createContainer(dbName, cName, pk, ru, ttl, scale);
        log.warn("Container list after create container");
        listContainers(dbName);
        displayContainerInfo(cName);

        deleteContainer(dbName, cName);
        log.warn("Container list after delete container");
        listContainers(dbName);

        deleteDatabase(dbName);
        log.warn("Database list after delete");
        listDatabases();
    }

    private static void listDatabases() throws Exception {

        ArrayList<String> dbNames = getCosmosNosqlUtil().getDatabaseNames();
        log.warn("listDatabases count: " + dbNames.size());
        for (int i = 0; i < dbNames.size(); i++) {
            log.warn(dbNames.get(i));
        }
    }

    private static void createDatabase(String dbName) throws Exception {

        log.warn("createDatabase dbName: " + getCosmosNosqlUtil().createDatabase(dbName));
    }

    private static void deleteDatabase(String dbName) throws Exception {

        int respCode = getCosmosNosqlUtil().deleteDatabase(dbName);
        log.warn("deleteDatabase response code: " + respCode + " for dbName: " + dbName);
    }

    private static void listContainers(String dbName) throws Exception {

        getCosmosNosqlUtil().setCurrentDatabase(dbName);
        ArrayList<String> cNames = getCosmosNosqlUtil().getContainerNames();
        log.warn("getContainerNames count: " + cNames.size());
        for (int i = 0; i < cNames.size(); i++) {
            log.warn(cNames.get(i));
        }
    }

    private static int createContainer(
            String dbName, String cName, String pk, int ru, int ttl, String scale) throws Exception {

        boolean autoscale = scale.toLowerCase().contains(THROUGHPUT_AUTOSCALE);

        getCosmosNosqlUtil().setCurrentDatabase(dbName);
        int respCode = getCosmosNosqlUtil().createContainer(cName, pk, ru, ttl, autoscale);
        log.warn("createContainer response code: " + respCode + " for dbName: " + dbName + " for cName: " + cName);
        return respCode;
    }

    private static void displayContainerInfo(String cName) {

        getCosmosNosqlUtil().displayContainerInfo(cName);
    }

    private static void deleteContainer(String dbName, String cName) throws Exception {

        getCosmosNosqlUtil().setCurrentDatabase(dbName);
        getCosmosNosqlUtil().deleteContainer(cName);
    }

    private static CosmosNosqlUtil getCosmosNosqlUtil() {

        if (cosmosNosqlUtil == null) {
            log.warn("getCosmosNosqlUtil creating instance...");
            cosmosNosqlUtil = new CosmosNosqlUtil();
        }
        return cosmosNosqlUtil;
    }
}

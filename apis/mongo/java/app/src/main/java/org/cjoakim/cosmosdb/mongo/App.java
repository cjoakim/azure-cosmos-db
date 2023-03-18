package org.cjoakim.cosmosdb.mongo;

import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmosdb.common.CommonConfig;
import org.cjoakim.cosmosdb.common.CommonConstants;
import org.cjoakim.cosmosdb.common.io.FileUtil;
import org.cjoakim.cosmosdb.common.mongo.MongoUtil;

import java.util.List;
import java.util.Map;


@Slf4j
public class App implements CommonConstants {

    // Class variables:
    private static MongoUtil mongoUtil = null;
    private static List<Map> rawVehicleActivityData = null;

    public static void main(String[] args) {

        CommonConfig.setCommandLineArgs(args);

        try {
            String processType = args[0];
            String dbName = args[1];
            String cName  = args[2];
            log.warn("processType: " + processType);

            // Common setup logic
            rawVehicleActivityData = readVehicleActivityData();
            log.warn("rawVehicleActivityData read, document count: " + rawVehicleActivityData.size());
            getMongoUtil();
            log.warn("using database: " + dbName + ", container: " + cName);
            mongoUtil.setCurrentDatabase(dbName);
            mongoUtil.setCurrentCollection(cName);


            switch (processType) {
                case "crud":
                    crudOperationsExamples(dbName, cName);
                    break;
                case "exception_handling":
                    exceptionHandlingExample(dbName, cName);
                    break;
                case "flat_delete":
                    flatDeleteExample(dbName, cName);
                    break;
                case "flat_load":
                    flatLoadExample(dbName, cName);
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
            if (mongoUtil != null) {
                mongoUtil.close();
            }
        }
    }

    private static void crudOperationsExamples(String dbName, String cName) throws Exception {

        try {
            log.warn("crudOperationsExamples...");


        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void exceptionHandlingExample(String dbName, String cName) throws Exception {

        try {
            log.warn("exceptionHandlingExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void flatDeleteExample(String dbName, String cName) throws Exception {

        try {
            log.warn("flatDeleteExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void flatLoadExample(String dbName, String cName) throws Exception {

        try {
            log.warn("flatLoadExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MongoUtil getMongoUtil() throws Exception {

        if (mongoUtil == null) {
            log.warn("getMongoUtil creating instance...");
            mongoUtil = new MongoUtil();
        }
        return mongoUtil;
    }

    private static List<Map> readVehicleActivityData() throws Exception {

        // C:\Users\chjoakim\github\azure-cosmos-db\apis\mongo\java\app
        String infile = "../../../../data/common/vehicle_activity/vehicle_activity_data.json";
        return new FileUtil().readJsonListOfMaps(infile);
    }
}


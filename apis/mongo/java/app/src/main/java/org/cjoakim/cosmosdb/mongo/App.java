package org.cjoakim.cosmosdb.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObjectBuilder;
import org.cjoakim.cosmosdb.common.CommonConfig;
import org.cjoakim.cosmosdb.common.CommonConstants;
import org.cjoakim.cosmosdb.common.io.FileUtil;
import org.cjoakim.cosmosdb.common.mongo.MongoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Slf4j
public class App implements CommonConstants {

    // Class variables:
    private static MongoUtil mongoUtil = null;
    private static List<HashMap> rawVehicleActivityData = null;

    private static JsonWriterSettings jws = JsonWriterSettings.builder().indent(true).build();

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

    /**
     * See https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/
     * See https://www.baeldung.com/java-mongodb
     */
    private static void crudOperationsExamples(String dbName, String cName) throws Exception {

        try {
            log.warn("crudOperationsExamples...");

            HashMap hm = randomVehicleActivityMap();
            String pk = (String) hm.get("pk");
            log.warn("raw HashMap:\n" + jsonValue(hm, true));
            InsertOneResult ir = mongoUtil.insertDoc(hm);
            ObjectId oid = ((BsonObjectId) ir.getInsertedId()).getValue();
            log.warn("InsertOneResult, ObjectId toHexString(): " + oid.toHexString());
            log.warn("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));

            // db.getCollection("vehicle_activity").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})
            // db.getCollection("vehicle_activity").find({"_id" : ObjectId("6415c49fd6270153cf8785cf")})

            Document queryDoc = new Document();
            queryDoc.put("_id", oid.toHexString());
            queryDoc.put("pk", pk);

            FindIterable<Document> cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    log.warn("Response Document:\n" + (cursorIterator.next().toJson(jws)));
                    log.warn("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
                }
            }
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

    private static List<HashMap> readVehicleActivityData() throws Exception {

        // C:\Users\chjoakim\github\azure-cosmos-db\apis\mongo\java\app
        String infile = "../../../../data/common/vehicle_activity/vehicle_activity_data.json";
        return new FileUtil().readJsonListOfHashMaps(infile);
    }

    private static HashMap randomVehicleActivityMap() {

        Random rand = new Random();
        int idx = rand.nextInt(rawVehicleActivityData.size());
        return rawVehicleActivityData.get(idx);
    }

    private static String jsonValue(Object obj, boolean pretty) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
        else {
            return mapper.writeValueAsString(obj);
        }
    }
}


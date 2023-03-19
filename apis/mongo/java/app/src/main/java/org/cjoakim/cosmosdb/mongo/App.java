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
import org.cjoakim.cosmosdb.common.CommonConfig;
import org.cjoakim.cosmosdb.common.CommonConstants;
import org.cjoakim.cosmosdb.common.io.FileUtil;
import org.cjoakim.cosmosdb.common.mongo.MongoUtil;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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
            System.out.println("processType: " + processType);

            // Common setup logic
            rawVehicleActivityData = readVehicleActivityData();
            System.out.println("rawVehicleActivityData read, document count: " + rawVehicleActivityData.size());
            getMongoUtil();
            System.out.println("using database: " + dbName + ", container: " + cName);
            mongoUtil.setCurrentDatabase(dbName);
            mongoUtil.setCurrentCollection(cName);


            switch (processType) {
                case "crud":
                    crudOperationsExamples(dbName, cName);
                    break;
                case "ttl":
                    ttlExample(dbName, cName);
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
            System.out.println("crudOperationsExamples...");

            HashMap hm = randomVehicleActivityMap();
            hm.put("ttl", 60);  // specify the TTL of an individual document
            // TTL, see https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live

            String pk = (String) hm.get("pk");
            System.out.println("raw HashMap:\n" + jsonValue(hm, true));
            InsertOneResult ir = mongoUtil.insertDoc(hm);
            ObjectId oid = ((BsonObjectId) ir.getInsertedId()).getValue();
            System.out.println("InsertOneResult, ObjectId toHexString(): " + oid.toHexString());
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));

            // db.getCollection("vehicle_activity").find({"_id" : ObjectId("640e03cc74f91c0cf7885eda")})
            // db.getCollection("vehicle_activity").find({"_id" : ObjectId("6415c49fd6270153cf8785cf")})

            Document queryDoc = new Document();
            queryDoc.put("_id", oid.toHexString());
            queryDoc.put("pk", pk);

            FindIterable<Document> cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    System.out.println("Response Document:\n" + (cursorIterator.next().toJson(jws)));
                    System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void ttlExample(String dbName, String cName) throws Exception {

        try {
            System.out.println("ttlExample...");
            // See https://learn.microsoft.com/en-us/azure/cosmos-db/mongodb/time-to-live
            // TTL Index for the Container is:
            // db.getCollection("vehicle_activity").createIndex({"_ts":1}, {expireAfterSeconds: 3600})

            HashMap hm = randomVehicleActivityMap();
            hm.put("ttl", 5);  // <-- specify the TTL of an individual document; override the container TTL

            String pk = (String) hm.get("pk");
            System.out.println("----------\nraw HashMap:\n" + jsonValue(hm, true));
            InsertOneResult ir = mongoUtil.insertDoc(hm);
            ObjectId oid = ((BsonObjectId) ir.getInsertedId()).getValue();
            System.out.println("InsertOneResult, ObjectId toHexString(): " + oid.toHexString());

            Document queryDoc = new Document();
            queryDoc.put("_id", oid.toHexString());
            queryDoc.put("pk", pk);

            System.out.println("----------\nFirst find() on the new Document...");
            FindIterable<Document> cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                int docsReadCount = 0;
                while (cursorIterator.hasNext()) {
                    docsReadCount++;
                    System.out.println("Response Document:\n" + (cursorIterator.next().toJson(jws)));
                }
                System.out.println("end of cursor; docsReadCount: " + docsReadCount);
            }

            int seconds = 20;
            System.out.println("sleeping for " + seconds + " seconds...");
            Thread.sleep(seconds * 1000);

            System.out.println("----------\nSecond find() on the new Document...");
            FindIterable<Document> cursor2 = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator2 = cursor2.cursor()) {
                int docsReadCount = 0;
                while (cursorIterator2.hasNext()) {
                    docsReadCount++;
                    System.out.println("Response Document:\n" + (cursorIterator2.next().toJson(jws)));
                }
                System.out.println("end of cursor; docsReadCount: " + docsReadCount);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void exceptionHandlingExample(String dbName, String cName) throws Exception {

        try {
            System.out.println("exceptionHandlingExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void flatDeleteExample(String dbName, String cName) throws Exception {

        try {
            System.out.println("flatDeleteExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void flatLoadExample(String dbName, String cName) throws Exception {

        try {
            System.out.println("flatLoadExample...");
            getMongoUtil();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MongoUtil getMongoUtil() throws Exception {

        if (mongoUtil == null) {
            System.out.println("getMongoUtil creating instance...");
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


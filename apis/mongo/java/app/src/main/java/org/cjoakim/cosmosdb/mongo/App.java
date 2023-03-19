package org.cjoakim.cosmosdb.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.cjoakim.cosmosdb.common.CommonConfig;
import org.cjoakim.cosmosdb.common.CommonConstants;
import org.cjoakim.cosmosdb.common.io.FileUtil;
import org.cjoakim.cosmosdb.common.mongo.MongoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Updates.set;


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
            // - read the Vehicle Activity JSON file
            // - create the MongoClient, MongoDatabase, MongoCollection in my class MongoUtil
            rawVehicleActivityData = readVehicleActivityData();
            System.out.println("rawVehicleActivityData read, document count: " + rawVehicleActivityData.size());
            getMongoUtil();
            System.out.println("using database: " + dbName + ", container: " + cName);
            mongoUtil.setCurrentDatabase(dbName);
            mongoUtil.setCurrentCollection(cName);

            switch (processType) {
                case "ttl":
                    ttlExample(dbName, cName);
                    break;
                case "crud":
                    crudOperationsExamples(dbName, cName);
                    break;
                case "insert_many_spike":
                    insertManySpike(dbName, cName);
                    break;
                case "delete_many_spike":
                    deleteManySpike(dbName, cName);
                    break;
                case "insert_many_flatter":
                    insertManyFlatter(dbName, cName);
                    break;
                case "delete_many_flatter":
                    deleteManyFlatter(dbName, cName);
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

    private static void ttlExample(String dbName, String cName) throws Exception {

        try {
            System.out.println("ttlExample...");
            HashMap haahMap = randomVehicleActivityMap();
            haahMap.put("ttl", 5);  // <-- specify the TTL of an individual document; override the container TTL

            String pk = (String) haahMap.get("pk");
            System.out.println("----------\nraw HashMap:\n" + jsonValue(haahMap, true));
            InsertOneResult ir = mongoUtil.insertDoc(haahMap);
            ObjectId oid = ((BsonObjectId) ir.getInsertedId()).getValue();
            System.out.println("InsertOneResult, ObjectId toHexString(): " + oid.toHexString());

            System.out.println("----------\nFirst find() on the new Document...");
            Document queryDoc = new Document();
            queryDoc.put("_id", oid.toHexString());
            queryDoc.put("pk", pk);
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

    /**
     * See https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/
     * See https://www.baeldung.com/java-mongodb
     */
    private static void crudOperationsExamples(String dbName, String cName) throws Exception {

        try {
            System.out.println("crudOperationsExamples...");

            // Create the Document
            HashMap hm = randomVehicleActivityMap();
            String pk = (String) hm.get("pk");
            System.out.println("----------\nraw HashMap:\n" + jsonValue(hm, true));
            InsertOneResult ir = mongoUtil.insertDoc(hm);
            ObjectId oid = ((BsonObjectId) ir.getInsertedId()).getValue();
            System.out.println("InsertOneResult, ObjectId toHexString(): " + oid.toHexString());
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));

            // Read the Document
            Document queryDoc = new Document();
            queryDoc.put("_id", oid.toHexString());
            queryDoc.put("pk", pk);
            FindIterable<Document> cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    System.out.println("----------\nDocument after Insert:\n" + (cursorIterator.next().toJson(jws)));
                    System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
                }
            }

            // Update the Document
            Bson updateOperation = set("comment", "Vehicle was speeding; 97 mph in a 65 mph zone");
            UpdateResult updateResult = mongoUtil.getCurrentCollection().updateOne(queryDoc, updateOperation);
            System.out.println("----------\nUpdateOne - matched: " + updateResult.getMatchedCount() + ", modified: " + updateResult.getModifiedCount());
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));

            // Read the Document again
            cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    System.out.println("----------\nDocument after Update:\n" + (cursorIterator.next().toJson(jws)));
                    System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
                }
            }

            // Delete the Document
            DeleteResult deleteResult = mongoUtil.getCurrentCollection().deleteOne(queryDoc);
            System.out.println("----------\nDeleteOne - deleted count: " + deleteResult.getDeletedCount());
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));

            // Read the Document again
            System.out.println("----------\nAttempting to read the deleted document...");
            cursor = mongoUtil.getCurrentCollection().find(queryDoc);
            try (final MongoCursor<Document> cursorIterator = cursor.cursor()) {
                while (cursorIterator.hasNext()) {
                    System.out.println("----------\nDocument after Delete:\n" + (cursorIterator.next().toJson(jws)));
                }
                System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertManySpike(String dbName, String cName) throws Exception {

        try {
            System.out.println("insertManySpike...");
            getMongoUtil();

            ArrayList<Document> documents = new ArrayList<>();
            for (int i = 0; i < rawVehicleActivityData.size(); i++) {
                HashMap hashMap = rawVehicleActivityData.get(i);
                documents.add(new Document(hashMap));
            }
            System.out.println("Inserting " + documents.size() + " documents");

            InsertManyResult result = mongoUtil.getCurrentCollection().insertMany(documents);
            System.out.println("Inserted documents: " + result.getInsertedIds().size());
        }
        catch (Exception e) {
            System.out.println(e.getClass().getName() + " -> " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
        }
    }

    private static void deleteManySpike(String dbName, String cName) throws Exception {

        try {
            System.out.println("deleteManySpike...");
            getMongoUtil();

            System.out.println("Deleting documents ...");
            DeleteResult result = mongoUtil.getCurrentCollection().deleteMany(new Document());
            System.out.println("Deleted " + result.getDeletedCount() + " documents");
        }
        catch (Exception e) {
            System.out.println(e.getClass().getName() + " -> " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
        }
    }
    private static void insertManyFlatter(String dbName, String cName) throws Exception {

        try {
            System.out.println("insertManyFlatter...");
            getMongoUtil();

            ArrayList<Document> documents = new ArrayList<>();
            for (int i = 0; i < rawVehicleActivityData.size(); i++) {
                HashMap hashMap = rawVehicleActivityData.get(i);
                documents.add(new Document(hashMap));
            }

            // Configure these parameters per your application:
            int  batchIndex  = 0;
            int  batchSize   = 100;
            long sleepMs     = 500;
            boolean continueToProcess = true;

            while (continueToProcess) {
                ArrayList<Document> documentBatch = nextBatchOfDocuments(documents, batchIndex, batchSize);
                if (documentBatch.size() > 0) {
                    System.out.println("Inserting " + documentBatch.size() + " documents in batch " + batchIndex);
                    InsertManyResult result = mongoUtil.getCurrentCollection().insertMany(documentBatch);
                    System.out.println("Inserted documents: " + result.getInsertedIds().size());
                    System.out.println("LastRequestStatistics:\n" + jsonValue(mongoUtil.getLastRequestStatistics(), true));
                    Thread.sleep(sleepMs);
                }
                else {
                    continueToProcess = false;
                }
                batchIndex++;
            }
        }
        catch (Exception e) {
            System.out.println(e.getClass().getName() + " -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ArrayList<Document> nextBatchOfDocuments(ArrayList<Document> documents, int batchIndex, int batchSize) {

        ArrayList<Document> batch = new ArrayList<Document>();
        int startIdx = batchIndex * batchSize;
        int endIdx = startIdx + batchSize;
        if (endIdx > (documents.size() - 1)) {
            endIdx = documents.size();
        }

        for (int i = startIdx; i < endIdx; i++) {
            HashMap hashMap = rawVehicleActivityData.get(i);
            batch.add(documents.get(i));
        }
        return batch;
    }

    private static void deleteManyFlatter(String dbName, String cName) throws Exception {

        try {
            System.out.println("deleteManyFlatter...");
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


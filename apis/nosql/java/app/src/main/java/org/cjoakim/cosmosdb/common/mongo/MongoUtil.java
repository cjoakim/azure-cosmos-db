package org.cjoakim.cosmosdb.common.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.cjoakim.cosmosdb.common.CommonConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

/**
 * This class implements MongoDB database operations, including the Cosmos DB Mongo API.
 *
 * Chris Joakim, Microsoft
 */

@Slf4j
public class MongoUtil {

    // Instance variables:
    protected MongoClient mongoClient;
    protected MongoDatabase currentDatabase;
    protected MongoCollection<Document> currentCollection;
    protected String currentShardKey;
    protected JsonWriterSettings jsonWriterSettings;

    public MongoUtil() throws Exception {

        super();
        String connStr = getConnectionString();
        log.error("connStr: " + connStr);

        ConnectionString connectionString = new ConnectionString(connStr);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applicationName("swift")
                .applyConnectionString(connectionString)
                .build();

        log.warn("MongoClientSettings, app name: " + settings.getApplicationName());
        mongoClient = MongoClients.create(settings);

        if (mongoClient == null) {
            log.error("MongoClients.create returned null object");
        }
        else {
            log.warn("MongoClients.create " + mongoClient.getClusterDescription());
        }
        setJsonWriterSettings(true);
    }

    private String getConnectionString() {

        return CommonConfig.getMongoConnectionString();
    }
    public MongoClient getMongoClient() {

        return mongoClient;
    }

    public MongoDatabase getCurrentDatabase() {

        return currentDatabase;
    }

    public void setCurrentDatabase(String name) {

        currentDatabase = mongoClient.getDatabase(name);
    }

    public MongoCollection<Document> getCurrentCollection() {

        return currentCollection;
    }

    public void setCurrentCollection(String name) {

        this.currentCollection = this.currentDatabase.getCollection(name);
    }

    public String getCurrentShardKey() {

        return currentShardKey;
    }

    public void setShardKey(String name) {

        this.currentShardKey = name;
    }

    public JsonWriterSettings getJsonWriterSettings() {

        return jsonWriterSettings;
    }

    public void setJsonWriterSettings(boolean pretty) {

        jsonWriterSettings = JsonWriterSettings.builder().indent(pretty).build();
    }

    public InsertOneResult insertDoc(HashMap map) {

        return this.insertDoc(new Document(map));
    }

    public InsertOneResult insertDoc(Document doc) {

        return this.currentCollection.insertOne(doc);
    }

    public UpdateResult replaceOne(Document doc) throws Exception {

        // https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/replaceOne/
        ObjectId id = (ObjectId) doc.get("_id");
        Bson query = eq("_id", id);
        log.warn("replaceOne query: " + query.toBsonDocument());
        ReplaceOptions opts = new ReplaceOptions().upsert(true);
        return this.currentCollection.replaceOne(query, doc, opts);
    }

    public DeleteResult deleteOne(Document doc) throws Exception {

        ObjectId id = (ObjectId) doc.get("_id");
        Bson query = eq("_id", id);
        log.warn("deleteOne query: " + query.toBsonDocument());
        return this.currentCollection.deleteOne(query);
    }

    public FindIterable<Document> find() {

        return this.currentCollection.find();
    }

    public Document findOne() {

        return this.currentCollection.find().first();
    }

    public FindIterable<Document> findByPk(String pk, boolean explain) {

        Bson pkFilter = eq("pk", pk);
        if (explain) {
            Document doc = this.currentCollection.find(pkFilter).explain();
            log.warn("findByPk explain: " + doc.toJson(jsonWriterSettings));
        }
        return this.currentCollection.find(pkFilter);
    }

    public Document findByIdPk(String id, String pk, boolean explain) {

        Bson idFilter = eq("_id", id);
        Bson pkFilter = eq("pk", pk);
        if (explain) {
            Document doc = this.currentCollection.find(Filters.and(idFilter, pkFilter)).explain();
            log.warn("findByIdPk explain: " + doc.toJson(jsonWriterSettings));
        }
        return this.currentCollection.find(Filters.and(idFilter, pkFilter)).first();
    }

    /**
     * This method applies only to CosmosDB, not MongoDB.
     * See https://docs.microsoft.com/en-us/azure/cosmos-db/mongodb/find-request-unit-charge-mongodb#use-the-mongodb-java-driver
     */
    public Document getLastRequestStatistics() {

        return this.currentDatabase.runCommand(new Document("getLastRequestStatistics", 1));
    }

    /**
     * This method applies only to CosmosDB, not MongoDB.
     */
    public double getLastRequestCharge() {

        Document stats = this.getLastRequestStatistics();
        if (stats != null) {
            return stats.getDouble("RequestCharge");
        }
        else {
            return -1.0;
        }
    }

    public String createIndex(ArrayList<Object> indexes, boolean unique, Long expireAfterSeconds) {

        Bson index = null;
        if (indexes.toArray().length == 1) // Single field index
        {
            String key = (String) ((ArrayList<Object>) indexes.toArray()[0]).toArray()[0];
            Integer direction = (Integer) ((ArrayList<Object>) indexes.toArray()[0]).toArray()[1];

            //Object key, Integer direction

            if (!unique)
                if (expireAfterSeconds != null && expireAfterSeconds >= 0) {
                    index = (direction == 1) ? Indexes.ascending("_ts") : Indexes.descending((String) key); // ignoring the key found in index and substituting with the only supported _ts

                    return this.currentCollection.createIndex(index, new IndexOptions().expireAfter((Long) expireAfterSeconds, TimeUnit.SECONDS));
                }
                else
                {
                    index = (direction == 1) ? Indexes.ascending((String) key) : Indexes.descending((String) key);
                    return this.currentCollection.createIndex(index);
                }
            else {
                index = (direction == 1) ? Indexes.compoundIndex(Indexes.ascending(this.currentShardKey), Indexes.ascending((String) key)) : Indexes.compoundIndex(Indexes.ascending(this.currentShardKey), Indexes.descending((String) key));
                return this.currentCollection.createIndex(index, new IndexOptions().unique(unique));
            }
        }
        else
        {
            List<Bson> compoundIndex = new ArrayList<Bson>();
            for (int i = 0; i < indexes.toArray().length; i++)
            {
                if (i == 0)
                    continue;

                if (i == 1) {
                    String firstKey = (String) ((ArrayList<Object>) indexes.toArray()[0]).toArray()[0];
                    Number firstDirection = (Number) ((ArrayList<Object>) indexes.toArray()[0]).toArray()[1];
                    compoundIndex.add((firstDirection.equals(1.0)) ? Indexes.ascending(firstKey) : Indexes.descending(firstKey));

                    String secondKey = (String) ((ArrayList<Object>) indexes.toArray()[1]).toArray()[0];
                    Number secondDirection = (Number) ((ArrayList<Object>) indexes.toArray()[1]).toArray()[1];
                    compoundIndex.add((secondDirection.equals(1.0)) ? Indexes.ascending(secondKey) : Indexes.descending(secondKey));
                }
                else
                {
                    String key = (String) ((ArrayList<Object>) indexes.toArray()[i]).toArray()[0];
                    Number direction = (Number) ((ArrayList<Object>) indexes.toArray()[i]).toArray()[1];

                    compoundIndex.add((direction.equals(1.0)) ? Indexes.ascending(key) : Indexes.descending(key));
                }
            }
            index = Indexes.compoundIndex(compoundIndex);

            return this.currentCollection.createIndex(index, new IndexOptions().unique(unique));
        }
    }
}

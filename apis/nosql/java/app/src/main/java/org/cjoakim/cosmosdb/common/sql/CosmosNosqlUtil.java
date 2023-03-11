package org.cjoakim.cosmosdb.common.sql;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedFlux;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmosdb.common.CommonConfig;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements CosmosDB/NoSQL database operations.
 *
 * See https://docs.microsoft.com/en-us/azure/cosmos-db/sql/sql-api-sdk-java-v4
 * See https://github.com/Azure-Samples/azure-cosmos-java-getting-started
 *
 * Chris Joakim, Microsoft
 */

@Slf4j
public class CosmosNosqlUtil {

    // Constants
    public static final int DEFAULT_PAGE_SIZE = 1000;

    // Instance variables
    protected String uri;
    protected CosmosAsyncClient client;
    protected CosmosAsyncDatabase currentDatabase;
    protected CosmosAsyncContainer currentContainer;

    public CosmosNosqlUtil() {

        super();
        getClient();
    }


    public CosmosAsyncClient getClient() {

        if (client == null) {
            uri = CommonConfig.getCosmosNosqlUri();
            String key = CommonConfig.getCosmosNosqlKey();
            ArrayList<String> prefRegions = CommonConfig.getCosmosNosqlPrefRegions();
            log.warn("Creating CosmosAsyncClient with URI: " + uri);

            client = new CosmosClientBuilder()
                    .endpoint(uri)
                    .key(key)
                    .preferredRegions(prefRegions)
                    .consistencyLevel(ConsistencyLevel.EVENTUAL)
                    .contentResponseOnWriteEnabled(true)
                    .buildAsyncClient();
            log.warn("client created");
        }
        return client;
    }

    public ArrayList<String> getDatabaseNames() {

        ArrayList<String> dbNames = new ArrayList<String>();
        CosmosPagedFlux<CosmosDatabaseProperties> databases = getClient().readAllDatabases();
        databases.byPage(100).flatMap(readAllDatabasesResponse -> {
            log.warn("read {} database(s) with request charge of {}",
                    readAllDatabasesResponse.getResults().size(),readAllDatabasesResponse.getRequestCharge());

            for (CosmosDatabaseProperties response : readAllDatabasesResponse.getResults()) {
                dbNames.add(response.getId());
            }
            return Flux.empty();
        }).blockLast();
        return dbNames;
    }

    public String createDatabase(String dbName) {

        CosmosDatabaseResponse databaseResponse = getClient().createDatabaseIfNotExists(dbName).block();
        return databaseResponse.getProperties().getId();
    }

    public int deleteDatabase(String dbName) {

        CosmosDatabaseResponse dbResp = getClient().getDatabase(dbName).delete(new CosmosDatabaseRequestOptions()).block();
        return dbResp.getStatusCode();
    }

    public CosmosAsyncDatabase getCurrentDatabase() {

        return currentDatabase;
    }

    public String getCurrentDatabaseName() {

        if (currentDatabase == null) {
            return null;
        } else {
            return currentDatabase.getId();
        }
    }

    public void setCurrentDatabase(String dbName) {

        currentDatabase = getClient().getDatabase(dbName);
    }

    public ArrayList<String> getContainerNames() {

        ArrayList<String> cNames = new ArrayList<String>();
        CosmosPagedFlux<CosmosContainerProperties> containers = getCurrentDatabase().readAllContainers();

        containers.byPage(100).flatMap(readAllContainersResponse -> {
            log.warn("read {} containers(s) with request charge of {}",
                    readAllContainersResponse.getResults().size(),readAllContainersResponse.getRequestCharge());
            for (CosmosContainerProperties response : readAllContainersResponse.getResults()) {
                cNames.add(response.getId());
            }
            return Flux.empty();
        }).blockLast();
        return cNames;
    }

    public int createContainer(String cName, String pk, int ru, int ttl, boolean autoscale) {

        CosmosContainerProperties ccp = new CosmosContainerProperties(cName, pk);
        ccp.setDefaultTimeToLiveInSeconds(ttl);

        ThroughputProperties tp = null;
        if (autoscale) {
            tp = ThroughputProperties.createAutoscaledThroughput(ru);
        }
        else {
            tp = ThroughputProperties.createManualThroughput(ru);
        }
        CosmosContainerResponse databaseResponse =
                getCurrentDatabase().createContainerIfNotExists(ccp, tp).block();
        return databaseResponse.getStatusCode();
    }

    public void displayContainerInfo(String cName) {

        log.warn("displayContainerInfo: " + cName);
        setCurrentContainer(cName);
        ThroughputResponse tr = getThoughput(cName);
        List<FeedRange> feedRanges = getFeedRanges(cName);

        ThroughputProperties props = tr.getProperties();
        CosmosDiagnostics diag = tr.getDiagnostics();
        log.warn("ThroughputResponse.getMinThroughput:                     " + tr.getMinThroughput());
        log.warn("ThroughputResponse.getMaxResourceQuota:                  " + tr.getMaxResourceQuota());
        log.warn("ThroughputResponse.getMaxResourceQuota:                  " + tr.getCurrentResourceQuotaUsage());
        log.warn("ThroughputResponse.properties.getAutoscaleMaxThroughput: " + props.getAutoscaleMaxThroughput());
        log.warn("ThroughputResponse.properties.getManualThroughput:       " + props.getManualThroughput());
        log.warn("ThroughputResponse.properties.getId:                     " + props.getId());
        log.warn("ThroughputResponse.diag.getContactedRegionNames:         " + diag.getContactedRegionNames());

        log.warn("FeedRanges.size: " + feedRanges.size());
        for (int f = 0; f < feedRanges.size(); f++) {
            FeedRange fr = feedRanges.get(f);
            log.warn("FeedRange " + f + ": " + fr.toString());
        }

        // https://azuresdkdocs.blob.core.windows.net/$web/java/azure-resourcemanager-cosmos/2.0.0-beta.4/com/azure/resourcemanager/cosmos/fluent/PartitionKeyRangeIdsClient.html

    }
    public ThroughputResponse getThoughput(String cName) {

        setCurrentContainer(cName);
        return currentContainer.readThroughput().block();
    }

    public List<FeedRange> getFeedRanges(String cName) {

        setCurrentContainer(cName);
        return currentContainer.getFeedRanges().block();
    }

    public int deleteContainer(String cName) {

        CosmosContainerResponse cResp = getCurrentDatabase().getContainer(cName).delete().block();
        return cResp.getStatusCode();
    }

    public CosmosAsyncContainer getCurrentContainer() {

        return currentContainer;
    }

    public String getCurrentContainerName() {

        if (currentContainer == null) {
            return null;
        } else {
            return currentContainer.getId();
        }
    }

    public void setCurrentContainer(String containerName) {

        currentContainer = currentDatabase.getContainer(containerName);
    }

    public SqlQueryResult executeDocumentQuery(String sql) {

        log.warn("executeDocumentQuery, sql: " + sql);
        SqlQueryResult qr = new SqlQueryResult(sql);
        qr.setMethod("executeDocumentQuery");

        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        CosmosPagedFlux<Map> flux = getCurrentContainer().queryItems(sql, queryOptions, Map.class);

        flux.byPage(DEFAULT_PAGE_SIZE).flatMap(fluxResponse -> {
            List<Map> results = fluxResponse.getResults().stream().collect(Collectors.toList());
            qr.incrementTotalRequestUnits(fluxResponse.getRequestCharge());
            for (int r = 0; r < results.size(); r++) {
                qr.addItem(results.get(r));
            }
            return Flux.empty();
        }).blockLast();

        qr.stopTimer();
        return qr;
    }

    public SqlQueryResult executeCountQuery(String sql) {

        log.warn("executeCountQuery, sql: " + sql);
        SqlQueryResult qr = new SqlQueryResult(sql);
        qr.setMethod("executeCountQuery");

        CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
        CosmosPagedFlux<Map> flux = getCurrentContainer().queryItems(sql, queryOptions, Map.class);

        flux.byPage(DEFAULT_PAGE_SIZE).flatMap(fluxResponse -> {
            List<Map> results = fluxResponse.getResults().stream().collect(Collectors.toList());
            qr.incrementTotalRequestUnits(fluxResponse.getRequestCharge());
            for (int r = 0; r < results.size(); r++) {
                qr.addItem(results.get(r));
            }
            return Flux.empty();
        }).blockLast();

        qr.stopTimer();
        return qr;
    }

    public void close() {

        if (client != null) {
            log.warn("closing client...");
            client.close();
            log.warn("client closed");
        }
    }

}


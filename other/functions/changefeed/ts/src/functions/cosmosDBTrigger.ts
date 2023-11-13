import { app, InvocationContext } from "@azure/functions";

//import util from "util";

export async function cosmosDBTrigger(documents: unknown[], context: InvocationContext): Promise<void> {
    context.log(`Cosmos DB function processed ${documents.length} documents`);
}

let connString = process.env["AZURE_COSMOSDB_NOSQL_CONN_STRING1"];
let dbname = 'dev';
let cname = 'test';

console.log("connString: " + connString);
console.log("dbname: " + dbname);
console.log("cname: " + cname);

app.cosmosDB('cosmosDBTrigger', {
    connectionStringSetting: connString,
    databaseName: dbname,
    collectionName: cname,
    createLeaseCollectionIfNotExists: true,
    handler: cosmosDBTrigger
});

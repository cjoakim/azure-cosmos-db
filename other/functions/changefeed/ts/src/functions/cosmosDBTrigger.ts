// import { app, InvocationContext } from "@azure/functions";

// export async function cosmosDBTrigger(documents: unknown[], context: InvocationContext): Promise<void> {
//     context.log(`Cosmos DB function processed ${documents.length} documents`);
// }

// app.cosmosDB('cosmosDBTrigger', {
//     connectionStringSetting: '',
//     databaseName: '',
//     collectionName: '',
//     createLeaseCollectionIfNotExists: true,
//     handler: cosmosDBTrigger
// });

// Following from https://learn.microsoft.com/en-us/azure/azure-functions/functions-bindings-cosmosdb-v2-trigger?pivots=programming-language-javascript&tabs=python-v2%2Cisolated-process%2Cextensionv4%2Cnodejs-v4#example

const { app } = require('@azure/functions');

app.cosmosDB('cosmosDBTrigger1', {
    connection: 'AZURE_COSMOSDB_NOSQL_CONN_STRING1',
    databaseName: 'dev',
    containerName: 'test',
    createLeaseContainerIfNotExists: true,
    handler: (documents, context) => {
        context.log(`Cosmos DB function processed ${documents.length} documents`);
    },
});

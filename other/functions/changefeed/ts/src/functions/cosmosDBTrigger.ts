import { app, InvocationContext } from '@azure/functions';

export async function cosmosDBEventHandler(documents: unknown[], context: InvocationContext): Promise<void> {
    if (documents) {
        if (documents.length > 0) {
            context.log(`${documents.length} documents passed to this Function invocation`);
            for (let i = 0; i < documents.length; i++) {
                let doc = documents[i];
                if (doc) {
                    await processDocument(doc, context);
                }
            }
        }
    }
}

async function processDocument(doc : unknown, context: InvocationContext) : Promise<void> {
    context.log(JSON.stringify(doc, null, 2));
    return;
}

// Enable the database name, container name, and maxItemsPerInvocation
// to be configured with the following environment variables:

let dbname   : string = process.env['AZURE_COSMOSDB_NOSQL_DB'] || 'dev';
let cname    : string = process.env['AZURE_COSMOSDB_NOSQL_CONTAINER'] || 'test';
let maxItems : string = process.env['AZURE_FUNCTION_MAX_ITEMS'] || '1';
let maxItemsPerInvocation : number = parseInt(maxItems);

// Note: there is no function.json file for this runtime v4 Azure Function.
// Instead it is configured as follows:

app.cosmosDB('cosmosDBTrigger1', {
    connection: 'AZURE_COSMOSDB_NOSQL_CONN_STRING1',
    databaseName: dbname,
    containerName: cname,
    createLeaseContainerIfNotExists: true,
    maxItemsPerInvocation: maxItemsPerInvocation,
    handler: cosmosDBEventHandler
});

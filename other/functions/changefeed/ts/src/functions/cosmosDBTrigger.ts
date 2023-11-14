// See https://learn.microsoft.com/en-us/azure/azure-functions/functions-bindings-cosmosdb-v2-trigger?pivots=programming-language-javascript&tabs=python-v2%2Cisolated-process%2Cextensionv4%2Cnodejs-v4#example

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
        else {
            context.log("Error: zero documents passed to this Function invocation");
        }
    }
    else {
        context.log("Error: null documents passed to this Function invocation");
    }
}

async function processDocument(doc : unknown, context: InvocationContext) : Promise<void> {
    context.log(JSON.stringify(doc, null, 2));
    return;
}

app.cosmosDB('cosmosDBTrigger1', {
    connection: 'AZURE_COSMOSDB_NOSQL_CONN_STRING1',
    databaseName: 'dev',
    containerName: 'test',
    createLeaseContainerIfNotExists: true,
    maxItemsPerInvocation: 1,
    handler: cosmosDBEventHandler
});

# readme for other/functions/changefeed 

## Links

- https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/change-feed-functions
- https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local
  - download install the func-cli-x64.msi installer from this page

## Create a TypeScript Change Feed Function

See https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=windows%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-typescript

See https://github.com/Azure/azure-functions-nodejs-library


```
PS ...\changefeed> func init ts --worker-runtime typescript --model V4
The new Node.js programming model is generally available. Learn more at https://aka.ms/AzFuncNodeV4
Writing package.json
Writing .funcignore
Writing tsconfig.json
Writing .gitignore
Writing host.json
Writing local.settings.json
Writing C:\Users\chjoakim\github\azure-cosmos-db\other\functions\changefeed\ts\.vscode\extensions.json
Running 'npm install'...

PS ...\changefeed> cd .\ts\
PS ...\ts> func new
Use the up/down arrow keys to select a template:Azure Cosmos DB trigger
Function name: [cosmosDBTrigger]
Creating a new file C:\Users\chjoakim\github\azure-cosmos-db\other\functions\changefeed\ts\src\functions\cosmosDBTrigger.ts
The function "cosmosDBTrigger" was created successfully from the "Azure Cosmos DB trigger" template.
```

---

## Service Bus

- https://learn.microsoft.com/en-us/javascript/api/overview/azure/service-bus?view=azure-node-latest


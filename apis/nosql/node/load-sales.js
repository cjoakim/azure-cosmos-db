// https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/quickstart-nodejs?tabs=azure-portal%2Cwindows

// Get environment variables from .env
import * as dotenv from 'dotenv';
dotenv.config();

//const uuid = require('uuid');
import { v1 } from "uuid"

// Get Cosmos Client
import { CosmosClient } from "@azure/cosmos";

const key = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
const endpoint = process.env.AZURE_COSMOSDB_NOSQL_URI;

// Uniqueness for database and container
const timeStamp = + new Date();

// Set Database name and container name with unique timestamp
const databaseName = 'retail';
const containerName = 'sales0';
const partitionKeyPath = ["/pk"]

// Authenticate to Azure Cosmos DB
const cosmosClient = new CosmosClient({ endpoint, key });

// Create database if it doesn't exist
const { database } = await cosmosClient.databases.createIfNotExists({ id: databaseName });
console.log(`${database.id} database ready`);

// Create container if it doesn't exist
const { container } = await database.containers.createIfNotExists({
    id: containerName,
    partitionKey: {
        paths: partitionKeyPath
    }
});
console.log(`${container.id} container ready`);


const data = fs.readFileSync('data/sales2.json', 'UTF-8');

// Create all items
// for (const item of items) {
//     item['id'] = v1().toString(); 
//     const { resource } = await container.items.create(item);
//     console.log(`'${resource.name}' inserted`);
// }


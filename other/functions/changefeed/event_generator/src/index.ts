
import util from "util";
import { v4 as uuidv4 } from 'uuid';
import { ItemResponse, SqlQuerySpec } from "@azure/cosmos";
import { FileUtil, CosmosNoSqlUtil } from "azu-js";

let func = process.argv[2];

switch (func) {
    case "createChangeFeedEvents":
        createChangeFeedEvents();
        break;
    default:
        displayCommandLineExamples();
        break;
}

async function createChangeFeedEvents() {
    // node .\dist\index.js createChangeFeedEvents dev unittests --new-ids --sleep-ms:5000
    let dbname  : string = process.argv[3];
    let cname   : string = process.argv[4];
    let newIds  : boolean = false;
    let load    : boolean = true;
    let sleepMs : number = 1000;

    console.log(util.format('dbname is %s per command-line arg', dbname));
    console.log(util.format('cname is %s per command-line arg', cname));

    for (let i = 0; i < process.argv.length; i++) {
        let arg : string = process.argv[i];
        //console.log(util.format('  arg: %s : %s', i, arg));
        if (arg === '--new-ids') {
            newIds = true;
            console.log('newIds is true per command-line arg');
        }
        if (arg === '--noload') {
            load = false;
            console.log('load is false per command-line arg');
        }
        if (arg.startsWith('--sleep-ms')) {
            let tokens : string[] = arg.split(':');
            if (tokens.length === 2) {
                if (tokens[0] === '--sleep-ms') {
                    sleepMs = Number(tokens[1]);
                    console.log(util.format('sleepMs is %s per command-line arg', sleepMs));
                }
            }
        }
    }

    let cosmos : CosmosNoSqlUtil = new CosmosNoSqlUtil(
        'AZURE_COSMOSDB_NOSQL_URI',
        'AZURE_COSMOSDB_NOSQL_RW_KEY1');
    // await cosmos.setCurrentDatabaseAsync(dbname);
    // await cosmos.setCurrentContainerAsync(cname);

    let fu = new FileUtil();
    let airports = fu.readJsonArrayFile('data/world-airports-50.json');

    for (let i = 0; i < airports.length; i++) {
        console.log('---');
        let doc : object = airports[i];
        if (newIds) {
            doc['id'] = uuidv4();
        }
        console.log(JSON.stringify(doc));
        if (load) {
            let createResp : ItemResponse<Object> = await cosmos.insertDocumentAsync(dbname, cname, doc);
            let resourceJson : string = JSON.stringify(createResp.resource, null, 2);
            console.log(util.format('idx: %s, createResp: %s', i, resourceJson));
        }
        await new Promise(f => setTimeout(f, sleepMs));
    }
}

function displayCommandLineExamples() {
    console.log('');
    console.log("node .\\dist\\index.js createChangeFeedEvents <aaa> <bbb> <ccc>");
    console.log('');
}

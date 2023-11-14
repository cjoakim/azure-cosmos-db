
import util from "util";

import {
    Config,
    FileUtil,
    CosmosNoSqlUtil
} from "azu-js";

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
    // node .\dist\index.js createChangeFeedEvents dev test --new-ids
    let dbname : string = process.argv[3];
    let cname  : string = process.argv[4];
    let newIds : boolean = false;

    for (let i = 0; i < process.argv.length; i++) {
        let arg : string = process.argv[i];
        console.log(util.format('  arg: %s : %s', i, arg));
        if (arg === '--new-ids') {
            newIds = true;
        }
    }

    let cosmos : CosmosNoSqlUtil = new CosmosNoSqlUtil(
        'AZURE_COSMOSDB_NOSQL_URI',
        'AZURE_COSMOSDB_NOSQL_RW_KEY1');
    await cosmos.setCurrentDatabaseAsync(dbname);
    await cosmos.setCurrentContainerAsync(cname);
}

function displayCommandLineExamples() {
    console.log('');
    console.log("node .\\dist\\index.js createChangeFeedEvents <aaa> <bbb> <ccc>");
    console.log('');
}

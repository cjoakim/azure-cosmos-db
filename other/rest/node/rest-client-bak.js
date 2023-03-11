const crypto = require("crypto");
const fs     = require("fs");
const https  = require('https');
const util   = require('util');

// https://learn.microsoft.com/en-us/rest/api/cosmos-db/querying-cosmosdb-resources-using-the-rest-api
// https://learn.microsoft.com/en-us/rest/api/cosmos-db/access-control-on-cosmosdb-resources

if (process.argv.length < 3) {
  display_usage('invalid command line args');
}
else {
  var runtype = process.argv[2];

  switch (runtype) {
    case 'gen_token':
      genSampleAuthToken();
      break;
    case 'list_databases':
      listDatabases();
      break;
    case 'get_database':
      getDatabase(process.argv[3]); // dbName
      break;
    case 'list_containers':
      listContainers(process.argv[3]); // dbName
      break;
    case 'get_container':
      getContainer(process.argv[3], process.argv[4]);  // dbName, cName
      break;
    case 'pk_ranges':
      getPkRanges(process.argv[3], process.argv[4]);  // dbName, cName
      break;
    case 'list_offers':
      listOffers();
      break;
    case 'get_offer':
      getOffer(process.argv[3]); // offer rid
      break;
    default:
      display_usage('undefined runtype; ' + runtype);
  }
}

function display_usage(msg) {
  if (msg) {
    console.log(msg);
  }
  console.log('usage:');
  console.log('  node rest-client.js gen_token');
  console.log('  node rest-client.js list_databases');
  console.log('  node rest-client.js get_database dev');
  console.log('  node rest-client.js list_containers dev');
  console.log('  node rest-client.js get_container dev telemetry');
  console.log('  node rest-client.js pk_ranges dev telemetry');
  console.log('  node rest-client.js list_offers');
  console.log('  node rest-client.js get_offer 7FMe   (_rid)');
}

function genSampleAuthToken() {
  var verb = 'GET';
  var resourceType = 'dbs';
  var resourceId = 'dbs';
  var adate = authDate();
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var token = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, adate, masterKey);
  console.log('adate: ' + adate);
  console.log('token: ' + token);
}

function listDatabases() {
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'dbs';
  var resourceId = '';
  var date = authDate();
  var path = '/dbs';
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function getDatabase(dbName) {
  console.log('getDatabase; dbName: ' + dbName);
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'dbs';
  var resourceId = util.format('dbs/%s', dbName);
  var date = authDate();
  var path = util.format('/dbs/%s', dbName);
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function listContainers(dbName) {
  // https://{databaseaccount}.documents.azure.com/dbs/{db-id}/colls
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'colls';
  var resourceId = util.format('dbs/%s', dbName);
  var path = util.format('/dbs/%s/colls', dbName);
  var date = authDate();
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function getContainer(dbName, cName) {
  console.log('getContainer; dbName: ' + dbName + ', cName: ' + cName);
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'colls';
  var resourceId = util.format('dbs/%s/colls/%s', dbName, cName);
  var date = authDate();
  var path = util.format('/dbs/%s/colls/%s', dbName, cName);
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);

  // See https://learn.microsoft.com/en-us/rest/api/cosmos-db/get-partition-key-ranges
  // https://{databaseaccount}.documents.azure.com/dbs/{db-id}/colls/{coll-id}/pkranges
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function getPkRanges(dbName, cName) {
  console.log('getPkRanges; dbName: ' + dbName + ', cName: ' + cName);
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'pkranges';
  var resourceId = util.format('dbs/%s/colls/%s', dbName, cName);
  var date = authDate();
  var path = util.format('/dbs/%s/colls/%s/pkranges', dbName, cName);
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);

  // See https://learn.microsoft.com/en-us/rest/api/cosmos-db/get-partition-key-ranges
  // https://{databaseaccount}.documents.azure.com/dbs/{db-id}/colls/{coll-id}/pkranges
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function listOffers() {
  console.log('listOffers');
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'offers';
  var resourceId = '';
  var date = authDate();
  var path = '/offers';
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(
                    verb, resourceType, resourceId, date, masterKey);
  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function getOffer(rid) {
  console.log('getOffer: ' + rid);
  var acct = process.env.AZURE_COSMOSDB_NOSQL_ACCT;
  var host = util.format('%s.documents.azure.com', acct);
  var verb = 'GET';
  var resourceType = 'offers';
  var resourceId = rid.toLowerCase();
  var date = authDate();
  var path = util.format('/offers/%s', rid);
  var masterKey = process.env.AZURE_COSMOSDB_NOSQL_RW_KEY1;
  var authToken = getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, date, masterKey);

  // See https://learn.microsoft.com/en-us/rest/api/cosmos-db/get-an-offer

  var options = buildOptions(host, path, 'GET', authToken, date);
  executeHttpRequest(options);
}

function authDate() {
  // current server time: Sun, 15 Jan 2023 20:44:57 GMT
  return new Date().toUTCString().toLowerCase();
}

function getAuthorizationTokenUsingMasterKey(verb, resourceType, resourceId, authDate, masterKey) {
  // https://learn.microsoft.com/en-us/rest/api/cosmos-db/access-control-on-cosmosdb-resources?redirectedfrom=MSDN
  var key = new Buffer.from(masterKey, "base64");
  var text = (verb || "").toLowerCase() + "\n" +
      (resourceType || "").toLowerCase() + "\n" +
      (resourceId || "") + "\n" +
      authDate + "\n" +
      "" + "\n";
  var body = new Buffer.from(text, "utf8");
  var signature = crypto.createHmac("sha256", key).update(body).digest("base64");
  var MasterToken = "master";
  var TokenVersion = "1.0";

  return encodeURIComponent("type=" + MasterToken + "&ver=" + TokenVersion + "&sig=" + signature);
}

function buildOptions(host, path, method, authToken, date) {
  const options = {
    host: host,
    path: path,
    method: method,
    headers: {
      'Host': host,
      'Accept': 'application/json',
      'authorization': authToken,
      'x-ms-date': date,
      'x-ms-version': '2015-08-06'
    }
  };
  return options;
}

function executeHttpRequest(options) {
  console.log('executeHttpRequest options:')
  console.log(JSON.stringify(options, null, 4));

  var request = https.request(options, (res) => {
    console.log('---');
    console.log('statusCode: ' + res.statusCode );
    var data = '';
    res.on('data', (chunk) => {
      data += chunk;
    });
    res.on('close', () => {
      // console.log('---');
      // console.log(data);
      console.log('---');
      console.log(JSON.stringify(JSON.parse(data), null, 4));
    });
  });
  request.end();
  request.on('error', (err) => {
    console.error(`Error: ${err.message}`);
  });
}

// The ResourceType portion of the string identifies the type of resource that the request is for.
// Possible values are:
// Database operations: dbs
// Container operations: colls
// Stored Procedures: sprocs
// User Defined Functions: udfs
// Triggers: triggers
// Users: users
// Permissions: permissions
// Item level operations: docs

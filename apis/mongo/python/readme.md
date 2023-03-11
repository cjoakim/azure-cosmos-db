# readme for apis/mongo/python

Example of CRUD operations vs Cosmos DB Mongo API with Python.

---

## Quick Start

### Provisioning Cosmos DB

Provision Azure Cosmos DB SQL API and/or Mongo API accounts.
Or, use your currently existing account(s).

### Environment Variables

Set the following enviornment variables which point to your Cosmos DB account(s):

#### SQL API:

You can obtain these values from Azure Portal; see your Cosmos DB SQL API account.

```
AZURE_COSMOSDB_SQL_URI        <-- your Cosmos DB account URI
AZURE_COSMOSDB_SQL_RW_KEY1    <-- your read-write key
AZURE_COSMOSDB_SQL_DB         <-- your database name
```

#### Mongo API:

You can obtain these values from Azure Portal; see your Cosmos DB Mongo API account.

```
TODO - complete this section
```

## Clone this Repo, create Python Virtual Environment

```
> git clone https://github.com/cjoakim/azure-Cosmos DB-python.git
> cd azure-Cosmos DB-python

> .\venv.ps1                # create a python virtual environment from the requirements files
> .\venv\Scripts\activate   # activate the python virtual environment

> (venv) PS ...\azure-Cosmos DB-python> pip list
Package         Version
--------------- ---------
arrow           1.2.2
azure-core      1.11.0
azure-cosmos    4.2.0
build           0.8.0
certifi         2020.12.5
chardet         4.0.0
click           8.1.3
colorama        0.4.5
docopt          0.6.2
flake8          3.8.4
idna            2.10
mccabe          0.6.1
packaging       21.3
pep517          0.13.0
pip             22.2.2
pip-tools       6.8.0
pipdeptree      2.2.1
pycodestyle     2.6.0
pyflakes        2.2.0
pymongo         3.11.3
pyparsing       3.0.9
python-dateutil 2.8.2
requests        2.25.1
setuptools      58.1.0
six             1.15.0
tomli           2.0.1
urllib3         1.26.3
wheel           0.37.1


(venv) PS ...\azure-Cosmos DB-python> python .\main.py
Error: no command-line args provided
Usage:
  python main.py <func>
  python main.py env         <-- displays necessary environment variables
  python main.py cosmos      <-- executes a suite of Cosmos DB SQL API operations
Options:
  -h --help     Show this screen.
  --version     Show version.
  
(venv) PS ...\azure-Cosmos DB-python> python .\main.py env 

(venv) PS ...\azure-Cosmos DB-python> python .\main.py cosmos_sql 

...
deleting document:
{
  "postal_cd": "27013",
  "country_cd": "US",
  "city_name": "Cleveland",
  "state_abbrv": "NC",
  "latitude": "35.7634680000",
  "longitude": "-80.7037300000",
  "pk": "27013",
  "id": "2d7087a8-62a4-45a8-adc9-43920cc2871c",
  "_rid": "gklzANDudqYHAAAAAAAAAA==",
  "_self": "dbs/gklzAA==/colls/gklzANDudqY=/docs/gklzANDudqYHAAAAAAAAAA==/",
  "_etag": "\"24008f89-0000-0100-0000-630e258a0000\"",
  "_attachments": "attachments/",
  "updated": true,
  "_ts": 1661871498
}
deleting document:
{
  "postal_cd": "27016",
  "country_cd": "US",
  "city_name": "Danbury",
  "state_abbrv": "NC",
  "latitude": "36.4445880000",
  "longitude": "-80.2165700000",
  "pk": "27016",
  "id": "7c6db266-1429-4949-a4ee-cff96d911956",
  "_rid": "gklzANDudqYJAAAAAAAAAA==",
  "_self": "dbs/gklzAA==/colls/gklzANDudqY=/docs/gklzANDudqYJAAAAAAAAAA==/",
  "_etag": "\"24009189-0000-0100-0000-630e258a0000\"",
  "_attachments": "attachments/",
  "updated": true,
  "_ts": 1661871498
}
```

--- 

## Docs and Notes 

### Cosmos DB with Python and SQL API

- pip install azure-cosmos
- https://pypi.org/project/azure-cosmos/
- https://github.com/Azure/azure-cosmos-python
- https://github.com/Azure/azure-cosmos-python/blob/master/samples/CollectionManagement/Program.py

### Cosmos DB with Python and Mongo API

- pip install pymongo
- https://pymongo.readthedocs.io/en/stable/
- https://pymongo.readthedocs.io/en/stable/tutorial.html
- https://api.mongodb.com/python/current/api/pymongo/collection.html

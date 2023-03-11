# readme for apis/nosql/python

Example of CRUD operations vs Cosmos DB NoSQL API with Python.

---

## Quick Start

### Provisioning Cosmos DB

Provision an Azure Cosmos DB SQL API account or use your currently existing account(s).

### Environment Variables

Set the following enviornment variables which point to your Cosmos DB account(s):

#### SQL API:

You can obtain these values from Azure Portal; see your Cosmos DB SQL API account.

```
AZURE_COSMOSDB_SQL_URI        <-- your Cosmos DB account URI
AZURE_COSMOSDB_SQL_RW_KEY1    <-- your read-write key
AZURE_COSMOSDB_SQL_DB         <-- your database name
```

## Clone this Repo, create Python Virtual Environment

```
PS .\venv.ps1                # create a python virtual environment from the requirements files
PS .\venv\Scripts\activate   # activate the python virtual environment

(venv) PS ...\python> python --version
Python 3.10.9
(venv) PS ...\python> pip list
Package         Version
--------------- ---------
arrow           1.2.2
azure-core      1.11.0
azure-cosmos    4.2.0
build           0.9.0
certifi         2020.12.5
chardet         4.0.0
click           8.1.3
colorama        0.4.6
docopt          0.6.2
flake8          3.8.4
idna            2.10
mccabe          0.6.1
packaging       23.0
pep517          0.13.0
pip             22.3.1
pip-tools       6.12.1
pipdeptree      2.2.1
pycodestyle     2.6.0
pyflakes        2.2.0
python-dateutil 2.8.2
requests        2.25.1
setuptools      65.5.0
six             1.15.0
tomli           2.0.1
urllib3         1.26.3
wheel           0.38.4


(venv) PS ...\python> python .\main.py
Error: no command-line args provided
Usage:
  python main.py <func>
  python main.py env         <-- displays necessary environment variables
  python main.py cosmos      <-- executes a suite of Cosmos DB SQL API operations
Options:
  -h --help     Show this screen.
  --version     Show version.
  
(venv) PS ...\python> python .\main.py env 

(venv) PS ...\python> python .\main.py test_suite 
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

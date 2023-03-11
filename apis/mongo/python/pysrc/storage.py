import os

#from azure.storage.blob import BlobServiceClient, BlobClient, ContainerClient

from azure.identity import DefaultAzureCredential
from azure.storage.blob import BlobServiceClient, BlobClient, ContainerClient

from azure.core.exceptions import ResourceExistsError
from azure.core.exceptions import ResourceNotFoundError

from pysrc.constants import Constants

# This class is used to access Azure Storage.
# For macOS, install libffi: $ brew install libffi
# Chris Joakim, Microsoft

# https://learn.microsoft.com/en-us/azure/storage/blobs/storage-quickstart-blobs-python
# https://docs.microsoft.com/en-us/azure/storage/blobs/storage-quickstart-blobs-python
# https://github.com/Azure/azure-sdk-for-python/blob/azure-storage-blob_12.11.0/sdk/storage/azure-storage-blob/samples/blob_samples_service.py

class Storage(object):

    def __init__(self, opts={}):
        acct_name = opts['name']
        acct_key  = opts['key']
        if acct_name == None:
            acct_name = os.environ[Constants.AZURE_STORAGE_ACCOUNT]
        if acct_key == None:
            acct_key = os.environ[Constants.AZURE_STORAGE_KEY]

        acct_url  = 'https://{}.blob.core.windows.net/'.format(acct_name)
        print('acct_name: {}'.format(acct_name))
        print('acct_key:  {}'.format(acct_key))
        print('acct_url:  {}'.format(acct_url))

        self.blob_service_client = BlobServiceClient(
            account_url=acct_url, credential=acct_key)

    def account_info(self):
        return self.blob_service_client.get_account_information()

    def list_containers(self):
        clist = list()
        try:
            containers = self.blob_service_client.list_containers(include_metadata=True)
            for container in containers:
                clist.append(container)
                #print('container: ' + str(container))
            return clist
        except ResourceExistsError:
            return clist

    def create_container(self, cname):
        try:
            container_client = self.blob_service_client.get_container_client(cname)
            container_client.create_container()
            print('create_container: {}'.format(cname))
        except ResourceExistsError:
            pass

    def delete_container(self, cname):
        try:
            container_client = self.blob_service_client.get_container_client(cname)
            container_client.delete_container()
            print('delete_container: {}'.format(cname))
        except ResourceNotFoundError:
            pass

    def list_container(self, cname):
        try:
            container_client = self.blob_service_client.get_container_client(cname)
            return container_client.list_blobs()
        except ResourceExistsError:
            return list()

    def upload_blob(self, local_file_path, cname, blob_name, overwrite=True):
        try:
            blob_client = self.blob_service_client.get_blob_client(container=cname, blob=blob_name)
            with open(local_file_path, "rb") as data:
                blob_client.upload_blob(data, overwrite=overwrite)
            print('upload_blob: {} -> {} {}'.format(local_file_path, cname, blob_name))
            return True
        except ResourceNotFoundError:
            return False

    def download_blob(self, cname, blob_name, local_file_path):
        try:
            blob_client = self.blob_service_client.get_blob_client(container=cname, blob=blob_name)
            with open(local_file_path, "wb") as download_file:
                download_file.write(blob_client.download_blob().readall())
            print('download_blob: {} {} -> {}'.format(cname, blob_name, local_file_path))
        except ResourceNotFoundError:
            pass

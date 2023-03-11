#!/bin/bash

# Script to login to your Azure account with the az CLI program
# and set the current subscription for the account.
# Chris Joakim, Microsoft

echo 'logging in...'
az login

echo 'setting subscription...'
az account set --subscription $AZURE_SUBSCRIPTION_ID

echo 'current account...'
az account show > tmp/az_account_show.json

echo 'done'

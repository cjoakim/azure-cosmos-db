# Login/Connect to your Azure Subscription
# Chris Joakim, Microsoft

.\env.ps1

# See https://learn.microsoft.com/en-us/powershell/module/az.accounts/connect-azaccount?view=azps-9.3.0

Connect-AzAccount `
    -Subscription $Env:AZURE_SUBSCRIPTION_ID `
    -TenantId     $Env:AZURE_SUBSCRIPTION_DIRECTORY_ID

echo 'done'

# Get meta information about the PowerShell installation.
# Chris Joakim, Microsoft

$PSVersionTable.PSVersion > tmp\Meta-PSVersion.txt 

$psversiontable           > tmp\Meta-psversiontable.txt  

Get-InstalledModule       > tmp\Meta-Get-InstalledModule.txt

Get-Package               > tmp\Meta-Get-Package.txt

Get-Module -ListAvailable > tmp\Meta-Get-Module-ListAvailable.txt

Get-Command               > tmp\Meta-Get-Command.txt

cat tmp\Meta-Get-Command.txt | grep Cosmos > tmp\Meta-Get-Command-Cosmos.txt

Get-AzProviderFeature -ListAvailable > tmp\Meta-Get-AzProviderFeature-ListAvailable.txt

echo 'done'

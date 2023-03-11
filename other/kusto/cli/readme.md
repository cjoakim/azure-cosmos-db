# readme for other/kusto/cli 

## Kusto CLI

- https://learn.microsoft.com/en-us/azure/data-explorer/kusto/tools/kusto-cli


### Get the tool

> Kusto.Cli is part of the NuGet package Microsoft.Azure.Kusto.Tools that you can download for .NET.
> After you download the package, extract the package's tools folder to the target folder.
> No additional installation is required because it's xcopy-installable.

### Create DotNet Project

```
> cd cli
> dotnet new console
> dotnet add package Microsoft.Azure.Kusto.Tools --version 7.2.1

> C:\Users\chjoakim\.nuget\packages\microsoft.azure.kusto.tools\7.2.1\tools\net6.0\Kusto.Cli.exe "https://help.kusto.windows.net/Samples;Fed=true"
```


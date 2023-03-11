# readme for apis/nosql/dotnet 

## Links

- https://learn.microsoft.com/en-us/azure/data-explorer/kusto/api/netfx/about-kusto-data

## Project Creation

See https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/how-to-dotnet-get-started

```
> dotnet --version
7.0.102

> dotnet new console
The template "Console App" was created successfully.

Processing post-creation actions...
Restoring C:\Users\chjoakim\github\azure-cosmos-db\apis\nosql\dotnet\dotnet.csproj:
  Determining projects to restore...
  Restored C:\Users\chjoakim\github\azure-cosmos-db\apis\nosql\dotnet\dotnet.csproj (in 60 ms).
Restore succeeded.

> dotnet add package Microsoft.Azure.Cosmos
> dotnet add package Azure.Storage.Blobs

> dotnet build
MSBuild version 17.4.1+9a89d02ff for .NET
  Determining projects to restore...
  All projects are up-to-date for restore.
  dotnet -> C:\Users\chjoakim\github\azure-cosmos-db\apis\nosql\dotnet\bin\Debug\net7.0\dotnet.dll
Build succeeded.
    0 Warning(s)
    0 Error(s)

> dotnet run
Hello, World!

> dotnet add package Microsoft.Azure.Kusto.Data

> dotnet list package
Project 'bordeaux' has the following package references
   [net7.0]:
   Top-level Package                 Requested   Resolved
   > Microsoft.Azure.Kusto.Data      11.2.1      11.2.1
```

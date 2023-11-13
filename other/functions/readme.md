# readme for other/functions 

# Links

- https://learn.microsoft.com/en-us/azure/azure-functions/functions-reference-node
- https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local
  - Azure Functions Core Tools v4 is recommended
  - Windows: Download and execute file func-cli-x64.msi
  - macOS with homebrew:
    ```
        brew tap azure/functions
        brew install azure-functions-core-tools@4
        # if upgrading on a machine that has 2.x or 3.x installed:
        brew link --overwrite azure-functions-core-tools@4
    ```

---

## Creating a Function with the Azure Functions Core Tools v4

### func tool help info

```
> func

                  %%%%%%
                 %%%%%%
            @   %%%%%%    @
          @@   %%%%%%      @@
       @@@    %%%%%%%%%%%    @@@
     @@      %%%%%%%%%%        @@
       @@         %%%%       @@
         @@      %%%       @@
           @@    %%      @@
                %%
                %


Azure Functions Core Tools
Core Tools Version:       4.0.4915 Commit hash: N/A  (64-bit)
Function Runtime Version: 4.14.0.19631

Usage: func [context] [context] <action> [-/--options]

Contexts:
azure       Commands to log in to Azure and manage resources
durable     Commands for working with Durable Functions
extensions  Commands for installing extensions
function    Commands for creating and running functions locally
host        Commands for running the Functions host locally
kubernetes  Commands for working with Kubernetes and Azure Functions
settings    Commands for managing environment settings for the local Functions host
templates   Commands for listing available function templates

Actions:
start   Launches the functions runtime host
    --port [-p]             Local port to listen on. Default: 7071
    --cors                  A comma separated list of CORS origins with no spaces. Example: https://functions.azure.com
                            ,https://functions-staging.azure.com
    --cors-credentials      Allow cross-origin authenticated requests (i.e. cookies and the Authentication header)
    --timeout [-t]          Timeout for the functions host to start in seconds. Default: 20 seconds.
    --useHttps              Bind to https://localhost:{port} rather than http://localhost:{port}. By default it creates
                             and trusts a certificate.
    --cert                  for use with --useHttps. The path to a pfx file that contains a private key
    --password              to use with --cert. Either the password, or a file that contains the password for the pfx f
                            ile
    --language-worker       Arguments to configure the language worker.
    --no-build              Do no build current project before running. For dotnet projects only. Default is set to fal
                            se.
    --enableAuth            Enable full authentication handling pipeline.
    --functions             A space seperated list of functions to load.
    --verbose               When false, hides system logs other than warnings and errors.
    --dotnet-isolated-debug When specified, set to true, pauses the .NET Worker process until a debugger is attached.
    --enable-json-output    Signals to Core Tools and other components that JSON line output console logs, when applica
                            ble, should be emitted.
    --json-output-file      If provided, a path to the file that will be used to write the output when using --enable-j
                            son-output.

new     Create a new function from a template. Aliases: new, create
    --language [-l]  Template programming language, such as C#, F#, JavaScript, etc.
    --template [-t]  Template name
    --name [-n]      Function name
    --authlevel [-a] Authorization level is applicable to templates that use Http trigger, Allowed values: [function, a
                     nonymous, admin]. Authorization level is not enforced when running functions from core tools
    --csx            use old style csx dotnet functions

init    Create a new Function App in the current folder. Initializes git repo.
    --source-control       Run git init. Default is false.
    --worker-runtime       Runtime framework for the functions. Options are: dotnet, dotnetIsolated, node, python, powe
                           rshell, custom
    --force                Force initializing
    --docker               Create a Dockerfile based on the selected worker runtime
    --docker-only          Adds a Dockerfile to an existing function app project. Will prompt for worker-runtime if not
                            specified or set in local.settings.json
    --csx                  use csx dotnet functions
    --language             Initialize a language specific project. Currently supported when --worker-runtime set to nod
                           e. Options are - "typescript" and "javascript"
    --target-framework     Initialize a project with the given target framework moniker. Currently supported only when
                           --worker-runtime set to dotnet-isolated. Options are - "net48", "net6.0", and "net7.0"
    --managed-dependencies Installs managed dependencies. Currently, only the PowerShell worker runtime supports this f
                           unctionality.
    --model [-m]           Selects the programming model for the function app. Options are V1, V2. Currently, only the
                           Python worker runtime supports the preview programming model
    --no-docs              Do not create getting started documentation file. Currently supported when --worker-runtime
                           set to python.

logs    Gets logs of Functions running on custom backends
    --platform Hosting platform for the function app. Valid options: kubernetes
    --name     Function name
```

---

### DotNet Function

#### func init

```
> func init --worker-runtime dotnetIsolated --target-framework net7.0 --docker

Writing C:\Users\chjoakim\github\azure-cosmos-db\other\functions\changefeed\dotnet\.vscode\extensions.json
Writing Dockerfile
Writing .dockerignore
```

#### func new

```
> func new 

edited the dotnet.csproj file; changed this line to disable Nullable
    <Nullable>disable</Nullable>  

> dotnet build
MSBuild version 17.4.1+9a89d02ff for .NET
  Determining projects to restore...
  All projects are up-to-date for restore.
  dotnet -> C:\Users\chjoakim\github\azure-cosmos-db\other\functions\changefeed\dotnet\bin\Debug\net7.0\dotnet.dll
  Determining projects to restore...
  Restored C:\Users\chjoakim\AppData\Local\Temp\gzze0ucs.xy1\WorkerExtensions.csproj (in 549 ms).
  WorkerExtensions -> C:\Users\chjoakim\AppData\Local\Temp\gzze0ucs.xy1\buildout\Microsoft.Azure.Functions.Worker.Exten
  sions.dll

Build succeeded.
    0 Warning(s)
    0 Error(s)

Time Elapsed 00:00:03.14
```

---

### DotNet Function

See https://learn.microsoft.com/en-us/azure/azure-functions/create-first-function-cli-node

#### func init

``` 
> func init --worker-runtime node --language javascript --docker
```

```
> npm install

> npm list
npm WARN config global `--global`, `--local` are deprecated. Use `--location=global` instead.
node@1.0.0 C:\Users\chjoakim\github\azure-cosmos-db\other\functions\changefeed\node
`-- azure-functions-core-tools@4.0.4915
```

#### func new

``` 
> func new

then scroll to "Azure Cosmos DB trigger" and hit enter
```


``` 
> func start
```
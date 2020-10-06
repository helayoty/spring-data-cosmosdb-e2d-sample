# spring-data-cosmosdb-e2d-sample

This is an end-2-end sample for a spring-boot application on how to use the new Java Azure SDK to pull secrets from keyvault and read/write/query from CosmosDb using [Spring Data](https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/cosmos/azure-spring-data-cosmos)

### Azure Components

- Azure Key Vault
- Azure Cosmos DB


## Demo Install

### Prerequisites

- Azure subscription with the following ressources:
  - Resource Groups
  - Keyvault (You will need to set a policy for get&list permission)
  - Cosmos DB
- Bash shell (tested on Mac, Ubuntu, Windows with WSL2)
- Visual Studio Code (optional) ([download](https://code.visualstudio.com/download)) or IntelliJ


### Setup

Clone the repo to your local machine

```bash

git clone https://github.com/helayoty/spring-data-cosmosdb-e2d-sample.git

```

Set the environment variables for keyvault name

```bash
KEYVAULT_NAME=<Your-KV-Name>
```

### Credit

Thanks for [Helium](https://github.com/retaildevcrews/helium-java) project for providing the best practice for pulling secrets from KV

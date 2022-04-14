# Presto Tezos Connector (Work in Progress )
Unleash the Power of Presto Interactive SQL Querying on Tezos Blockchain. 
This plugin is based on the [Presto Ethereum Plugin](https://github.com/xiaoyao1991/presto-ethereum).

### Introduction
[Presto](https://prestosql.io) is a powerful interactive querying engine that enables running SQL queries on anything -- be it MySQL, HDFS, local file, Kafka -- as long as there exist a connector to the source.

This is a Presto connector to the Tezos blockchain. With this connector, you can explore the Tezos blockchain from the comforts of SQL.

### Note
Specify a block range where you can (e.g. `WHERE block.block_number > x AND block.block_number < y`, or `WHERE transaction.tx_blocknumber > x AND transaction.tx_blocknumber < y`, or `WHERE erc20.erc20_blocknumber > x AND erc20.erc20_blocknumber < y`). Block number is the default and only predicate that can push down to narrow down data scan range. Queries without block ranges will cause presto to retrieve blocks all the way from the first block, which takes forever. 

### Usage
1. [Install Presto](https://prestosql.io/docs/current/installation/deployment.html). *Follow the instructions on that page to create relevant config files.*  
  By the end of this step, your presto installation folder structure should look like:
    ```
    ├── bin
    ├── lib
    ├── etc
    │   ├── config.properties
    │   ├── jvm.config
    │   └── node.properties
    ├── plugin
    ```
1. [Install Presto CLI](https://prestosql.io/docs/current/installation/cli.html)
1. Clone this repo and run `mvn clean package` to build the plugin. You will find the built plugin in the `target` folder.
1. Load the plugin to Presto  
    a. Create the tezos connector config inside of `etc`.  
    `$ mkdir -p etc/catalog && touch etc/catalog/tezos.properties`   
    Paste the following to the tezos.properties:
    ```
    connector.name=tezos

    # The endpoint to which you want to connect. Can be either
    # https://api.tzstats.com if you want to test quickly, but can also be
    # a local instance of the tzindex project.
    tezos.endpoint=https://api.tzstats.com
    ```
    b. Copy and extract the built plugin to your presto plugin folder  
    ```
    $ ./deploy.sh <path to your presto-server directory>
    ```  

    By the end of this step, your presto installation folder structure should look like:  
      ```
      ├── bin
      ├── lib
      ├── etc
      │   ├── catalog
      │   │   └── tezos.properties
      │   ├── config.properties
      │   ├── jvm.config
      │   └── node.properties
      ├── plugin
      │   ├── tezos
      │   │   └── <some jars>
      ```
1. There you go. You can now start the presto server, and query through presto-cli:  
  ```
  $ bin/launcher start
  $ presto-cli --server localhost:8080 --catalog tezos --schema default
  ```
### Example Queries
TODO
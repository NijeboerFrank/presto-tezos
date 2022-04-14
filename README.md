# Presto Tezos Connector (Work in Progress )
Unleash the Power of Presto Interactive SQL Querying on Tezos Blockchain.
This plugin is based on the [Presto Ethereum Plugin](https://github.com/xiaoyao1991/presto-ethereum).

### Introduction
[Presto](https://prestosql.io) is a powerful interactive querying engine that enables running SQL queries on anything -- be it MySQL, HDFS, local file, Kafka -- as long as there exist a connector to the source.

This is a Presto connector to the Tezos blockchain. With this connector, you can explore the Tezos blockchain from the comforts of SQL.

### Note
Specify a block range where you can (e.g. `WHERE block.block_height > x AND block.block_height < y`, or `WHERE operation.operation_height > x AND operation.operation_height < y`. Block height or contract/election/proposal ID is the default and only predicate that can push down to narrow down data scan range. Queries without block ranges will cause Presto to retrieve all table items, which might take a long time depending on the table.

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
    b. Build the plugin and copy and extract the built plugin to your presto plugin folder
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
Inspired by [An Analysis of the First 100000 Blocks](https://blog.ethereum.org/2015/08/18/frontier-first-100k-blocks/), the following SQL queries capture partially what was depicted in that post.

- The first 50 block times (in seconds)
```sql
SELECT b.id, (to_unixtime(b.block_time) - to_unixtime(a.block_time)) AS delta
FROM
    (SELECT block_height AS id, block_time
     FROM block
     WHERE block_height >= 0 AND block_height < 50) AS a
        JOIN
    (SELECT (block_height - 1) AS id, block_time
     FROM block
     WHERE block_height >= 1 AND block_height < 51) AS b
    ON a.id = b.id
ORDER BY b.id;
```

- Average block time (every 200th block from genesis to block 10000)
```sql
WITH
X AS (SELECT b.id, (to_unixtime(b.block_time) - to_unixtime(a.block_time)) AS delta
        FROM
            (SELECT block_height AS id, block_time
            FROM block
            WHERE block_height >= 0 AND block_height < 10000) AS a
        JOIN
            (SELECT (block_height-1) AS id, block_time
            FROM block
            WHERE block_height >= 1 AND block_height < 10001) AS b
        ON a.id = b.id
        ORDER BY b.id)
SELECT min(id) AS chunkStart, avg(delta) AS avg_delta
FROM
    (SELECT ntile(10000/200) OVER (ORDER BY id) AS chunk, * FROM X) AS T
GROUP BY chunk
ORDER BY chunkStart;
```

- Biggest bakers in the first 100k blocks
```sql
SELECT block_baker, count(*) AS blocks_baked, count(*)/100000.0 AS percentage
FROM block
WHERE block_height < 100000
GROUP BY block_baker
ORDER BY blocks_baked DESC
LIMIT 15;
```

- Describe the database structure
```sql
SHOW TABLES;
   Table
-----------
 block
 contract
 election
 operation
 proposal

DESCRIBE block;
         Column         |   Type    | Extra | Comment
------------------------+-----------+-------+---------
 block_hash             | varchar   |       |
 block_predecessor      | varchar   |       |
 block_baker            | varchar   |       |
 block_height           | bigint    |       |
 block_cycle            | bigint    |       |
 block_iscyclesnapshot  | boolean   |       |
 block_time             | timestamp |       |
 block_solvetime        | bigint    |       |
 block_version          | bigint    |       |
 block_round            | bigint    |       |
 block_nonce            | varchar   |       |
 block_votingperiodkind | varchar   |       |
 block_nendorsedslots   | bigint    |       |
 block_nopsapplied      | bigint    |       |
 block_nopsfailed       | bigint    |       |
 block_volume           | double    |       |
 block_fee              | double    |       |
 block_reward           | double    |       |
 block_deposit          | double    |       |
 block_activatedsupply  | double    |       |
 block_burnedsupply     | double    |       |
 block_naccounts        | bigint    |       |
 block_nnewaccounts     | bigint    |       |
 block_nnewcontracts    | bigint    |       |
 block_nclearedaccounts | bigint    |       |
 block_nfundedaccounts  | bigint    |       |
 block_gaslimit         | bigint    |       |
 block_gasused          | bigint    |       |
 block_storagepaid      | bigint    |       |
 block_pctaccountreuse  | double    |       |
 block_nevents          | bigint    |       |
 block_lbescvote        | boolean   |       |
 block_lbescema         | bigint    |       |

DESCRIBE contract;
        Column        |  Type   | Extra | Comment
----------------------+---------+-------+---------
 contract_id          | bigint  |       |
 contract_address     | varchar |       |
 contract_accountid   | bigint  |       |
 contract_creatorid   | bigint  |       |
 contract_firstseen   | bigint  |       |
 contract_lastseen    | bigint  |       |
 contract_storagesize | bigint  |       |
 contract_storagepaid | bigint  |       |
 contract_script      | varchar |       |
 contract_storage     | varchar |       |
 contract_ifacehash   | varchar |       |
 contract_codehash    | varchar |       |
 contract_storagehash | varchar |       |
 contract_callstats   | varchar |       |
 contract_features    | varchar |       |
 contract_interfaces  | varchar |       |
 contract_creator     | varchar |       |

DESCRIBE election;
          Column           |  Type   | Extra | Comment
---------------------------+---------+-------+---------
 election_id               | bigint  |       |
 election_proposalid       | bigint  |       |
 election_numperiods       | bigint  |       |
 election_numproposals     | bigint  |       |
 election_votingperiod     | bigint  |       |
 election_starttime        | varchar |       |
 election_endtime          | varchar |       |
 election_startheight      | bigint  |       |
 election_endheight        | bigint  |       |
 election_isempty          | boolean |       |
 election_isopen           | boolean |       |
 election_isfailed         | boolean |       |
 election_noquorum         | boolean |       |
 election_nomajority       | boolean |       |
 election_proposal         | varchar |       |
 election_lastvotingperiod | varchar |       |

DESCRIBE operation;
         Column          |   Type    | Extra | Comment
-------------------------+-----------+-------+---------
 operation_id            | bigint    |       |
 operation_type          | varchar   |       |
 operation_hash          | varchar   |       |
 operation_height        | bigint    |       |
 operation_cycle         | bigint    |       |
 operation_time          | timestamp |       |
 operation_opn           | bigint    |       |
 operation_opp           | bigint    |       |
 operation_status        | varchar   |       |
 operation_issuccess     | boolean   |       |
 operation_iscontract    | boolean   |       |
 operation_isevent       | boolean   |       |
 operation_isinternal    | boolean   |       |
 operation_counter       | bigint    |       |
 operation_gaslimit      | bigint    |       |
 operation_gasused       | bigint    |       |
 operation_storagelimit  | bigint    |       |
 operation_storagepaid   | bigint    |       |
 operation_volume        | double    |       |
 operation_fee           | double    |       |
 operation_reward        | double    |       |
 operation_deposit       | double    |       |
 operation_burned        | double    |       |
 operation_senderid      | bigint    |       |
 operation_receiverid    | bigint    |       |
 operation_managerid     | bigint    |       |
 operation_bakerid       | bigint    |       |
 operation_data          | varchar   |       |
 operation_parameters    | varchar   |       |
 operation_storage       | varchar   |       |
 operation_bigmapdiff    | varchar   |       |
 operation_errors        | varchar   |       |
 operation_daysdestroyed | double    |       |
 operation_sender        | varchar   |       |
 operation_receiver      | varchar   |       |
 operation_creator       | varchar   |       |
 operation_baker         | varchar   |       |
 operation_block         | varchar   |       |
 operation_entrypoint    | varchar   |       |

DESCRIBE proposal;
        Column         |  Type   | Extra | Comment
-----------------------+---------+-------+---------
 proposal_id           | bigint  |       |
 proposal_hash         | varchar |       |
 proposal_height       | bigint  |       |
 proposal_time         | varchar |       |
 proposal_sourceid     | bigint  |       |
 proposal_opid         | bigint  |       |
 proposal_electionid   | bigint  |       |
 proposal_votingperiod | bigint  |       |
 proposal_rolls        | bigint  |       |
 proposal_voters       | bigint  |       |
 proposal_source       | varchar |       |
 proposal_op           | varchar |       |
```
package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TezosClient {
    private final String endpoint;

    public TezosClient(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Returns the head (latest block)
     * 
     * @return the head block of the network
     * @throws IOException
     */
    public Block getHeadBlock() throws IOException {
        return getBlock("head");
    }

    /**
     * Returns the block number of the head
     * 
     * @return the block number of the head
     * @throws Exception
     */
    public long getLastBlockNumber() throws Exception {
        return getHeadBlock().getHeight();
    }

    /**
     * Returns the block at the given height
     * 
     * @param height the height of the requested block
     * @return the block at the given height
     * @throws IOException
     */
    public Block getBlock(long height) throws IOException {
        return getBlock(String.valueOf(height));
    }

    /**
     * Retrieves a single block with the given hash from the API
     * 
     * @param hash the hash of the requested block
     * @return
     * @throws IOException
     */
    public Block getBlock(String hash) throws IOException {
        try {
            String json = doGetRequest(endpoint + "/explorer/block/" + hash);
            return new ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .readValue(json, Block.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get block " + hash);
        }
    }

    /**
     * Retrieves blocks with the given heights from the API
     * 
     * @param heights list of heights of requested blocks
     * @return a list of Blocks with the given heights
     * @throws IOException
     */
    public List<Block> getBlocks(long[] heights) throws IOException {
        String heightsIn = Arrays.stream(heights).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(
                    endpoint + "/tables/block?columns=" + getBlocksColumns() + "&limit=50000&height.in=" + heightsIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Block.class, new BlockTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Block>>() {
                    })
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks " + heightsIn);
        }
    }

    /**
     * Retrieves blocks with the given hashes from the API
     * 
     * @param hashes list of hashes of requested blocks
     * @return a list of Blocks with the given hashes
     * @throws IOException
     */
    public List<Block> getBlocks(String[] hashes) throws IOException {
        String hashesIn = String.join(",", hashes);
        try {
            String json = doGetRequest(
                    endpoint + "/tables/block?columns=" + getBlocksColumns() + "&limit=50000&hash.in=" + hashesIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Block.class, new BlockTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Block>>() {
                    })
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks " + hashesIn);
        }
    }

    /**
     * Retrieves operations with the given heights from the API
     * 
     * @param heights list of heights of requested operations
     * @return a list of Operations with the given heights
     * @throws IOException
     */
    public List<Operation> getOperations(long[] heights) throws IOException {
        String heightsIn = Arrays.stream(heights).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(
                    endpoint + "/tables/op?columns=" + getOperationsColumns() + "&limit=50000&height.in=" + heightsIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Operation.class, new OperationTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Operation>>() {
                    })
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get operations " + heightsIn);
        }
    }

    /**
     * Retrieves operations with the given hashes from the API
     * 
     * @param hashes list of hashes of requested operations
     * @return a list of Operations with the given hashes
     * @throws IOException
     */
    public List<Operation> getOperations(String[] hashes) throws IOException {
        String hashesIn = String.join(",", hashes);
        try {
            String json = doGetRequest(
                    endpoint + "/tables/op?columns=" + getOperationsColumns() + "&limit=50000&hash.in=" + hashesIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Operation.class, new OperationTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Operation>>() {
                    })
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks " + hashesIn);
        }
    }

    /**
     * Hardcoded list of columns to be returned for the blocks table API
     * 
     * @return string of column names for the blocks table API
     */
    private String getBlocksColumns() {
        return "hash,predecessor,baker,height,cycle,is_cycle_snapshot,time,solvetime,version,fitness,priority,nonce,voting_period_kind,slot_mask,n_endorsed_slots,n_ops,n_ops_failed,n_ops_contract,n_contract_calls,n_tx,n_activation,n_seed_nonce_revelation,n_double_baking_evidence,n_double_endorsement_evidence,n_endorsement,n_delegation,n_reveal,n_origination,n_proposal,n_ballot,n_register_constant,volume,fee,reward,deposit,activated_supply,burned_supply,n_accounts,n_new_accounts,n_new_contracts,n_cleared_accounts,n_funded_accounts,gas_limit,gas_used,gas_price,storage_size,days_destroyed,pct_account_reuse,n_ops_implicit,lb_esc_vote,lb_esc_ema";
    }

    /**
     * Hardcoded list of columns to be returned for the operations table API
     * 
     * @return string of columns names for the operations table API
     */
    private String getOperationsColumns() {
        return "type,hash,height,cycle,time,op_n,op_p,status,is_success,is_contract,is_internal,is_event,counter,gas_limit,gas_used,storage_limit,storage_paid,volume,fee,reward,deposit,burned,sender_id,receiver_id,creator_id,baker_id,data,parameters,storage,big_map_diff,errors,days_destroyed,sender,receiver,creator,baker,block,entrypoint";
    }

    /**
     * Performs a get request to the given URL and returns the reponse.
     * 
     * @param url the URL to do the GET request to
     * @return the response to the GET request
     * @throws Exception
     */
    private String doGetRequest(String url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) (new URL(url)).openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        return new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}

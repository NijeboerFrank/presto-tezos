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

    public Block getHeadBlock() throws IOException {
        return getBlock("head");
    }

    public long getLastBlockNumber() throws Exception {
        return getHeadBlock().getHeight();
    }

    public Block getBlock(long height) throws IOException {
        return getBlock(String.valueOf(height));
    }

    // public Operation getOperation(String hash) throws IOException {
    // try {
    // String json = doGetRequest(endpoint + "/explorer/op/" + hash);
    // return new ObjectMapper()
    // .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    // .readValue(json, Operation.class);
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new IOException("Failed to get block " + hash);
    // }
    // }

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

    // TODO for operation, get operation by something else than a single hash

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

    private String getBlocksColumns() {
        return "hash,predecessor,baker,height,cycle,is_cycle_snapshot,time,solvetime,version,fitness,priority,nonce,voting_period_kind,slot_mask,n_endorsed_slots,n_ops,n_ops_failed,n_ops_contract,n_contract_calls,n_tx,n_activation,n_seed_nonce_revelation,n_double_baking_evidence,n_double_endorsement_evidence,n_endorsement,n_delegation,n_reveal,n_origination,n_proposal,n_ballot,n_register_constant,volume,fee,reward,deposit,activated_supply,burned_supply,n_accounts,n_new_accounts,n_new_contracts,n_cleared_accounts,n_funded_accounts,gas_limit,gas_used,gas_price,storage_size,days_destroyed,pct_account_reuse,n_ops_implicit,lb_esc_vote,lb_esc_ema";
    }

    private String getOperationsColumns() {
        return "type,hash,height,cycle,time,op_n,op_p,status,is_success,is_contract,is_internal,is_event,counter,gas_limit,gas_used,storage_limit,storage_paid,volume,fee,reward,deposit,burned,sender_id,receiver_id,creator_id,baker_id,data,parameters,storage,big_map_diff,errors,days_destroyed,sender,receiver,creator,baker,block,entrypoint";
    }

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

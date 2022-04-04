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

    public Block getBlock(String hash) throws IOException {
        try {
            String json = doGetRequest(endpoint+"/explorer/block/"+hash);
            return new ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .readValue(json,  Block.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get block "+hash);
        }
    }

    public List<Block> getBlocks(long[] heights) throws IOException {
        String heightsIn = Arrays.stream(heights).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(endpoint+"/tables/block?columns="+getBlocksColumns()+"&limit=50000&height.in="+heightsIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Block.class, new BlockTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Block>>() {})
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks "+heightsIn);
        }
    }

    public List<Block> getBlocks(String[] hashes) throws IOException {
        String hashesIn = String.join(",", hashes);
        try {
            String json = doGetRequest(endpoint+"/tables/block?columns="+getBlocksColumns()+"&limit=50000&hash.in="+hashesIn);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Block.class, new BlockTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Block>>() {})
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks "+hashesIn);
        }
    }

    private String getBlocksColumns() {
        return "hash,predecessor,baker,height,cycle,is_cycle_snapshot,time,solvetime,version,round,nonce,voting_period_kind,n_endorsed_slots,n_ops_applied,n_ops_failed,volume,fee,reward,deposit,activated_supply,burned_supply,n_accounts,n_new_accounts,n_new_contracts,n_cleared_accounts,n_funded_accounts,gas_limit,gas_used,storage_paid,pct_account_reuse,n_events,lb_esc_vote,lb_esc_ema";
    }

    private String getElectionColumns() {
        return "row_id,proposal_id,num_periods,num_proposals,voting_period,start_time,end_time,start_height,end_height,is_empty,is_open,is_failed,no_quorum,no_majority,proposal,last_voting_period";
    }

    public List<Election> getElections(long[] proposalIds) throws IOException {
        String proposalIdList = Arrays.stream(proposalIds).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(endpoint+"/tables/election?columns="+getElectionColumns()+"&limit=50000&proposal_id.in="+proposalIdList);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Election.class, new ElectionTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Election>>() {})
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get elections "+proposalIdList);
        }
    }

    public List<Election> getElections(String[] proposals) throws IOException {
        String electionString = String.join(",", proposals);
        try {
            String json = doGetRequest(endpoint+"/tables/election?columns="+getElectionColumns()+"&limit=50000&proposal.in="+electionString);
            return new ObjectMapper()
                    .registerModule(new SimpleModule()
                            .addDeserializer(Election.class, new ElectionTableDeserializer()))
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .reader()
                    .forType(new TypeReference<List<Election>>() {})
                    .readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get elections "+electionString);
        }
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

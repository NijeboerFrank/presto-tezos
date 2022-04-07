package nl.utwente.presto.tezos.tezos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
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

    /**
     * Create a new Tezos client
     * 
     * @param endpoint endpoint to retrieve the data from
     */
    public TezosClient(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Get block by its height (ID)
     * 
     * @param height height of block to retrieve
     * @return block
     * @throws IOException if block failed to retrieve
     */
    public Block getBlock(long height) throws IOException {
        return getBlock(String.valueOf(height));
    }

    /**
     * Get block by its hash
     * 
     * @param hash hash of block to retrieve
     * @return block
     * @throws IOException if block failed to retrieve
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
     * Get blocks by heights (IDs)
     * 
     * @param heights heights (IDs) of blocks to retrieve
     * @return blocks
     * @throws IOException if blocks failed to retrieve
     */
    public List<Block> getBlocks(long[] heights) throws IOException {
        String heightsIn = Arrays.stream(heights).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(
                    endpoint + "/tables/block?columns=" + getBlocksColumns() + "&limit=50000&height.in=" + heightsIn);
            return convertJsonList(json, Block.class, new TypeReference<List<Block>>() {
            }, new BlockTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks " + heightsIn);
        }
    }

    /**
     * Get blocks by hashes
     * 
     * @param hashes hashes of blocks to retrieve
     * @return blocks
     * @throws IOException if blocks failed to retrieve
     */
    public List<Block> getBlocks(String[] hashes) throws IOException {
        String hashesIn = String.join(",", hashes);
        try {
            String json = doGetRequest(
                    endpoint + "/tables/block?columns=" + getBlocksColumns() + "&limit=50000&hash.in=" + hashesIn);
            return convertJsonList(json, Block.class, new TypeReference<List<Block>>() {
            }, new BlockTableDeserializer());
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
     * Get the block that was added last
     * 
     * @return last block
     * @throws IOException if block failed to retrieve
     */
    public Block getLastBlock() throws IOException {
        return getBlock("head");
    }

    /**
     * Columns of the block table to retrieve from the API
     * 
     * @return table columns
     */
    private String getBlocksColumns() {
        return "hash,predecessor,baker,height,cycle,is_cycle_snapshot,time,solvetime,version,round,nonce,voting_period_kind,n_endorsed_slots,n_ops_applied,n_ops_failed,volume,fee,reward,deposit,activated_supply,burned_supply,n_accounts,n_new_accounts,n_new_contracts,n_cleared_accounts,n_funded_accounts,gas_limit,gas_used,storage_paid,pct_account_reuse,n_events,lb_esc_vote,lb_esc_ema";
    }

    /**
     * Get election by its ID
     * 
     * @param electionId ID of election to retrieve
     * @return election
     * @throws IOException if election failed to retrieve
     */
    public Election getElection(long electionId) throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/election?columns=" + getElectionColumns()
                    + "&limit=50000&row_id=" + electionId);
            List<Election> election = convertJsonList(json, Election.class, new TypeReference<List<Election>>() {
            }, new ElectionTableDeserializer());
            if (election.isEmpty())
                throw new IOException("Failed to get election " + electionId);
            return election.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get election " + electionId);
        }
    }

    /**
     * Get elections by their ID
     * 
     * @param electionIds IDs of elections to retrieve
     * @return elections
     * @throws IOException if elections failed to retrieve
     */
    public List<Election> getElections(long[] electionIds) throws IOException {
        String proposalIdList = Arrays.stream(electionIds).mapToObj(String::valueOf).collect(Collectors.joining(","));
        try {
            String json = doGetRequest(endpoint + "/tables/election?columns=" + getElectionColumns()
                    + "&limit=50000&row_id.in=" + proposalIdList);
            return convertJsonList(json, Election.class, new TypeReference<List<Election>>() {
            }, new ElectionTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get elections " + proposalIdList);
        }
    }

    /**
     * Get the most recent election
     * 
     * @return last election
     * @throws IOException if election failed to retrieve
     */
    public Election getLastElection() throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/election?columns=" + getElectionColumns()
                    + "&limit=1&order=desc");
            List<Election> elections = convertJsonList(json, Election.class, new TypeReference<List<Election>>() {
            }, new ElectionTableDeserializer());
            if (elections.isEmpty())
                throw new IOException("Failed to get last proposal");
            return elections.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get election head");
        }
    }

    /**
     * Columns of the election table to retrieve from the API
     * 
     * @return table columns
     */
    private String getElectionColumns() {
        return "row_id,proposal_id,num_periods,num_proposals,voting_period,start_time,end_time,start_height,end_height,is_empty,is_open,is_failed,no_quorum,no_majority,proposal,last_voting_period";
    }

    /**
     * Get proposal by its ID
     * 
     * @param proposalId ID of proposal to retrieve
     * @return proposal
     * @throws IOException if proposal failed to retrieve
     */
    public Proposal getProposal(long proposalId) throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/proposal?columns=" + getProposalColumns()
                    + "&limit=50000&row_id=" + proposalId);
            List<Proposal> proposals = convertJsonList(json, Proposal.class, new TypeReference<List<Proposal>>() {
            }, new ProposalTableDeserializer());
            if (proposals.isEmpty())
                throw new IOException("Failed to get proposal" + proposalId);
            return proposals.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get proposal " + proposalId);
        }
    }

    /**
     * Get the most recent proposal
     * 
     * @return last proposal
     * @throws IOException if proposal failed to retrieve
     */
    public Proposal getLastProposal() throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/proposal?columns=" + getProposalColumns()
                    + "&limit=1&order=desc");
            List<Proposal> proposals = convertJsonList(json, Proposal.class, new TypeReference<List<Proposal>>() {
            }, new ProposalTableDeserializer());
            if (proposals.isEmpty())
                throw new IOException("Failed to get last proposal");
            return proposals.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get last proposal");
        }
    }

    /**
     * Columns of the proposal table to retrieve from the API
     * 
     * @return table columns
     */
    private String getProposalColumns() {
        return "row_id,hash,height,time,source_id,op_id,election_id,voting_period,rolls,voters,source,op";
    }

    /**
     * Convert a JSON string into a list of objects
     *
     * @param json         JSON string
     * @param type         object class
     * @param deserializer object deserializer
     * @param <T>          object type
     * @return list of deserialized objects
     * @throws IOException if deserialization fails
     */
    private <T> List<T> convertJsonList(String json, Class<T> type, TypeReference<List<T>> listType,
            JsonDeserializer<? extends T> deserializer) throws IOException {
        return new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addDeserializer(type, deserializer))
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .reader()
                .forType(listType)
                .readValue(json);
    }

    /**
     *
     * Hardcoded list of columns to be returned for the operations table API
     * 
     * @return string of columns names for the operations table API
     */
    private String getOperationsColumns() {
        return "type,hash,height,cycle,time,op_n,op_p,status,is_success,is_contract,is_internal,is_event,counter,gas_limit,gas_used,storage_limit,storage_paid,volume,fee,reward,deposit,burned,sender_id,receiver_id,creator_id,baker_id,data,parameters,storage,big_map_diff,errors,days_destroyed,sender,receiver,creator,baker,block,entrypoint";
    }

    /**
     * Execute a get request
     *
     * @param url resource to get
     * @return string response
     * @throws Exception if request fails
     * 
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

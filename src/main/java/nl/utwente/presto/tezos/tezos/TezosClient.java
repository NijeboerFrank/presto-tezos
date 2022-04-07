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
     * Retrieve blocks in a range
     * 
     * @param start lower bound block height (ID)
     * @param end   upper bound block height (ID)
     * @return blocks
     * @throws IOException if blocks failed to retrieve
     */
    public List<Block> getBlocks(long start, long end) throws IOException {
        try {
            String json = doGetRequest(
                    endpoint + "/tables/block?columns=" + getBlocksColumns() + "&limit=50000&height.gte=" + start
                            + "&height.lte=" + end);
            return convertJsonList(json, Block.class, new TypeReference<List<Block>>() {
            }, new BlockTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get blocks between " + start + " and " + end);
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

    private String getContractColumns() {
        return "row_id,address,account_id,creator_id,first_seen,last_seen,storage_size,storage_paid,script,storage,iface_hash,code_hash,storage_hash,call_stats,features,interfaces,creator";
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
     * Retrieve elections in a range
     * 
     * @param start lower bound election ID
     * @param end   upper bound election ID
     * @return elections
     * @throws IOException if elections failed to retrieve
     */
    public List<Election> getElections(long start, long end) throws IOException {
        try {
            String json = doGetRequest(
                    endpoint + "/tables/election?columns=" + getElectionColumns() + "&limit=50000&row_id.gte=" + start
                            + "&row_id.lte=" + end);
            return convertJsonList(json, Election.class, new TypeReference<List<Election>>() {
            }, new ElectionTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get elections between " + start + " and " + end);
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
     * Retrieve proposals in a range
     * 
     * @param start lower bound proposal ID
     * @param end   upper bound proposal ID
     * @return proposals
     * @throws IOException if proposals failed to retrieve
     */
    public List<Proposal> getProposals(long start, long end) throws IOException {
        try {
            String json = doGetRequest(
                    endpoint + "/tables/proposal?columns=" + getProposalColumns() + "&limit=50000&row_id.gte=" + start
                            + "&row_id.lte=" + end);
            return convertJsonList(json, Proposal.class, new TypeReference<List<Proposal>>() {
            }, new ProposalTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get proposals between " + start + " and " + end);
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
     * Get contract by its ID
     *
     * @param contractId ID of contract to retrieve
     * @return contract
     * @throws IOException if contract failed to retrieve
     */
    public Contract getContract(long contractId) throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/contract?columns=" + getContractColumns()
                    + "&limit=50000&row_id=" + contractId);
            List<Contract> contracts = convertJsonList(json, Contract.class, new TypeReference<List<Contract>>() {
            }, new ContractTableDeserializer());
            if (contracts.isEmpty())
                throw new IOException("Failed to get contract" + contractId);
            return contracts.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get contract " + contractId);
        }
    }

    /**
     * Get contract by its Hash
     *
     * @param hash hash of contract we want to retrieve
     * @return contract
     * @throws IOException if contract failed to retrieve
     */
    public Contract getContract(String hash) throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/contract?columns=" + getContractColumns()
                    + "&limit=50000&address=" + hash);
            List<Contract> contracts = convertJsonList(json, Contract.class, new TypeReference<List<Contract>>() {
            }, new ContractTableDeserializer());
            if (contracts.isEmpty())
                throw new IOException("Failed to get contract" + hash);
            return contracts.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get contract " + hash);
        }
    }


    /**
     * Retrieve contracts in a range
     *
     * @param start lower bound contract ID
     * @param end   upper bound contract ID
     * @return contracts
     * @throws IOException if contracts failed to retrieve
     */
    public List<Contract> getContracts(long start, long end) throws IOException {
        try {
            String json = doGetRequest(
                    endpoint + "/tables/contract?columns=" + getContractColumns() + "&limit=50000&row_id.gte=" + start
                            + "&row_id.lte=" + end);
            return convertJsonList(json, Contract.class, new TypeReference<List<Contract>>() {
            }, new ContractTableDeserializer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get contracts between " + start + " and " + end);
        }
    }

    /**
     * Get the most recent contract
     *
     * @return last contract
     * @throws IOException if contract failed to retrieve
     */
    public Contract getLastContract() throws IOException {
        try {
            String json = doGetRequest(endpoint + "/tables/contract?columns=" + getContractColumns()
                    + "&limit=1&order=desc");
            List<Contract> contracts = convertJsonList(json, Contract.class, new TypeReference<List<Contract>>() {
            }, new ContractTableDeserializer());
            if (contracts.isEmpty())
                throw new IOException("Failed to get last contract");
            return contracts.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to get last contract");
        }
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
     * Execute a get request
     *
     * @param url resource to get
     * @return string response
     * @throws Exception if request fails
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

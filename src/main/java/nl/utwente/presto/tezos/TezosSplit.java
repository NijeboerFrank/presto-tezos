package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TezosSplit implements ConnectorSplit {
    private final TezosTable table;
    private final Type type;
    private final Object value;

    /**
     * Create a new split
     * @param table table of the split
     * @param type type of the split
     * @param value value of the split
     */
    @JsonCreator
    public TezosSplit(
            @JsonProperty("table") TezosTable table,
            @JsonProperty("type") Type type,
            @JsonProperty("value") Object value) {
        this.table = table;
        this.type = type;
        this.value = value;
    }

    /**
     * Create a new split for a block
     * @param blockId block height (ID)
     * @return new split
     */
    public static TezosSplit forBlock(long blockId) {
        return new TezosSplit(TezosTable.BLOCK, Type.BLOCK, blockId);
    }

    /**
     * Create new splits for a range of blocks
     * @param blockIdStart lower bound block height (ID)
     * @param blockIdEnd upper bound block height (ID)
     * @return new splits
     */
    public static List<ConnectorSplit> forBlockRange(long blockIdStart, long blockIdEnd) {
        return forRange(blockIdStart, blockIdEnd, TezosTable.BLOCK, Type.BLOCK_RANGE);
    }

    /**
     * Create new splits for a range of elections
     * @param electionIdStart lower bound election ID
     * @param electionIdEnd upper bound election ID
     * @return new splits
     */
    public static List<ConnectorSplit> forElectionRange(long electionIdStart, long electionIdEnd) {
        return forRange(electionIdStart, electionIdEnd, TezosTable.ELECTION, Type.ELECTION_RANGE);
    }

    /**
     * Create new splits for a range of proposals
     * @param proposalIdStart lower bound proposal ID
     * @param proposalIdEnd upper bound proposal ID
     * @return new splits
     */
    public static List<ConnectorSplit> forProposalRange(long proposalIdStart, long proposalIdEnd) {
        return forRange(proposalIdStart, proposalIdEnd, TezosTable.PROPOSAL, Type.PROPOSAL_RANGE);
    }

    /**
     * Create new splits for a range of IDs
     * @param rangeStart lower bound ID
     * @param rangeEnd upper bound ID
     * @param table split table
     * @param type split type
     * @return new splits
     */
    private static List<ConnectorSplit> forRange(long rangeStart, long rangeEnd, TezosTable table, Type type) {
        long start = rangeStart;
        List<ConnectorSplit> splits = new ArrayList<>();
        while (start <= rangeEnd) { // Max length
            splits.add(new TezosSplit(
                    table,
                    type,
                    ImmutableList.of(start, Math.min(rangeEnd, start + 49999))
            ));
            start += 50000;
        }
        return splits;
    }

    /**
     * Create a new split for a proposal
     * @param proposalId proposal ID
     * @return new split
     */
    public static TezosSplit forProposal(long proposalId) {
        return new TezosSplit(TezosTable.PROPOSAL, Type.PROPOSAL, proposalId);
    }

    /**
     * Create a new split for an election
     * @param electionId election ID
     * @return new split
     */
    public static TezosSplit forElection(long electionId) {
        return new TezosSplit(TezosTable.ELECTION, Type.ELECTION, electionId);
    }

    /**
     * Get the table of the split
     * @return split table
     */
    @JsonProperty
    public TezosTable getTable() {
        return table;
    }

    /**
     * Get the type of the split
     * @return split type
     */
    @JsonProperty
    public  Type getType() {
        return type;
    }

    /**
     * Get the value of the split
     * @return split value
     */
    @JsonProperty
    public Object getValue() {
        return value;
    }

    /**
     * Get block height (ID) of split
     * @return block height (ID)
     * @throws IllegalArgumentException if split type is not for block
     */
    public long getBlockId() {
        if (type != Type.BLOCK) throw new IllegalArgumentException();
        return Long.parseLong(value.toString());
    }

    /**
     * Get lower bound block height (ID) of range
     * @return block height (ID)
     */
    public long getBlockStartId() {
        if (type != Type.BLOCK_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(0).toString());
    }

    /**
     * Get upper bound block height (ID) of range
     * @return block height (ID)
     */
    public long getBlockEndId() {
        if (type != Type.BLOCK_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(1).toString());
    }

    /**
     * Get lower bound election ID of range
     * @return election ID
     */
    public long getElectionStartId() {
        if (type != Type.ELECTION_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(0).toString());
    }

    /**
     * Get upper bound election ID of range
     * @return election ID
     */
    public long getElectionEndId() {
        if (type != Type.ELECTION_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(1).toString());
    }

    /**
     * Get lower bound proposal ID of range
     * @return proposal ID
     */
    public long getProposalStartId() {
        if (type != Type.PROPOSAL_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(0).toString());
    }

    /**
     * Get upper bound proposal ID of range
     * @return proposal ID
     */
    public long getProposalEndId() {
        if (type != Type.PROPOSAL_RANGE) throw new IllegalArgumentException();
        return Long.parseLong(((List) value).get(1).toString());
    }



    /**
     * Get election id of split
     * @return election id
     * @throws IllegalArgumentException if split type is not for election
     */
    public long getElectionId() {
        if (type != Type.ELECTION) throw new IllegalArgumentException();
        return Long.parseLong(value.toString());
    }

    /**
     * Get proposal id of split
     * @return proposal id
     * @throws IllegalArgumentException if split type is not for proposal
     */
    public long getProposalId() {
        if (type != Type.PROPOSAL) throw new IllegalArgumentException();
        return Long.parseLong(value.toString());
    }

    @Override
    public NodeSelectionStrategy getNodeSelectionStrategy() {
        return NodeSelectionStrategy.NO_PREFERENCE;
    }

    @Override
    public List<HostAddress> getPreferredNodes(NodeProvider nodeProvider) {
        return Collections.emptyList();
    }

    @Override
    public Object getInfo() {
        return this;
    }

    /**
     * Enum of possible split types
     */
    enum Type {
        BLOCK,
        BLOCK_RANGE,
        ELECTION,
        ELECTION_RANGE,
        PROPOSAL,
        PROPOSAL_RANGE
    }
}

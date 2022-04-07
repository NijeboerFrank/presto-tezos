package nl.utwente.presto.tezos;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.HostAddress;
import com.facebook.presto.spi.NodeProvider;
import com.facebook.presto.spi.schedule.NodeSelectionStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        ELECTION,
        PROPOSAL
    }
}

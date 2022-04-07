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

    @JsonCreator
    public TezosSplit(
            @JsonProperty("table") TezosTable table,
            @JsonProperty("type") Type type,
            @JsonProperty("value") Object value) {
        this.table = table;
        this.type = type;
        this.value = value;
    }

    public static TezosSplit forBlock(long blockId) {
        return new TezosSplit(TezosTable.BLOCK, Type.BLOCK, blockId);
    }

    public static TezosSplit forProposal(long proposalId) {
        return new TezosSplit(TezosTable.ELECTION, Type.PROPOSAL, proposalId);
    }

    public static TezosSplit forElection(long electionId) {
        return new TezosSplit(TezosTable.ELECTION, Type.ELECTION, electionId);
    }

    @JsonProperty
    public TezosTable getTable() {
        return table;
    }

    @JsonProperty
    public  Type getType() {
        return type;
    }

    @JsonProperty
    public Object getValue() {
        return value;
    }

    public long getBlockId() {
        if (type != Type.BLOCK) throw new IllegalArgumentException();
        return Long.parseLong(value.toString());
    }

    public long getElectionId() {
        if (type != Type.ELECTION) throw new IllegalArgumentException();
        return Long.parseLong(value.toString());
    }

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

    enum Type {
        BLOCK,
        ELECTION,
        PROPOSAL
    }
}

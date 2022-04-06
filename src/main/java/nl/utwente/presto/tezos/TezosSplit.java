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
    private final long blockId;
    private final String blockHash;
    private final long proposalId;

    @JsonCreator
    public TezosSplit(
            @JsonProperty("table") TezosTable table,
            @JsonProperty("blockId") long blockId,
            @JsonProperty("proposalId") long proposalId) {
        this.table = table;
        this.blockId = blockId;
        this.proposalId = proposalId;
        this.blockHash = null;
    }

    @JsonProperty
    public TezosTable getTable() {
        return table;
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

    @JsonProperty
    public long getBlockId() {
        return blockId;
    }

    @JsonProperty
    public String getBlockHash() {
        return blockHash;
    }

    @JsonProperty
    public long getProposalId() {
        return proposalId;
    }

}
